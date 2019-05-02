#!/bin/sh
sudo apt install docker.io -y
sudo systemctl enable docker
curl -s https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key add
sudo apt-add-repository "deb http://apt.kubernetes.io/ kubernetes-xenial main"
sudo apt install kubeadm -y
sudo swapoff -a
sudo hostnamectl set-hostname node-1
echo "Execute kubeadm token create --print-join-command on the master node and run that command on this server to connect this node"
