package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static String TAG = "ComposeActivity";
    public static int MAX_LENGTH_TWEET = 280;
    Button btnTweet;
    EditText etCompose;
    TextView tvCharCount;
    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApplication.getRestClient(this);

        btnTweet = findViewById(R.id.btnTweet);
        etCompose = findViewById(R.id.etCompose);
        tvCharCount = findViewById(R.id.tvCharCount);
        tvCharCount.setTextColor(Color.BLUE);

        //Add click listener to tweet button
        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tweetContent = etCompose.getText().toString();
                if(tweetContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "Tweet cannot be empty!", Toast.LENGTH_LONG).show();
                }
                if(tweetContent.length()>MAX_LENGTH_TWEET){
                    Toast.makeText(ComposeActivity.this, "Tweet cannot exceed 280 characters", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(ComposeActivity.this, tweetContent, Toast.LENGTH_LONG);
                //Make api call to twitter api
                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess "+TAG);
                        Tweet tweet = Tweet.fromJson(json.jsonObject);
                        Log.i(TAG, "published tweet "+ tweet.body);
                        Intent intent = new Intent();
                        //pass tweet back
                        intent.putExtra("tweet", Parcels.wrap(tweet));
                        setResult(RESULT_OK);
                        //close activity
                        finish();
                    }
                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure compose publish tweet "+response, throwable);
                    }
                });
            }
        });
        //Add listener to compose
        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Fires right as the text is being changed (even supplies the range of text)
//                Log.i(TAG, "i: "+i+" i1: "+i1+" i2: "+i2+" charSeq: "+charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Fires right before text is changing
//                Log.i(TAG, "Before changing: i: "+i+" i1: "+i1+" i2: "+i2+" charSeq: "+charSequence);
                int length  = charSequence.length();
                tvCharCount.setText(length+"/280");
                if(length>280)
                    tvCharCount.setTextColor(Color.RED);
                else
                    tvCharCount.setTextColor(Color.BLUE);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Fires right after the text has changed
            }
        });
    }
}