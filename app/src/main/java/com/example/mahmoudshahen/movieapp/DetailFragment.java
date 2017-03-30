package com.example.mahmoudshahen.movieapp;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mahmoudshahen.movieapp.databinding.FragmentDetailBinding;
import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment {
    Movie movie;
    FragmentDetailBinding fragmentDetailBinding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView ;
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
        }
        return rootView;
    }
}
