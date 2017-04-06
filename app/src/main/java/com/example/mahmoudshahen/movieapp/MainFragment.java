package com.example.mahmoudshahen.movieapp;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MainFragment extends Fragment implements MoviesAdapter.ListItemClickListener,
                                SharedPreferences.OnSharedPreferenceChangeListener {

    String MOVIE_URL ;
    String TOP_POP;
    List<Movie> movies;
    MoviesAdapter moviesAdapter;
    RecyclerView moviesRecyclerView;
    private final String LIFECYCLE_MOVIE_RESULT = "result";
    SharedPreferences sharedPreferences;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setHasOptionsMenu(true);
        moviesRecyclerView = (RecyclerView)rootView.findViewById(R.id.rv_movie_recycler);
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        moviesRecyclerView.setItemAnimator(new DefaultItemAnimator());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        MOVIE_URL = getString(R.string.MOVIE_URL);
        String stored = sharedPreferences.getString(getString(R.string.key_list), getString(R.string.POPULAR));
        if(stored.equals(getString(R.string.FAVOURITE))) {
            fillListFromDataBase();
        }
        else
            TOP_POP = stored;

        if(savedInstanceState != null) {
            movies = (List<Movie>) savedInstanceState.getSerializable(LIFECYCLE_MOVIE_RESULT);
            SetMovieAdapter();
        }
        else if(isNetworkAvailable(getContext()) && !stored.equals(getString(R.string.FAVOURITE)))
            GetMovieQuarry(getContext());
        else if(!isNetworkAvailable(getContext()))
            Toast.makeText(getContext(), "No Internet Connection", Toast.LENGTH_LONG).show();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(LIFECYCLE_MOVIE_RESULT, (Serializable) movies);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.refresh) {
            if(sharedPreferences.getString(getString(R.string.key_list), getString(R.string.POPULAR)).equals(getString(R.string.FAVOURITE))) {
                fillListFromDataBase();
            }
            else
                GetMovieQuarry(getContext());
            return true;
        }
        if(id == R.id.setting) {
            startActivity(new Intent(getContext(), SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void GetMovieQuarry(Context context) {

        Uri build = Uri.parse(MOVIE_URL).buildUpon()
                .appendEncodedPath(TOP_POP+"?"+getString(R.string.API_KEY))
                .build();
        Log.v("URL", String.valueOf(build));
        Ion.with(context)
                .load(String.valueOf(build))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        movies = new ArrayList<>();
                        if (result != null && result.has("results")) {
                            JsonArray jsonArray = result.get("results").getAsJsonArray();

                            for(int i=0 ; i<jsonArray.size() ; i++) {
                                Movie movie = new Movie();
                                movie.setId(jsonArray.get(i).getAsJsonObject().get("id").getAsString());
                                movie.setOriginalTitle(jsonArray.get(i).getAsJsonObject().get("original_title").getAsString());
                                movie.setOverview(jsonArray.get(i).getAsJsonObject().get("overview").getAsString());
                                movie.setPosterPath(jsonArray.get(i).getAsJsonObject().get("poster_path").getAsString());
                                movie.setReleaseDate(jsonArray.get(i).getAsJsonObject().get("release_date").getAsString());
                                movie.setVoteAverage(jsonArray.get(i).getAsJsonObject().get("vote_average").getAsString());
                                movies.add(movie);
                            }
                        }
                        SetMovieAdapter();
                    }
                });
    }
    private void SetMovieAdapter() {
        moviesAdapter = new MoviesAdapter(movies, getContext(), this);
        moviesRecyclerView.setAdapter(moviesAdapter);
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("movie", movies.get(clickedItemIndex));
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public boolean isNetworkAvailable(final Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
    }
    public void fillListFromDataBase() {
        Cursor cursor = getContext().getContentResolver().query(MovieContract.MovieTable.CONTENT_URI, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        movies = new ArrayList<>();
        while (cursor.moveToNext()) {
            Movie movie = new Movie();
            movie.setId(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_NAME_ID)));
            movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_NAME_RATING)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_NAME_RELEASEDATE)));
            movie.setPosterPath(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_NAME_IMAGE)));
            movie.setOverview(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_NAME_OVERVIEW)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(MovieContract.MovieTable.COLUMN_NAME_ORIGINALTITLE)));
            movies.add(movie);
        }
        SetMovieAdapter();
        moviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Log.v("shared", sharedPreferences.getString(s, "dff"));
        if(sharedPreferences.getString(s, "null").equals(getString(R.string.FAVOURITE))) {
            fillListFromDataBase();
        }
        else if(sharedPreferences.getString(s, "null").equals(getString(R.string.POPULAR))) {
            TOP_POP = getString(R.string.POPULAR);
            GetMovieQuarry(getContext());
        }
        else {
            TOP_POP = getString(R.string.TOP_RATED);
            GetMovieQuarry(getContext());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }
}
