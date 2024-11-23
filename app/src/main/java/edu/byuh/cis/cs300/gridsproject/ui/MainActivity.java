package edu.byuh.cis.cs300.gridsproject.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import edu.byuh.cis.cs300.gridsproject.logic.GameMode;

public class MainActivity extends AppCompatActivity {

    public Horse game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String gameMode = getIntent().getStringExtra("GAME_MODE");
        game = new Horse(this);
        if ("ONE_PLAYER".equals(gameMode)) {
            game.setGameMode(GameMode.ONE_PLAYER);
        } else if ("TWO_PLAYER".equals(gameMode)) {
            game.setGameMode(GameMode.TWO_PLAYER);
        } else {
            game.setGameMode(GameMode.ONE_PLAYER);
        }
        setContentView(game);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (game != null) {
            game.pauseMusic();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (game != null) {
            game.resumeMusic();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (game != null) {
            game.unloadMusic();
        }
    }
}
