# Admin-Site
* This site is used to manage prntscrn users and test backend endpoints
## Docker File
* To run with docker execute the following command
```bash
chmod u+rtx build.sh
./build.sh
```

## Env file
* Create a file called .env in parent directory of the project and add the following values to the file
* Where $URL is the ip of the backend
```
PORT=3001
REACT_APP_BACKEND=$URL
```
