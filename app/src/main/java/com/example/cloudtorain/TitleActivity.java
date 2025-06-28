package com.example.cloudtorain;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


public class TitleActivity extends AppCompatActivity {

    Button startButton;
    MediaPlayer introPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        introPlayer = MediaPlayer.create(this, R.raw.intro_narration);
        introPlayer.start();

        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(v -> {
            if (introPlayer != null && introPlayer.isPlaying()) {
                introPlayer.stop();
                introPlayer.release();
                introPlayer = null;
            }

            Intent intent = new Intent(TitleActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (introPlayer != null) {
            introPlayer.release();
            introPlayer = null;
        }
    }
}