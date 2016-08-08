package com.example.admin.Task.dbhelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.admin.Task.dbmodel.UserLocation;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Created by ADMIN on 02-08-2016.
 */
public class UserLocationOpenDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "Ormlite.db";
    private static final int DATABASE_VERSION = 1;
    /**
     * The data access object used to interact with the Sqlite database to do C.R.U.D operations.
     */
    private Dao<UserLocation, Long> locnDao;

    public UserLocationOpenDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {

        try {

            /**
             * creates the Todo database table
             */
            TableUtils.createTable(connectionSource, UserLocation.class);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

        try {
            /**
             * Recreates the database when onUpgrade is called by the framework
             */
            try {
                TableUtils.dropTable(connectionSource, UserLocation.class, false);
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
            onCreate(database, connectionSource);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns an instance of the data access object
     *
     * @return
     * @throws SQLException
     */

    //lconDao = getDao(UserLocation.class);


    public Dao<UserLocation, Long> getDao() throws SQLException {

        if (locnDao == null) {
            try {
                locnDao = getDao(UserLocation.class);   //getDao(UserLocation.class)
            } catch (java.sql.SQLException e) {
                e.printStackTrace();
            }
        }
        return locnDao;
    }

    @Override
    public void close() {
        locnDao = null;
        super.close();
    }
}
