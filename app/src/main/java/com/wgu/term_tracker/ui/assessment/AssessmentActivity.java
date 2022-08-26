package com.wgu.term_tracker.ui.assessment;

import static com.wgu.term_tracker.TermTrackerApplication.ARG_FOREIGN_KEY;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Assessment;
import com.wgu.term_tracker.utilities.AlertsHelper;

import java.util.Objects;

/**
 * The type Assessment activity.
 */
public class AssessmentActivity extends AppCompatActivity {


    /**
     * The constant KEY_SELECTED_ASSESSMENT.
     */
    public static final String KEY_SELECTED_ASSESSMENT = "key_selected_assessment";
    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        assessmentViewModel = new ViewModelProvider(this).get(AssessmentViewModel.class);
        assessmentViewModel.setSelectedCourse(getIntent().getExtras().getLong(ARG_FOREIGN_KEY));

        setContentView(R.layout.activity_assessment);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_assessment, AssessmentListFragment.class, null)
                    .commit();
        }

        if (getIntent().getParcelableExtra(KEY_SELECTED_ASSESSMENT) != null) {
            Assessment assessment = getIntent().getParcelableExtra(KEY_SELECTED_ASSESSMENT);
            assessmentViewModel.setSelectedAssessment(assessment);
            Fragment fragment = AssessmentDetailFragment.newInstance(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_assessment, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getSupportFragmentManager().popBackStack();
                } else {
                    finish();
                }
                return true;
            case R.id.delete:
                if (assessmentViewModel.getSelectedAssessment() == null) {
                    Toast.makeText(this, "Can't delete, item has not been saved yet!", Toast.LENGTH_SHORT).show();
                } else {
                    processDelete();
                }
                return true;
            case R.id.menu_detail_save:
                processSave();
                return true;
            case R.id.list_menu_add:
                AssessmentListFragment assessmentListFragment = (AssessmentListFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_container_assessment);
                if (assessmentListFragment != null) {
                    assessmentListFragment.startAssessmentDetail(true);
                }
                return true;
            case R.id.detail_menu_set_alert:
                AssessmentDetailFragment assessmentDetailFragment = (AssessmentDetailFragment)
                        getSupportFragmentManager().findFragmentById(R.id.fragment_container_assessment);
                if (assessmentDetailFragment != null) {
                    if (assessmentDetailFragment.isNewAssessment) {
                        AlertsHelper.showAssessmentNeedsToBeSavedFirstToast(this);
                    } else {
                        assessmentDetailFragment.showSetAlertsPopup();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void processSave() {
        AssessmentDetailFragment assessmentDetailFragment = (AssessmentDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container_assessment);
        if (assessmentDetailFragment != null) {
            assessmentDetailFragment.processSaveAssessment();
        }
    }

    private void processDelete() {
        AssessmentDetailFragment assessmentDetailFragment = (AssessmentDetailFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container_assessment);
        if (assessmentDetailFragment != null) {
            assessmentDetailFragment.processDelete();
        }
    }
}