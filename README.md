# SHS Trophies 
___________________________________________________
### Links to original data:
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

- enable recyclerview pagination (Ujjwal)
  
- file hashing for seamless app restarts (Ujjwal)

(POSTPONED)- add FTS in the Database (Ujjwal & Carolina)
___________________________________________________
**Remaining Search Features**
___________________________________________________

- have search bar start without any suggestions at start
- have autocomplete with catergies. if you type in foo, you want 'football in sports' 
- add icons to search results (Arman)
- nice to have: show # of results found on search bar



type in "Arman Rafati"
suggest sports or years or trophies that would have a result

search results are:

"Arman Rafati"
"Arman Rafati Basketball" (make sure I have basketball trophies)
"Arman Rafati 2006" (because I have trophies in 2006)
"Arman Rafati most valuable player" (because I won an MVP trophy)


(add amazon image here)



___________________________________________________
**UI Related Backend**
___________________________________________________

- We should create a wrapper activity called MenuActivity that each Activity extends, and have that override onOptionsMenuItemClicked (whatever the method is), and have that deal with the menu options instead of copy pasting code between each activity.

- think about Team Trophy Text

- Fix possibility of looping bug if someone keeps on pressing on a name. have it be able to be clicked max once


___________________________________________________
### UI:
___________________________________________________

  
  - look into back buttons (nice to have, but not needed)
    
  

___________________________________________________
### Torrens/Leadership Kids
___________________________________________________

- update spreadsheet with real data so we can see if there will be storage and load time issues (S)
