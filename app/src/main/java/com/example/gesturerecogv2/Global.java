package com.example.gesturerecogv2;
import android.app.Application;

import java.util.HashMap;
import java.util.Map;

public class Global {
    private static Global instance;
    private static int[] counter = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    private static Map<String,Integer> gestureMap = new HashMap<String,Integer>();
    private static int curPos = 0;
    private static Map<String,String> gestureNameMapper = new HashMap<String,String>();

    public static void setGestureNameMapper(){
        gestureNameMapper.put("Turn on lights","LightOn");
        gestureNameMapper.put("Turn off lights","LightOff");
        gestureNameMapper.put("Turn on fan","FanOn");
        gestureNameMapper.put("Turn off fan","FanOff");
        gestureNameMapper.put("Increase Fan Speed","FanUp");
        gestureNameMapper.put("decrease fan speed","FanDown");
        gestureNameMapper.put("Set Thermostat to specified temperature","SetThermo");
        gestureNameMapper.put("0","Num0");
        gestureNameMapper.put("1","Num1");
        gestureNameMapper.put("2","Num2");
        gestureNameMapper.put("3","Num3");
        gestureNameMapper.put("4","Num4");
        gestureNameMapper.put("5","Num5");
        gestureNameMapper.put("6","Num6");
        gestureNameMapper.put("7","Num7");
        gestureNameMapper.put("8","Num8");
        gestureNameMapper.put("9","Num9");
    }

    public String getGestureNameMapper(String Key){
        return gestureNameMapper.get(Key);
    }
    public void setCurPos(int n){
        curPos = n;
    }

    public int getCurPos(){
        return curPos;
    }

    public int getGestureNumber(String key){
        return gestureMap.get(key);
    }


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
        gestureMap.put("Turn on lights", 1);
        gestureMap.put("Turn off lights",2 );
        gestureMap.put("Turn on fan", 3);
        gestureMap.put("Turn off fan", 4);
        gestureMap.put("Increase Fan Speed", 5);
        gestureMap.put("decrease fan speed", 6);
        gestureMap.put("Set Thermostat to specified temperature", 7);
        gestureMap.put("0", 8);
        gestureMap.put("1", 9);
        gestureMap.put("2", 10);
        gestureMap.put("3", 11);
        gestureMap.put("4", 12);
        gestureMap.put("5", 13);
        gestureMap.put("6", 14);
        gestureMap.put("7", 15);
        gestureMap.put("8", 16);
        gestureMap.put("9", 17);
        setGestureNameMapper();

        return instance;
    }
}
