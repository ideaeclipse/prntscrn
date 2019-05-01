#!/bin/bash

#Modify These values
local_address=$(/sbin/ip -o -4 addr list ens160 | awk '{print $4}' | cut -d/ -f1)
public_address="$local_address:3000"
mysql_root_password="root"
rails_mysql_user_name="rails"
rails_mysql_user_password="rails"
imgur_key="d20b3fd520e3603"
admin_username="admin"
admin_password="admin"

#Update system
sudo apt-get update -y
#Install essentials
sudo apt-get install build-essential -y
#Install ssl libs
sudo apt-get install -y libssl-dev libreadline-dev -y  zlib1g-dev -y
#Install docker
sudo apt-get install -y docker.io
#Start mysql container with default login
sudo docker run -dit --name=rails-mysql --env="MYSQL_ROOT_PASSWORD=$mysql_root_password" -p 3306:3306 mysql
#Install local mysql client
sudo apt-get install -y mysql-client
#Create rails user
mysql -u root -h 127.0.0.1 -proot -Bse "create user '$rails_mysql_user_name'@'%' identified with mysql_native_password by '$rails_mysql_user_password';grant all privileges on *.* to $rails_mysql_user_name;flush privileges;"

#change to api dir
cd prntscrn-api/

#generate database.yml
rm config/database.yml
cat > config/database.yml << EOL
development:
  adapter: mysql2
  encoding: utf8
  database: rails_dev
  username: $rails_mysql_user_name
  password: $rails_mysql_user_password
  host: $local_address
  port: 3306

test:
  adapter: mysql2
  encoding: utf8
  database: rails_test
  username: $rails_mysql_user_name
  password: $rails_mysql_user_password
  host: $local_address
  port: 3306

production:
  adapter: mysql2
  encoding: utf8
  database: rails_production
  username: $rails_mysql_user_name
  password: $rails_mysql_user_password
  host: $local_address
  port: 3306
EOL

#Setup Env for api
cat > .env << EOL
IMGUR_API=$imgur_key
API_URL=http://$public_address
EOL

#Download rbenv
git clone https://github.com/rbenv/rbenv.git ~/.rbenv
echo 'export PATH="$HOME/.rbenv/bin:$PATH"' >> ~/.bashrc
echo 'eval "$(rbenv init -)"' >> ~/.bashrc

git clone https://github.com/rbenv/ruby-build.git ~/.rbenv/plugins/ruby-build
echo 'export PATH="$HOME/.rbenv/plugins/ruby-build/bin:$PATH"' >> ~/.bashrc

#download 2.5.5
exec bash
rbenv install 2.5.5
rbenv global 2.5.5

#install mysql2 dependencies
sudo apt-get install -y default-libmysqlclient-dev

gem install bundle
#install gems
bundle install

#Allow for rails execution
rbenv rehash

#Set all database variables
rails active_storage:install
rake RAILS_ENV=production db:create
rails RAILS_ENV=production db:migrate

#Generate secret
secret="$(rails secret)"

#Setup secrets.yml
cat > config/secrets.yml << EOL
development:
  secret_key_base: $secret
test:
  secret_key_base: $secret
production:
  secret_key_base: $secret

EOL

echo "User.create!(username: \"$admin_username\", password: Digest::SHA256.hexdigest(\"$admin_password\"), is_admin: true)" | bundle exec rails c -e production

#Start api
chmod u+rtx build.sh
sudo bash build.sh

#Start frontend
cd ../admin-site

#Setup env
cat > .env << EOL
PORT=3001
REACT_APP_BACKEND=http://$public_address
EOL

chmod u+rtx build.sh
sudo bash build.sh