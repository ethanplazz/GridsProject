package edu.byuh.cis.cs300.gridsproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import edu.byuh.cis.cs300.gridsproject.ui.Horse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Horse game;
        super.onCreate(savedInstanceState);
        game = new Horse(this);
        setContentView(game);

    }
}