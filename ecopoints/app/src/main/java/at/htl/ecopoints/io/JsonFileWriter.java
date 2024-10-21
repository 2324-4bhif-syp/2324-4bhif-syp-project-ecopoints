package at.htl.ecopoints.io;

import android.content.Context;
import android.util.Log;

import dagger.hilt.android.qualifiers.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.io.BufferedReader;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

@Singleton
public class JsonFileWriter {

    private final String TAG = this.getClass().getSimpleName();

    private static final String FILE_NAME = "data.json";
    private static final String LOG_FILE_NAME = "ecopoints-log.txt";
    private final File file;
    private final File logFile;

    @Inject
    public JsonFileWriter(@ApplicationContext Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            logFile = new File(externalFilesDir, LOG_FILE_NAME);
            file = new File(externalFilesDir, FILE_NAME);

            if (!logFile.exists()) {
                try {
                    boolean created = logFile.createNewFile();
                    if (!created) {
                        throw new RuntimeException("The log file could not be created.");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error creating the log file", e);
                    throw new RuntimeException("Error creating the log file", e);
                }
            }

            if (!file.exists()) {
                try {
                    boolean created = file.createNewFile();
                    if (!created) {
                        throw new RuntimeException("The file could not be created.");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error creating the file", e);
                    throw new RuntimeException("Error creating the file", e);
                }
            }
        } else {
            throw new RuntimeException("External storage directory is not available.");
        }
    }

    public synchronized void startJsonFile() {
        appendJson("[");
    }

    public synchronized void endJsonFile() {
        appendJson("{}]");
    }

    public synchronized void log(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.append(message + "\n");
        } catch (IOException e) {
            Log.d(TAG, "Error appending to the file", e);
            throw new RuntimeException("Error appending to the file", e);
        }
    }

    public synchronized void clearLog() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, false))) {
            writer.write("");
        } catch (IOException e) {
            Log.e(TAG, "Error clearing the log file", e);
            throw new RuntimeException("Error clearing the log file", e);
        }
    }

    public synchronized void writeJson(String json) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write(json);
        } catch (IOException e) {
            Log.e(TAG, "Error writing to the file", e);
            throw new RuntimeException("Error writing to the file", e);
        }
    }

    public synchronized String readJson() {
        try {
            return Files.readAllLines(file.toPath()).toString();
        } catch (IOException e) {
            Log.e(TAG, "Error reading file", e);
            throw new RuntimeException("Error reading file", e);
        }
    }


    public synchronized void appendJson(String json) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(json);
        } catch (IOException e) {
            Log.d(TAG, "Error appending to the file", e);
            throw new RuntimeException("Error appending to the file", e);
        }
    }


    public synchronized void clearFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
            writer.write("");
        } catch (IOException e) {
            Log.e(TAG, "Error clearing the file", e);
            throw new RuntimeException("Error clearing the file", e);
        }
    }

    public String getLogFilePath(){
        return logFile.getAbsolutePath();
    }

    public String getFilePath() {
        return file.getAbsolutePath();
    }
}
