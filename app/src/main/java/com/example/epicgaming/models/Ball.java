package com.example.epicgaming.models;

import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.epicgaming.MainActivity;
import com.example.epicgaming.R;

import java.util.Random;

public class Ball {

    protected int position; //current position of the ball
    protected int range; //range of the ball's positions (8 10 or 12)
    protected int row; //on which row the ball is moving
    protected boolean goingRight; //ball's current direction

    public Ball(int row, float speed){
        Random random = new Random();
        this.row = row;

        if      (row==1) { this.range = 12 ;}
        else if (row==2) { this.range = 10 ;}
        else if (row==3) { this.range = 8  ;}

        this.position = random.nextInt(range-4)+2;
        this.goingRight = true;
    }

    public int getPosition() {
        return position;
    }

    public int getRange() {
        return range;
    }

    public int getRow() {
        return row;
    }

    public boolean isGoingRight() {
        return goingRight;
    }

    public void progress() {
        if(this.goingRight){
            this.position+=1;
        }else{
            this.position-=1;
        }
        if(this.position==1||this.position==this.range){
            this.goingRight=!this.goingRight;
        }
    }
}
