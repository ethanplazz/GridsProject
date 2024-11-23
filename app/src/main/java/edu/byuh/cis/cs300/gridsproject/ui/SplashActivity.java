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
            if (y > h / 4 && y < h / 2) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("GAME_MODE", "ONE_PLAYER");
                startActivity(intent);
                finish();
            }
            else if (y > h / 2 && y < 3 * h / 4) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("GAME_MODE", "TWO_PLAYER");
                startActivity(intent);
                finish();
            }
            if (x > w / 2 && y > 3 * h / 4) {
                openInfoDialog();
            }
        }
        return true;
    }

    private void openInfoDialog() {
        new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("Hello beautiful person! Thank you for playing five fish. This is my biggest programming project i've ever done. I hope you like it!")
                .setPositiveButton("OK", (dialog, id) -> {
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }
}

