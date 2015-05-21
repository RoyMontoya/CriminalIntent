package com.example.amado.criminalintent;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by Amado on 18/05/2015.
 */
public class SelectorDialogFragment extends DialogFragment {
    private static final String DIALOG_DATE ="date";
    private static final String DIALOG_TIME ="time";
    private static final int REQUEST_TIME =12;
    private static final String TAG ="SelectorDialogFragment";
    private Crime mCrime;
    private Date mDate;
    private int mHour;
    private int mMinute;
    private UUID mCrimeid;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrimeid = CrimeLab.getCurrentCrime();

        mCrime = CrimeLab.get(getActivity()).getCrime(mCrimeid);
        mDate= mCrime.getDate();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_selector, null);

        Button DatePickerButton = (Button)v.findViewById(R.id.date_picker_button);
        DatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(SelectorDialogFragment.this, CrimeFragment.REQUEST_DATE);
                dialog.show(fm, DIALOG_DATE);
            }
        });

        Button TimePickerButton = (Button)v.findViewById(R.id.time_picker_button);
        TimePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.setTargetFragment(SelectorDialogFragment.this,REQUEST_TIME);
                dialog.show(fm,DIALOG_TIME );

            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.dialog_selector_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK);
                    }
                })
                .create();



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) return;

        if(requestCode == CrimeFragment.REQUEST_DATE){
            Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            setDime(date);

        }
        if(requestCode == REQUEST_TIME){
            Date date = (Date)data.getSerializableExtra(TimePickerFragment.TIME_DATE);

            setDime(date);

        }
    }


    public void setDime(Date date){

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int hour = calendar.get(calendar.HOUR_OF_DAY);
        int minute = calendar.get(calendar.MINUTE);
        mDate = new GregorianCalendar(year,month,day,hour,minute).getTime();



    }

    public void sendResult(int requestCode){
        if(getTargetFragment() == null){
            return;
        }

        Intent i = new Intent();
        i.putExtra(DatePickerFragment.EXTRA_DATE, mDate);

        getTargetFragment().onActivityResult(getTargetRequestCode(), requestCode, i);
    }

}
