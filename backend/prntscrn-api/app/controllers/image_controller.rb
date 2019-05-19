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
    send_data(image.file.download, type: image.file.content_type, filename: image.file.filename.to_s, disposition: 'inline')
  end

  # POST /image, must pass image
  # USER PRIV
  # upload image and returns url to view
  def create
    # Take param from user
    image = Image.create!({file: params[:file], uuid: SecureRandom.uuid})
    render json: {uuid: image.uuid}
  end

  # DELETE /image/:id
  # ADMIN PRIV
  # deletes image based on url id
  def destroy
    image = Image.find_by_uuid(params[:id])
    if image.nil?
      return render json: {status: "Image Doesn't exist"}, status: 400
    end
    image.file.purge
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