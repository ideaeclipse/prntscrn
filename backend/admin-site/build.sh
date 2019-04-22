#!/bin/sh
if [ "$(docker inspect -f '{{.State.Running}}' prntscrn-admin-panel)" ]; then
    docker kill prntscrn-admin-panel
    docker container rm prntscrn-admin-panel
    docker build -t react-app .
    docker run -dit -p 3001:3001 --restart=unless-stopped --name=prntscrn-admin-panel react-app
fi