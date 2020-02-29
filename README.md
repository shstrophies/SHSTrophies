# Saratoga-High-School-Trophy-Application

## Setup:

Use a 10.1 inch WXGA Tablet running API 28. Press download data and then load database from then on

## TODO:

# Backend:
- find out the structure of the spreadsheet and format of the database (Ujjwal & Carolina)
  - remove row ID from spreadsheet and fix seedDatabaseWorker.java
  
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
