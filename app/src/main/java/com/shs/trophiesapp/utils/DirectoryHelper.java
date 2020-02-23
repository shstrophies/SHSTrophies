package com.shs.trophiesapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import java.io.File;

public class DirectoryHelper extends ContextWrapper {

    public static final String ROOT_DIRECTORY_NAME = Constants.DATA_DIRECTORY_NAME;

    private DirectoryHelper(Context context) {
        super(context);
        createFolderDirectories();
    }

    public static void createDirectory(Context context) {
        new DirectoryHelper(context);
    }

    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    private void createFolderDirectories() {
        if (isExternalStorageAvailable()) {
            createDirectory(ROOT_DIRECTORY_NAME);
            createDirectory(Constants.DATA_DIRECTORY_SPORT_IMAGES);
            createDirectory(Constants.DATA_DIRECTORY_TROPHY_IMAGES);
        }
    }

    private void createDirectory(String directoryName) {
        if (!isDirectoryExists(directoryName)) {
            File file = new File(Environment.getExternalStorageDirectory(), directoryName);
            file.mkdir();
        }
    }

    private boolean isDirectoryExists(String directoryName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + directoryName);
        return file.isDirectory() && file.exists();
    }

    public static File getLatestFilefromDir(String dirPath){
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile;
    }
}
