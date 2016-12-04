# Synchro (Android)
Made for NUS Orbital 2016. Supports Android 4.1 (SDK 16) and up.

Only available for NUS students with valid NUSNET accounts for now. Login is via IVLE authentication. <br>
Note: Synchro will collect the following information from IVLE: name, faculty, major, and modules

## Purpose
Aims to provide a platform for NUS students to be able to easily group up with other students, whether for school projects, studying, or playing games. We identified a need for a more effective and flexible method of grouping up with people given that the user does not know many people in the university. This app can hopefully help more introverted people to find people with common interests and expand their networks. Also, the app would be a convenient way for people with many group projects to keep track of their progresses.

## Implemented Features
### Group Pages (CRUD)
Group page will be available for all other users to view after creation. Can be updated and deleted as well.
Groups can be tagged with multiple tags to increase number of hits when other users are searching.  

### Search Other Groups
All groups are publicly listed. Groups pages can be searched by name and tags.

### Join Groups
You can search for groups and join them by tapping the button at the bottom right corner of the group page.

### Profiles
All profiles are publicly listed. Meaning you will be able to view other users' profiles, and other users will be able to view your profile as well. There is also a personal self-intro that can be updated.

### Group Forum
Able to post messages in the group for other members to view and view other members' messages as well. Accessed by button at the bottom right corner of group page. <br>
Note: You will need to be part of the group to view the forum. <br>
Note: Does not support live updates, need to manually refresh the page for updates.

## Server API (the brother to this app)
https://github.com/kfwong/synchro-api

## Libraries Used
Ion: https://github.com/koush/ion  
CircleImageView: https://github.com/hdodenhof/CircleImageView  
TextDrawable: https://github.com/amulyakhare/TextDrawable  

## Screenshots

<img src="docs/screenshots/prototype02_screenshots 01.jpg">
<img src="docs/screenshots/prototype_03 screenshot.jpg">
