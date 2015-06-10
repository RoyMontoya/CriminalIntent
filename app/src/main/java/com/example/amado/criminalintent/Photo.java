package com.example.amado.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Amado on 09/06/2015.
 */
public class Photo {
    private static final String JSON_FILENAME = "filename";
    private String mFilename;

    public Photo(String filename){
        mFilename = filename;
    }

    public Photo(JSONObject json) throws JSONException{
        mFilename = json.getString(JSON_FILENAME);
    }

    public JSONObject toJSON() throws JSONException{
        JSONObject json = new JSONObject();
        json.put(JSON_FILENAME, mFilename);
        return json;
    }

    public String getFilename() {
        return mFilename;
    }
}
