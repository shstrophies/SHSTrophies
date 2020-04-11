package com.shs.trophiesapp.database;

import android.util.Log;

import androidx.sqlite.db.SimpleSQLiteQuery;

import com.shs.trophiesapp.database.daos.TrophyAwardDao;
import com.shs.trophiesapp.database.daos.TrophyDao;
import com.shs.trophiesapp.database.entities.Trophy;
import com.shs.trophiesapp.database.relations.SportWithTrophies;
import com.shs.trophiesapp.database.entities.TrophyAward;
import com.shs.trophiesapp.database.relations.TrophyWithAwards;
import com.shs.trophiesapp.utils.Constants;

import java.util.List;
import java.util.stream.Collectors;


public class TrophyRepository {
    private static final String TAG = "TrophyRepository";
    private TrophyDao trophyDao;
    private TrophyAwardDao trophyAwardDao;
    private static volatile TrophyRepository instance;

    private TrophyRepository(TrophyDao trophyDao,
                             TrophyAwardDao trophyAwardDao) {
        this.trophyDao = trophyDao;
        this.trophyAwardDao = trophyAwardDao;
    }

    public List<TrophyAward> getAwards() {
        return trophyAwardDao.getAwards();
    }

    public List<SportWithTrophies> getSportWithTrophies() {
        // Gets List holding @Relation object
        return trophyDao.getSportWithTrophies();
    }

    public List<SportWithTrophies> getSportWithTrophiesBySportName(String sportName) {
        // Gets List holding @Relation object
        return trophyDao.getSportWithTrophiesBySportName(sportName);
    }

    public List<TrophyWithAwards> getTrophiesWithAwards() {
        return trophyDao.getTrophiesWithAwards();
    }

    public List<TrophyAward> getTrophyAwardsByYear(int year) {
        return trophyDao.getTrophyAwardsByYear(year);
    }

    public List<TrophyAward> getTrophyAwardsByPlayer(String player) {
        return trophyDao.getTrophyAwardsByPlayer("%" + player + "%");
    }

    public List<TrophyAward> getTrophyAwardsByPlayerLimited(String player, int page) {
        return trophyAwardDao.findByPlayerLimited(player, Constants.TROPHIES_PER_PAGE, page);
    }

    // select * from trophyaward where year in (1970,1971);
    // select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6));
    // select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (year IN (1961, 1983, 1992));
    // select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (year IN (1961, 1983, 1992)) AND ((player LIKE '%glen%') OR (player like '%Joy%'));
    // select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (year IN (1961, 1983, 1992)) AND ((player LIKE '%%'));
    // select * FROM trophyaward INNER JOIN trophy ON trophy.id=trophyId WHERE (trophy.sportId IN (1,2,3,4,5,6)) AND (title LIKE '%inspirational%')
    public List<TrophyAward> getTrophyAwardsBySportsAndTitlesAndYearsAndPlayers(List<Long> sportIds, List<String> titles, List<Integer> years, List<String> players) {
        String sportsExpr = sportIds.isEmpty() ? "" :
                "(trophy.sportId IN (" + sportIds.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "))";
        String titlesExpr = titles.isEmpty() ? "" :
                "(" + titles.stream().map(elem -> "(title LIKE \"%" + String.valueOf(elem).trim() + "%\")").collect(Collectors.joining(" OR ")) + ")";
        String yearsExpr = years.isEmpty() ? "" :
                "(year IN (" + years.stream().map(String::valueOf).collect(Collectors.joining(", ")) + "))";
        String playersExpr = players.isEmpty() ? "" :
                "(" + players.stream().map(elem -> "(player LIKE \"%" + String.valueOf(elem).trim() + "%\")").collect(Collectors.joining(" OR ")) + ")";

        boolean titlesSameAsPlayers = titles.stream().anyMatch(players::contains);
        Log.d(TAG, "getTrophyAwardsBySportsAndTitlesAndYearsAndPlayers: titlesSameAsPlayers ==> " + titlesSameAsPlayers);
        String expression = sportsExpr +
                (yearsExpr.isEmpty() ? "" : " AND " + yearsExpr) +
                ((titlesExpr.isEmpty() && !playersExpr.isEmpty()) ? " AND ( " + playersExpr + " )" :
                        (!titlesExpr.isEmpty() && playersExpr.isEmpty()) ? " AND ( " + titlesExpr + " )" :
                                (titlesExpr.isEmpty() && playersExpr.isEmpty()) ? "" :
                                        ((!titlesExpr.isEmpty() && !playersExpr.isEmpty() && !titlesSameAsPlayers) ? " AND ( " + titlesExpr + " AND " + playersExpr + " )" :
                                                " AND ( " + titlesExpr + " OR " + playersExpr + " )"));
        ;
        String querystr = "SELECT trophyaward.id, trophyId, year, player, category FROM trophyaward INNER JOIN trophy ON trophy.id = trophyId " +
                (expression.isEmpty() ? "" : (" WHERE " + expression));
        Log.d(TAG, "getTrophyAwardsBySportsAndTitlesAndYearsAndPlayers: query=" + querystr);
        SimpleSQLiteQuery query = new SimpleSQLiteQuery(querystr);
        return trophyDao.getTrophyAwardsByExpression(query);
    }

