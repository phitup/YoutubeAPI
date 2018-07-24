package com.example.phitup.youtubeapi.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phitup.youtubeapi.Model.YoutubeDataModel;
import com.example.phitup.youtubeapi.interfaces.OnitemClickListener;
import com.example.phitup.youtubeapi.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class VideoPostAdapter extends RecyclerView.Adapter<VideoPostAdapter.YoutubePostHolder>{

    private ArrayList<YoutubeDataModel> dataset;
    private Context mContext;
    private final OnitemClickListener listener;

    public VideoPostAdapter(ArrayList<YoutubeDataModel> dataset, Context mContext , OnitemClickListener listener) {
        this.dataset = dataset;
        this.mContext = mContext;
        this.listener = listener;
    }

    @NonNull
    @Override
    public YoutubePostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_post_layout,parent,false);
        YoutubePostHolder postHolder = new YoutubePostHolder(view);
        return postHolder;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public void onBindViewHolder(@NonNull YoutubePostHolder holder, int position) {

        // set the views here
        TextView textViewTitle = holder.textViewTitle;
        TextView textViewDes = holder.textViewDes;
        TextView textViewData = holder.textViewData;
        ImageView ImageThumb = holder.ImageThumb;

        YoutubeDataModel object = dataset.get(position);

        textViewTitle.setText(object.getTitle());
        textViewData.setText(object.getPublishedAt());
        textViewDes.setText(object.getDescription());
        holder.bind(dataset.get(position), listener);

        // imgae
        Picasso.with(mContext).load(object.getThumbnail()).into(ImageThumb);
    }

    public static class YoutubePostHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle , textViewDes , textViewData;
        ImageView ImageThumb;


        public YoutubePostHolder(View itemView) {
            super(itemView);
            this.textViewTitle = itemView.findViewById(R.id.textViewTitle);
            this.textViewDes = itemView.findViewById(R.id.textViewDes);
            this.textViewData = itemView.findViewById(R.id.textViewDate);
            this.ImageThumb = itemView.findViewById(R.id.ImageThumb);
        }

        public void bind(final YoutubeDataModel item, final OnitemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }
}
