package com.example.accelerometer1;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.util.Log;

import java.io.File;
import java.util.logging.FileHandler;

/*
public class FileStuff {
    private static final int CREATE_FILE = 1;

    private  void createFile(Uri pickerInitialUri) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/txt");
        intent.putExtra(Intent.EXTRA_TITLE, "Log.txt");

        intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);

        startActivityForResult(intent, CREATE_FILE);

    }
}

/*
public class FileStuff {

    public static FileHandler logger = null;
    private static String filename = "Accelerometer_Log";

    static boolean isExternalStorageAvailable = false;
    static boolean isExternerStorageWriteable = false;
    static String state = Environment.getExternalStorageState();

    public static void addRecordToLog(String Message) {
        File dir = new File("/sdcard/Files/Accelerometer");
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            if(!dir.exists()) {
                Log.d("Dir created ", "Dir created ");
                dir.mkdirs();
            }

            File logFile = new File("/sdcard/Files/Accelerometer/"+filename+".txt");

            if (!logFile.exists()) {
                try {
                    Log.d("File created ", "File created " );

                }
            }
        }
    }

}
*/