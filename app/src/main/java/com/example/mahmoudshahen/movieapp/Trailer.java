package com.example.mahmoudshahen.movieapp;

/**
 * Created by mahmoud shahen on 05/04/2017.
 */

public class Trailer {
    String name;
    String key;

 public Trailer(String n, String k) {
     name = n;
     key = k;
     key = key.substring(1, key.length()-1);
     name = name.substring(1, name.length()-1);
 }

}
