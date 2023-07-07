package com.example.epicgaming;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.epicgaming.models.Ball;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity{

    // TODO: add a score counter

    public int armsPosition = 2;
    protected int score = 0;
    protected int scoreTillSpeedup = 3;
    protected long speed = 1000; //interval between each ball movement in milliseconds
    protected Ball[] listeBalls = new Ball[3];
    protected boolean canMove = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);
        findViewById(R.id.btnLeft).setOnClickListener(v -> {
            // press left button
            if(armsPosition !=1 && canMove){
                armsPosition -=1;
                updateArms();
            };
        });
        findViewById(R.id.btnRight).setOnClickListener(v -> {
            // press right button
            if(armsPosition !=3 && canMove){
                armsPosition +=1;
                updateArms();
            };
        });
        findViewById(R.id.btnReset).setOnClickListener(v -> {
            // press reset button
                score=0;
                scoreTillSpeedup=3;
                speed=1000;

                for (Thread t : Thread.getAllStackTraces().keySet()) {
                    if (t.getName().equals("gameThread")){t.interrupt();};
                }

                // start a game
                // generate balls
                for (int i = 0; i < listeBalls.length; i++) {
                    listeBalls[i] = new Ball(
                            i + 1
                    );
                    display(listeBalls[i]);
                }

                // reset arms
                armsPosition = 2;
                updateArms();
                findViewById(R.id.guy).setVisibility(View.VISIBLE);
                canMove = true;

                // hide crash sprites
                findViewById(R.id.crash_left).setVisibility(View.INVISIBLE);
                findViewById(R.id.crash_right).setVisibility(View.INVISIBLE);

                new Thread("gameThread") {
                    public void run() {
                        int count = 0;
                        while (canMove) {
                            count += 1;
                            try {
                                int finalCount = count;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ballCheck();
                                        if(canMove) {
                                            for (int i = 0; i < listeBalls.length; i++) {
                                                listeBalls[i].progress();
                                                display(listeBalls[i]);
                                            }
                                        }
                                        if (finalCount == 999) {
                                            canMove = false;
                                        }
                                        if (!canMove || Thread.currentThread().isInterrupted()) {
                                            return;
                                        }
                                    }
                                });
                                Thread.sleep(speed);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                                return;
                            }
                        }
                    }
                }.start();

        });
    }

    public void ballCheck(){
        boolean gameover = false;
        boolean crashsideRight = false;

        for (int i = 0; i < listeBalls.length; i++) {
            if(listeBalls[i].getRow()==1){
                if(listeBalls[i].getPosition()==1){
                    if(armsPosition==1){
                        score += 1;
                        scoreTillSpeedup -= 1;
                    }else{
                        // game over
                        gameover = true;
                        findViewById(R.id.ball1_1).setVisibility(View.INVISIBLE);
                    }
                }
                if(listeBalls[i].getPosition()==listeBalls[i].getRange()){
                    if(armsPosition==3){
                        score += 1;
                        scoreTillSpeedup -= 1;
                    }else{
                        // game over
                        gameover = true;
                        crashsideRight = true;
                        findViewById(R.id.ball1_12).setVisibility(View.INVISIBLE);
                    }
                }
            }
            else if(listeBalls[i].getRow()==2) {
                if (listeBalls[i].getPosition() == 1 || listeBalls[i].getPosition() == listeBalls[i].getRange()) {
                    if (armsPosition == 2) {
                        score += 1;
                        scoreTillSpeedup -= 1;
                    } else {
                        // game over
                        gameover = true;
                        findViewById(R.id.ball2_1).setVisibility(View.INVISIBLE);
                        findViewById(R.id.ball2_10).setVisibility(View.INVISIBLE);
                        if (listeBalls[i].getPosition() == listeBalls[i].getRange()) {
                            crashsideRight = true;
                        }
                    }
                }
            }
            else if(listeBalls[i].getRow()==3){
                if(listeBalls[i].getPosition()==1){
                    if(armsPosition==3){
                        score += 1;
                        scoreTillSpeedup -= 1;
                    }else{
                        // game over
                        gameover = true;
                        findViewById(R.id.ball3_1).setVisibility(View.INVISIBLE);
                    }
                }
                if(listeBalls[i].getPosition()==listeBalls[i].getRange()){
                    if(armsPosition==1){
                        score += 1;
                        scoreTillSpeedup -= 1;
                    }else{
                        // game over
                        gameover = true;
                        crashsideRight = true;
                        findViewById(R.id.ball3_8).setVisibility(View.INVISIBLE);
                    }
                }
            }
        }
        if(scoreTillSpeedup==0){
            speed -= 100;
            scoreTillSpeedup = 3;
        }
        if(gameover){
            canMove = false;
            if(!crashsideRight){
                findViewById(R.id.crash_left).setVisibility(View.VISIBLE);
            }else{
                findViewById(R.id.crash_right).setVisibility(View.VISIBLE);
            }
        }
        TextView scoreText=(TextView)findViewById(R.id.scoreDisplay);
        scoreText.setText(Integer.toString(score));
    }

    protected void updateArms(){
        // update the arms
        findViewById(R.id.armsPos1).setVisibility(armsPosition ==1? View.VISIBLE:View.INVISIBLE);
        findViewById(R.id.armsPos2).setVisibility(armsPosition ==2? View.VISIBLE:View.INVISIBLE);
        findViewById(R.id.armsPos3).setVisibility(armsPosition ==3? View.VISIBLE:View.INVISIBLE);
    }

    private void display(Ball ball){
        for(int i = 1; i < ball.getRange()+1; i++) {
            String ballID = "ball"+ball.getRow()+"_"+i;
            executeDisplay(ballID,false);
        }
        String ballID = "ball"+ball.getRow()+"_"+ball.getPosition();
        executeDisplay(ballID,true);
    }

    public void executeDisplay(String ballID, boolean visible) {
        int resID = getResources().getIdentifier(ballID, "id", getPackageName());
        if (visible) {
            findViewById(resID).setVisibility(View.VISIBLE);
        } else {
            findViewById(resID).setVisibility(View.INVISIBLE);
        }
    }

}
