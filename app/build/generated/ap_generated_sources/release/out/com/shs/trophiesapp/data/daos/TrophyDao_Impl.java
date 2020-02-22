package com.shs.trophiesapp.data.daos;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.room.util.StringUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.shs.trophiesapp.data.entities.Trophy;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TrophyDao_Impl implements TrophyDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Trophy> __insertionAdapterOfTrophy;

  private final EntityDeletionOrUpdateAdapter<Trophy> __deletionAdapterOfTrophy;

  public TrophyDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfTrophy = new EntityInsertionAdapter<Trophy>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `trophy` (`id`,`Sport`,`Year`,`Trophy_Title`,`Trophy_Image_URI`,`Player_Name`,`Category`) VALUES (nullif(?, 0),?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trophy value) {
        stmt.bindLong(1, value.id);
        if (value.sport_name == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.sport_name);
        }
        stmt.bindLong(3, value.year);
        if (value.tr_title == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.tr_title);
        }
        if (value.tr_image_url == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.tr_image_url);
        }
        if (value.player == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.player);
        }
        if (value.category == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.category);
        }
      }
    };
    this.__deletionAdapterOfTrophy = new EntityDeletionOrUpdateAdapter<Trophy>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `trophy` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Trophy value) {
        stmt.bindLong(1, value.id);
      }
    };
  }

  @Override
  public void insertAll(final Trophy... trophies) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfTrophy.insert(trophies);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Trophy trophy) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfTrophy.handle(trophy);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Trophy> getAll() {
    final String _sql = "SELECT `trophy`.`id` AS `id`, `trophy`.`Sport` AS `Sport`, `trophy`.`Year` AS `Year`, `trophy`.`Trophy_Title` AS `Trophy_Title`, `trophy`.`Trophy_Image_URI` AS `Trophy_Image_URI`, `trophy`.`Player_Name` AS `Player_Name`, `trophy`.`Category` AS `Category` FROM trophy";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sport");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "Year");
      final int _cursorIndexOfTrTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Title");
      final int _cursorIndexOfTrImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Image_URI");
      final int _cursorIndexOfPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "Player_Name");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "Category");
      final List<Trophy> _result = new ArrayList<Trophy>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trophy _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final String _tmpTr_title;
        _tmpTr_title = _cursor.getString(_cursorIndexOfTrTitle);
        final String _tmpTr_image_url;
        _tmpTr_image_url = _cursor.getString(_cursorIndexOfTrImageUrl);
        final String _tmpPlayer;
        _tmpPlayer = _cursor.getString(_cursorIndexOfPlayer);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item = new Trophy(_tmpSport_name,_tmpYear,_tmpTr_title,_tmpTr_image_url,_tmpPlayer,_tmpCategory);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trophy> getTrophies() {
    final String _sql = "SELECT `trophy`.`id` AS `id`, `trophy`.`Sport` AS `Sport`, `trophy`.`Year` AS `Year`, `trophy`.`Trophy_Title` AS `Trophy_Title`, `trophy`.`Trophy_Image_URI` AS `Trophy_Image_URI`, `trophy`.`Player_Name` AS `Player_Name`, `trophy`.`Category` AS `Category` FROM trophy";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sport");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "Year");
      final int _cursorIndexOfTrTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Title");
      final int _cursorIndexOfTrImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Image_URI");
      final int _cursorIndexOfPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "Player_Name");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "Category");
      final List<Trophy> _result = new ArrayList<Trophy>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trophy _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final String _tmpTr_title;
        _tmpTr_title = _cursor.getString(_cursorIndexOfTrTitle);
        final String _tmpTr_image_url;
        _tmpTr_image_url = _cursor.getString(_cursorIndexOfTrImageUrl);
        final String _tmpPlayer;
        _tmpPlayer = _cursor.getString(_cursorIndexOfPlayer);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item = new Trophy(_tmpSport_name,_tmpYear,_tmpTr_title,_tmpTr_image_url,_tmpPlayer,_tmpCategory);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trophy> loadAllByIds(final int[] userIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT ");
    _stringBuilder.append("*");
    _stringBuilder.append(" FROM trophy WHERE rowid IN (");
    final int _inputSize = userIds.length;
    StringUtil.appendPlaceholders(_stringBuilder, _inputSize);
    _stringBuilder.append(")");
    final String _sql = _stringBuilder.toString();
    final int _argCount = 0 + _inputSize;
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, _argCount);
    int _argIndex = 1;
    for (int _item : userIds) {
      _statement.bindLong(_argIndex, _item);
      _argIndex ++;
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sport");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "Year");
      final int _cursorIndexOfTrTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Title");
      final int _cursorIndexOfTrImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Image_URI");
      final int _cursorIndexOfPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "Player_Name");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "Category");
      final List<Trophy> _result = new ArrayList<Trophy>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trophy _item_1;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final String _tmpTr_title;
        _tmpTr_title = _cursor.getString(_cursorIndexOfTrTitle);
        final String _tmpTr_image_url;
        _tmpTr_image_url = _cursor.getString(_cursorIndexOfTrImageUrl);
        final String _tmpPlayer;
        _tmpPlayer = _cursor.getString(_cursorIndexOfPlayer);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item_1 = new Trophy(_tmpSport_name,_tmpYear,_tmpTr_title,_tmpTr_image_url,_tmpPlayer,_tmpCategory);
        _item_1.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item_1);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trophy> findByTrophyTitle(final String tr_title) {
    final String _sql = "SELECT `trophy`.`id` AS `id`, `trophy`.`Sport` AS `Sport`, `trophy`.`Year` AS `Year`, `trophy`.`Trophy_Title` AS `Trophy_Title`, `trophy`.`Trophy_Image_URI` AS `Trophy_Image_URI`, `trophy`.`Player_Name` AS `Player_Name`, `trophy`.`Category` AS `Category` FROM trophy WHERE Trophy_Title LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (tr_title == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, tr_title);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sport");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "Year");
      final int _cursorIndexOfTrTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Title");
      final int _cursorIndexOfTrImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Image_URI");
      final int _cursorIndexOfPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "Player_Name");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "Category");
      final List<Trophy> _result = new ArrayList<Trophy>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trophy _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final String _tmpTr_title;
        _tmpTr_title = _cursor.getString(_cursorIndexOfTrTitle);
        final String _tmpTr_image_url;
        _tmpTr_image_url = _cursor.getString(_cursorIndexOfTrImageUrl);
        final String _tmpPlayer;
        _tmpPlayer = _cursor.getString(_cursorIndexOfPlayer);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item = new Trophy(_tmpSport_name,_tmpYear,_tmpTr_title,_tmpTr_image_url,_tmpPlayer,_tmpCategory);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trophy> findByName(final String player) {
    final String _sql = "SELECT `trophy`.`id` AS `id`, `trophy`.`Sport` AS `Sport`, `trophy`.`Year` AS `Year`, `trophy`.`Trophy_Title` AS `Trophy_Title`, `trophy`.`Trophy_Image_URI` AS `Trophy_Image_URI`, `trophy`.`Player_Name` AS `Player_Name`, `trophy`.`Category` AS `Category` FROM trophy WHERE Player_Name LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (player == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, player);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sport");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "Year");
      final int _cursorIndexOfTrTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Title");
      final int _cursorIndexOfTrImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Image_URI");
      final int _cursorIndexOfPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "Player_Name");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "Category");
      final List<Trophy> _result = new ArrayList<Trophy>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trophy _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final String _tmpTr_title;
        _tmpTr_title = _cursor.getString(_cursorIndexOfTrTitle);
        final String _tmpTr_image_url;
        _tmpTr_image_url = _cursor.getString(_cursorIndexOfTrImageUrl);
        final String _tmpPlayer;
        _tmpPlayer = _cursor.getString(_cursorIndexOfPlayer);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item = new Trophy(_tmpSport_name,_tmpYear,_tmpTr_title,_tmpTr_image_url,_tmpPlayer,_tmpCategory);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trophy> findByYear(final int year) {
    final String _sql = "SELECT `trophy`.`id` AS `id`, `trophy`.`Sport` AS `Sport`, `trophy`.`Year` AS `Year`, `trophy`.`Trophy_Title` AS `Trophy_Title`, `trophy`.`Trophy_Image_URI` AS `Trophy_Image_URI`, `trophy`.`Player_Name` AS `Player_Name`, `trophy`.`Category` AS `Category` FROM trophy WHERE Year LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, year);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sport");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "Year");
      final int _cursorIndexOfTrTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Title");
      final int _cursorIndexOfTrImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Image_URI");
      final int _cursorIndexOfPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "Player_Name");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "Category");
      final List<Trophy> _result = new ArrayList<Trophy>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trophy _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final String _tmpTr_title;
        _tmpTr_title = _cursor.getString(_cursorIndexOfTrTitle);
        final String _tmpTr_image_url;
        _tmpTr_image_url = _cursor.getString(_cursorIndexOfTrImageUrl);
        final String _tmpPlayer;
        _tmpPlayer = _cursor.getString(_cursorIndexOfPlayer);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item = new Trophy(_tmpSport_name,_tmpYear,_tmpTr_title,_tmpTr_image_url,_tmpPlayer,_tmpCategory);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trophy> findBySport(final String sport_name) {
    final String _sql = "SELECT `trophy`.`id` AS `id`, `trophy`.`Sport` AS `Sport`, `trophy`.`Year` AS `Year`, `trophy`.`Trophy_Title` AS `Trophy_Title`, `trophy`.`Trophy_Image_URI` AS `Trophy_Image_URI`, `trophy`.`Player_Name` AS `Player_Name`, `trophy`.`Category` AS `Category` FROM trophy WHERE Sport LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (sport_name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sport_name);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sport");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "Year");
      final int _cursorIndexOfTrTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Title");
      final int _cursorIndexOfTrImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Image_URI");
      final int _cursorIndexOfPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "Player_Name");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "Category");
      final List<Trophy> _result = new ArrayList<Trophy>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trophy _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final String _tmpTr_title;
        _tmpTr_title = _cursor.getString(_cursorIndexOfTrTitle);
        final String _tmpTr_image_url;
        _tmpTr_image_url = _cursor.getString(_cursorIndexOfTrImageUrl);
        final String _tmpPlayer;
        _tmpPlayer = _cursor.getString(_cursorIndexOfPlayer);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item = new Trophy(_tmpSport_name,_tmpYear,_tmpTr_title,_tmpTr_image_url,_tmpPlayer,_tmpCategory);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Trophy> findByCategory(final String category) {
    final String _sql = "SELECT `trophy`.`id` AS `id`, `trophy`.`Sport` AS `Sport`, `trophy`.`Year` AS `Year`, `trophy`.`Trophy_Title` AS `Trophy_Title`, `trophy`.`Trophy_Image_URI` AS `Trophy_Image_URI`, `trophy`.`Player_Name` AS `Player_Name`, `trophy`.`Category` AS `Category` FROM trophy WHERE Category LIKE ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (category == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, category);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sport");
      final int _cursorIndexOfYear = CursorUtil.getColumnIndexOrThrow(_cursor, "Year");
      final int _cursorIndexOfTrTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Title");
      final int _cursorIndexOfTrImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Trophy_Image_URI");
      final int _cursorIndexOfPlayer = CursorUtil.getColumnIndexOrThrow(_cursor, "Player_Name");
      final int _cursorIndexOfCategory = CursorUtil.getColumnIndexOrThrow(_cursor, "Category");
      final List<Trophy> _result = new ArrayList<Trophy>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Trophy _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final int _tmpYear;
        _tmpYear = _cursor.getInt(_cursorIndexOfYear);
        final String _tmpTr_title;
        _tmpTr_title = _cursor.getString(_cursorIndexOfTrTitle);
        final String _tmpTr_image_url;
        _tmpTr_image_url = _cursor.getString(_cursorIndexOfTrImageUrl);
        final String _tmpPlayer;
        _tmpPlayer = _cursor.getString(_cursorIndexOfPlayer);
        final String _tmpCategory;
        _tmpCategory = _cursor.getString(_cursorIndexOfCategory);
        _item = new Trophy(_tmpSport_name,_tmpYear,_tmpTr_title,_tmpTr_image_url,_tmpPlayer,_tmpCategory);
        _item.id = _cursor.getLong(_cursorIndexOfId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
