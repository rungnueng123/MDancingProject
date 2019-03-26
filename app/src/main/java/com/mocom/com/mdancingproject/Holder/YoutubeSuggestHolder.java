package com.mocom.com.mdancingproject.Holder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeThumbnailView;
import com.mocom.com.mdancingproject.R;

public class YoutubeSuggestHolder extends RecyclerView.ViewHolder {

    private RelativeLayout relativeLayoutOverYouTubeThumbnailView;
    private YouTubeThumbnailView youTubeThumbnailView;
    private ImageView playButton;
    private TextView title, author;

    public TextView getAuthor() {
        return author;
    }

    public TextView getTitle() {
        return title;
    }

    public RelativeLayout getRelativeLayoutOverYouTubeThumbnailView() {
        return relativeLayoutOverYouTubeThumbnailView;
    }

    public YouTubeThumbnailView getYouTubeThumbnailView() {
        return youTubeThumbnailView;
    }

    public ImageView getPlayButton() {
        return playButton;
    }

    public YoutubeSuggestHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.txt_title);
        author = itemView.findViewById(R.id.txt_author);
        playButton = itemView.findViewById(R.id.btnYoutube_player);
        relativeLayoutOverYouTubeThumbnailView = itemView.findViewById(R.id.relativeLayout_over_youtube_thumbnail);
        youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);

    }
}
