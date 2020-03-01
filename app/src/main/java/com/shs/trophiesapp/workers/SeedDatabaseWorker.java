package com.shs.trophiesapp.workers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.shs.trophiesapp.data.AppDatabase;
import com.shs.trophiesapp.data.entities.Sport;
import com.shs.trophiesapp.data.entities.Trophy;
import com.shs.trophiesapp.utils.Constants;
import com.shs.trophiesapp.utils.DirectoryHelper;

import static com.shs.trophiesapp.utils.CSVUtils.parseLine;

public class SeedDatabaseWorker extends Worker {
    private static final String TAG = "SeedDatabaseWorker";

    public SeedDatabaseWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            Log.d(TAG, "doWork: loading data (into database)");
            DirectoryHelper.createDirectory(getApplicationContext());
            Sport[] sportCSVData = getSportsCSVData();
            Log.d(TAG, "onCreate: sportCSVData length=" + sportCSVData.length);
            AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());
            appDatabase.sportDao().insertAll(sportCSVData);
            Log.d(TAG, "doWork: sport data loaded");
            Trophy[] trophyCSVData = getTrophiesCSVData();
            Log.d(TAG, "onCreate: trophyCSVData length=" + trophyCSVData.length);
            appDatabase.trophyDao().insertAll(trophyCSVData);
            Log.d(TAG, "doWork: trophy data loaded");

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private static Sport[] getSportsCSVData() {
        Log.d(TAG, "getSportsCSVData: ");
        List<Sport> sports = new ArrayList<>();
        try {
            File file = DirectoryHelper.getLatestFilefromDir(Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME + "/" + Constants.SPORTS_DIRECTORY_NAME + "/");
            Log.d(TAG, "getSportsCSVData: getting sport data from file=" + file.getAbsolutePath());
            Scanner scanner = new Scanner(file);
            boolean first = true;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                Log.d(TAG, "onReceive: line=" + line);
                List<String> commaSeparatedLine = parseLine(line);
                if (first) first = false;
                else {
                    String sport = commaSeparatedLine.get(0); 
                    String url = commaSeparatedLine.get(2);
                    sports.add(new Sport(sport, url));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return sports.toArray(new Sport[0]);
    }

    private static Trophy[] getTrophiesCSVData() {
        Log.d(TAG, "getTrophiesCSVData: ");
        List<Trophy> trophies = new ArrayList<>();

        Sport[] sports = getSportsCSVData();
        for(int i=0; i<sports.length; i++) {
            String sport = sports[i].sport_name;
            try {
                File file = DirectoryHelper.getLatestFilefromDir(Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME + "/" + sport + "/");
                if(file != null) {
                    Log.d(TAG, "getTrophiesCSVData: getting trophy data from file=" + file.getAbsolutePath());
                    Scanner scanner = new Scanner(file);
                    boolean first = true;
                    while (scanner.hasNext()) {
                        List<String> line = parseLine(scanner.nextLine());
                        if (first) first = false;
                        else {
                            String[] players = line.get(3).split(",");
                            for(String player : players) {
                                if(!line.get(0).isEmpty()) {
                                    String year = line.get(0);
                                    String title = line.get(1);
                                    String uri = line.get(2);
                                    String category = "TBD";
                                    Log.d(TAG, "getTrophiesCSVData: line.get(0)=" + line.get(0) + " line.get(1)=" + line.get(1));
                                    trophies.add(new Trophy(sport, Integer.parseInt(year), title, uri, player, category));
                                }

                            }
                        }
                    }
                }
                else {
                    Log.d(TAG, "getTrophiesCSVData: no files found ...");
                }

            } catch (Exception e) { e.printStackTrace(); }
        }

        return trophies.toArray(new Trophy[0]);
    }
}
