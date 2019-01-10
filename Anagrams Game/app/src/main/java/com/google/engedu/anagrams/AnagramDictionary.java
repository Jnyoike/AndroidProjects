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

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 3;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 8;
    private static int wordLength = DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    public static ArrayList<String> wordList = new ArrayList<String>();
    public static Set<String> wordSet = new HashSet<String>();
    public static HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    public static HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();



    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            String wordSorted = sortLetters(word);
            Integer l = wordSorted.length();
            if (lettersToWord.containsKey(wordSorted)) {
                lettersToWord.get(wordSorted).add(word);
            }
            else {
                ArrayList<String> arr = new ArrayList<String>();
                arr.add(word);
                lettersToWord.put(wordSorted, arr);
            }
            if(sizeToWords.containsKey(l)){
                sizeToWords.get(l).add(word);
            }
            else {
                ArrayList<String> array = new ArrayList<String>();
                array.add(word);
                sizeToWords.put(l, array);
            }
            wordList.add(word);
            wordSet.add(word);
            }


        }


    public boolean isGoodWord(String word, String base) {
        return (wordSet.contains(word) && !word.contains(base));
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String word = sortLetters(targetWord);
        if(lettersToWord.containsKey(word)){
            result = lettersToWord.get(word);
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        for (char c = 'a'; c <= 'z'; c++){
            for (char d = 'a'; d <= 'z'; d++) {
                String newWord = sortLetters(word.concat("" + c+d));
                if (lettersToWord.containsKey(newWord)) {
                    result.addAll(lettersToWord.get(newWord));
                }
            }

        }

        return result;
    }

    public String pickGoodStarterWord() {


        String Word = null;
        boolean check = false;
        ArrayList<String> Arr = sizeToWords.get(wordLength);
        int randomInt = random.nextInt(Arr.size());
        for (int i = randomInt; i<Arr.size(); i++){
            Word = Arr.get(i);
            List<String> result = getAnagramsWithOneMoreLetter(Word);
            if(result.size() >= MIN_NUM_ANAGRAMS){
                check = true;
                break;
            }
        }
        if (check == false){
            for (int j= 0; j<randomInt; j++){
                Word = Arr.get(j);
                List<String> result = getAnagramsWithOneMoreLetter(Word);
                if(result.size() >= MIN_NUM_ANAGRAMS){
                    check = true;
                    break;
                }
            }
        }
        if(check == false){
            Word = "pots";
        }
        if(wordLength < MAX_WORD_LENGTH){
            wordLength++;
        }
        else{
            wordLength = MAX_WORD_LENGTH;
        }
        return Word;

    }

    public String sortLetters(String word){
        char temp[] = word.toCharArray();
        Arrays.sort(temp);
        return new String(temp);
    }
}
