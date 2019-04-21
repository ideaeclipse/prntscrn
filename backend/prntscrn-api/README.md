# Prntscrn-api
Api for prnstscrn client to interact with along with the admin/public website
Ruby version: 2.6.X
Rails version: 5.2.3

## Install
* all you have to do is install all gems and install active_storage to handle file uploads
```
bundle install
rails active_storage:install
```
* If you are installing the mysql connector for windows
* First install the connector archive from the mysql [website](https://downloads.mysql.com/archives/c-odbc/) then extract it to C:\mysql-connector and run the following command
```
gem install mysql2 -v 0.5.2 --platform=ruby -- '--with-mysql-lib="C:\mysql-connector\lib" --with-mysql-include="C:\mysql-connector\include" --with-mysql-dir="C:\mysql-connector"'
```

## Database Setup For Remote Host
* You must have a valid linux system on either your local network or external network that is accessible from the rails app
* I would recommend running this setup in docker, to do that you first must install docker
```
# For Update / Debian systems
sudo apt-get install docker.io
sudo apt-get update
```
* After the setup is complete Add your linux user to the docker group
```
# $USER is the name of the linux user you want to add to the docker group
# This allows you to execute docker commands without using root or sudo
sudo usermod -aG docker $USER
```
* Then run the following docker command
```
# $ROOT_PASSWORD is the password to login with the root user
docker run --detach --name=rails-mysql --env="MYSQL_ROOT_PASSWORD=$ROOT_PASSWORD" --network="host" mysql
```
* To check to see if the container is running do
```
docker ps
```
* Then login to the mysql database (it will take a miunute or two for the mysql server to start)
```
# This will prompt you for the password, enter $ROOT_PASSWORD
mysql -u root -h 127.0.0.1 -p
```
* Then enter the following commands
```
# Substitue $USERNAME and $PASSWORD for login credentials you want to use in your rails app
# The will create a user and give it all privileges on databases
create user '$UERNAME'@'%' identified with mysql_native_password by '$PASSWORD';
grant all privileges on *.* to $USERNAME;
flush privileges;
```
* Then in rails database.yml it should look like this
```yml
# $USERNAME is the user account you created
# $PASSWORD is the password for the user account you created
# $DB_HOST is the ip address of the server that the mysql server is accessible from
development:
  adapter: mysql2
  encoding: utf8
  database: rails_dev
  username: $USERNAME
  password: $PASSWORD
  host: $DB_HOST
  port: 3306

test:
  adapter: mysql2
  encoding: utf8
  database: rails_test
  username: $USERNAME
  password: $PASSWORD
  host: $DB_HOST
  port: 3306

production:
  adapter: mysql2
  encoding: utf8
  database: rails_production
  username: $USERNAME
  password: $PASSWORD
  host: $DB_HOST
  port: 3306
```

## First account
* To create your first account 
* First Modify the config/database.yml file with your mysql login and host settings
* You have to first create the database and then migrate
```
rake db:create
rails db:migrate
```
* Then open a console instance
```
rails c
```
* Then create a new user with the following values
```
User.create!(username: "$USERNAME", password: Digest::SHA256.hexdigest("$PASSWORD"), is_admin: true)
```
* Substitute USERNAME and PASSWORD for values of your choice
* Setting is_admin to true makes the newly created user an admin or vice versa

## Authenticating Users
* There are two built in methods that allow you to authenticate a user before endpoint execution with either admin level privileges or user level privileges
* if you call auth_user before your endpoint both admin and regular users will have access
* if you call auth_admin before your endpoint only admin users will have access
    * Response Errors
        * Returns a status code of 400 if you are missing the auth token in your header
        * Returns a status code of 401 if any part of the authentication process didn't work
    * Success
        * Executes endpoint, see that endpoints description for errors

## Endpoints


### Users/Authentication endpoints
* POST /login
    * Params: json string with keys username and password
        ```json
        {
          "username": "test",
          "password": "test-password"
        }
        ```
    * Errors
        * Returns a status code of 400 if you have missing parameters
        * Returns a status code of 401 if you passed an invalid pair of credentials
    * Success
        * Returns a status code of 200 and a json string with an authentication token
            ```json
            {
              "token": "**token**"
            }
            ```

* GET /user
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * Handled by authentication
    * Success
        * Returns a json array of json strings containing users id, username and is_admin boolean

* POST /user
    * User must pass a valid Admin Authorization token in request header
    * Params: json string with keys username and password
        ```json
        {
          "username": "test",
          "password": "test-password"
        }
        ```
    * Errors
        * Returns a status code of 400 if you have missing parameters
        * Returns a status code of 401 if the username you passed is already a registered user
    * Success
        * Returns a status code of 200 a json string saying the user has been created, if you want an authentication token you must call /login

* DELETE /user/:id
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * Returns a status code of 400 if the user couldn't be deleted due to the id doesn't exist
    * Success
        * Json string saying user was deleted

* GET /auth_test
    * User must pass a valid Admin/User Authorization token in request header
    * If valid will return a json string saying Authorized
    * Else will return a json string saying unauthorized
    
* GET /admin_test
    * User must pass a valid Admin Authorization token in request header
    * If valid will return a json string saying Authorized
    * Else will return a json string saying unauthorized

### Prntscrn endpoints

* GET /image
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * All errors are handled by authentication methods
    * Success
        * Returns a json array of all images entries that exist, each sub json string contains one key, id

* GET /image/:id
    * Public endpoint
    * Errors
        * Returns a status code of 400 if the id doesn't exist in the database
    * Success
        * Renders a image to the browser

* POST /image
    * User must pass a valid User/Admin Authorization token in request header
    * Requires form data with the key file and the value must be an image file
      ```
      curl -H "Authorization: $TOKEN" -F "file=@meme.jpg" localhost:3000/image
      ```
    * Errors
        * All errors are handled by authentication methods
    * Success
        * Returns a json string with key url, which is a link to the image

* DELETE /image/:id
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * Returns a status code of 400 if the id doesn't exist in the database
    * Success
        * Returns a json string saying the image was deleted 
