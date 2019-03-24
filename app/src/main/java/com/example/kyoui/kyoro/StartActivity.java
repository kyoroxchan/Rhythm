package com.example.kyoui.kyoro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity {
    String YoutubeAPI = "AIzaSyAMeuJ8mCHOCfi94FNjcAqAautMx-WGUu0";
    YouTubePlayerView player;
    YouTubePlayer mYouTubePlayer;

    EditText editText;
    TextView loadText;
    Button startButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        startButton = (Button) findViewById(R.id.start_button);
        editText = (EditText) findViewById(R.id.editText);
        loadText = (TextView) findViewById(R.id.loadtext);

        player.initialize(YoutubeAPI, new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                mYouTubePlayer = youTubePlayer;
                mYouTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
                    @Override
                    public void onLoading() {
                        loadText.setText("動画を取得中...");

                    }

                    @Override
                    public void onLoaded(String s) {
                        // 読み込み成功したとき

                    }

                    @Override
                    public void onAdStarted() {

                    }

                    @Override
                    public void onVideoStarted() {

                    }

                    @Override
                    public void onVideoEnded() {

                    }

                    @Override
                    public void onError(YouTubePlayer.ErrorReason errorReason) {
                        // 読み込み失敗したとき

                    }
                });
                mYouTubePlayer.loadVideo("6j_e-7VXOHc");

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }

    public void onSearchClick(View v) {
        String url = editText.getText().toString();
        String videoId = getYoutubeID(url);
        mYouTubePlayer.loadVideo(videoId);
    }

    private String getYoutubeID(String youtubeUrl) {
        if (TextUtils.isEmpty(youtubeUrl)) {
            return "";
        }
        String videoId = "";

        String expression =
                "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(youtubeUrl);
        if (matcher.matches()) {
            String groupIndex1 = matcher.group(1);
            if (groupIndex1 != null && groupIndex1.length() == 11)
                videoId = groupIndex1;
        }

        return videoId;
    }
}
