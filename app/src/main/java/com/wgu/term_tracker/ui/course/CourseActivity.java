package com.wgu.term_tracker.ui.course;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.wgu.term_tracker.R;
import com.wgu.term_tracker.TermTrackerApplication;
import com.wgu.term_tracker.models.Course;
import com.wgu.term_tracker.utilities.AlertsHelper;

import java.util.Objects;

/**
 * The type Course activity.
 */
public class CourseActivity extends AppCompatActivity implements CourseDetailFragment.TabController {

    /**
     * The constant KEY_SELECTED_COURSE.
     */
    public static final String KEY_SELECTED_COURSE = "key_selected_course";
    private TabLayout courseTabLayout;
    private CourseViewModel courseViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        courseTabLayout = findViewById(R.id.course_tab_layout);

        Bundle extras = getIntent().getExtras();
        courseViewModel.setSelectedTerm(extras.getLong(TermTrackerApplication.ARG_FOREIGN_KEY));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container_course, CourseListFragment.class, null)
                    .commit();
            hideTabLayout();
        }

        if (getIntent().getParcelableExtra(KEY_SELECTED_COURSE) != null) {
            Course course = getIntent().getParcelableExtra(KEY_SELECTED_COURSE);
            courseViewModel.setSelectedCourse(course);
            Fragment fragment = CourseDetailFragment.newInstance(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container_course, fragment)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }


    @Override
    public void hideTabLayout() {
        courseTabLayout.setVisibility(View.GONE);
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
                if (courseViewModel.getSelectedCourse() == null) {
                    Toast.makeText(this,
                            "Can't delete, item has not been saved yet!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    processDelete();
                }
                return true;
            case R.id.menu_detail_save:
                processSave();
                return true;
            case R.id.list_menu_add:
                CourseListFragment courseListFragment = (CourseListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_course);
                if (courseListFragment != null) {
                    courseListFragment.startCourseDetail(true);
                }
                return true;
            case R.id.detail_menu_set_alert:
                ViewPager2 pager2 = findViewById(R.id.pager);
                pager2.setCurrentItem(0);
                CourseDetailPagerTopFragment courseDetailPagerTopFragment =
                        (CourseDetailPagerTopFragment) getSupportFragmentManager().findFragmentByTag("f" + pager2.getCurrentItem());
                if (courseDetailPagerTopFragment != null) {
                    if (courseDetailPagerTopFragment.isNewCourse) {
                        AlertsHelper.showCourseNeedsToBeSavedFirstToast(this);
                    } else {
                        courseDetailPagerTopFragment.showSetAlertsPopup();
                    }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Process delete.
     */
    void processDelete() {
        confirmDelete();
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to delete this Course?")
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> deleteCourse())
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void showItemDeletedToast() {
        Toast.makeText(this, "Course has been deleted!", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void processSave() {
        ViewPager2 pager2 = findViewById(R.id.pager);
        pager2.setCurrentItem(0);
        CourseDetailPagerTopFragment courseDetailPagerTopFragment =
                (CourseDetailPagerTopFragment) getSupportFragmentManager().findFragmentByTag("f" + pager2.getCurrentItem());
        if (courseDetailPagerTopFragment != null) {
            courseDetailPagerTopFragment.processSaveCourse();
        }
    }


    private void deleteCourse() {
        courseViewModel.deleteCourse();
        showItemDeletedToast();
        courseViewModel.setSelectedCourse(null);
        getSupportFragmentManager().popBackStack();
    }


    @Override
    public void showTabLayout() {
        courseTabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public TabLayout getTabLayout() {
        return courseTabLayout;
    }

}