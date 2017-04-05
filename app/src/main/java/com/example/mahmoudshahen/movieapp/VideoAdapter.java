package com.example.mahmoudshahen.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mahmoud shahen on 05/04/2017.
 */

public class VideoAdapter  extends RecyclerView.Adapter<VideoAdapter.videoHolder> {

    List<Trailer> videos;
    Context context;

    public VideoAdapter(List<Trailer> videos, Context context) {
        this.videos = videos;
        this.context = context;

    }

    @Override
    public VideoAdapter.videoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(parent.getContext()).
                inflate(R.layout.video_adaper_item, parent,false);
        return new VideoAdapter.videoHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VideoAdapter.videoHolder holder, final int position) {
        holder.video.setText(videos.get(position).name);
        holder.video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.YOUTUBE_LINK)+videos.get(position).key));
                if(intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, "No APP To Open Video.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class videoHolder extends RecyclerView.ViewHolder {

        TextView video;
        public videoHolder(View itemView) {
            super(itemView);
            video = (TextView) itemView.findViewById(R.id.tv_movie_video);
        }

    }

}
