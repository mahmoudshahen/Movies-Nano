package com.example.mahmoudshahen.movieapp;


import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by mahmoud shahen on 21/03/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    List<Movie> movies;
    Context context;
    final private ListItemClickListener listItemClickListener;
    public MoviesAdapter(List<Movie> movies, Context context, ListItemClickListener listener)
    {
        this.movies = movies;
        this.context = context;
        listItemClickListener = listener;
    }

    @Override
    public MoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.movie_adapter_item, parent,false);
        return new MoviesAdapter.MoviesHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MoviesHolder holder, int position) {
        Log.v("IMAGEURL",movies.get(position).getPosterPath( ));
        Picasso.with(context).load(context.getString(R.string.PHOTO_URL)+movies.get(position).getPosterPath())
                .placeholder(R.drawable.loading).error(R.drawable.notification_error).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MoviesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        public MoviesHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            listItemClickListener.onListItemClick(clickedPosition);
        }
    }
    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

}
