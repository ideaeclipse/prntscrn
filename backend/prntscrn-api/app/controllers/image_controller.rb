require 'open-uri'
class ImageController < ApplicationController
  skip_before_action :auth_user, only: [:show]
  before_action :auth_admin, only: [:index, :destroy]

  # GET /image
  # ADMIN PRIV
  # Returns a json array of all image data, used for website
  def index
    value = []
    Image.all.each do |image|
      value << {uuid: image.uuid}
    end
    render json: value
  end

  # GET /image/:id
  # PUBLIC
  # returns an image
  def show
    image = Image.find_by_uuid(params[:id])
    if image.nil?
      return render json: {status: "Image Doesn't exist"}, status: 400
    end
    data = open(image.url)
    send_data(data, type: 'image/png', disposition: 'inline')
  end

  # POST /image, must pass image
  # USER PRIV
  # upload image and returns url to view
  def create
    # Take param from user
    image = Image.create!(image_params)
    # Upload to imgur
    response = JSON.parse(RestClient.post('https://api.imgur.com/3/image', {:image => File.new("#{ActiveStorage::Blob.service.path_for(image.file.key)}", 'rb')}, {:"Authorization" => "Client-ID #{ENV["IMGUR_API"]}", :multipart => true}))
    if response["status"] == 200
      response = response["data"]
      # Delete temp file
      image.file.purge
      # Save imgur direct url
      image.url = response["link"]
      # Save delete hash to delete in the future
      image.deletehash = response["deletehash"]
      # Set uuid
      image.uuid = get_uuid
      # Updated model row in table
      image.save
      render json: {url: "#{ENV["API_URL"]}/image/#{image.uuid}", status: "File Uploaded"}
    else
      image.file.purge
      image.delete
      render json: {status: "Error uploading file"}
    end
  end

  # DELETE /image/:id
  # ADMIN PRIV
  # deletes image based on url id
  def destroy
    image = Image.find_by_uuid(params[:id])
    if image.nil?
      return render json: {status: "Image Doesn't exist"}, status: 400
    end
    unless image.deletehash.nil?
      RestClient.delete("https://api.imgur.com/3/image/#{image.deletehash}", {:"Authorization" => "Client-ID #{ENV["IMGUR_API"]}", :multipart => true})
    end
    image.delete
    render json: {status: "Image deleted"}
  end

  private

  #Checks to make sure the file param is passed before execution the post method
  def image_params
    unless params[:file].present?
      render json: {status: "Must pass file with proper key"}, status: 400
    end
    params.permit(:file)
  end

  #Gets a uuid that isn't already registered with a user
  def get_uuid
    secure = SecureRandom.uuid
    image = Image.find_by_uuid(secure)
    if image.nil?
      secure
    else
      get_uuid
    end
  end
end