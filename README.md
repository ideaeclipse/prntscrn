# prntscrn-clone
Clone of the popular tool lightshot https://app.prntscr.com/en/index.html

This is project will utilize the public imgur api to store photos or local storage
The purpose of this is to create a private alternative to lightshot, with no adds and when you click on the link you get the raw photo or gif instead of a landing page used to make ad revenue

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
  - [X] Manage Uploads via admin website
  - [ ] Kubernetes shell to run all services
  - [ ] "Make" file to install everything with 1 command
  - [ ] Add 2FA to boiler plate 
  - [ ] Staticstics on admin panel


- Auto purger
  - [ ] Let c work with mysql
  - [ ] Sleeper thread that has an accessible queue from rails backend
  - [ ] Execute everyhour where the stack of images will be removed from imgur and from local db
