package com.shs.trophiesapp.data;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.shs.trophiesapp.data.daos.SportDao;
import com.shs.trophiesapp.data.daos.SportDao_Impl;
import com.shs.trophiesapp.data.daos.TrophyDao;
import com.shs.trophiesapp.data.daos.TrophyDao_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile SportDao _sportDao;

  private volatile TrophyDao _trophyDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `sport` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Sports` TEXT, `Image_URL` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `trophy` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `Sport` TEXT, `Year` INTEGER NOT NULL, `Trophy_Title` TEXT, `Trophy_Image_URI` TEXT, `Player_Name` TEXT, `Category` TEXT)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '8fe0ca160e9e2e5fc11f17f6c65cfbe2')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `sport`");
        _db.execSQL("DROP TABLE IF EXISTS `trophy`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSport = new HashMap<String, TableInfo.Column>(3);
        _columnsSport.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSport.put("Sports", new TableInfo.Column("Sports", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSport.put("Image_URL", new TableInfo.Column("Image_URL", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSport = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSport = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSport = new TableInfo("sport", _columnsSport, _foreignKeysSport, _indicesSport);
        final TableInfo _existingSport = TableInfo.read(_db, "sport");
        if (! _infoSport.equals(_existingSport)) {
          return new RoomOpenHelper.ValidationResult(false, "sport(com.shs.trophiesapp.data.entities.Sport).\n"
                  + " Expected:\n" + _infoSport + "\n"
                  + " Found:\n" + _existingSport);
        }
        final HashMap<String, TableInfo.Column> _columnsTrophy = new HashMap<String, TableInfo.Column>(7);
        _columnsTrophy.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrophy.put("Sport", new TableInfo.Column("Sport", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrophy.put("Year", new TableInfo.Column("Year", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrophy.put("Trophy_Title", new TableInfo.Column("Trophy_Title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrophy.put("Trophy_Image_URI", new TableInfo.Column("Trophy_Image_URI", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrophy.put("Player_Name", new TableInfo.Column("Player_Name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsTrophy.put("Category", new TableInfo.Column("Category", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysTrophy = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesTrophy = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoTrophy = new TableInfo("trophy", _columnsTrophy, _foreignKeysTrophy, _indicesTrophy);
        final TableInfo _existingTrophy = TableInfo.read(_db, "trophy");
        if (! _infoTrophy.equals(_existingTrophy)) {
          return new RoomOpenHelper.ValidationResult(false, "trophy(com.shs.trophiesapp.data.entities.Trophy).\n"
                  + " Expected:\n" + _infoTrophy + "\n"
                  + " Found:\n" + _existingTrophy);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "8fe0ca160e9e2e5fc11f17f6c65cfbe2", "b635be34fd9f912caae4eba43aa37c81");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "sport","trophy");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `sport`");
      _db.execSQL("DELETE FROM `trophy`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public SportDao sportDao() {
    if (_sportDao != null) {
      return _sportDao;
    } else {
      synchronized(this) {
        if(_sportDao == null) {
          _sportDao = new SportDao_Impl(this);
        }
        return _sportDao;
      }
    }
  }

  @Override
  public TrophyDao trophyDao() {
    if (_trophyDao != null) {
      return _trophyDao;
    } else {
      synchronized(this) {
        if(_trophyDao == null) {
          _trophyDao = new TrophyDao_Impl(this);
        }
        return _trophyDao;
      }
    }
  }
}
