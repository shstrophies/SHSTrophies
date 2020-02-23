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
        if (isExternalStorageAvailable())
            createDirectory(ROOT_DIRECTORY_NAME);
    }

    public void createDirectory(String directoryName) {
        if (!doesDirectoryExist(directoryName)) {
            File file = new File(Environment.getExternalStorageDirectory(), directoryName);
            file.mkdir();
        }
    }

    // ??
    public static File[] listFilesInDirectory(String dirPath) {
        // TODO
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        return files;
    }
    // ??



    public static void deleteOlderFiles(String directoryName, int keepNewestNumberOfFiles) {
        // TODO

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

        Log.d(TAG, "deleteOlderFiles: "+ files.toString());

        // no need to remove files
        if(files.length <= keepNewestNumberOfFiles){
            return;
        }

        // objective is to eliminate elements that that are the oldest, aka delete the files with the smallest numbers
        for(int i=0;i < (files.length - keepNewestNumberOfFiles); i++){
            files[i].delete();
            Log.d(TAG, "deleteOlderFiles: DELETING FILE: " + files[i]);
        }

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
