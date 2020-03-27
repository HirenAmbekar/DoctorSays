package com.example.doctorsays;

import android.app.Activity;
import android.widget.ArrayAdapter;

import java.util.List;

public class StringList extends ArrayAdapter<String> {
    private Activity context;
    List<String> userID;

    public StringList(Activity context, List<String> userID) {
        super(context, R.layout.fragment_home);
    }
}
