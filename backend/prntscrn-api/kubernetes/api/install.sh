#!/bin/sh
cd ../../
sudo docker build -t rails-app .
sudo docker tag $(sudo docker images rails-app --format "{{.ID}}") ideaeclipse/rails-app:latest
sudo docker push ideaeclipse/rails-app
cd kubernetes/api/
kubectl create -f deployment.yaml
kubectl apply -f service.yaml
kubectl autoscale deployment rails-app --max 10 --min 2 --cpu-percent 95
