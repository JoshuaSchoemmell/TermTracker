package com.wgu.term_tracker.ui.term;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Term;

import java.util.Objects;

/**
 * The type Term activity.
 */
public class TermActivity extends AppCompatActivity {

    /**
     * The constant KEY_SELECTED_TERM.
     */
    public static final String KEY_SELECTED_TERM = "key_selected_term";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TermViewModel viewModel = new ViewModelProvider(this).get(TermViewModel.class);
        setContentView(R.layout.activity_term);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_term, TermListFragment.class, null)
                    .commit();
        }

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                Term term = getIntent().getParcelableExtra(KEY_SELECTED_TERM);
                viewModel.setSelectedTerm(term);
                TermDetailFragment fragment = TermDetailFragment.newInstance(false);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container_term, fragment)
                        .setReorderingAllowed(true)
                        .addToBackStack(null)
                        .commit();
            }
        }
        Log.d("BACK_STACK_COUNT: ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
    }


}

