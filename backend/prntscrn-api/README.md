# Prntscrn-api
Api for prnstscrn client to interact with along with the admin/public website
Ruby version: 2.6.X
Rails version: 5.2.3

## Authenticating Users
* There are two built in methods that allow you to authenticate a user before endpoint execution with either admin level privileges or user level privileges
* if you call auth_user before your endpoint both admin and regular users will have access
* if you call auth_admin before your endpoint only admin users will have access
    * Response Errors
        * Returns a status code of 400 if you are missing the auth token in your header
        * Returns a status code of 401 if any part of the authentication process didn't work
    * Success
        * Executes endpoint, see that endpoints description for errors

## Endpoints


### Users/Authentication endpoints
* POST /login
    * Params: json string with keys username and password
        ```json
        {
          "username": "test",
          "password": "test-password"
        }
        ```
    * Errors
        * Returns a status code of 400 if you have missing parameters
        * Returns a status code of 401 if you passed an invalid pair of credentials
    * Success
        * Returns a status code of 200 and a json string with an authentication token
            ```json
            {
              "token": "**token**"
            }
            ```

* GET /user
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * Handled by authentication
    * Success
        * Returns a json array of json strings containing users id, username and is_admin boolean

* POST /user
    * User must pass a valid Admin Authorization token in request header
    * Params: json string with keys username and password
        ```json
        {
          "username": "test",
          "password": "test-password"
        }
        ```
    * Errors
        * Returns a status code of 400 if you have missing parameters
        * Returns a status code of 401 if the username you passed is already a registered user
    * Success
        * Returns a status code of 200 a json string saying the user has been created, if you want an authentication token you must call /login

* DELETE /user/:id
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * Returns a status code of 400 if the user couldn't be deleted due to the id doesn't exist
    * Success
        * Json string saying user was deleted

* GET /auth_test
    * User must pass a valid Admin/User Authorization token in request header
    * If valid will return a json string saying Authorized
    * Else will return a json string saying unauthorized
    
* GET /admin_test
    * User must pass a valid Admin Authorization token in request header
    * If valid will return a json string saying Authorized
    * Else will return a json string saying unauthorized

### Prntscrn endpoints

* GET /image
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * All errors are handled by authentication methods
    * Success
        * Returns a json array of all images entries that exist, each sub json string contains one key, id

* GET /image/:id
    * Public endpoint
    * Errors
        * Returns a status code of 400 if the id doesn't exist in the database
    * Success
        * Renders a image to the browser

* POST /image
    * User must pass a valid User/Admin Authorization token in request header
    * Requires form data with the key file and the value must be an image file
      ```
      curl -H "Authorization: $TOKEN" -F "file=@meme.jpg" localhost:3000/image
      ```
    * Errors
        * All errors are handled by authentication methods
    * Success
        * Returns a json string with key url, which is a link to the image

* DELETE /image/:id
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * Returns a status code of 400 if the id doesn't exist in the database
    * Success
        * Returns a json string saying the image was deleted 

* GET /executable
    * Public
    * Error
        * None
    * Success
        * Returns a json array of all executables entries that exist, each sub json string contains one url and one version

* GET /executable/:version
    * Public
    * Errors
        * Returns a status code of 400 if the version doesn't exist in the database
    * Success
        * Downloads the file

* POST /executable
    * User must pass a valid Admin Authorization token in request header
    * Requires form data with the key jar and the value must be file, also must pass a key version with the version number
    * Errors
        * Authentication is handled by the application controller
        * Returns 400 if the version exists
    * Success
        * Returns a json string with the success status

* DELETE /executable/:version
    * User must pass a valid Admin Authorization token in request header
    * Errors
        * Returns a status code of 400 if the version doesn't exist in the database
    * Success
        * Returns a json string saying the image was deleted 

