#!/bin/sh
kubectl apply -f config-map.yaml
kubectl apply -f pod.yaml
kubectl apply -f service.yaml
