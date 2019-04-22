#!/bin/sh
if [ "$(docker inspect -f '{{.State.Running}}' prntscrn-api)" ]; then
    docker kill prntscrn-api
    docker container rm prntscrn-api
    docker build -t rails-app .
    docker run -dit -p 3000:3000 --restart=unless-stopped --name=prntscrn-api rails-app
fi
