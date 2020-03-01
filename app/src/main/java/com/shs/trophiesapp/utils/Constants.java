package com.shs.trophiesapp.utils;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    public static final String DOWNLOAD_URL = "https://docs.google.com/spreadsheets/d/" +
            "1bCjaCRR1ezrEWUXnxYyPRpUK5nr6u3NTP7iEitLEyxo/export?gid=YOURGID&format=csv";
//    public static final String[] GIDS = {"0", "2132257028"};

    private static final Map<String, String> tabs = new HashMap<String, String>()
    {
        {
            put("Homepage", "0");
            put("Football", "6994296");
            put("Basketball", "1119657436");
            put("Track", "470902160");
            put("Soccer", "1791621088");
            put("Swimming", "462073050");
            put("Water Polo", "1525300849");
            put("Volleyball", "479108315");
            put("Tennis", "1576432332");
            put("Baseball", "1636311282");
        };
    };


    public static final String sportsGID ="0";
    public static final String trophiesGID ="2132257028";

    public static final String titleSports = "sports";
    public static final String titleTrophies = "trophies";

    public static final String DRIVE_URL = "https://drive.google.com/uc?export=download&id=";

    public static final String tableName_1 = "Sports";
    public static final String columns_1 = "_Sports, Image URL";

    public static final String DATA_DIRECTORY_NAME = "SHSDATA";
    public static final String DATA_FILENAME_NAME = "export";


    public static final String DATABASE_NAME = "SHSDATABASE";
}
