# Saratoga-High-School-Trophy-Application


## Links to original data:
- figma: https://www.figma.com/file/q213rSM1bDSIGCbETgCeAL/Official-SHS-Trophy-UI?node-id=1%3A2
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


## Setup:

Use a 10.1 inch WXGA Tablet running API 28. Press download data and then load database from then on

# To extract the database from the device or emulator:

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




## TODO:

# Backend:
- new spreadsheet (Shayan and Carolina)
  - fix seedDatabaseWorker.java
  
- new DB schema (Ujjwal)
  
- download images in case wifi is slow? (Ujjwal & Carolina)

- remaining search tasks
     - homepage: add functionality for '{sport}, {name}'(ex: Football Goni), '{name}, {year}' (ex: Goni 1990), '{sport}, {year}' (ex: Football, 1990)
     - homepage: add a search by trophy functionality on homepage 
     - have a "no results found: we support these types of searches..." page if someone types in something and no results are found.
     
  
   
- add more of the trophyAward info (Ujjwal & Carolina)
      - add 'trophy_with_awards_activity' page
      - add 'corner_view_trophy_with_awards_activity.xml' which contains cards that go inside of      'trophy_with_awards_activity' page
- add cleanup code to clean export files (Shayan)

- players&years page: make the color matching so each year is associated with one color. Right now it does not work properly!

Here is an example of how color matching should work on players&years page--it's fine for trophy page:

If this is the Array of all colors:
[red, orange, green, blue, yellow, purple]

1960 cards --> all are red
1961 cards --> all are orange
...
1966 cards --> all are purple
Then it goes back to 1967 is red. And so on.


# UI:


>>>>>>> 71c0c60471561e1c11fdd90f8f81a9a6fb52d86a

Home screen: Round Home-screen Inside images of cards

Trophy page: make it so clicking text also brings it to the next page. Right now, only clicking the image will bring it to next page.

Players&years: make it so top left image links to zoomed in page and so normal cards are not clickable. (Make it obvious that the top left card is clickable)

Search results page: 

make it so top left image links to zoomed in image and that clicking normal cards links to the players&years page.

Research possible new layouts for this page


  
For torrens to do:
- update spreadsheet with real data and see if there will be storage and load time issues (S)
