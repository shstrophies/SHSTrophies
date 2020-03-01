# Saratoga-High-School-Trophy-Application


## Links to original data:
- figma: https://www.figma.com/file/q213rSM1bDSIGCbETgCeAL/Official-SHS-Trophy-UI?node-id=1%3A2
- Images provided by Mr. Torrens: https://drive.google.com/drive/folders/1jjymid-BPNTvE8sj15OUT0PvNo9TRVqW
- Spreadsheet provided by Mr. Torrens of trophy information: https://drive.google.com/drive/folders/1uUnlIY1g8QrJMeidAtESJx-MJrDoLoLm
- Test Spreadsheet provided by Arman: https://docs.google.com/spreadsheets/d/1bCjaCRR1ezrEWUXnxYyPRpUK5nr6u3NTP7iEitLEyxo/edit#gid=0
- Example Spreadsheet Mr. Torrens will be using: https://docs.google.com/spreadsheets/d/1opVRw44HNCm08cL_0wqbAiBdh5DROUH563LWNTwtSpA/edit?ts=5e5adf83#gid=0


-- To get the image url: click on the image, then on the right hand side top, click three dots, then click on "Open in new window", and the copy the url


-- P7220156.JPG - Trophy with one year many players - football: https://drive.google.com/file/d/1zOlNzaB-VGyegnQ_3UWZtq64KsVLK4Q7/view
-- P7220173.JPG - Trophy with no players - football:  https://drive.google.com/file/d/1V1SN5c5wDbUyB0yNlPphWoLM-yMQor98/view
-- P7220176.JPG - Trophy with no year no players - football: https://drive.google.com/file/d/1cEOO6vceis57f0MU_wSk-hTojf_Ct_0W/view
-- P7220178.JPG - Trophy with many years and one player per year - football: https://drive.google.com/file/d/1ZZkED9UHRSnEVuw-oJVN75B4FvV8jCfX/view

https://drive.google.com/file/d/1zOlNzaB-VGyegnQ_3UWZtq64KsVLK4Q7/view?usp=sharing


## Setup:

Use a 10.1 inch WXGA Tablet running API 28. Press download data and then load database from then on

## TODO:

# Backend:
- new spreadsheet (Shayan and Carolina)
  - fix seedDatabaseWorker.java
  
- new DB schema (Ujjwal)
  
- download images in case wifi is slow? (Ujjwal & Carolina)
- add more search capability especially with homepage (Shayan)
  - homepage searching
    - <s> search for sports within homepage</s>
    ~~- search for trophies within homepage~~
    ~~- search for names within homepage~~
      - possible suggestion: should we allow search by year? If we decide yes, then we need it so if you type in 2005 on homepage search bar, cards will be organized with the year on the top instead of the Sport
   - trophy page searching
      ~~- search for trophies within trophy page~~
      - search for names within trophy page (i'll check if it works when im home)
   - trophy players and years page searching
      - search for people's names(Shayan)
      - search for year(Shayan)
- add more of the trophy info (Ujjwal & Carolina)
      - add 'trophy_players_and_years_activity' page
      - add 'corner_view_trophy_players_and_years_activity.xml' which contains cards that go inside of      'trophy_players_and_years_activity' page
- add cleanup code to clean export files (Shayan)
- try to assign a certain color to a certain year. ex: lets say we have 10 colors in our array ["red","green","blue","orange", etc....]. Then if the first date we have is 1960, then all trophies from 1960 will be red, from 1961 will be green, and so on... and then once we run out of the colors, we loop back from the start until everything is colored.

# UI:
- ~~add search bar (Ujjwal)~~

>>>>>>> 71c0c60471561e1c11fdd90f8f81a9a6fb52d86a
- finish homescreen UI (Arman & Vignav) 
  - design cards
    - round the inside images
  - design top header
  - design search bar
    - make screen move up on searchbar tap

- finish trophy page UI (Arman & Vignav) 
  - design cards
    - add proper rounding to cards (maybe change to card view)
    - make cards clickable for new screen
  - design top header 
  - design search bar
    - make screen move up on searchbar tap

- finish names page UI (Arman & Vignav) 
  - design cards
    - add proper rounding to cards (maybe change to card view)
    - allow to click on trophy to get more details about it (Arman & Vignav)
  - design top header 
    - add proper image on left of text
  - design search bar
    - make screen move up on searchbar tap

- finish zoomed in trophy UI (Arman & Vignav)
  - design cards
      - add proper rounding to cards (maybe change to card view)

- design search results page UI (Arman & Vignav)
  - design search results cards
  
  
For torrens to do:
- update spreadsheet with real data and see if there will be storage and load time issues (S)
