package com.essco.seller.Clases;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.recyclerview.widget.RecyclerView;

import com.essco.seller.R;

import java.util.List;


public class Adaptador_Video extends RecyclerView.Adapter<Adaptador_Video.VideoViewHolder> {
    List<Class_YouTubeVideos> youTubeVideosList;

    public Adaptador_Video() {

    }

    public Adaptador_Video(List<Class_YouTubeVideos> youTubeVideosList) {
        this.youTubeVideosList = youTubeVideosList;
    }


    @Override
    public VideoViewHolder onCreateViewHolder (ViewGroup parent, int viewType){
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.video_view,parent,false);
        return new VideoViewHolder(view);


    }
    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position){
        holder.videoWeb.loadData(youTubeVideosList.get(position).getVideoUrl(),"text/html","utf-8");
    }

    @Override
    public int getItemCount(){ return youTubeVideosList.size(); }

    public class VideoViewHolder extends RecyclerView.ViewHolder{

        WebView videoWeb;

        public VideoViewHolder (View itemView){
            super(itemView);

            videoWeb=(WebView) itemView.findViewById(R.id.videoWebView);
            videoWeb.getSettings().setJavaScriptEnabled(true);
            videoWeb.setWebChromeClient(new WebChromeClient(){

            });
        }


    }


}

