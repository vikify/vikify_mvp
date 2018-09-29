package com.bannuranurag.android.vikify.DataSaving;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavingImages {
    private static final String TAG = "SavingImages";
    final static int BUFFERSIZE = 4 * 1024;

    public static String savingvids(File mFile, File nFile){


        try {
           String pathis= transfertonewfile(mFile,nFile);
           Log.v(TAG,"pathiss"+pathis);
           return pathis;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

        }

    public static String transfertonewfile(File originalFile, File newCopyToFile) throws IOException {

        FileInputStream fin = null;
        FileOutputStream fout = null;
        Log.v(TAG,"File size before is "+newCopyToFile.length());
        try {
            fin = new FileInputStream(originalFile);
            fout = new FileOutputStream(newCopyToFile);
            byte[] buffer = new byte[BUFFERSIZE];
            int bytesRead;
            while (fin.available() != 0) {
                bytesRead = fin.read(buffer);
                fout.write(buffer, 0, bytesRead);
                Log.v(TAG,"Bytesread"+bytesRead);
            }
            fin.close();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
            return newCopyToFile.getAbsolutePath();
//            PopulateDbAsync.getFilePath(newCopyToFile.getAbsolutePath());
       // Log.v(TAG,"PopulateDbAsync Called");
        }



    }



