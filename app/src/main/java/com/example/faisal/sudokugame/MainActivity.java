package com.example.faisal.sudokugame;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MyDBHandler dbHandler;
    MediaPlayer mediaPlayer;
    int current=0 ;
    Button check;
    TextView myTime;
    private long startTime = 0L;
    private Handler myHandler = new Handler();
    long timeInMillies = 0L;
    long timeSwap = 0L;
    long finalTime = 0L;
    public static ArrayList<String> list = null;
    EditText[] editTexts = new  EditText[81];
    static int[][] arr;
    String m_Text = "",m_Time;
    EditText input;




    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.game);
        mediaPlayer.seekTo(current);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
        startTime = SystemClock.uptimeMillis();
        myHandler.postDelayed(updateTimerMethod, 0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
        current = mediaPlayer.getCurrentPosition();
        timeSwap += timeInMillies;
        myHandler.removeCallbacks(updateTimerMethod);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.stop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=new ArrayList<String>();
        Initialize();
        try {
            readFromAssets(MainActivity.this, "gameGenerator");
        }catch (IOException e){;
            e.fillInStackTrace();
        }
        int index = (int) (0 + (Math.random() * (list.size() - 0)));
        String[] tempArray = list.get(index).split("[-]");
        for (int i = 0; i < tempArray.length; i++) {
            if (!tempArray[i].equals("0")) {
                editTexts[i].setText(tempArray[i].toString());
                editTexts[i].setTextColor(Color.BLACK);
                editTexts[i].setKeyListener(null);
                editTexts[i].setCursorVisible(false);
                editTexts[i].setPressed(false);
                editTexts[i].setFocusable(false);
            } else {
                editTexts[i].setText("");
            }

        }
        arr = fillArray(arr);


        check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int i=0;

                solve(new Box(0, 0));
                boolean Solved = false;

                int k = 0;

                for ( i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (editTexts[k].getText().toString().equals(arr[i][j]+"")) {
                            Solved = true;
                            k++;
                        } else {
                            Solved = false;
                            i = 9;
                            break;
                        }
                    }
                }

                if (Solved) {
                    timeSwap += timeInMillies;
                    myHandler.removeCallbacks(updateTimerMethod);
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                    alertDialog2.setTitle("Congratulation You Are Smart");
                    alertDialog2.setMessage("Please Enter Your Name:");
                     input = new EditText(MainActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT );
                    alertDialog2.setView(input);
                    alertDialog2.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            m_Text = input.getText().toString();
                            Intent i = new Intent(MainActivity.this,StartupActivity.class);
                            Names n = new Names(m_Text,m_Time);
                            dbHandler.addScore(n);
                            startActivity(i);
                            finish();

                        }
                    });
                    alertDialog2.show();


                }
                else {
                    AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(MainActivity.this);
                    alertDialog2.setTitle("Checker");
                    alertDialog2.setMessage("Incorrect Try Again");
                    alertDialog2.show();
                }

            }
        });


    }
    private Runnable updateTimerMethod = new Runnable() {

        public void run() {
            timeInMillies = SystemClock.uptimeMillis() - startTime;
            finalTime = timeSwap + timeInMillies;

            int seconds = (int) (finalTime / 1000);
            int minutes = seconds / 60;
            int hours = minutes/60;
            seconds = seconds % 60;
            m_Time="" +String.format("%02d", hours)+":"+ String.format("%02d", minutes) + ":" + String.format("%02d", seconds);
            myTime.setText(m_Time);
            myHandler.postDelayed(this, 0);
        }

    };
    public void Initialize(){
        dbHandler = new MyDBHandler(this, null, null, 1);
        myTime = (TextView)findViewById(R.id.myTime);
        check = (Button)findViewById(R.id.check);
        arr = new int[9][9];
        for(int i=0;i<81;i++){
            int myID = getResources().getIdentifier("box"+i,"id", getPackageName());
            editTexts[i] = (EditText)findViewById(myID);
            editTexts[i].setTextColor(Color.parseColor("#2aa1c1"));
        ShapeDrawable shape = new ShapeDrawable(new RectShape());
        shape.getPaint().setColor(Color.WHITE);
        shape.getPaint().setStyle(Paint.Style.STROKE);
        shape.getPaint().setStrokeWidth(3);
        editTexts[i].setBackground(shape);
            GridLayout.LayoutParams layoutparams = (GridLayout.LayoutParams) editTexts[i].getLayoutParams();
            layoutparams.width = 117;
            layoutparams.height = 130;
            editTexts[i].setGravity(Gravity.CENTER);
            editTexts[i].setTypeface(Typeface.DEFAULT_BOLD);
            editTexts[i].setLayoutParams(layoutparams);

       }
    }
    public static void readFromAssets(Context context, String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
        String mLine = reader.readLine();
        String Line = "", Res = "";
        while (mLine != null) {
            Line = mLine;
            if (!Line.contains("//")) {
                Res += Line;
            }
            if (!Res.equals(""))
                list.add(Res);

            Res = "";

            mLine = reader.readLine();
        }
        reader.close();
    }
    static boolean solve(Box current) {

        // if the box is null, we have reached the end, Finished all rows
        if (current == null)
            return true;

        // if the current box has a value ,, then we go to the next box
        // and call Solve recursion again
        if (arr[current.row][current.col] != 0)
            return solve(getNextBox(current));

        // if current value don't has a value ,, then we try numbers from 1 - 9
        for (int num = 1; num <= 9; num++) {
            boolean valid = isSafe(current, num);

            // if its not valid then we must try another value( go back to loop)
            if (!valid)
                continue;

            // If its valid then we assign the value to it
            arr[current.row][current.col] = num;

            // Now Check to ALl of next box
            boolean solved = solve(getNextBox(current));

            // if we can assign the next box, then return true
            if (solved)
                return true;
            else
                // If its not solved ,, then we reset from start
                arr[current.row][current.col] = 0;

        }

        // if you reach here, then no value from 1 - 9 for this box can solve
        // return false
        return false;
    }

    static Box getNextBox(Box current) {

        int row = current.row;
        int col = current.col;

        col++;
        if (col > 8) {
            col = 0;
            row++;
        }
        if (row > 8)
            return null;

        Box next = new Box(row, col);

        return next;
    }

    static boolean isSafe(Box box, int value) {


        for (int c = 0; c < 9; c++) {
            if (arr[box.row][c] == value)
                return false;
        }
        for (int r = 0; r < 9; r++) {
            if (arr[r][box.col] == value)
                return false;
        }
        int x1 = 3 * (box.row / 3);
        int y1 = 3 * (box.col / 3);
        int x2 = x1 + 2;
        int y2 = y1 + 2;
        for (int x = x1; x <= x2; x++){
            for (int y = y1; y <= y2; y++){
                if (arr[x][y] == value)
                    return false;
            }
        }
        return true;
    }
    private int[][] fillArray(int[][] arr2) {
        int k = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (editTexts[k].getText().toString().equals("")) {
                    arr2[i][j] = 0;
                    k++;
                } else {
                    arr2[i][j] = Integer.parseInt(editTexts[k].getText().toString());
                    k++;
                }
            }
        }

        return arr2;

    }

}
