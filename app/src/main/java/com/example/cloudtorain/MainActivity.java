package com.example.cloudtorain;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.graphics.drawable.AnimationDrawable;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView sun, cloud, rain;
    TextView statusText;
    MediaPlayer mediaPlayer;
    boolean isEvaporated = false;
    AnimationDrawable rainAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sun = findViewById(R.id.sun);
        cloud = findViewById(R.id.cloud);
        rain = findViewById(R.id.rain);
        statusText = findViewById(R.id.statusText);

        // Tap on sun → Evaporation
        sun.setOnClickListener(v -> {
            isEvaporated = true;
            statusText.setText("Water is evaporating into clouds!");
            //playNarration(R.raw.evaporation); // Add narration mp3 in res/raw
            //cloud.setImageResource(R.drawable.cloud_full); // cloud_full.png
        });

        // Tap on cloud → Rain
        cloud.setOnClickListener(v -> {
            if (isEvaporated) {
                statusText.setText("Cloud is full, it's raining!");
                rain.setVisibility(View.VISIBLE);
                rain.setBackgroundResource(R.drawable.rain_anim);
                rainAnimation = (AnimationDrawable) rain.getBackground();
                rainAnimation.start();
                //playNarration(R.raw.precipitation); // Add rain narration
            } else {
                statusText.setText("Tap the sun first to start evaporation.");
            }
        });
    }

    private void playNarration(int audioRes) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(this, audioRes);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
    }
}