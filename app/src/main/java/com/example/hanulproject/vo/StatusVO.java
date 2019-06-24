package com.example.hanulproject.vo;

import java.io.Serializable;

public class StatusVO implements Serializable {
    String id, light, secure, weather, window, boiler, gas, door, autoWindow;
    int water, temper, dust;

    public StatusVO () {}
    public StatusVO(String id, String light, String secure, String weather, int water, int temper, int dust, String window, String boiler, String gas, String door, String autoWindow) {
        this.id = id;
        this.light = light;
        this.secure = secure;
        this.weather = weather;
        this.water = water;
        this.temper = temper;
        this.dust = dust;
        this.window = window;
        this.boiler = boiler;
        this.gas = gas;
        this.door = door;
        this.autoWindow = autoWindow;
    }

    public String getAutoWindow() {
        return autoWindow;
    }
    public void setAutoWindow(String autoWindow) {

        this.autoWindow = autoWindow;
    }
    public String getDoor() {
        return door;
    }

    public void setDoor(String door) {
        this.door = door;
    }

    public String getGas() {
        return gas;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public String getBoiler() {
        return boiler;
    }

    public void setBoiler(String boiler) {
        this.boiler = boiler;
    }

    public String getWindow() {
        return window;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public String getSecure() {
        return secure;
    }

    public void setSecure(String secure) {
        this.secure = secure;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getTemper() {
        return temper;
    }

    public void setTemper(int temper) {
        this.temper = temper;
    }

    public int getDust() {
        return dust;
    }

    public void setDust(int dust) {
        this.dust = dust;
    }



}
