package com.mocom.com.mdancingproject.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mocom.com.mdancingproject.Dao.YoutubeSuggestDao;
import com.mocom.com.mdancingproject.Holder.YoutubeSuggestHolder;
import com.mocom.com.mdancingproject.R;
import com.mocom.com.mdancingproject.config.config;

import java.util.List;

public class YoutubeSuggestAdapter extends RecyclerView.Adapter<YoutubeSuggestHolder> {

    private List<YoutubeSuggestDao> youtubeList;
    Context context;

    public YoutubeSuggestAdapter(List<YoutubeSuggestDao> youtubeList, Context context) {
        this.youtubeList = youtubeList;
        this.context = context;
    }

    @NonNull
    @Override
    public YoutubeSuggestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_menu, parent, false);
        return new YoutubeSuggestHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubeSuggestHolder holder, int position) {
        YoutubeSuggestDao dao = youtubeList.get(position);
        holder.getTitle().setText(dao.getTitle());
        holder.getAuthor().setText(dao.getAuthor());

        final YouTubeThumbnailLoader.OnThumbnailLoadedListener  onThumbnailLoadedListener = new YouTubeThumbnailLoader.OnThumbnailLoadedListener(){
            @Override
            public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                youTubeThumbnailView.setVisibility(View.VISIBLE);
                holder.getRelativeLayoutOverYouTubeThumbnailView().setVisibility(View.VISIBLE);
            }
        };

        holder.getYouTubeThumbnailView().initialize(config.getYoutubeKey(), new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(dao.getYoutubeUrl());
                youTubeThumbnailLoader.setOnThumbnailLoadedListener(onThumbnailLoadedListener);
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                //write something for failure
            }
        });

        holder.getPlayButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = YouTubeStandalonePlayer.createVideoIntent((Activity) context, config.getYoutubeKey(), dao.getYoutubeUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (youtubeList == null) {
            return 0;
        }
        if (youtubeList.size() == 0) {
            return 0;
        }
        return youtubeList.size();
    }
}
