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

  
- file hashing merge into master (Needs Fixing)(Ujjwal)

- enable recyclerview pagination (In Progress)(Ujjwal)


~~(POSTPONED)- add FTS in the Database (Ujjwal & Carolina)~~
___________________________________________________
**Remaining Search Features**
___________________________________________________

- suggestions for homepage (more in depth info about how this should be implemented is at the bottom of this file)
- add suggestions for trophy page 
- add suggestions for players page

- fix advanced search bugs

- nice to have: show # of results found on search bar suggestion
- add different icons to search maybe






___________________________________________________
**UI Related Backend**
___________________________________________________

- We should create a wrapper activity called MenuActivity that each Activity extends, and have that override onOptionsMenuItemClicked (whatever the method is), and have that deal with the menu options instead of copy pasting code between each activity. (Ujjwal)

- Fix possibility of looping bug if someone keeps on pressing on a name. have it be able to be clicked max once

- fix searching on homepage to work

- add onclicklistener to trophy image in the search results page

- add onclicklistener to trophy image in the personal award's page

- make it so if there is no image in the spreadsheet, it will read in the default trophy




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

General explanation:

When you type in a few letters of a name:

There will be 1 suggestion for "{the Name}" (in players)
There will be x amount of suggestions for "{theName} {sportName}". BTW, there's no "in..." for these
    - x represents the number of sports this player has trophies for. 
    - Let's say he does Basketball, Baseball, and Soccer.
    - in this case, there will be "{theName} {sportName}" 3 times 
    - ex: "Jeff Holmes Basketball", "Jeff Holmes Baseball", "Jeff Holmes Soccer"
There will be x amount of suggestions for "{theName} {trophy year}"
    - x represents the number of years this player has trophies for. 
    - Let's say he won in 1995, 1996, 1996, 1997.
    - in this case, there will be "{theName} {the sport}" 3 times  (we dont have duplicates for year) 
    - ex: "Jeff Holmes 1995", "Jeff Holmes 1996", "Jeff Holmes 1997"
    
    
Now, let's say they have "Jeff Homles" in their search bar and then type a 1. 
So the search space has  "Jeff Homles 1" inside of it.
Immediatly, all of the "Jeff Holmes {sportName}" suggestions will go away since the user is clearly looking for a year

Now, let's say they have "Jeff Homles" in their search bar and then type a "B". 
So the search space has  "Jeff Homles B" inside of it.
Immediatly, all of the "Jeff Holmes {trophy year}" suggestions will go away since the user is clearly not looking for a year

At this point, lets say they have "Jeff Homles 1995" in search bar.
As soon as they finish the 4 digit number:

It will now expect a sport name.

I think just supporting Name+ Year or Name + Sport search suggestions is good enough for homepage if we include this advnaced search that I was thinking about.


Let's say they type in the name of a sport. Let's use "Football". The subsequent suggestions would be:
"Football {the years of football trophies}"
"Football {different types of football trophy names}"


Mike Cable M
___________

Mike Cable in Players
Mike Calbe Basketball
Mike Cable Football
Mike Cable 1995
Mike Cable 1996
Mike Cable 1997
Mike Calble Most Insipration
Mike Cable Fearless Player


Spreadsheet assignments:
------------------------

