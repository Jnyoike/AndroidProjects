package com.example.demouser.scarnesdice;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int userTotalScore = 0;
    private static int userTurnScore = 0;
    private static int compTotalScore = 0;
    private static int compTurnScore = 0;
    private boolean userTurn;
    private Random random = new Random();
    Handler handler = new Handler();

//    private static String turn;

    public void rollOnClick(View view){
       int rand = random.nextInt(6);
       ImageView image = (ImageView) findViewById(R.id.dice);
       if (rand == 1){
            image.setImageResource(R.drawable.dice1);
       }else if (rand == 2){
           image.setImageResource(R.drawable.dice2);
       }else if(rand == 3){
            image.setImageResource(R.drawable.dice3);
       }else if (rand == 4){
            image.setImageResource(R.drawable.dice4);
       }else if(rand == 5){
           image.setImageResource(R.drawable.dice5);
       }else if(rand == 6){
           image.setImageResource(R.drawable.dice6);
       }
       gameLogic(rand);

    }
    public void updateView(int rand){
        ImageView image = (ImageView) findViewById(R.id.dice);
        if (rand == 1){
            image.setImageResource(R.drawable.dice1);
        }else if (rand == 2){
            image.setImageResource(R.drawable.dice2);
        }else if(rand == 3){
            image.setImageResource(R.drawable.dice3);
        }else if (rand == 4){
            image.setImageResource(R.drawable.dice4);
        }else if(rand == 5){
            image.setImageResource(R.drawable.dice5);
        }else if(rand == 6){
            image.setImageResource(R.drawable.dice6);
        }
        //gameLogic(rand);
    }
    public void gameLogic(int rand){
        if (rand != 1 && userTurn){
            userTurnScore = userTurnScore + rand;
//            TextView turn = (TextView) findViewById(R.id.Turn);
//            String n = Integer.toString(userTurnScore);
//            turn.setText("Your score is "+n);
        } else if(rand == 1 && userTurn){
            userTurnScore = 0;
//            userTurn = false;
            compTurn();

        }
        TextView turn = (TextView) findViewById(R.id.Turn);
        String n = Integer.toString(userTurnScore);
        turn.setText("Your score is "+n);

    }
    Runnable runComputerTurn = new Runnable() {
        @Override
        public void run() {
            compTurn();
        }
    };
    public void compTurn() {
        userTurn = false;
        Button btn1 = (Button) findViewById(R.id.roll);
        btn1.setEnabled(false);
        Button btn2 = (Button) findViewById(R.id.bank);
        btn2.setEnabled(false);
        TextView turn = (TextView) findViewById(R.id.Turn);
        if (!userTurn && compTurnScore < 20) {
            int rand = random.nextInt(6);
            updateView(rand);
            if (rand != 1){
              compTurnScore = compTurnScore + rand;
              turn.setText("Computer Score: "+Integer.toString(compTurnScore));
              handler.postDelayed(runComputerTurn, 1000);
            }else {
               compTurnScore = 0;
                endComputerTurn(btn1, btn2, compTurnScore);
            }
        }else {
            endComputerTurn(btn1, btn2, compTurnScore);
        }

    }

    public void endComputerTurn(Button btn1, Button btn2, int compTurnScore){
        compTotalScore = compTurnScore + compTotalScore;
        TextView comp = (TextView) findViewById(R.id.CompScore);
        TextView turn = (TextView) findViewById(R.id.Turn);
        if (compTotalScore >= 100) {
            comp.setText("The Computer Score: "+Integer.toString(compTotalScore));
            turn.setText("Computer Won!!!!!!");

        } else {
            comp.setText("The Computer Score: "+Integer.toString(compTotalScore));
            turn.setText("Your turn");
            userTurn = true;
            btn1.setEnabled(true);
            btn2.setEnabled(true);
        }
    }
    public void Reset(View view){
        userTurnScore = 0;
        compTotalScore = 0;
        userTotalScore = 0;
        compTurnScore = 0;
        TextView turn = (TextView) findViewById(R.id.Turn);
        turn.setText("Your score is: 0");
        TextView compScore = (TextView) findViewById(R.id.CompScore);
        compScore.setText("Computer score is: 0");
        TextView playerScore = (TextView) findViewById(R.id.PlayerTotalScore);
        playerScore.setText("Your score is: 0");


    }
    public void Bank(View view){
        userTotalScore = userTotalScore + userTurnScore;
        userTurnScore = 0;
        TextView totalScore = (TextView) findViewById(R.id.PlayerTotalScore);
        String n = Integer.toString(userTotalScore);
        TextView turn = (TextView) findViewById(R.id.Turn);
        totalScore.setText("Your total score: "+ n);
        if (userTotalScore >= 100) {
            turn.setText("User Wins!");
        }
        else {
            turn.setText("Your score is: 0");
            compTurn();

        }




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onStart(null);
    }
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        if (userTurn) {
            userTurn = true;
        } else {
            compTurn();
        }
        return true;
    }
}
