/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String TAG = "computerTurn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private static final String gameStatus = "gameStatus";
    private static final String typedText = "typedText";
    private Random random = new Random();
    TextView text;
    TextView label;
//    String word = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            label.setText(savedInstanceState.getCharSequence(gameStatus));
            text.setText(savedInstanceState.getCharSequence(typedText));
        }
        setContentView(R.layout.activity_ghost);
        AssetManager assetManager = getAssets();

        try {
            InputStream stream = assetManager.open("words.txt");
            dictionary = new SimpleDictionary(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
    public void challenge(View view){
        String word = text.getText().toString();
        if(word.length()>=4 && dictionary.isWord(word)){
            label.setText("User won!!");
        }else {
            String otherWord = dictionary.getGoodWordStartingWith(word);
            if (otherWord != null && otherWord != word){
                label.setText("Computer Wins "+otherWord);
            }else {
                label.setText("The user wins");
            }

        }
    }
    public void restart(View view){
        label.setText("");
        text.setText("");
        Button challenge = (Button) findViewById(R.id.Challenge);
        challenge.setEnabled(true);
        onStart(null);
    }

    private void computerTurn() {
        label = (TextView) findViewById(R.id.gameStatus);
        Log.i(TAG, "Its the computer's turn");
//        TextView text = (TextView) findViewById(R.id.ghostText);
        String word = text.getText().toString();
        // TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        if(word.length()>=4 && dictionary.isWord(word)){
            Log.i(TAG, "Its the computer's victory");
            label.setText("VICTORY");
            Button challenge = (Button) findViewById(R.id.Challenge);
            challenge.setEnabled(false);
        }else {
            String longerWord = dictionary.getGoodWordStartingWith(word);
            if (longerWord == null) {
                Log.i(TAG, "Its the computer's victory again");
                label.setText("VICTORY");
                Button challenge = (Button) findViewById(R.id.Challenge);
                challenge.setEnabled(false);
                Log.i(TAG, label.getText().toString());
            } else {
                String newWord = word.concat("" + longerWord.charAt(word.length()));
                Log.i(TAG, "word: " + word);
                Log.i(TAG, "newword: " + newWord);
                Log.i(TAG, "longerword: " + longerWord);
                Log.i(TAG, Character.toString(longerWord.charAt(word.length())));
                text.setText(newWord);
                userTurn = true;
                label.setText(USER_TURN);
            }

        }
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        char c = (char) event.getUnicodeChar();
        KeyCharacterMap map = event.getKeyCharacterMap();
        Character c = map.getDisplayLabel(keyCode);

        if(Character.isLetter(c)){
            String t = text.getText().toString();
            String word = t.concat(""+c);
            text.setText(word);
            computerTurn();

            return true;
        }else{
            return super.onKeyUp(keyCode, event);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putCharSequence(typedText, text.getText().toString());
        outState.putCharSequence(gameStatus, label.getText().toString());
        super.onSaveInstanceState(outState);
    }
}
