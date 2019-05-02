#!/bin/sh
kubectl delete service,deployment mysql
kubectl delete pvc mysql-pv-claim
kubectl delete pv mysql-volume
