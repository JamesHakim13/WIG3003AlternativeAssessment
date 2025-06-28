package com.example.cloudtorain;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.TranslateAnimation;
import android.animation.ObjectAnimator;
import android.animation.AnimatorSet;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ImageView sun, cloud, rain;
    TextView statusText;
    MediaPlayer mediaPlayer;
    boolean isEvaporated = false;
    AnimationDrawable rainAnimation;
    Button resetButton, backButton;
    MediaPlayer narrationPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetButton = findViewById(R.id.resetButton);
        backButton = findViewById(R.id.backButton);

        sun = findViewById(R.id.sun);
        cloud = findViewById(R.id.cloud);
        rain = findViewById(R.id.rain);
        statusText = findViewById(R.id.statusText);

        // Tap on sun → Evaporation
        sun.setOnClickListener(v -> {
            isEvaporated = true;

            ImageView steam = findViewById(R.id.steam);
            steam.setVisibility(View.VISIBLE);

            // 🔼 Scale it larger before animating
            steam.setScaleX(1.5f);
            steam.setScaleY(1.5f);

            // Animate steam: fade in + move up
            steam.setAlpha(0f);
            steam.setTranslationY(0f);
            steam.setVisibility(View.VISIBLE);
            steam.setScaleX(1.5f);
            steam.setScaleY(1.5f);

            // Fade in
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(steam, "alpha", 0f, 1f);
            fadeIn.setDuration(300);

            // Move up higher
            ObjectAnimator moveUp = ObjectAnimator.ofFloat(steam, "translationY", 0f, -900f);
            moveUp.setDuration(1500);

            // Fade out
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(steam, "alpha", 1f, 0f);
            fadeOut.setDuration(500);

            //Create first cycle
            AnimatorSet steamCycle1 = new AnimatorSet();
            steamCycle1.playSequentially(fadeIn, moveUp, fadeOut);

            //Create second cycle (reuse steam, reset Y)
            AnimatorSet steamCycle2 = new AnimatorSet();
            steamCycle2.playSequentially(
                    ObjectAnimator.ofFloat(steam, "translationY", 0f), // reset position
                    fadeIn.clone(),
                    moveUp.clone(),
                    fadeOut.clone()
            );

            //Chain both cycles
            AnimatorSet fullSteamAnimation = new AnimatorSet();
            fullSteamAnimation.playSequentially(steamCycle1, steamCycle2);

            fullSteamAnimation.start();

            //Hide after final animation
            fullSteamAnimation.addListener(new android.animation.AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    steam.setVisibility(View.INVISIBLE);
                }
            });


                statusText.setText("Water is evaporating into clouds!");
                playNarration(R.raw.condensation);

            // Step 2: Condensation message after short delay
                        statusText.postDelayed(() -> {
                            statusText.setText("Condensation is happening in the clouds!");

                        }, 3900); // slight delay for natural transition

            // Step 3: Final prompt after condensation narration
                        statusText.postDelayed(() -> {
                            statusText.setText("Now tap the cloud to make it rain!");
                        }, 8000); // ~4 seconds after previous
        });

        // Tap on cloud → Rain
        cloud.setOnClickListener(v -> {
            if (isEvaporated) {
                statusText.setText("Cloud is full, it's raining!");
                playNarration(R.raw.precipitation);

                rain.setVisibility(View.VISIBLE);
                rain.setBackgroundResource(R.drawable.rain_anim);
                rainAnimation = (AnimationDrawable) rain.getBackground();
                rainAnimation.start();

                TranslateAnimation fall = new TranslateAnimation(0, 0, 0, 300);
                fall.setDuration(800);
                fall.setRepeatMode(TranslateAnimation.RESTART);
                fall.setRepeatCount(TranslateAnimation.INFINITE);
                rain.startAnimation(fall);

                // ⏱️ Stop rain after 3 seconds
                rain.postDelayed(() -> {
                    if (rainAnimation != null && rainAnimation.isRunning()) {
                        rainAnimation.stop();
                    }
                    rain.clearAnimation();
                    rain.setVisibility(View.INVISIBLE);

                    // ✅ Show buttons after rain ends
                    resetButton.setVisibility(View.VISIBLE);
                }, 3000);
            } else {
                statusText.setText("Tap the sun first to start evaporation.");
            }
        });

        resetButton.setOnClickListener(v -> {
            // Reset all states
            isEvaporated = false;
            rain.setVisibility(View.INVISIBLE);
            rain.clearAnimation();

            statusText.setText("Tap the sun to start evaporation.");
            resetButton.setVisibility(View.INVISIBLE);
        });

        backButton.setOnClickListener(v -> {
            finish(); // Ends MainActivity, returns to TitleActivity
        });

        playNarration(R.raw.tap_sun);
    }

    private void playNarration(int audioRes) {
        if (narrationPlayer != null) {
            narrationPlayer.release();
        }
        narrationPlayer = MediaPlayer.create(this, audioRes);
        narrationPlayer.start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        if (narrationPlayer != null) {
            narrationPlayer.release();
        }
    }
}