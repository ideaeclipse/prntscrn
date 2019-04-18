#Used to encode and decode authentication tokens into ids and vice versa
class JsonWebToken
  class << self

    # takes a hash and encodes it with the application secret
    def encode(payload)
      JWT.encode(payload, Rails.application.secrets.secret_key_base)
    end

    # takes the token passed from get_token and decodes it into the hash that was passed as payload
    # in the above encode function, else nil
    def decode(token)
      body = JWT.decode(token, Rails.application.secrets.secret_key_base)[0]
      HashWithIndifferentAccess.new body
    rescue
      nil
    end

    # @param headers is request.headers
    # If the key Authorization exists then split the data from it
    def get_token(headers = {})
      if headers['Authorization'].present?
        headers['Authorization'].split(' ').last
      else
        nil
      end
    end
  end
end