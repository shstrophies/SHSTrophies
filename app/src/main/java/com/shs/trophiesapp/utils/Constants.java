package com.shs.trophiesapp.utils;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

public final class Constants {
    // Ujjwal's
    //public static final String DOWNLOAD_URL = "https://docs.google.com/spreadsheets/d/1opVRw44HNCm08cL_0wqbAiBdh5DROUH563LWNTwtSpA/export?gid=YOURGID&format=csv";
    public static final String BUG_REPORT_FORM_URL = "https://docs.google.com/forms/d/e/1FAIpQLSdWPGHDIwUss11ROFUS-BaJ2CM0x_jvcC_OiUIyBizXWqY1_Q/viewform";
    //https://drive.google.com/drive/folders/1uUnlIY1g8QrJMeidAtESJx-MJrDoLoLm/export?gid=0&format=csv
    public static final String DOWNLOAD_URL = "https://docs.google.com/spreadsheets/d/1DBYBQEN4fZd0ByzcxvvGMDZ--I2-Ku2Rr5zMRpbFRsU/export?gid=YOURGID&format=csv";
    public static final String DRIVE_URL = "https://drive.google.com/uc?export=download&id=";
    public static final String DRIVE_IMAGE_PREFIX = "https://drive.google.com/file/d/";

    public static final String tableName_1 = "Sports";
    public static final String columns_1 = "_Sports, Image URL";

    public static final String DATA_DIRECTORY_NAME = "SHSDATA";
    public static final String DATA_DIRECTORY_DISK_CACHE_IMAGES = "SHSDATA/DISK_CACHE_IMAGES/";
    public static final String SPORTS_GID ="0";

    public static final String BUG_REPORT_EMAIL = "ujjwal.krishnamurthi@gmail.com"; //TODO: Fix this email and log in on Kiosk to Trophy-App-specific email

    public static final String SPORTS_DIRECTORY_NAME = "sports";
    public static final String DATA_FILENAME_NAME = "exported.csv";
    public static final String DATABASE_NAME = "SHSDATABASE";

    public static int colors[] = new int[]{
            Color.parseColor("#FF3232"),
            Color.parseColor("#FF5C00"),
            Color.parseColor("#FF8900"),
            Color.parseColor("#009A28"),
            Color.parseColor("#00CB0C"),
            Color.parseColor("#009A95"),
            Color.parseColor("#006E9A"),
            Color.parseColor("#004CCB"),
            Color.parseColor("#4761FF"),
            Color.parseColor("#7A2AFF"),
            Color.parseColor("#6900B2"),
            Color.parseColor("#910094"),
            Color.parseColor("#8A0013"),
            Color.parseColor("#C62DDF") ,
            Color.parseColor("#A8C100")
    };

    public static int TROPHIES_PER_PAGE = 12;

    public static String DEFAULT_TROPHY = "default-trophy.png";

}
