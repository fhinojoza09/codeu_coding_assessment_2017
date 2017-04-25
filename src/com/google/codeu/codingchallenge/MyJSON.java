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

import java.util.ArrayList;
import java.util.Collection;

final class MyJSON implements JSON {
  
  private String jName;
  private String value;
  private JSON jValue;
  private ArrayList jValueList = new ArrayList<String>();
  private ArrayList jNameList = new ArrayList<String>();
  
    public MyJSON (String name, JSON value){
    jName = name;
    jValue = value;
  }
  
  public MyJSON (String name, String val){
    jName = name;
    jValue = null;
    value = val;
    if (val.indexOf(',') != -1) {
      render();
    }
  }

  public MyJSON () {
    jName = null;
    jValue = null;
    value = null;
}
  @Override
  public JSON getObject(String name) {
    // TODO: implement this
    JSON output = new MyJSON();
      if (jName.equals(name)){
        if (!jNameList.isEmpty()){
          Collection<String> nameList = new ArrayList<String>(jNameList);
            int i = 0;
          Collection<String> valueList = new ArrayList<String>(jValueList);
            i = 0;
            output.getObjects(valueList);
            output.getStrings(nameList);
            return output;
        }
        else {
            return null;
        }
    }
    else if (jNameList.contains(name)){
        int name_index = jNameList.indexOf(name);
        return (JSON)jValueList.get(name_index);
    }
    else {
        return null;
    }

}

  @Override
  public JSON setObject(String name, JSON value) {
    // TODO: implement this
    jName = name;
    jValue = value;
    return this;
  }

  public JSON set(String val) {
    if (val.indexOf(',') != -1){ 
    }
    else { String value;
      value = val;
    }
    render();
    return this;
}
  
  @Override
  public String getString(String name) {
    // TODO: implement this
      if (jName != null && jName.equals(name)){
        return value;
      }
      else if (jValueList != null && !jValueList.isEmpty()){
          int name_index = jNameList.indexOf(name);
          if (jNameList.contains(name)){
              String output = (String) jValueList.get(name_index);
              return output;
          }
      }
      return null;
}

  @Override
  public JSON setString(String name, String value) {
    // TODO: implement this
    if (jName == name){
      jValue = value;
    }
    return this;
}
  }

  @Override
  public void getObjects(Collection<String> names) {
    // TODO: implement this
      Object[] arr = names.toArray();
      if (jNameList.isEmpty()){
          jNameList.clear();
      }
      for (Object x: arr){
          jNameList.add((String)x);
      }
  }

    /*Add objects and values to array list */
    private void render(){
        String[] keyValList = value.split(",");
        String n, v, temp;
        int[] commas = new int[4];
        for (String x: keyValList){
            int i = 0;
            int commaIndex = 0;
            while (i < x.length()){
                if (x.charAt(i) == '\"'){
                    commas[commaIndex] = i;
                    commaIndex++;
                }
                i++;
            }
            n = x.substring(commas[0] + 1, commas[1]);
            v = x.substring(commas[2] + 1, commas[3]);
            jNameList.add(n);
            jValueList.add(v);
        }
    }
