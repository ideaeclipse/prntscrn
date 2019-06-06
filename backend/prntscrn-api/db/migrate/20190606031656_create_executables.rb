class CreateExecutables < ActiveRecord::Migration[5.2]
  def change
    create_table :executables do |t|
      t.string :version

      t.timestamps
    end
  end
end
