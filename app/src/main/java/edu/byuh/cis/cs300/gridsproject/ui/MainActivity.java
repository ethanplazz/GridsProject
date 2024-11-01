package edu.byuh.cis.cs300.gridsproject.ui;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Horse game = new Horse(this);
        setContentView(game);
    }

}