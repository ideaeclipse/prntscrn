#!/bin/sh
kubectl apply -f pv.yaml
kubectl apply -f pvc.yaml
kubectl create -f deployment.yaml
kubectl apply -f service.yaml
