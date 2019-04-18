class ApplicationController < ActionController::API
  before_action :auth_user

  # gets user object if the user is authorized
  def auth_user
    puts "TEST"
    unless request.headers['Authorization'].present?
      return render json: {status: "Missing auth token"}, status: 400
    end
    token = JsonWebToken.get_token(request.headers)
    decoded ||= JsonWebToken.decode(token)
    if decoded.nil?
      render json: {status: "Invalid token"}, status: 401
    else
      user = User.find_by_id(decoded[:user_id])
      if user.nil?
        render json: {status: "Unauthorized"}, status: 401
      else
        if user.token == token
          @user = user
        else
          render json: {status: "Please call /login again, your token has expired"}, status: 400
        end
      end
    end
  end

  #gets whether or not the user is an admin user
  def auth_admin
    unless @user.is_admin
      render json: {status: "Unauthorized"}, status: 401
    end
  end
end
