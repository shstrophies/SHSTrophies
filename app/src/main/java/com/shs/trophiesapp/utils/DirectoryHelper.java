package com.shs.trophiesapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;

import java.io.File;

public class DirectoryHelper extends ContextWrapper {

    private static final String TAG = "DirectoryHelper";
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

    public void createFolderDirectories() {
        if (isExternalStorageAvailable())
            createDirectory(ROOT_DIRECTORY_NAME);
    }

    public void createDirectory(String directoryName) {
        if (!doesDirectoryExist(directoryName)) {
            File file = new File(Environment.getExternalStorageDirectory(), directoryName);
            file.mkdir();
        }
    }

    public static File[] listFilesInDirectory(String directoryName) {
        // TODO

        return null;
    }

    public static void deleteFileFromDirectory(String directoryName, String fileName) {
        // TODO
        // see this.deleteFile method in ContextWrapper which this class extends from
    }

    public static void deleteOlderFiles(String directoryName, int keepNewestNumberOfFiles) {
        // TODO

    }




    private boolean doesDirectoryExist(String directoryName) {
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
