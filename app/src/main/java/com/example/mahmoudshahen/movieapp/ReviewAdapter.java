package com.example.mahmoudshahen.movieapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import java.util.List;

/**
 * Created by mahmoud shahen on 05/04/2017.
 */

public class ReviewAdapter  extends RecyclerView.Adapter<ReviewAdapter.reviewHolder> {

    List<String> reviews;
    Context context;

    public ReviewAdapter(List<String> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewAdapter.reviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.review_adapter_item, parent,false);
        return new ReviewAdapter.reviewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.reviewHolder holder, final int position) {
        holder.review.setText(reviews.get(position));
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class reviewHolder extends RecyclerView.ViewHolder {

        TextView review;
        public reviewHolder(View itemView) {
            super(itemView);
            review = (TextView) itemView.findViewById(R.id.tv_movie_review);
        }

    }

}
