package com.genius.connectguard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class Splash extends AppCompatActivity {

    ProgressBar splashProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spalsh);

        progressBar();

    }

    public void progressBar(){

        final Intent intent = new Intent(Splash.this, RegisterActivity.class);
        final Handler handler = new Handler();
        splashProgressBar = findViewById(R.id.splashProgressBar);
        final int[] progressStatues = {0};

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (progressStatues[0] <= 100){

                    progressStatues[0] += 10;

                    try {
                        // Sleep for 300 milliseconds.
                        //Just to display the progress slowly
                        Thread.sleep(150);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (progressStatues[0] == 100){

                        startActivity(intent);
                        finish();

                    }else {

                        handler.post(new Runnable() {
                            public void run() {
                                splashProgressBar.setProgress(progressStatues[0]);
                            }
                        });
                    }

                }

            }
        }).start();

    }


}