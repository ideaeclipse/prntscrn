# TODO Seperate the image attached from the image model, create a file model so you can validate the attributes of both models
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
    temp_file = TempFile.create!(file_params)
    if temp_file.file.image?
      # Upload to imgur
      response = JSON.parse(RestClient.post('https://api.imgur.com/3/image', {:image => File.new("#{ActiveStorage::Blob.service.path_for(temp_file.file.key)}", 'rb')}, {:"Authorization" => "Client-ID #{ENV["IMGUR_API"]}", :multipart => true}))
      temp_file.file.purge
      temp_file.delete
      if response["status"] == 200
        response = response["data"]
        image = Image.create!({url: response["link"], deletehash: response["deletehash"], uuid: get_uuid})
        render json: {url: "#{ENV["API_URL"]}/image/#{image.uuid}", status: "File Uploaded"}
      else
        render json: {status: "Error uploading file"}
      end
    else
      temp_file.file.purge
      temp_file.delete
      render json: {status: "Not an image file"}
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
  def file_params
    unless params[:file].present?
      render json: {status: "Must pass file with proper key"}, status: 400
    end
    params.permit(:file)
  end

  #Gets a uuid that isn't already registered with an image
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