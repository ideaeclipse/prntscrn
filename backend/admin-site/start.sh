#!/bin/sh
docker run -dit -p 3001:3001 --restart=unless-stopped --name=prntscrn-admin-panel react-app