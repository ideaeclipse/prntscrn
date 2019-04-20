# TODO: secret in production doesn't exist
class UserController < ApplicationController
  skip_before_action :auth_user, only: :login
  before_action :auth_admin, only: [:admin_test, :index, :create, :destroy]
  before_action :validate_login_params, only: [:login]

  # ENDPOINTS

  # POST /login, requires JSON with keys [username,password]
  #passes username and password, returns authentication token
  def login
    user = User.find_by_username(params[:username])
    if user and user.password == Digest::SHA256.hexdigest(params[:password])
      user.token = JsonWebToken.encode(time: Time.now.strftime("%d/%m/%Y %H:%M"), user_id: user.id, username: params[:username], user_password: params[:password], nonce: Random.rand(0..1000))
      user.save
      render json: {token: user.token}
    else
      render json: {status: "Invalid login"}, status: 401
    end
  end

  # GET /user
  # ADMIN PRIV
  # Gets all users from sql table
  def index
    value = []
    User.all.each do |user|
      value << {id: user.id, username: user.username, admin: user.is_admin}
    end
    render json: value
  end

  # POST /user requires JSON with keys [username,password]
  # ADMIN PRIV
  # Admin user can create a user account
  def create
    temp = User.find_by_username(params[:username])
    if temp.nil?
      user = User.create!({username: params[:username], password: Digest::SHA256.hexdigest(params[:password]), is_admin: false})
      user.save
      render json: {status: "User Created"}
    else
      render json: {status: "That username is already taken please try another one"}, status: 400
    end
  end

  # DELETE /user/:id requires JSON with keys [username]
  # ADMIN PRIV
  # deletes user from db
  def destroy
    user = User.find_by_id(params[:id])
    if user.nil?
      render json: {status: "User couldn't be deleted"}, status: 400
    else
      user.delete
      render json: {status: "User Deleted"}
    end
  end


  # TEST METHODS

  #test method that will only execute if the user passed a valid auth token
  def auth_test
    render json: {status: "Authorized"}
  end

  # Tests admin privileges
  def admin_test
    render json: {status: "Authorized"}
  end

  private

  # Validates json params
  def validate_login_params
    unless params[:username].present? and params[:password].present?
      render json: {status: "Missing params"}, status: 400
    end
  end
end
