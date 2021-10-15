package com.example.gesturerecogv2;
import android.app.Application;

public class Global {
    private static Global instance;
    private static int[] counter = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

    private Global(){};

    public void setCounter(int n){
        counter[n]++;
    }

    public int getCounter(int n){
        return counter[n];
    }

    public static synchronized Global getInstance(){
        if(instance == null){
            instance = new Global();
        }
        return instance;
    }
}
