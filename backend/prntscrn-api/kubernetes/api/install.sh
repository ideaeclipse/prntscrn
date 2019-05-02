#!/bin/sh
cd ../
sudo docker build -t rails-app .
sudo docker tag $(sudo docker images rails-app --format "{{.ID}}") ideaeclipse/rails-app:latest
sudo docker push ideaeclipse/rails-app
cd kubernetes/
kubectl apply -f pod.yaml
kubectl apply -f service.yaml
