package com.idemtree.yupinay;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.Date;
import java.util.LinkedHashMap;

public abstract class LogMessage extends LinkedHashMap<String, String> {

    public LogMessage() {
        put("logDate", new Date().toString());
    }

    @Override
    public String toString() {
        return new JSONObject(this).toString();
    }
}
