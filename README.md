# prntscrn-clone
Clone of the popular tool lightshot https://app.prntscr.com/en/index.html

This is project will utilize the public imgur api to store photos or local storage
The purpose of this is to create a private alternative to lightshot, with no adds and when you click on the link you get the raw photo or gif

## End points from private backend
### OAuth2.0
- TODO
### Images
- All must not be user accessible, need to figure out how to make that happen
- POST /image/
  - INPUT: Must pass form data to use
  - LOGIC: Uploads image to imgur API and stores all data neccessary into tabel
  - RETURN: Returns a json string with status of upload, and url that will bring you to the image
- GET /image/{id}
  - INPUT: N/A
  - LOGIC: grabs imgur url from db, loads file into temp and sends to user
  - RETURN: raw image to screen
- DELETE /image/{id}
  - INPUT: N/A
  - LOGIC: grabs imgurl url from db, along with delete hash and sends the request to imgur, then removes the row from table
  - RETURN: status

## Task tracking list
- Client
  - [ ] Take a screenshot of the main monitor and store it inside a temp directory
  - [ ] Take a screenshot of a user slected region of the monitor and stor it
  - [ ] Create a network utility to allow the client to access the backend
  - [ ] Once the photo is taken, upload the photo to the designated end point, and display the url that it was upload to, to the user
  - [ ] Setup the client to user OAuth2.0 client to restrict usage of the service
  - [ ] Screen captcher support for quick gif's
  - [ ] User Installer
  - [ ] Updater from web
  - [ ] 2FA

- Backend
  - [X] Boiler plate OAuth2.0 Rails system
  - [X] Basic admin panel to manage users
  - [ ] Setup MySQL to work with boiler plate authentication system
  - [X] Create endpoints for client to interact with.
  - [ ] Manage Uploads via admin website
  - [ ] Add 2FA to boiler plate 
  - [ ] Staticstics on admin panel
  - [ ] User accessible API


- Auto purger
  - [ ] Let c work with mysql
  - [ ] Sleeper thread that has an accessible queue from rails backend
  - [ ] Execute everyhour where the stack of images will be removed from imgur and from local db
