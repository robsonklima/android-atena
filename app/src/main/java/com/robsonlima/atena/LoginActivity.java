package com.robsonlima.atena;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        hideActionBar();
    }

    public void login(View view) {
        progress = new ProgressDialog(this);
        progress.setTitle("Authenticating");
        progress.setMessage("Wait while authenticating...");
        progress.setCancelable(false);
        progress.show();

        postDelayed();
    }

    private void postDelayed() {
        new android.os.Handler().postDelayed(
            new Runnable() {
                public void run() {
                    progress.dismiss();
                    Intent intent = new Intent(LoginActivity.this, ProjectsActivity.class);
                    startActivity(intent);
                }
            }, 1500);
    }

    private void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }
}
