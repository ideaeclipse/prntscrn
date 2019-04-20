class User < ApplicationRecord
  validates_presence_of :username, :password, :is_admin
end
