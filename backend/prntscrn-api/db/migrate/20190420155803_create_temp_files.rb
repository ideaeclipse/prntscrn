class CreateTempFiles < ActiveRecord::Migration[5.2]
  def change
    create_table :temp_files do |t|
      t.timestamps
    end
  end
end
