package com.example.mahmoudshahen.movieapp;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

/**
 * Created by mahmoud shahen on 05/04/2017.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.mahmoudshahen.movieapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "favourite";
    private MovieContract() {
    }

    public static abstract class MovieTable implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final Uri CONTENT_URI_ID = CONTENT_URI.buildUpon().appendPath("id").build();
        public static final String TABLE_NAME = "favourite";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_ORIGINALTITLE = "originalTitle";
        public static final String COLUMN_NAME_OVERVIEW = "overView";
        public static final String COLUMN_NAME_RELEASEDATE= "releaseDate";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_IMAGE = "image";


    }
}
