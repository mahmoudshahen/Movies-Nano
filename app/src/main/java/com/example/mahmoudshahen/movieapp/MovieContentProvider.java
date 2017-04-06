package com.example.mahmoudshahen.movieapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by mahmoud shahen on 05/04/2017.
 */

public class MovieContentProvider extends ContentProvider {
    private MovieOpenHelper movieOpenHelper;
    public static final short MOVIES = 100;
    public static final short MOVIES_WITH_ID = 101;
    public static final UriMatcher uriMatcher = buildUriMatcher();
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/id", MOVIES_WITH_ID);

        return uriMatcher;
    }
    @Override
    public boolean onCreate() {
        movieOpenHelper = new MovieOpenHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        SQLiteDatabase db = movieOpenHelper.getReadableDatabase();
        Cursor retCursor = null;
        switch (uriMatcher.match(uri)) {

            case MOVIES: {
                retCursor =  db.query(MovieContract.MovieTable.TABLE_NAME,
                        strings, s, strings1, null, null, s1);
                break;
            }
            case  MOVIES_WITH_ID: {
                retCursor = db.rawQuery("select* from " + MovieContract.MovieTable.TABLE_NAME + " WHERE " +
                        MovieContract.MovieTable.COLUMN_NAME_ID + " = " + s, null);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        if(retCursor.isAfterLast())
            return null;
        return retCursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase db = movieOpenHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)){
            case MOVIES:{
                long newRowID;
                newRowID = db.insert(MovieContract.MovieTable.TABLE_NAME, null, contentValues);

                if(newRowID > 0)
                {
                    returnUri = ContentUris.withAppendedId(MovieContract.MovieTable.CONTENT_URI, newRowID);
                }
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase db = movieOpenHelper.getWritableDatabase();
        int count = db.delete(MovieContract.MovieTable.TABLE_NAME, MovieContract.MovieTable.COLUMN_NAME_ID +"="+ s, strings);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
