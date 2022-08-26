package com.wgu.term_tracker.ui.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.wgu.term_tracker.BootReceiver;
import com.wgu.term_tracker.R;
import com.wgu.term_tracker.ui.term.TermActivity;

/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * The Progress bar.
     */
    ProgressBar progressBar;
    /**
     * The Main layout.
     */
    View mainLayout;

    /**
     * The onCreate Method.
     * Sets up the Landing Page.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Enable the boot receiver
        ComponentName bootReceiver = new ComponentName(getApplicationContext(), BootReceiver.class);
        getPackageManager().setComponentEnabledSetting(bootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        mainLayout = findViewById(R.id.main_layout);
        progressBar.setVisibility(View.INVISIBLE);
        findViewById(R.id.enterButton).setOnClickListener(view -> navigateToTerm());
    }

    private void navigateToTerm() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, TermActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        progressBar.setVisibility(View.INVISIBLE);
        super.onStop();
    }
}
