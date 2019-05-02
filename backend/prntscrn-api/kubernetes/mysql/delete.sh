#!/bin/sh
kubectl delete configmap mysql-config
kubectl delete service,pod mysql
