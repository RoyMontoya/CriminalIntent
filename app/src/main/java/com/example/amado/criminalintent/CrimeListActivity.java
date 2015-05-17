package com.example.amado.criminalintent;

import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;



public class CrimeListActivity extends SingleFragmentActivity {
    private static final String TAG= "CrimeListActivity";

    @Override
    protected Fragment createFragment() {
        
        return new CrimeListFragment();

    }

}
