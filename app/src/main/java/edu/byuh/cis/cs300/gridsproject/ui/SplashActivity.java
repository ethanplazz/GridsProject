package edu.byuh.cis.cs300.gridsproject.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;

import edu.byuh.cis.cs300.gridsproject.R;

public class SplashActivity extends AppCompatActivity {

    private ImageView iv;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.splashscreen);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_UP) {
            float x = m.getX();
            float y = m.getY();
            float w = iv.getWidth();
            float h = iv.getHeight();
            if (y > h / 15 * 6 && y < h / 15 * 8) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("GAME_MODE", "ONE_PLAYER");
                startActivity(intent);
                finish();
            }
            else if (y > h / 15 * 9 && y < h / 15 * 11) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("GAME_MODE", "TWO_PLAYER");
                startActivity(intent);
                finish();
            }
            if (x > w / 3 * 2 && y > h / 4 * 3) {
                openInfoDialog();
            }
            if (x < w / 3 && y > h / 4 * 3) {
                showSettings();
            }
        }
        return true;
    }

    private void openInfoDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.about_title))
                .setMessage(getString(R.string.about_message))
                .setPositiveButton(getString(R.string.ok), (dialog, id) -> {
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }

    private void showSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}

