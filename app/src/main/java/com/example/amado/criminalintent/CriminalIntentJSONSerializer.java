package com.example.amado.criminalintent;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by Amado on 21/05/2015.
 */
public class CriminalIntentJSONSerializer {

    private Context mContext;
    private String mFileName;
    private File mExtFile;


    public CriminalIntentJSONSerializer(Context c, String f){
        mContext = c;
        mFileName=f;
    }

    public void saveCrimes(ArrayList<Crime> crimes)throws JSONException, IOException{
        if(mExtFile == null) {
            mExtFile = new File(mContext.getExternalFilesDir(null), mFileName);
        }

        JSONArray array = new JSONArray();
        for(Crime c : crimes) array.put(c.toJSON());
        Writer writer = null;
        if(hasExternalStorage()){
            try {
                File extCrime = new File(mExtFile.toString());
                FileOutputStream out = new FileOutputStream(extCrime);
                writer = new OutputStreamWriter(out);
                writer.write(array.toString());
            }finally {
                if(writer!= null)
                    writer.close();
            }


        }else {


            try {
                OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
                writer = new OutputStreamWriter(out);
                writer.write(array.toString());
            } finally {
                if (writer != null)
                    writer.close();
            }
        }

    }

    public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
        ArrayList<Crime> crimes = new ArrayList<Crime>();
        BufferedReader reader = null;
        if(mExtFile == null) {
            mExtFile = new File(mContext.getExternalFilesDir(null), mFileName);
        }
        if (hasExternalStorage()) {
            try {
                FileInputStream extFileInputStream = new FileInputStream(mExtFile);
                reader = new BufferedReader(new InputStreamReader(extFileInputStream));

                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                for (int i = 0; i < array.length(); i++) {
                    crimes.add(new Crime(array.getJSONObject(i)));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (reader != null)
                    reader.close();
            }

            return crimes;

        } else {

            try {

                InputStream in = mContext.openFileInput(mFileName);
                reader = new BufferedReader(new InputStreamReader(in));


                StringBuilder jsonString = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    jsonString.append(line);
                }
                JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
                for (int i = 0; i < array.length(); i++) {
                    crimes.add(new Crime(array.getJSONObject(i)));
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (reader != null)
                    reader.close();
            }
            return crimes;
        }
    }

    public boolean hasExternalStorage(){
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)){
            return true;
        }
        return false;
    }
}
