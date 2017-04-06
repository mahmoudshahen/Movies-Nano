package com.example.mahmoudshahen.movieapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import com.example.mahmoudshahen.movieapp.databinding.FragmentDetailBinding;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class DetailFragment extends Fragment {
    Movie movie;
    FragmentDetailBinding fragmentDetailBinding;
    String MOVIE_URL ;
    String API_KEY ;
    List<Trailer> videos;
    VideoAdapter videosAdapter;
    List<String> reviews;
    ReviewAdapter reviewsAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView ;

         MOVIE_URL = getString(R.string.MOVIE_URL);
         API_KEY = getString(R.string.API_KEY);

        fragmentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        rootView = fragmentDetailBinding.getRoot();
        Intent intent = getActivity().getIntent();
        if(intent != null) {
            Bundle bundle = intent.getExtras();
            movie = (Movie) bundle.get("movie");

            fragmentDetailBinding.tvMovieReleaseDate.setText(movie.getReleaseDate());
            fragmentDetailBinding.tvVoteAverage.setText(movie.getVoteAverage());
            fragmentDetailBinding.tvMovieName.setText(movie.getOriginalTitle());
            fragmentDetailBinding.tvOverView.setText(movie.getOverview());
            Picasso.with(getContext()).load(getContext().getString(R.string.PHOTO_URL) + movie.getPosterPath())
                    .into(fragmentDetailBinding.ivMoviePoster);
            getMovieVideos(getContext());
            getMovieReviews(getContext());
        }
        fragmentDetailBinding.bFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragmentDetailBinding.bFavourite.getText().equals("MARK AS FAVOURITE")) {
                    addDataBase();
                    fragmentDetailBinding.bFavourite.setText("MARK AS UNFAVOURITE");
                }
                else {
                    deleteDataBase();
                    fragmentDetailBinding.bFavourite.setText("MARK AS FAVOURITE");
                }
            }
        });

        return rootView;
    }

    void getMovieVideos(final Context context)
    {
        String VIDEOS = getString(R.string.VIDEOS);

        Uri build = Uri.parse(MOVIE_URL).buildUpon()
                .appendEncodedPath(movie.getId()+"/"+VIDEOS+"?"+API_KEY)
                .build();
        Log.v("URL", String.valueOf(build));
        Ion.with(context)
                .load(String.valueOf(build))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        videos = new ArrayList<>();
                        if (result != null && result.has("results")) {
                            JsonArray jsonArray = result.get("results").getAsJsonArray();

                            for(int i=0 ; i<jsonArray.size() ; i++) {
                                videos.add(new Trailer(jsonArray.get(i).getAsJsonObject().get("name").toString(),
                                            jsonArray.get(i).getAsJsonObject().get("key").toString()));
                            }
                            videosAdapter = new VideoAdapter(videos, context);
                            fragmentDetailBinding.rvMovieTrailers.setLayoutManager(new LinearLayoutManager(getActivity()));
                            fragmentDetailBinding.rvMovieTrailers.setAdapter(videosAdapter);
                        }
                    }
                });
    }
    void getMovieReviews(final Context context)
    {
        String REVIEWS = getString(R.string.REVIEWS);

        Uri build = Uri.parse(MOVIE_URL).buildUpon()
                .appendEncodedPath(movie.getId()+"/"+REVIEWS+"?"+API_KEY)
                .build();
        Log.v("URL", String.valueOf(build));
        Ion.with(context)
                .load(String.valueOf(build))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        reviews = new ArrayList<>();
                        if (result != null && result.has("results")) {
                            JsonArray jsonArray = result.get("results").getAsJsonArray();

                            for(int i=0 ; i<jsonArray.size() ; i++) {
                                reviews.add(jsonArray.get(i).getAsJsonObject().get("content").toString());
                            }
                            reviewsAdapter = new ReviewAdapter(reviews, context);
                            fragmentDetailBinding.rvMovieReviews.setLayoutManager(new LinearLayoutManager(getActivity()));
                            fragmentDetailBinding.rvMovieReviews.setAdapter(reviewsAdapter);
                        }
                    }
                });
    }

    public void addDataBase() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MovieContract.MovieTable.COLUMN_NAME_ID, movie.getId());
            contentValues.put(MovieContract.MovieTable.COLUMN_NAME_IMAGE, movie.getPosterPath());
            contentValues.put(MovieContract.MovieTable.COLUMN_NAME_ORIGINALTITLE, movie.getOriginalTitle());
            contentValues.put(MovieContract.MovieTable.COLUMN_NAME_OVERVIEW, movie.getOverview());
            contentValues.put(MovieContract.MovieTable.COLUMN_NAME_RATING, movie.getVoteAverage());
            contentValues.put(MovieContract.MovieTable.COLUMN_NAME_RELEASEDATE, movie.getReleaseDate());
            Uri uri = getActivity().getContentResolver().insert(MovieContract.MovieTable.CONTENT_URI, contentValues);
    }
    public void deleteDataBase() {
        getContext().getContentResolver().delete(MovieContract.MovieTable.CONTENT_URI, movie.getId(), null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieTable.CONTENT_URI_ID, null, movie.getId(), null, null);
        if(cursor != null) {
            fragmentDetailBinding.bFavourite.setText("MARK AS UNFAVOURITE");
        }
    }
}
