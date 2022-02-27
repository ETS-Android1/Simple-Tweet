package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.codepath.apps.restclienttemplate.TimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tweet {
    public String body;
    public String createdAt;
    public long id;
    public User user;
    public String timeStamp;
    public static final String TAG = "TWEET";

    //Empty constructor for parceler
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try {
            tweet.body = jsonObject.getString("text");
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
            tweet.id = jsonObject.getLong("id");
            tweet.getFormattedTimestamp();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for(int i=0;i<jsonArray.length();i++){
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }
    public void getFormattedTimestamp(){
        Log.i(TAG, "formatting time stamp");
        timeStamp = TimeFormatter.getTimeDifference(createdAt);
    }
}
