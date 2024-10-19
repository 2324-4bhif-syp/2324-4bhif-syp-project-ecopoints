package at.htl.ecopoints.io;

import android.content.Context;
import dagger.hilt.android.qualifiers.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

@Singleton
public class JsonFileWriter {

    private final String fileName = "data.json";
    private File file;

    @Inject
    public JsonFileWriter(@ApplicationContext Context context) {
        File externalFilesDir = context.getExternalFilesDir(null);
        if (externalFilesDir != null) {
            file = new File(externalFilesDir, fileName);
            if (!file.exists()) {
                try {
                    boolean created = file.createNewFile();
                    if (!created) {
                        throw new RuntimeException("Die Datei konnte nicht erstellt werden.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Fehler beim Erstellen der Datei", e);
                }
            }
        } else {
            throw new RuntimeException("Externes Speicherverzeichnis ist nicht verfügbar.");
        }
    }

    public synchronized void writeJson(String json) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(json);
        } catch (IOException e) {
            // Angemessene Fehlerbehandlung hier
            e.printStackTrace();
        }
    }

    public synchronized void appendJson(String json) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.append(json);
        } catch (IOException e) {
            // Angemessene Fehlerbehandlung hier
            e.printStackTrace();
        }
    }

    public String getFilePath() {
        return file.getAbsolutePath();
    }
}
