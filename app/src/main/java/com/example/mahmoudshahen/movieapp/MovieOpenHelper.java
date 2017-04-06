package com.example.mahmoudshahen.movieapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mahmoud shahen on 05/04/2017.
 */

public class MovieOpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "movies.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+ MovieContract.MovieTable.TABLE_NAME + " ( "+
            MovieContract.MovieTable.COLUMN_NAME_ID + TEXT_TYPE + COMA_SEP +
            MovieContract.MovieTable.COLUMN_NAME_ORIGINALTITLE + TEXT_TYPE + COMA_SEP +
            MovieContract.MovieTable.COLUMN_NAME_OVERVIEW + TEXT_TYPE + COMA_SEP +
            MovieContract.MovieTable.COLUMN_NAME_RELEASEDATE + TEXT_TYPE + COMA_SEP +
            MovieContract.MovieTable.COLUMN_NAME_RATING + TEXT_TYPE + COMA_SEP +
            MovieContract.MovieTable.COLUMN_NAME_IMAGE + TEXT_TYPE + " ); ";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXIST " + MovieContract.MovieTable.TABLE_NAME;

    public MovieOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
