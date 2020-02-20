package com.shs.trophiesapp.utils;

public final class Constants {
    public static final String DOWNLOAD_URL = "https://docs.google.com/spreadsheets/d/" +
            "1bCjaCRR1ezrEWUXnxYyPRpUK5nr6u3NTP7iEitLEyxo/export?gid=YOURGID&format=csv";
    public static final String[] GIDS = {"0", "2132257028"};
    public static final String[] titles = {"sports", "trophies"};
    public static final String titleSports = titles[0];
    public static final String titleTrophies = titles[1];

    public static final String DRIVE_URL = "https://drive.google.com/uc?export=download&id=";

    public static final String tableName_1 = "Sports";
    public static final String columns_1 = "_Sports, Image URL";

    public static final String DATA_DIRECTORY_NAME = "SHSDATA";
    public static final String DATA_FILENAME_NAME = "export";


    public static final String DATABASE_NAME = "SHSDATABASE";
}
