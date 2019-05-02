#!/bin/sh
cd ../
sudo docker build -t react-app .
sudo docker tag $(sudo docker images react-app --format "{{.ID}}") ideaeclipse/react-app:latest
sudo docker push ideaeclipse/react-app
cd kubernetes/
kubectl create -f deployment.yaml
kubectl apply -f service.yaml
kubectl autoscale deployment react-app --max 10 --min 2 --cpu-percent 95
