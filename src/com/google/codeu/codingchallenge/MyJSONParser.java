// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.codingchallenge;

import java.util.stack;
import java.io.IOException;

final class MyJSONParser implements JSONParser {

  @Override
  public JSON parse(String in) throws IOException {
    // TODO: implement this
        int firstQuoteIndex = -1;
        int secondQuoteIndex = -1;
        Character charr;
        boolean valueObject = false;
        boolean valid = false;
        int thirdQuoteIndex = -1, fourthQuoteIndex = -1, secondBracketIndex = -1, closingBracketIndex = -1;
        if ( bracketCheck(in) ) {
          int colonIndex = in.indexOf(':');
            if ( colonIndex != -1 ) {
                int[] firstHalfFormat = {0, 0, 0};
                int i = 0;
                boolean stop = false;
                charr = in.charAt(i);

                while (i < colonIndex && !stop) {
                    if ( charr == '{' || firstHalfFormat[ 0 ] == 1 ) {
                        firstHalfFormat[ 0 ] = 1;
                        if (firstHalfFormat[1] != 1){
                            i+=1;
                        }
                        charr = in.charAt(i);
                        if ( charr == '\"' || firstHalfFormat[ 1 ] == 1 ) {
                            if (firstQuoteIndex == -1)
                                firstQuoteIndex = i;
                            if (firstHalfFormat[1] != 1){
                                i+=1;
                            }
                            firstHalfFormat[ 1 ] = 1;
                            charr = in.charAt(i);
                            if ( charr == '\"' ) {
                                firstHalfFormat[ 2 ] = 1;
                                if (secondQuoteIndex == -1)
                                    secondQuoteIndex = i;
                                i++;
                                stop = true;
                            } else { 
                                i++;
                            }
                        } else if ( charr == ' ' ) {
                            if (i > 0){
                                if (in.charAt(i - 1) == ' '){
                                    i++;
                                }
                            }

                        } 
                      else {
                            return null;
                        }
                    } else if ( charr == ' ' ) { 
                        i++;
                    } else {
                        return null;
                    }
                }
                for (int x : firstHalfFormat) {
                    if ( x != 1 )
                        return null;
                }
                if ( firstQuoteIndex + 1 == secondQuoteIndex ) { 
                    return null;
                }
                int[] secondHalfFormat = {0, 0};

                i = colonIndex + 1;
                int a = in.indexOf('{');
                String restOfInput = in.substring(i);
                if ( restOfInput.indexOf('{') != -1 ) { 
                    valueObject = true;
                }
                if ( !valueObject ) {
                    stop = false;
                    while (i < in.length() && !stop) {
                        charr = in.charAt(i);
                        if ( charr == '\"' || secondHalfFormat[0] == 1) {
                            if (thirdQuoteIndex == -1)
                                thirdQuoteIndex = i;
                            if (secondHalfFormat[0] != 1){
                                i++;
                            }
                            secondHalfFormat[ 0 ] = 1;
                            charr = in.charAt(i);
                            if ( charr == '\"' ) {
                                secondHalfFormat[ 1 ] = 1;
                                if (fourthQuoteIndex == -1)
                                    fourthQuoteIndex = i;
                                valid = true;
                                stop = true;
                                i++;
                            } else {
                                i++;
                            }
                        } else if ( charr == ' ' ) {
                            i++;
                        } else {
                            return null;
                        }
                    }
                    for (int x : secondHalfFormat) {
                        if ( x != 1 )
                            return null;
                    }
                } else {
                    stop = false;
                    while (i < in.length() && !stop) {
                        charr = in.charAt(i);
                        if ( charr == '{' ) {
                            secondBracketIndex = i;
                            closingBracketIndex = in.indexOf('}');
                            String jObject = in.substring(secondBracketIndex+ 1, closingBracketIndex);
                            if ( checkObject(jObject) ) {
                                valid = true;
                                stop = true;
                            }
                        } else if ( charr == ' ' ) {
                            i++;
                        } else {
                            return null;
                        }
                    }
                }
            } else {
                return null;
            }
        }
        if (valid) {
            JSON output = new MyJSON();
            String val;
            String key = in.substring(firstQuoteIndex + 1, secondQuoteIndex);
            if ( valueObject ) {
                val = in.substring(secondBracketIndex, closingBracketIndex + 1);
                output = new MyJSON(key, val);
            } else {
                val = in.substring(thirdQuoteIndex + 1, fourthQuoteIndex);
                output = new MyJSON(key, val);
            }
            return output;
        } else {
            return null;

        }

    }
  
    private boolean bracketCheck(String str) {
        Stack<Character> checker = new Stack();
        boolean balance = true;
        int i = 0;
        while (i < str.length() && balance) {
            Character charr = str.charAt(i);
            if ( charr.equals('{') ) {
                checker.push(charr);
            } else if ( charr.equals('}') ) {
                if ( checker.isEmpty() ) {
                    balance = false;
                } else {
                    checker.pop();
                }
            }
            i++;
        }

        if ( balance && checker.isEmpty() ) {
            return true;
        } else {
            return false;
        }
    }
  
    private boolean checkObject(String input) {
        String[] list = input.split(",");
        int index, quoteKeyOne = -1, quoteKeyTwo = -1, lastQuote = -1, count = 0;
        int[] check = {0,0,0,0};
        boolean output = false;
        boolean stop = false;
        for (String x : list) {
            index = 0;
            stop = false;
            for (int a = 0; a < 4; a++){
                check[a] = 0;
            }
            while (index < x.length() && !stop) {
                if ( x.charAt(index) == '\"' || check[0] == 1) {
                    if (quoteKeyOne == -1){
                        quoteKeyOne = index;
                    }

                    if (check[0] != 1){
                        index++;
                    }
                    check[0] = 1;
                    if ( x.charAt(index) == '\"' || check[1] == 1) {
                        if (quoteKeyTwo == -1){
                            quoteKeyTwo = index;
                        }
                        if (check[1] != 1){
                            index++;
                        }
                        check[1] = 1;
                        if ( quoteKeyOne + 1 != quoteKeyTwo ) { 
                            if ( x.charAt(index) == ':' || check[2] == 1) {
                                if (check[2] != 1){
                                    index++;
                                }
                                check[2] = 1;
                                if ( x.charAt(index) == '\"' || check[3] == 1) {
                                    if (check[3] != 1){
                                        index++;
                                    }
                                    check[3] = 1;
                                    if ( x.charAt(index) == '\"' ) {
                                        stop = true;
                                    }
                                    else {
                                        index++;
                                    }
                                } else if ( x.charAt(index) == ' ' ) {
                                    index++;
                                } else {
                                    return false;
                                }
                            } else if ( x.charAt(index) == ' ' ) {
                                index++;
                            } else {
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        index++;
                    }
                } else if ( x.charAt(index) == ' ' ) {
                    index++;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws IOException {
        MyJSONParser a = new MyJSONParser();
        JSON here = a.parse("{ \"name\":{\"first\":\"sam\", \"last\":\"doe\" } }");
        System.out.println(here);
    }

}
