# Backend
* How to setup the three micro services (backend, admin panel, mysql)
* You can either use docker or a kubernetes cluster
* All shell scripts where ran and tested on ubuntu machines

## One step installs

### Docker
* Execute the install-docker sh file
* This will install all 3 services inside separate docker containers
```bash
chmod u+rtx install-docker.sh
./install-docker.sh
```
* You will have to run this script twice and execute the following before re executing the script
```bash
source ~/.bashrc
```

### Kubernetes cluster
* You must have 2 separate linux machines, 1 master and 1 node
* On the master machine
```bash
chmod u+rtx install-kubernetes-master.sh
./install-kubernetes-master.sh
```
* On the node machine
```bash
chmod u+rtx install-kubernetes-node.sh
./install-kubernetes-node.sh
```
* Then execute the following command on master and record the result
```bash
sudo kubeadm token create --print-join-command
```
* Then execute the result as root on the node, and then the node should be connected to the master
* Now check to make sure the nodes are both ready by executing the following command on master
```bash
kubectl get nodes
```
* If they're both ready execute the kubernetes install service script on master
```bash
chmod u+rtx install-services-kubernetes.sh
./install-services-kubernetes.sh
```
* You will have to run this script twice and execute the following before re executing the script
```bash
source ~/.bashrc
```

## Manual install and walk through

### Setup Before starting the rails app

#### Database Setup For Remote Host
* You must have a valid linux system on either your local network or external network that is accessible from the rails app
* I would recommend running this setup in docker, to do that you first must install docker
```bash
# For Update / Debian systems
sudo apt-get install docker.io
sudo apt-get update
```
* After the setup is complete Add your linux user to the docker group
```bash
# $USER is the name of the linux user you want to add to the docker group
# This allows you to execute docker commands without using root or sudo
sudo usermod -aG docker $USER
```
* Then run the following docker command
```bash
# $ROOT_PASSWORD is the password to login with the root user
docker run --detach --name=rails-mysql --env="MYSQL_ROOT_PASSWORD=$ROOT_PASSWORD" --network="host" mysql
```
* To check to see if the container is running do
```bash
docker ps
```
* Then login to the mysql database (it will take a miunute or two for the mysql server to start)
```bash
# This will prompt you for the password, enter $ROOT_PASSWORD
mysql -u root -h 127.0.0.1 -p
```
* Then enter the following commands
```bash
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

#### Secrets
* The JWT token system requires a secure to tokenize hashs
* You must declare a token in config/secrets.yml 
```yml
development:
  secret_key_base: $KEY
test:
  secret_key_base: $KEY
production:
  secret_key_base: $KEY
```
* To generate a secret you can simply do
```bash
rails secret
```

#### Enviromental Variables
* Two enviromental variables are needed to run this app
* One is a valid imgur api key to offload all images to imgur
* The second is the address of your backend to pass in the response of creating an image
```
IMGUR_API=$IMGUR_API_CLIENT_ID
API_URL=$URL_OF_API
```

#### First account
* To create your first account 
* You have to first create the database and then migrate
```bash
rake db:create:all
rails db:migrate
```
* Then open a console instance
```bash
rails c
```
* Then create a new user with the following values
```bash
User.create!(username: "$USERNAME", password: Digest::SHA256.hexdigest("$PASSWORD"), is_admin: true)
```
* Substitute USERNAME and PASSWORD for values of your choice
* Setting is_admin to true makes the newly created user an admin or vice versa

### Install Options

#### Manual Install
* all you have to do is install all gems and install active_storage to handle file uploads
```
bundle install
rails active_storage:install
```
* If you are installing the mysql connector for windows
* First install the connector archive from the mysql [website](https://downloads.mysql.com/archives/c-odbc/) then extract it to C:\mysql-connector and run the following command
```bash
gem install mysql2 -v 0.5.2 --platform=ruby -- '--with-mysql-lib="C:\mysql-connector\lib" --with-mysql-include="C:\mysql-connector\include" --with-mysql-dir="C:\mysql-connector"'
```

#### Docker install
* To install using docker all you have to do is run the following command inside the parent directory
```bash
chmod u+rtx docker.sh
./docker.sh
```