package edu.byuh.cis.cs300.gridsproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.byuh.cis.cs300.gridsproject.ui.Horse;

public class MainActivity extends AppCompatActivity {

    private Horse yes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        yes = new Horse(this);
        setContentView(yes);

    }
}