#!/bin/sh
cd ../
sudo docker build -t react-app .
sudo docker tag $(sudo docker images react-app --format "{{.ID}}") ideaeclipse/react-app:latest
sudo docker push ideaeclipse/react-app
cd kubernetes/
kubectl apply -f pod.yaml
kubectl apply -f service.yaml
