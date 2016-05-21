package com.example.faisal.sudokugame;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;

public class TopScores extends AppCompatActivity {
    MyDBHandler dbHandler;
    MediaPlayer mediaPlayer;
    int current=0 ;

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(TopScores.this,R.raw.tone);
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
        setContentView(R.layout.activity_top_scores);

        dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.databaseToString();



        ListAdapter scoresAdapter = new CustomAdapter(this, MyDBHandler.list);
        ListView NotesView = (ListView) findViewById(R.id.ScoresList);
        NotesView.setAdapter(scoresAdapter);

    }
}