    public List<TrophyAward> getTrophyAwardsBySportAndYear(long sportId, int year) {
        if (sportId == -1)
            return trophyDao.getTrophyAwardsByYear(year);
        else
            return trophyDao.getTrophyAwardsBySportAndYear(sportId, year);
    }

    public List<TrophyAward> getTrophyAwardsBySportAndPlayer(long sportId, String player) {
        if (sportId == -1)
            return getTrophyAwardsByPlayer(player);
        else
            return trophyDao.getTrophyAwardsBySportAndPlayer(sportId, "%" + player + "%");
    }

    public List<TrophyWithAwards> getTrophiesWithAwardsByYearORSportORPlayer(int year, String sportName, String player) {
        return trophyDao.getTrophiesWithAwardsByYearORSportORPlayer(year, sportName, player);
    }

    public Trophy getTropyById(long id) {
        return trophyDao.getTrophyById(id).get(0);
    }

    public List<TrophyWithAwards> getTrophyWithAwardsByTrophyId(long trophyId) {
        return trophyAwardDao.getAwardsByTrophyId(trophyId);
    }

    public List<TrophyAward> getTrophiesByYear(int year) {
        return trophyAwardDao.findByYear(year);
    }

    public List<TrophyAward> getTrophiesByPlayer(String player) {
        return trophyAwardDao.findByPlayer(player);
    }

    public List<String> searchPlayerName(String str, int limit) {
        return trophyAwardDao.searchPlayerName("%" + str + "%", limit);
    }

    public List<String> searchTrophyTitle(String str, int limit) {
        return trophyDao.searchTrophyTitle("%" + str + "%", limit);
    }

    public List<String> searchYear(int from, int to, int limit) {
        List<Integer> listOfYears = trophyAwardDao.searchYear(from, to, limit);
        // convert listOfYears to list of year strings
        //ArrayList<String> listOfStrings = new ArrayList<String>();

        //converting integer array to string array


        /*
        for(int i=0; i<listOfYears.size(); i++){
            listOfStrings.add(""+listOfYears.get(i));
        }
        */
        return listOfYears.stream().map(String::valueOf).collect(Collectors.toList());

    }

    static TrophyRepository getInstance(
            TrophyDao trophyDao,
            TrophyAwardDao trophyAwardDao
    ) {
        if (instance == null) {
            synchronized (TrophyRepository.class) {
                if (instance == null)
                    instance = new TrophyRepository(trophyDao, trophyAwardDao);
            }
        }
        return instance;
    }
}