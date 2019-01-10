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

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    private static final String TAG = "MYLOGS";

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if (prefix == ""){
            Random rand = new Random();
            int r = rand.nextInt(words.size());
            return words.get(r);
        } else {
            return binaryStringSearch(prefix);
        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        String selected = null;
        Random rand = new Random();
        if (prefix==""){
            selected = words.get(rand.nextInt(words.size()));
        }else{

        int l = binarySearchLeftMost(prefix);
        Log.e(TAG, "leftmost: "+Integer.toString(l));
        Log.e(TAG, "leftmost word: "+words.get(l));
        int r = binarySearchRightMost(prefix);
        Log.e(TAG, "rightmost: "+Integer.toString(r));
        Log.e(TAG, "rightmost word: "+words.get(r));
        Log.e(TAG, "prefix: "+prefix);
        List sub = words.subList(l, r+1);
        ArrayList<String> even = new ArrayList<String>();
        ArrayList<String> odd = new ArrayList<String>();
        for(int i = 0; i<sub.size();i++){
            String s = (String) sub.get(i);
            if (s.length() % 2 ==0){
                even.add(s);
            }else if (s.length()%2==1){
                odd.add(s);
            }
        }
        if (even.size() > 0 && prefix.length()%2 == 0){
            int k = rand.nextInt(even.size());
            Log.e(TAG, "left random k: "+Integer.toString(k));
            selected = even.get(k);
        }else if (odd.size() >0 && prefix.length()%2 == 1){
            int k = rand.nextInt(odd.size());
            Log.e(TAG, "right random k: "+Integer.toString(k));
            selected = odd.get(k);
        }else{
            if (sub.size()>0){
                selected = (String)sub.get(rand.nextInt(sub.size()));
            }else {
                selected = null;
            }

        }
        Log.e(TAG, "even array size: "+Integer.toString(even.size()));
        Log.e(TAG, "odd array size: "+Integer.toString(odd.size()));
        Log.e(TAG, "Selected "+ selected);

        }
        if (selected == prefix){
            selected = null;
        }
        return selected;
    }
    public int binarySearchLeftMost(String s){
        int low = 0;
        int high = words.size();
        int mid;
        while (low<high){
            mid = (low+high)/2;
//            Log.i(TAG, Integer.toString(mid));
            String word = words.get(mid);
            if (word.compareTo(s)< 0){
                low = mid + 1;
            }else{
                high = mid;
            }
        }
        return low;
    }
    public int binarySearchRightMost(String s){
        int low = 0;
        int high = words.size();
        int mid;
        while (low<high){
            mid = (low+high)/2;
            String word = words.get(mid);
            if (word.compareTo(s) < 0 || word.startsWith(s)){
                low = mid + 1;
            }else{
                high = mid;
            }
        }
        return low-1;
    }
    public String binaryStringSearch(String s){
        int low = 0;
        int high = words.size()-1;
        int mid;
        while(low<=high){
            mid = (low+high)/2;
            String word = words.get(mid);
//            String prefix = word.substring(0, s.length());
            if (word.length()>s.length() && word.substring(0, s.length()).equals(s)){
                return word;
            } else if (word.length()>s.length() && word.substring(0, s.length()).compareTo(s) < 0){
                low = mid + 1;
            } else {
                high = mid -1;
            }
        }
        return null;
    }
}
