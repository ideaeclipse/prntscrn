require 'open-uri'
class ImageController < ApplicationController
  skip_before_action :auth_user, only: [:show]
  before_action :auth_admin, only: [:index, :create, :destroy]

  # GET /image
  # ADMIN PRIV
  # Returns a json array of all image data, used for website
  def index
    value = []
    Image.all.each do |image|
      value << {id: image.id}
    end
    render json: value
  end

  # GET /image/:id
  # PUBLIC
  # returns an image
  def show
    image = Image.find_by_id(params[:id])
    if image.nil?
      return render json: {status: "Image Doesn't exist"}, status: 400
    end
    data = open(image.url)
    send_data(data, type: 'image/png', disposition: 'inline')
  end

  # POST /image, must pass image
  # ADMIN PRIV
  # upload image and returns url to view
  def create
    # Take param from user
    image = Image.create!(image_params)
    # Upload to imgur
    response = JSON.parse(RestClient.post('https://api.imgur.com/3/image', {:image => File.new("#{ActiveStorage::Blob.service.path_for(image.file.key)}", 'rb')}, {:"Authorization" => "Client-ID #{ENV["IMGUR_API"]}", :multipart => true}))
    response = response["data"]
    # Delete temp file
    image.file.purge
    # Save imgur direct url
    image.url = response["link"]
    # Save delete hash to delete in the future
    image.deletehash = response["deletehash"]
    # Updated model row in table
    image.save
    render json: {url: "#{ENV["API_URL"]}/image/#{image.id}"}
  end

  # DELETE /image/:id
  # ADMIN PRIV
  # deletes image based on url id
  def destroy
    image = Image.find_by_id(params[:id])
    if image.nil?
      return render json: {status: "Image Doesn't exist"}, status: 400
    end
    RestClient.delete("https://api.imgur.com/3/image/#{image.deletehash}", {:"Authorization" => "Client-ID #{ENV["IMGUR_API"]}", :multipart => true})
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
end