package com.example.amado.criminalintent;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Amado on 14/05/2015.
 */
public class CrimeLab {
    private static final String TAG = "CrimeLab";
    private static final String FILENAME = "crimes.json";


    private ArrayList<Crime> mCrimes;
    public static UUID sCurrentCrime;
    private CriminalIntentJSONSerializer mSerializer;

    private static CrimeLab sCrimeLab;
    private Context mAppContext;

    public static UUID getCurrentCrime() {
        return sCurrentCrime;
    }

    public static void setCurrentCrime(UUID currentCrime) {
        sCurrentCrime = currentCrime;
    }

    private CrimeLab (Context appContext){
        mAppContext = appContext;
        mSerializer = new CriminalIntentJSONSerializer(mAppContext, FILENAME);
        try {
            mCrimes = mSerializer.loadCrimes();
        }catch (Exception e){
            mCrimes = new ArrayList<Crime>();
            Log.e(TAG, "Error loading crimes: ", e);
        }

    }

    public boolean saveCrimes(){
        try {
            mSerializer.saveCrimes(mCrimes);
            Log.d(TAG,  "crimes saved to file");
            return true;
        }catch (Exception e ){
            Log.e(TAG, "Error saving crimes: ", e);
            return false;
        }
    }

    public void addCrime(Crime c){
        mCrimes.add(c);
    }

    public static CrimeLab get (Context context){
        if(sCrimeLab == null){
            sCrimeLab = new CrimeLab(context.getApplicationContext());
        }
        return sCrimeLab;
    }

    public ArrayList<Crime> getCrimes() {
        return mCrimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime c : mCrimes) {
            if (c.getId().equals(id))
                return c;
            }
            return null;
        }



}
