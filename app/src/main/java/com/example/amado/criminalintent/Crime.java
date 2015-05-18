package com.example.amado.criminalintent;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by Amado on 13/05/2015.
 */
public class Crime {

    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    public static final String TAG = "Crime";


    public Crime(){
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        String format = "yyyy-MM-dd HH:mm";
        SimpleDateFormat sdf= new SimpleDateFormat(format);
        String d1 = sdf.format(mDate);
        Date date=mDate;
        //parsing is not working
        try{

            date = sdf.parse(d1);
        }catch (ParseException e){
          e.printStackTrace();
        }
      return date;
    }

    @Override
    public String toString() {
        return mTitle;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }
}
