# prntscrn-clone
Clone of the popular tool lightshot https://app.prntscr.com/en/index.html

This is project will utilize the public imgur api to store photos or local storage
The purpose of this is to create a private alternative to lightshot, with no adds and when you click on the link you get the raw photo or gif instead of a landing page used to make ad revenue

## Task tracking list
- Publications
  - [ ] Setup Secondary server to handle the sites
  - [ ] Create frontend webpage to display the homepage, allow for downloading of the installer, and creating user accounts
  - [ ] Modify backend to allow for linkage of uploaded images to user accounts
  - [ ] Allow users to view all urls they have uploaded.
  - [ ] Setup smtp server to handle mailing requests view the new domain (prntscrn.ca)
  - [ ] Setup kubernetes cluster to host the three services (website, admin panel, api)
  - [ ] Make it so the api is public and documented on the main site so developers can use this also as image storage.

- Client
  - [X] Take a screenshot of the main monitor and store it inside a temp directory
  - [X] Take a screenshot of a user slected region of the monitor and store it
  - [X] Create a network utility to allow the client to access the backend/login
  - [X] Once the photo is taken, upload the photo to the designated end point, and display the url that it was upload to, to the user
  - [ ] Make the application a "trayable app" that waits for either someone to click the icon and hit take screenshot or their shortcut
  - [x] User Installer

- Backend
  - [X] Boiler plate OAuth2.0 Rails system
  - [X] Basic admin panel to manage users
  - [X] Setup MySQL to work with boiler plate authentication system
  - [X] Create endpoints for client to interact with.
  - [X] Manage Uploads via admin website
  - [X] Kubernetes cluster to run all services
  - [X] "Make" file to install everything with 1 command
  - [ ] Staticstics on admin panel