./golf/boys/P7220035.JPG,genius
./field hockey/girls/P7220133.JPG,genius
./field hockey/girls/P7220155.JPG,genius
./wrestling/P7220236.JPG,genius
./wrestling/P7220037.JPG,genius
./wrestling/P7220190.JPG,genius
./wrestling/P7220028.JPG,genius
./football/P7220255.JPG,boss
./football/P7220320.JPG,boss
./football/P7220322.JPG,boss
./football/P7220243.JPG,boss
./football/P7220247.JPG,boss
./football/P7220324.JPG,boss
./football/P7220250.JPG,boss
./football/P7220286.JPG,boss
./football/P7220319.JPG,boss
./football/P7220182.JPG,boss
./football/P7220194.JPG,boss
./football/P7220221.JPG,boss
./football/P7220208.JPG,boss
./football/P7220156.JPG,boss
./football/P7220185.JPG,boss
./football/P7220230.JPG,boss
./football/P7220219.JPG,boss
./football/P7220193.JPG,boss
./football/P7220178.JPG,boss
./football/P7220203.JPG,boss
./football/P7220216.JPG,boss
./football/P7220176.JPG,boss
./football/P7220214.JPG,boss
./football/P7220201.JPG,boss
./football/P7220173.JPG,genius
./football/P7220198.JPG,genius
./football/P7220239.JPG,genius
./football/P7220238.JPG,genius
./football/P7220260.JPG,genius
./football/P7220249.JPG,genius
./baseball/P7220269.JPG,ucla
./baseball/P7220296.JPG,ucla
./baseball/P7220080.JPG,ucla
./baseball/P7220268.JPG,ucla
./baseball/P7220295.JPG,ucla
./baseball/P7220292.JPG,ucla
./baseball/P7220235.JPG,ucla
./baseball/P7220277.JPG,ucla
./baseball/P7220271.JPG,ucla
./softball/P7220121.JPG,ucla
./soccer/girls/P7220131.JPG,ucla
./soccer/girls/P7220124.JPG,ucla
./soccer/girls/P7220127.JPG,ucla
./soccer/girls/P7220075.JPG,ucla
./soccer/girls/P7220099.JPG,ucla
./soccer/boys/P7220134.JPG,ucla
./soccer/boys/P7220086.JPG,ucla
./soccer/boys/P7220084.JPG,ucla
./soccer/boys/P7220076.JPG,ucla
./soccer/boys/P7220117.JPG,ucla
./basketball/girls/P7220233.JPG,vigvig
./basketball/boys/P7220225.JPG,vigvig
./basketball/boys/P7220229.JPG,vigvig
./basketball/boys/P7220266.JPG,vigvig
./basketball/P7220316.JPG,vigvig
./waterpolo/girls/P7220016.JPG,vigvig
./waterpolo/girls/P7220006.JPG,vigvig
./waterpolo/boys/P7220026.JPG,vigvig
./waterpolo/boys/P7220019.JPG,vigvig
./waterpolo/P7220002.JPG,vigvig
./volleyball/girls/P7220068.JPG,vigvig
./volleyball/boys/P7220109.JPG,vigvig
./volleyball/boys/P7220063.JPG,vigvig
./volleyball/boys/P7220110.JPG,vigvig
./volleyball/boys/P7220065.JPG,vigvig
./cross country/P7220042.JPG,vigvig
./cross country/girls/P7220051.JPG,vigvig
./cross country/girls/P7220060.JPG,vigvig
./cross country/P7220041.JPG,vigvig
./cross country/P7220046.JPG,vigvig
./cross country/boys/P7220054.JPG,vigvig
./cross country/boys/P7220317.JPG,vigvig
./cross country/boys/P7220048.JPG,vigvig
./dance/P7220326.JPG,genius
./dance/P7220328.JPG,genius
./track and field/girls/P7220093.JPG,genius
./track and field/girls/P7220102.JPG,genius
./track and field/boys/P7220095.JPG,
./track and field/boys/P7220082.JPG,
./track and field/boys/P7220276.JPG,
./swimming/girls/P7220105.JPG,
./swimming/girls/P7220107.JPG,
./swimming/boys/P7220032.JPG,
./swimming/boys/P7220029.JPG,
./swimming/boys/P7220039.JPG,
./swimming/P7220005.JPG,
./swimming/P7220272.JPG,
./tennis/girls/P7220168.JPG,
./tennis/girls/P7220157.JPG,
./tennis/girls/P7220146.JPG,
./tennis/girls/P7220144.JPG,
./tennis/girls/P7220160.JPG,
./tennis/girls/P7220163.JPG,
./tennis/girls/P7220158.JPG,
./tennis/girls/P7220165.JPG,
./tennis/girls/P7220114.JPG,
./tennis/boys/P7220284.JPG,
./tennis/boys/P7220141.JPG,
./tennis/boys/P7220152.JPG,
./tennis/boys/P7220162.JPG,
./tennis/boys/P7220167.JPG,
./tennis/boys/P7220170.JPG,
./tennis/boys/P7220139.JPG,
./overall/P7220257.JPG,
./overall/P7220291.JPG,
./overall/P7220187.JPG,
./overall/P7220314.JPG,
./overall/P7220299.JPG,
./overall/P7220305.JPG

