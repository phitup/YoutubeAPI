package com.example.phitup.youtubeapi.Fragments;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.phitup.youtubeapi.Model.YoutubeDataModel;
import com.example.phitup.youtubeapi.R;
import com.example.phitup.youtubeapi.activities.DetailsActivity;
import com.example.phitup.youtubeapi.adapters.VideoPostAdapter;
import com.example.phitup.youtubeapi.interfaces.OnitemClickListener;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelFragment extends Fragment {

    private static final String GOOGLE_YOUTUBE_API_KEY = "AIzaSyBoP7-tRg_cNFdTMvpZ8e5GzK-CHY0r2H0";
    private static String CHANNEL_ID = "UCZPDno5xQQkuM6T9jWVZGOw";
    private static String CHANNEL_GET_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&channelid="+ CHANNEL_ID +"&maxResults=20&key="+ GOOGLE_YOUTUBE_API_KEY +"";

//    private final String TAG = getClass().getSimpleName();

    private RecyclerView mList_videos;
    private VideoPostAdapter adapter;
    private ArrayList<YoutubeDataModel> mListData = new ArrayList<>();

    public ChannelFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channel, container, false);
        mList_videos = view.findViewById(R.id.mList_videos);
        initList(mListData);
        new RequestYoutubeAPI().execute();
        return view;
    }

    private void initList(ArrayList<YoutubeDataModel> mListData) {
//        mListData = new ArrayList<>();
        mList_videos.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VideoPostAdapter(mListData ,getActivity(), new OnitemClickListener() {
            @Override
            public void onItemClick(YoutubeDataModel item) {
                YoutubeDataModel youtubeDataModel = item;
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                intent.putExtra(YoutubeDataModel.class.toString() , youtubeDataModel.toString());
                startActivity(intent);
            }
        });
        mList_videos.setAdapter(adapter);
    }

    // create an asynctask to get all the data from youtube
    private class RequestYoutubeAPI extends AsyncTask<Void , String , String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(CHANNEL_GET_URL);
            Log.d("BBB" , CHANNEL_GET_URL);

            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                String json = EntityUtils.toString(httpEntity);
                return json;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s != null) {

                try {
                    JSONObject jsonObject = new JSONObject(s);
                    Log.d("response" , jsonObject.toString());
                    mListData = parseVideoListFromResponse(jsonObject);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    private ArrayList<YoutubeDataModel> parseVideoListFromResponse(JSONObject jsonObject) {

        ArrayList<YoutubeDataModel> mList = new ArrayList<>();
        if(jsonObject.has("items")){
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("items");
                for(int i = 0 ; i < jsonArray.length() ; i++){

                    JSONObject json = jsonArray.getJSONObject(i);
                    if(json.has("id")){
                        JSONObject jsonID = json.getJSONObject("id");
                        if(jsonID.has("kind")){
                            if(jsonID.getString("kind").equals("youtube#video")){
                                // get the data from snippet
                                JSONObject jsonSnippet = json.getJSONObject("snippet");
                                String title = jsonSnippet.optString("title");
                                String description = jsonSnippet.optString("description");
                                String publishedAt = jsonSnippet.optString("publishedAt");
                                String thumbnail = jsonSnippet.optJSONObject("thumbnails").optJSONObject("high").optString("url");

                                YoutubeDataModel youtube = new YoutubeDataModel();
                                youtube.setTitle(title);
                                youtube.setDescription(description);
                                youtube.setPublishedAt(publishedAt);
                                youtube.setThumbnail(thumbnail);
                                mListData.add(youtube);
                            }
                        }
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mList;
    }
}
