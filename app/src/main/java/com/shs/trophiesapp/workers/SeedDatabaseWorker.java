package com.shs.trophiesapp.workers;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.shs.trophiesapp.data.SportData;
import com.shs.trophiesapp.data.TrophyAwardData;
import com.shs.trophiesapp.database.AppDatabase;
import com.shs.trophiesapp.database.entities.Sport;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.entities.TrophyAward;
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
            DirectoryHelper.listFilesInDirectoryRecursively(Environment.getExternalStorageDirectory() + "/" + Constants.DATA_DIRECTORY_NAME);
            List<SportData> sportCSVData = getSportsCSVData();
            Log.d(TAG, "onCreate: sportCSVData length=" + sportCSVData.size());
            AppDatabase appDatabase = AppDatabase.getInstance(getApplicationContext());

            List<Sport> list = sportCSVData.stream()
                    .filter(Objects::nonNull)
                    .map(sd -> new Sport(sd.name, sd.imageUrl)).collect(Collectors.toList());
            appDatabase.sportDao().insertAll(list.toArray(new Sport[0]));
            List<Sport> sports = appDatabase.sportDao().getAll();
            for (Sport sport : sports) {
                Log.d(TAG, "doWork: got sport=" + sport.name);
                List<TrophyAwardData> trophyAwardCSVData = getTrophiesCSVData(sport.name);
                Log.d(TAG, "onCreate: trophyAwardCSVData length=" + trophyAwardCSVData.size());
                ArrayList<Trophy> trophies = new ArrayList();
                ArrayList<TrophyAward> awards = new ArrayList<>();
                HashMap<String, Trophy> map = new HashMap<>();
                for(TrophyAwardData awarditem : trophyAwardCSVData) {
                    Trophy trophy = new Trophy(awarditem.getTitle(), awarditem.getUrl());
                    Trophy trophyItem = map.get(awarditem.getUrl());
                    if(trophyItem == null) {
                        map.put(awarditem.getUrl(), trophy);
                        trophies.add(trophy);
                        long sportId = appDatabase.trophyDao().insert(sport);
                        trophy.setSportId(sportId);
                        long id = appDatabase.trophyDao().insert(trophy);
                        trophy.setId(id);
                    }
                    else trophy.setId(trophyItem.getId());
                    TrophyAward award = new TrophyAward(trophy.getId(), awarditem.getYear(), awarditem.getPlayer(), awarditem.getCategory());
                    awards.add(award);
                }
                appDatabase.trophyDao().insert(sport, trophies);
                appDatabase.trophyAwardDao().insertAll(awards.toArray(new TrophyAward[0]));
                Log.d(TAG, "doWork: trophy data loaded");
            }

            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure();
        }
    }

    private static List<SportData> getSportsCSVData() {
        Log.d(TAG, "getSportsCSVData: ");
        List<SportData> sports = new ArrayList<>();
        try {
            File file = DirectoryHelper.getLatestFilefromDir(Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME + "/" + Constants.SPORTS_DIRECTORY_NAME + "/");
            Log.d(TAG, "getSportsCSVData: getting sport data from file=" + file.getAbsolutePath());
            Scanner scanner = new Scanner(file);
            boolean first = true;
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                Log.d(TAG, "getSportsCSVData: line=" + line);
                List<String> commaSeparatedLine = parseLine(line);
                if (first) first = false;
                else {
                    String sport = commaSeparatedLine.get(0);
                    String url = commaSeparatedLine.get(2);
                    sports.add(new SportData(sport, url));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sports;
    }

    private static List<TrophyAwardData> getTrophiesCSVData(String sport) {
        Log.d(TAG, "getTrophiesCSVData: ");
        List<TrophyAwardData> trophyData = new ArrayList<>();

        try {
            File file = DirectoryHelper.getLatestFilefromDir(Environment.getExternalStorageDirectory() + "/" + DirectoryHelper.ROOT_DIRECTORY_NAME + "/" + sport + "/");
            if (file != null) {
                Log.d(TAG, "getTrophiesCSVData: getting trophy data from file=" + file.getAbsolutePath());
                Scanner scanner = new Scanner(file);
                boolean first = true;
                while (scanner.hasNext()) {
                    List<String> line = parseLine(scanner.nextLine());
                    if (first) first = false;
                    else {
                        String[] players = line.get(3).replaceAll("\"", "").split(",");
                        for (String player : players) {
                            if (!line.get(0).isEmpty()) {
                                String year = line.get(0);
                                String title = line.get(1);
                                String uri = line.get(2);
                                String category = "TBD";
                                Log.d(TAG, "getTrophiesCSVData: line.get(0)=" + line.get(0) + " line.get(1)=" + line.get(1));
                                trophyData.add(new TrophyAwardData(sport, Integer.parseInt(year), title, uri, player, category));
                            }

                        }
                    }
                }
            } else {
                Log.d(TAG, "getTrophiesCSVData: no files found ...");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        //return trophyData.toArray(new TrophyAwardData[0]);
        return trophyData;
    }
}
