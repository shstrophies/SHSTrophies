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

- add FTS in the Database (Ujjwal & Carolina)

- enable recyclerview pagination (Ujjwal & Carolina)

- add limit statements and offsets to the database(Ujjwal & Carolina)
  
- finish downloading images (Ujjwal)

- file hashing for seamless app restarts (ujjwal)

___________________________________________________
**Remaining Search Features**
___________________________________________________

add Hints using this- https://www.journaldev.com/14073/android-multi-search-filter-contacts (Ujjwal & Vignav)


- have autocomplete. if you type in foo, you want 'football in sports'

- show # of results found on search



Firstly, there are two types of searching that we can do.
1) I call it 'filter search'. When you type in 'f' on the homescreen, the cards which don't contain 'f' disappear.
2) I call it 'real search'. When you type in a name on the homescreen and press enter, you are taken to a new page with the search results




add functionality for:
- On Homepage: '{sport}, {name}'(ex: Football, Goni) -- real search
- On Homepage: '{name}, {year}' (ex: Goni, 1990) -- real search
- On Homepage: '{sport}, {year}' (ex: Football, 1990) -- real search
- On Homepage: be able to search for a trophy title IF there are no matches for the options above  -- real search

- On Trophy Page: when you start typing, it should do a filter search for the trophies -- filter search
- On Trophy Page: '{name}, {year}' (ex: Goni 1990) -- real search

- On players&trophies page: when you start typing, it should do a filter search for the names -- filter search
- On players&trophies page: when you start typing, it should do a filter search for the years -- filter search
- On players&trophies page: We don't need any 'real search' for this page. 



___________________________________________________
**Misc. Backend**
___________________________________________________

- have a "no results found: we support these types of searches..." page if someone types in something and no results are found.

___________________________________________________
**UI Related Backend**
___________________________________________________

- have the players&years cards clickable and have them show a screen with all trophies that person won.

- replace "--" with "Team Trophy"



- players&years page: make the color matching so each year is associated with one color. Right now it does not work properly!

Here is an example of how color matching should work on players&years page

If this is the Array of all colors:
[red, orange, green, blue, yellow, purple]

1960 cards --> all are red
1961 cards --> all are orange
...
1966 cards --> all are purple
Then it goes back to 1967 is red. And so on.

___________________________________________________
### UI:
___________________________________________________

  - Refine search results page
  
  - figure out which cropping attribute to use
    
  - eventually add an 'info page' so ppl see the history of the app, instructions on how to use it, etc.
  
  - remove dots on search bar
  
  - look into back buttons
  
  - create a footer for a icon to the credits page
  
  - add question mark to search bar with search options
  
  - adjust search page and tapping onto the card pages

___________________________________________________
### Torrens/Leadership Kids
___________________________________________________

- update spreadsheet with real data so we can see if there will be storage and load time issues (S)
