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
import com.shs.trophiesapp.data.entities.Sport;
import java.lang.Override;
import java.lang.String;
import java.lang.StringBuilder;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SportDao_Impl implements SportDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Sport> __insertionAdapterOfSport;

  private final EntityDeletionOrUpdateAdapter<Sport> __deletionAdapterOfSport;

  public SportDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSport = new EntityInsertionAdapter<Sport>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `sport` (`id`,`Sports`,`Image_URL`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Sport value) {
        stmt.bindLong(1, value.id);
        if (value.sport_name == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.sport_name);
        }
        if (value.image_url == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.image_url);
        }
      }
    };
    this.__deletionAdapterOfSport = new EntityDeletionOrUpdateAdapter<Sport>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `sport` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Sport value) {
        stmt.bindLong(1, value.id);
      }
    };
  }

  @Override
  public void insertAll(final Sport... sports) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSport.insert(sports);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void delete(final Sport sport) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSport.handle(sport);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public List<Sport> getAll() {
    final String _sql = "SELECT `sport`.`id` AS `id`, `sport`.`Sports` AS `Sports`, `sport`.`Image_URL` AS `Image_URL` FROM sport";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sports");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Image_URL");
      final List<Sport> _result = new ArrayList<Sport>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Sport _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final String _tmpImage_url;
        _tmpImage_url = _cursor.getString(_cursorIndexOfImageUrl);
        _item = new Sport(_tmpSport_name,_tmpImage_url);
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
  public List<Sport> getSports() {
    final String _sql = "SELECT `sport`.`id` AS `id`, `sport`.`Sports` AS `Sports`, `sport`.`Image_URL` AS `Image_URL` FROM sport";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sports");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Image_URL");
      final List<Sport> _result = new ArrayList<Sport>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Sport _item;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final String _tmpImage_url;
        _tmpImage_url = _cursor.getString(_cursorIndexOfImageUrl);
        _item = new Sport(_tmpSport_name,_tmpImage_url);
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
  public List<Sport> loadAllByIds(final int[] userIds) {
    StringBuilder _stringBuilder = StringUtil.newStringBuilder();
    _stringBuilder.append("SELECT ");
    _stringBuilder.append("*");
    _stringBuilder.append(" FROM sport WHERE rowId IN (");
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
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sports");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Image_URL");
      final List<Sport> _result = new ArrayList<Sport>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Sport _item_1;
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final String _tmpImage_url;
        _tmpImage_url = _cursor.getString(_cursorIndexOfImageUrl);
        _item_1 = new Sport(_tmpSport_name,_tmpImage_url);
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
  public Sport findByName(final String sport_name, final String image_url) {
    final String _sql = "SELECT `sport`.`id` AS `id`, `sport`.`Sports` AS `Sports`, `sport`.`Image_URL` AS `Image_URL` FROM sport WHERE Sports LIKE ? AND Image_URL LIKE ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 2);
    int _argIndex = 1;
    if (sport_name == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, sport_name);
    }
    _argIndex = 2;
    if (image_url == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, image_url);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSportName = CursorUtil.getColumnIndexOrThrow(_cursor, "Sports");
      final int _cursorIndexOfImageUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "Image_URL");
      final Sport _result;
      if(_cursor.moveToFirst()) {
        final String _tmpSport_name;
        _tmpSport_name = _cursor.getString(_cursorIndexOfSportName);
        final String _tmpImage_url;
        _tmpImage_url = _cursor.getString(_cursorIndexOfImageUrl);
        _result = new Sport(_tmpSport_name,_tmpImage_url);
        _result.id = _cursor.getLong(_cursorIndexOfId);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
