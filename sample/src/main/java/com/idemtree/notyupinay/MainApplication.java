package com.idemtree.notyupinay;

import android.app.Application;
import android.os.Environment;

import com.idemtree.yupinay.FileDumpLogger;
import com.idemtree.yupinay.LogMessage;
import com.idemtree.yupinay.Logger;
import com.idemtree.yupinay.Yupinay;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Logger logger = new FileDumpLogger.Builder(getApplicationContext())
                .filename(getApplicationInfo().packageName)
                .parentFolderPath(Environment.getExternalStorageDirectory().getAbsolutePath())
                .build();
        Yupinay.initializeWithDefaults("WebcontrolLog");
        Yupinay.addLogger(logger);

        for (int i=0; i<100; i++) {
            LogMessage message = new LogMessage() {
                @Override
                public String toString() {
                    return super.toString();
                }
            };
            Yupinay.log(message);
        }
    }
}
