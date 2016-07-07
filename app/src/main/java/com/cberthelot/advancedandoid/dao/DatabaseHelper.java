package com.cberthelot.advancedandoid.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cberthelot.advancedandoid.entities.SimpleData;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.Date;

/**
 * Created by cberthelot on 07/07/2016.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application
    private static final String DATABASE_NAME = "simpleDatabase.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 2;

    // the DAO object we use to access the SimpleData table
    private Dao<SimpleData, Integer> simpleDao = null;

    private static final String TAG = DatabaseHelper.class.getName();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, SimpleData.class);

            // here we try inserting data in the on-create as a test
            Dao<SimpleData, Integer> simpleDataDao = getSimpleDataDao();
            simpleDataDao.create(new SimpleData("premier", new Date()));
            simpleDataDao.create(new SimpleData("deuxieme", new Date()));
            simpleDataDao.create(new SimpleData("troisieme", new Date()));
            Log.i(TAG, "Datas inserted in database");
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, SimpleData.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<SimpleData, Integer> getSimpleDataDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(SimpleData.class);
        }
        return simpleDao;
    }
}
