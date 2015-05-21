package com.example.amado.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Amado on 14/05/2015.
 */
public class CrimeLab {
    public static CrimeLab sCrimeLab;
    private final Context mContext;
    private ArrayList<Crime> mCrimes;
    public static UUID sCurrentCrime;

    public static UUID getCurrentCrime() {
        return sCurrentCrime;
    }

    public static void setCurrentCrime(UUID currentCrime) {
        sCurrentCrime = currentCrime;
    }

    private CrimeLab (Context context){
        mContext = context;
        mCrimes = new ArrayList<Crime>();
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
