FROM node:9.6.1

RUN mkdir /app
WORKDIR /app

COPY package.json /app
COPY package-lock.json /app

RUN npm install

COPY . ./

CMD ["npm", "start"]