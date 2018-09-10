package com.idemtree.yupinay;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Yupinay {
    private final boolean IS_DEBUG_BUILD;
    private final String TAG;
    private static Yupinay instance;
    private static final String DEFAULT_TAG = "YupinayDefaultTag";
    private List<Logger> mLoggers;

    private Yupinay(String tag) {
        IS_DEBUG_BUILD = BuildConfig.DEBUG;
        TAG = (tag == null || tag.isEmpty())? DEFAULT_TAG : tag;
        mLoggers = new ArrayList<>();
    }

    private Yupinay(boolean isDebugBuild, String tag) {
        IS_DEBUG_BUILD = isDebugBuild;
        TAG = tag;
        mLoggers = new ArrayList<>();
    }

    public static Yupinay initializeWithDefaults(String tag) {
        if (instance == null)
            instance = new Yupinay(tag);
        return instance;
    }

    public static Yupinay initialize(boolean isDebugBuild, String tag) {
        if (instance != null)
            return instance;
        if (isDebugBuild)
            instance = new Yupinay(tag);
        else
            instance = new Yupinay(false, tag);
        return instance;
    }

    public static void log(LogMessage message) {
        log(message.toString());
    }

    public static void log(String message) {
        if (instance.IS_DEBUG_BUILD) {
            Log.d(instance.TAG, message);
        }

        Logger[] logger = instance.mLoggers.toArray(new Logger[instance.mLoggers.size()]);

        new RunInBackground(message).execute(logger);
    }

    public static <L extends Logger> Yupinay addLogger(L logger) {
        instance.mLoggers.add(logger);
        return instance;
    }

    private static class RunInBackground extends AsyncTask<Logger, Void, Void> {
        private String mMessage;
        private RunInBackground(String message) {
            mMessage = message;
        }
        @Override
        protected Void doInBackground(Logger... loggers) {
            for (Logger logger : loggers) {
                if (logger == null)
                    continue;
                logger.log(mMessage);
            }
            return null;
        }
    }
}
