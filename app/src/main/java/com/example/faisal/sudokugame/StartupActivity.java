package com.example.faisal.sudokugame;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartupActivity extends AppCompatActivity {

    Button exit,scores,newGame;
    MediaPlayer mediaPlayer;
    int current=0 ;


    @Override
    protected void onResume() {
        super.onResume();
         mediaPlayer = MediaPlayer.create(StartupActivity.this,R.raw.main);
        mediaPlayer.seekTo(current);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        current = mediaPlayer.getCurrentPosition();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);
        exit = (Button) findViewById(R.id.exit);
        newGame = (Button) findViewById(R.id.newGame);
        scores = (Button) findViewById(R.id.scores);


        exit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        newGame.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartupActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });

        scores.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StartupActivity.this, TopScores.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;

    }
}
