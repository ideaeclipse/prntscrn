# prntscrn-clone
Clone of the popular tool lightshot https://app.prntscr.com/en/index.html

This is project will utilize the public imgur api to store photos or local storage
The purpose of this is to create a private alternative to lightshot, with no adds and when you click on the link you get the raw photo or gif

## Task tracking list
1. Client
- [ ] Take a screenshot of the main monitor and store it inside a temp directory
- [ ] Take a screenshot of a user slected region of the monitor and stor it
- [ ] Create a network utility to allow the client to access the backend
- [ ] Once the photo is taken, upload the photo to the designated end point, and display the url that it was upload to, to the user
- [ ] Setup the client to user OAuth2.0 client to restrict usage of the service
- [ ] User Installer
- [ ] Updater from web
- [ ] 2FA

2. Backend
- [ ] Boiler plate OAuth2.0 Rails system
- [ ] Setup MySQL to work with boiler plate authentication system
- [ ] Create endpoints for client to interact with.
  - [ ] POST /image/
    - [ ] test
  - [ ] GET /image/{id}
  - [ ] DELETE /image
- [ ] Add 2FA to boiler plate
- [ ] Staticstics and admin panel
  - [ ] GET /admin/


3. Auto purger
- [ ] Let c work with mysql
- [ ] Sleeper thread that has an accessible queue from rails backend
- [ ] Execute everyhour where the stack of images will be removed from imgur
