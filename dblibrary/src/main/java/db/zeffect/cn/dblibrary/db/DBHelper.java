package db.zeffect.cn.dblibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "zeffect.db";
    private static final int DATABASE_VERSION = 1;
    private Context mContext;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String table = getTable(uri);
        if (TextUtils.isEmpty(table)) return null;
        try {
            return getReadableDatabase().query(table, projection, selection, selectionArgs, null, null, sortOrder, null);
        } catch (SQLiteException e) {
            return null;
        }
    }

    public Uri insert(Uri uri, ContentValues values) {
        String table = getTable(uri);
        if (TextUtils.isEmpty(table)) return uri;
        //查表
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + table + "(_id INTEGER PRIMARY KEY AUTOINCREMENT )");
        //查列
        Set<String> notExist = checkColumnExist(getReadableDatabase(), table, values.keySet());
        if (!notExist.isEmpty()) {
            for (String keyCol : notExist) {
                String ADD_COLUMNS = "ALTER TABLE " + table + " ADD COLUMN " + keyCol + " " + object2Class(values.get(keyCol));
                getWritableDatabase().execSQL(ADD_COLUMNS);
            }
        }
        mContext.getContentResolver().notifyChange(uri, null);
        try {
            getWritableDatabase().insert(table, null, values);
        } catch (SQLiteException e) {
        }
        return uri;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String table = getTable(uri);
        if (TextUtils.isEmpty(table)) return 0;
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + table + "(_id INTEGER PRIMARY KEY AUTOINCREMENT )");
        mContext.getContentResolver().notifyChange(uri, null);
        try {
            return getWritableDatabase().delete(table, selection, selectionArgs);
        } catch (SQLiteException e) {
            return 0;
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        String table = getTable(uri);
        if (TextUtils.isEmpty(table)) return 0;
        //查表
        getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + table + "(_id INTEGER PRIMARY KEY AUTOINCREMENT )");
        //查列
        Set<String> notExist = checkColumnExist(getReadableDatabase(), table, values.keySet());
        if (!notExist.isEmpty()) {
            for (String keyCol : notExist) {
                String ADD_COLUMNS = "ALTER TABLE " + table + " ADD COLUMN " + keyCol + " " + object2Class(values.get(keyCol));
                getWritableDatabase().execSQL(ADD_COLUMNS);
            }
        }
        mContext.getContentResolver().notifyChange(uri, null);
        try {
            return getWritableDatabase().update(table, values, selection, selectionArgs);
        } catch (SQLiteException e) {
            return 0;
        }
    }


    public String getTable(Uri uri) {
        return uri.getQueryParameter("table");
    }


    /**
     * 检查某表列是否存在,返回不存在的列
     *
     * @param db
     * @param tableName   表名
     * @param columnNames 列名
     * @return
     */
    private Set<String> checkColumnExist(SQLiteDatabase db, String tableName
            , Set<String> columnNames) {
        Set<String> result = new HashSet<>();
        if (columnNames == null || columnNames.isEmpty()) return result;
        Cursor cursor = null;
        try {
            //查询一行
            cursor = db.rawQuery("SELECT * FROM " + tableName + " LIMIT 0"
                    , null);
            if (cursor == null) return result;
            else {
                for (String keyCol : columnNames) {
                    if (cursor.getColumnIndex(keyCol) == -1) {
                        result.add(keyCol);
                    }
                }
            }
        } catch (Exception e) {
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }


    public String object2Class(Object o) {
        if (o instanceof String) {
            return "TEXT";
        } else if (o instanceof Boolean) {
            return "BOOLEAN";
        } else if (o instanceof Byte) {
            return "BLOB";
        } else if (o instanceof Double) {
            return "DOUBLE";
        } else if (o instanceof Float) {
            return "DOUBLE";
        } else if (o instanceof Long) {
            return "DOUBLE";
        } else if (o instanceof Integer) {
            return "INTEGER";
        } else {
            return "TEXT";
        }
    }

}
