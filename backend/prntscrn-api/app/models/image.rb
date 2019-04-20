class Image < ApplicationRecord
  validates_presence_of :url, :deletehash, :uuid
end
