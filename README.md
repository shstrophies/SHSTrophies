# SHS Trophies 
___________________________________________________
### Links to original data:
- master folder with all info for torrens: https://drive.google.com/open?id=1cZaUjLzV3hIbhGMP-mc-uywEq8HcYkPS
- Figma UI Designs: https://www.figma.com/file/q213rSM1bDSIGCbETgCeAL/Official-SHS-Trophy-UI?node-id=1%3A2
- Images provided by Mr. Torrens: https://drive.google.com/drive/folders/1jjymid-BPNTvE8sj15OUT0PvNo9TRVqW
- Spreadsheet provided by Mr. Torrens of trophyAward information: https://drive.google.com/drive/folders/1uUnlIY1g8QrJMeidAtESJx-MJrDoLoLm
- Test Spreadsheet provided by Arman: https://docs.google.com/spreadsheets/d/1bCjaCRR1ezrEWUXnxYyPRpUK5nr6u3NTP7iEitLEyxo/edit#gid=0
- Example Spreadsheet Mr. Torrens will be using: https://docs.google.com/spreadsheets/d/1DBYBQEN4fZd0ByzcxvvGMDZ--I2-Ku2Rr5zMRpbFRsU/edit#gid=0
- Database schema design: https://docs.google.com/document/d/1I4Tbaf91UoqEknFLpeujyCxKXef-D9utp799DpYx_7g/edit

-- To get the image url: click on the image, then on the right hand side top, click three dots, then click on "Open in new window", and the copy the url


-- P7220156.JPG - Trophy with one year many players - football: https://drive.google.com/file/d/1zOlNzaB-VGyegnQ_3UWZtq64KsVLK4Q7/view
-- P7220173.JPG - Trophy with no players - football:  https://drive.google.com/file/d/1V1SN5c5wDbUyB0yNlPphWoLM-yMQor98/view
-- P7220176.JPG - Trophy with no year no players - football: https://drive.google.com/file/d/1cEOO6vceis57f0MU_wSk-hTojf_Ct_0W/view
-- P7220178.JPG - Trophy with many years and one player per year - football: https://drive.google.com/file/d/1ZZkED9UHRSnEVuw-oJVN75B4FvV8jCfX/view

https://drive.google.com/file/d/1zOlNzaB-VGyegnQ_3UWZtq64KsVLK4Q7/view?usp=sharing

___________________________________________________
## Setup:

Use a 10.1 inch WXGA Tablet running API 28. Press download data and then load database from then on.
___________________________________________________

## To extract the database from the device or emulator:

* download SQLLiteStudio (https://sqlitestudio.pl/index.rvt?act=download)
* Use the following command to get the name of your device or emulator:
    > adb devices
* set environment variable DEVICE with the name of your device or emulator:
    > export DEVICE=emulator-5554
* run the following commands:
    > adb -s $DEVICE shell run-as com.shs.trophiesapp chmod -R 777 databases &&
        adb -s $DEVICE shell "mkdir -p /sdcard/tempDB" && 
        adb -s $DEVICE shell run-as com.shs.trophiesapp "cp -r databases /sdcard/tempDB/." && 
        adb -s $DEVICE pull sdcard/tempDB/ && 
        adb -s $DEVICE shell "rm -r /sdcard/tempDB/*"

___________________________________________________

## TODO:
___________________________________________________

### Backend:
___________________________________________________
**Database related**
___________________________________________________
- enable recyclerview pagination (In Progress)(Ujjwal)


~~(POSTPONED)- add FTS in the Database (Ujjwal & Carolina)~~
___________________________________________________
**Remaining Search Features**
___________________________________________________

- suggestions for homepage (more in depth info about how this should be implemented is at the bottom of this file)
- add suggestions for trophy page 
- add suggestions for players page

- fix advanced search bugs (Ujjwal)

- add different icons to search maybe






___________________________________________________
**UI Related Backend**
___________________________________________________

- Finish Trophy Modal Design and Enabling (Ujjwal)

- fix searching on homepage to work





___________________________________________________
### UI:
___________________________________________________
- add content for about the app
- add content for report a bug
  

___________________________________________________
### Torrens/Leadership Kids
___________________________________________________

- update spreadsheet with real data so we can see if there will be storage and load time issues (In Progress)

___________________________________________________
### Info on search
___________________________________________________
When you tap on the search bar, there will be no existing results underneath it. 




Mike Cable 
___________

Mike Cable in Players
Mike Calbe Basketball
Mike Cable Football
Mike Cable 1995
Mike Cable 1996
Mike Cable 1997
Mike Calble Most Insipration
Mike Cable Fearless Player

___________________________________________________
### Ujjwal TODO:
___________________________________________________

- Implement search suggestions on the Trophy Page. The searching works but no suggestions show up (i think you can copy most of the existing code from the homepgae search). But, one thing to note is that suggestions for Trophy page should only be for things that are inside of trophy page. 
- Implement search functionality and suggestions on the Players Page. Neither the searching nor suggestions work for this activity. 
- Fix search suggestions on homepage. When you tap search bar right now, it shows shaquille O'Neil, etc. It should show nothing when you first tap on it. Then as you type a letter, stuff should start appearing.
- If someone types in football on homepage, searching should bring them to the football trophies page.
- Fix advanced search. Rn, if you type in 1990 into the Names section, it just shows all results. It should show none since no one is named 1990. Make sure people can only enter numbers inside of the Year space or else we'll tell them it must be a year
- add pagination

~~- Fix sizing of trophy image when you tap on it (cuz rn they show up really small and are grainy when i try to adjust the size)~~

~~- add a pinch zoom in for the trophy images. (maybe use a library like this https://github.com/davemorrissey/subsampling-scale-image-view to start).~~
