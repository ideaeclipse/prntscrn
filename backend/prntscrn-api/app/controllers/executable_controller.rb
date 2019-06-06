class ExecutableController < ApplicationController
  skip_before_action :auth_user, only: [:index, :show]
  before_action :auth_admin, only: [:create, :destroy]
  before_action :file_params, only: :create

  # GET /executable/
  # Public
  # returns a json of all versions
  def index
    value = []
    Executable.all.each do |executable|
      value << {version: executable.version, url: "#{ENV["API_URL"]}/image/#{executable.version}"}
    end
    render json: value
  end

  # GET /executable/:version
  # Public
  # returns raw json value if exists
  def show
    executable = Executable.find_by_version(params[:id])
    send_data(executable.jar.download, type: executable.jar.content_type, filename: executable.jar.filename.to_s, disposition: 'inline')
  end

  # CREATE /executable
  # Admin priv
  # Must pass a file with name jar, and a version value
  # returns whether the version was successfully created or not
  def create
    if Executable.find_by_version(params[:version]) == nil
      executable = Executable.create!({jar: params[:jar], version: params[:version]})
      render json: {status: "Executable created with version #{executable.version}"}
    else
      render json: {status: "That version is already taken please choose another one"}, status: 400
    end
  end

  # DELETE /executable/:version
  # Admin priv
  # returns status
  def destroy
    executable = Executable.find_by_version(params[:id])
    if executable != nil
      executable.jar.purge
      executable.delete
      render json: {status: "Version deleted"}
    else
      render json: {status: "Unknown version"}, status: 400
    end
  end

  private

  #Checks to make sure the file param is passed before execution the post method
  def file_params
    unless params[:jar].present?
      render json: {status: "Missing file with key value \"jar\""}, status: 400
    end
    unless params[:version].present?
      render json: {status: "Missing version parameter"}, status: 400
    end
  end
end