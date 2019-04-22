#!/bin/sh
docker run -dit -p 3000:3000 --restart=unless-stopped --name=prntscrn-api rails-app
