package com.gdt.speedtest.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.gdt.speedtest.Constants;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;
import net.sqlcipher.database.SQLiteException;

public class DatabaseUtil {

    public static void checkAndMigrateDatabase(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Constants.MIGRATE_NAME, Context.MODE_PRIVATE);
            SQLiteDatabase.loadLibs(context);
            String databasePath = context.getDatabasePath(Constants.DATABASE_NAME).getPath();

            SQLiteDatabaseHook databaseHook = new SQLiteDatabaseHook() {
                @Override
                public void postKey(SQLiteDatabase database) {
                    database.rawQuery(Constants.DATABASE_QUERY, null);
                    Log.i("AIKO", "Migrated");
                }

                @Override
                public void preKey(SQLiteDatabase database) {
                }
            };

            try {
                SQLiteDatabase database = SQLiteDatabase.openDatabase(databasePath,
                        Constants.DATABASE_PASS, null, SQLiteDatabase.CREATE_IF_NECESSARY, databaseHook);
                database.close();
            } catch (SQLiteException e) {
                // Database null, ignore when firt init database
            }
            preferences.edit().putBoolean(Constants.MIGRATED, true).apply();
    }

}
