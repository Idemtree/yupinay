package com.idemtree.yupinay;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileDumpLogger extends Logger {
    public static final String FILENAME_PREFERENCE = "filename";
    public static final String PATH_PREFERENCE = "path";
    public static final String DEFAULT_FILENAME = "log";
    public static final String DEFAULT_PATH = Environment.getExternalStorageDirectory().getPath();
    private static final String PREFERENCES_TITLE = "yupinay";
    private final String FILENAME;
    private final String PARENT_FOLDER_ABS_PATH;
    private SharedPreferences mPreferences;

    private FileDumpLogger(Context context, String filename, String parentFolderPath) {
        FILENAME = filename;
        PARENT_FOLDER_ABS_PATH = parentFolderPath;
        mPreferences = context.getSharedPreferences(PREFERENCES_TITLE, Context.MODE_PRIVATE);
    }

    private FileDumpLogger(Context context) {
        FILENAME = DEFAULT_FILENAME;
        PARENT_FOLDER_ABS_PATH = DEFAULT_PATH;
        mPreferences = context.getSharedPreferences(PREFERENCES_TITLE, Context.MODE_PRIVATE);
    }

    public String getFilename() {
        if (FILENAME != null) {
            return FILENAME;
        }
        return mPreferences.getString(FILENAME_PREFERENCE, DEFAULT_FILENAME);
    }

    public String getFolderPath() {
        if (PARENT_FOLDER_ABS_PATH != null)
            return PARENT_FOLDER_ABS_PATH;

        return mPreferences.getString(PATH_PREFERENCE, DEFAULT_PATH);
    }

    @Override
    public <L extends LogMessage> void log(L logMessage) {
        File file = new File(getFolderPath(), getFilename());
        log(logMessage.toString(), file);
    }

    @Override
    public synchronized void log(String logMessage) {
        File file = new File(getFolderPath(), getFilename());
        log(logMessage, file);
    }

    private void log(String message, File file) {
        try {
            file.createNewFile();

            FileOutputStream outStream = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(outStream);
            writeToFile(message, osw, file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String message, OutputStreamWriter outputStreamWriter, File file)
            throws IOException {
        outputStreamWriter.append(message);
        outputStreamWriter.flush();
        outputStreamWriter.close();
    }

    public static class Builder {
        private Context mContext;
        private String mFilename;
        private String mParentFolderPath;

        public Builder(Context context) {
            mContext = context;
        }

        public Builder filename(String filename) {
            mFilename = filename;
            return this;
        }

        public Builder parentFolderPath(String parentFolderPath) {
            mParentFolderPath = parentFolderPath;
            return this;
        }

        public FileDumpLogger build() {
            boolean validFilename = mFilename != null && !mFilename.isEmpty();
            boolean validPath = mParentFolderPath != null && !mParentFolderPath.isEmpty();

            if (validFilename && validPath) {
                return new FileDumpLogger(mContext, mFilename, mParentFolderPath);
            } else {
                return new FileDumpLogger(mContext);
            }
        }

    }
}
