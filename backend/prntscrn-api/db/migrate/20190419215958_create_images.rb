class CreateImages < ActiveRecord::Migration[5.2]
  def change
    create_table :images do |t|
      t.string :url
      t.string :deletehash
      t.string :uuid

      t.timestamps
    end
  end
end
