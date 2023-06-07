package com.example.epicgaming;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.epicgaming.models.Ball;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    public int armsPosition = 2;
    protected int score = 0;
    protected long speed = 1000; //interval between each ball movement in milliseconds
    protected Ball[] listeBalls = new Ball[3];
    protected boolean canMove = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
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
            // reset();

            Toast.makeText(this, "test", Toast.LENGTH_LONG).show();


            // TEMP //

            // start a game
            // generate balls
            for(int i = 0; i < listeBalls.length; i++) {
                listeBalls[i] = new Ball(
                        i+1,
                        2
                );
                display(listeBalls[i]);
            }

            // reset arms
            armsPosition = 2;
            updateArms();

            // hide crash sprites
            findViewById(R.id.crash_left).setVisibility(View.INVISIBLE);
            findViewById(R.id.crash_right).setVisibility(View.INVISIBLE);

            // allow to move
            canMove = true;

            int count = 0;
            try{
                while(canMove) {

                    /// Problem here

                    android.os.SystemClock.sleep(10);

                    ///

                    count += 1;

                    for (int i = 0; i < listeBalls.length; i++) {
                        listeBalls[i].progress();
                        display(listeBalls[i]);
                    }
                    if (count == 100) {
                        canMove = false;
                    }
                }
            Toast.makeText(this, Integer.toString(count), Toast.LENGTH_SHORT).show();

            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }


            // TEMP //
        });


    }
    /*
    protected void reset(){
        // start a game
        // generate balls
        for(int i = 0; i < listeBalls.length; i++) {
            listeBalls[i] = new Ball(
                    i+1,
                    2
            );
            display(listeBalls[i]);
        }

        // reset arms
        armsPosition = 2;
        updateArms();

        // hide crash sprites
        findViewById(R.id.crash_left).setVisibility(View.INVISIBLE);
        findViewById(R.id.crash_right).setVisibility(View.INVISIBLE);

        // allow to move
        canMove = true;

        for(int i = 0; i < listeBalls.length; i++) {
            listeBalls[i].progress();
            display(listeBalls[i]);
        }

        Toast.makeText(this, "test2", Toast.LENGTH_LONG).show();

        startLoop();
    }

    protected void startLoop(){
        int count = 0;
        //try{

            //while(canMove){
                //TimeUnit.MILLISECONDS.sleep(speed);
                count+=1;

                for(int i = 0; i < listeBalls.length; i++) {
                    listeBalls[i].progress();
                    display(listeBalls[i]);
                }
                if(count==100){
                    canMove=false;
                }
        Toast.makeText(this, "test3", Toast.LENGTH_SHORT).show();
           // }
      //  } catch (Exception e) {
        //    System.out.println(e);
          //  throw new RuntimeException(e);
       // }
    }
    */

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