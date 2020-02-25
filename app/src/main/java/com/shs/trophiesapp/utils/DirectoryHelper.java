package com.shs.trophiesapp.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

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
        if (isExternalStorageAvailable()) {
            createDirectory(ROOT_DIRECTORY_NAME);
            createDirectory(Constants.DATA_DIRECTORY_SPORT_IMAGES);
            createDirectory(Constants.DATA_DIRECTORY_TROPHY_IMAGES);
        }
    }

    public void createDirectory(String directoryName) {
        if (!doesDirectoryExist(directoryName)) {
            File file = new File(Environment.getExternalStorageDirectory(), directoryName);
            file.mkdir();
        }
    }

    public static File[] listFilesInDirectory(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        Log.d(TAG, "listFilesInDirectory: dirPath="+ dirPath + Arrays.toString(files));

        return files;
    }



    public static void deleteOlderFiles(String directoryName, int keepNewestNumberOfFiles) {

        // actual directory, with ALL the files inside
        File dir = new File(directoryName);
        // file array with all the files
        File[] files = dir.listFiles(); //important

        // sort files from lowest to biggest number, oldest to newest
        Arrays.sort(files, new Comparator<File>(){
            public int compare(File f1, File f2)
            {
                return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
            } });

        Log.d(TAG, "deleteOlderFiles: "+ Arrays.toString(files));
        // no need to remove files
        if(files.length <= keepNewestNumberOfFiles){

            Log.d(TAG, "deleteOlderFiles: nothing to delete");
            return;
        }

        // objective is to eliminate elements that that are the oldest, aka delete the files with the smallest numbers
        for(int i=0;i < (files.length - keepNewestNumberOfFiles); i++){
            Log.d(TAG, "deleteOlderFiles: DELETING FILE: " + files[i]);
            files[i].delete();
        }

    }

    private boolean doesDirectoryExist(String directoryName) {
        File file = new File(Environment.getExternalStorageDirectory() + "/" + directoryName);
        return file.isDirectory() && file.exists();
    }

    public static File getLatestFilefromDir(String dirPath){
        Log.d(TAG, "getLatestFilefromDir: dirPath=" + dirPath);
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        Log.d(TAG, "getLatestFilefromDir: " + Arrays.toString(files));
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
