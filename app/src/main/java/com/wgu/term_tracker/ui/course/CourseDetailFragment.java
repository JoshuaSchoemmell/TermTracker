package com.wgu.term_tracker.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.wgu.term_tracker.R;

/**
 * The type Course detail fragment.
 */
public class CourseDetailFragment extends Fragment {

    /**
     * The constant ARG_IS_NEW_COURSE.
     */
    public static final String ARG_IS_NEW_COURSE = "arg_is_new_course";
    /**
     * The M primary key.
     */
    Long mPrimaryKey = -1L;
    /**
     * The Course view model.
     */
    CourseViewModel courseViewModel;
    private boolean isNewCourse;
    private TabController courseTabControllerListener;

    /**
     * New instance course detail fragment.
     *
     * @param isNewCourse the is new course
     * @return the course detail fragment
     */
    public static CourseDetailFragment newInstance(boolean isNewCourse) {
        CourseDetailFragment fragment = new CourseDetailFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_NEW_COURSE, isNewCourse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseViewModel = new ViewModelProvider(this).get(CourseViewModel.class);
        if (getArguments() != null) {
            isNewCourse = getArguments().getBoolean(ARG_IS_NEW_COURSE);
        }
        courseTabControllerListener = (TabController) getActivity();
        CourseViewModel courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        if (isNewCourse) {
            courseViewModel.setSelectedCourse(null);
        } else {
            mPrimaryKey = courseViewModel.getSelectedCourseId();
        }

        setToolbarTitle();
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        courseTabControllerListener.showTabLayout();
        CoursePageAdapter pageAdapter = new CoursePageAdapter(getParentFragmentManager(), getLifecycle());
        ViewPager2 pager = view.findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);

        TabLayout tabLayout = courseTabControllerListener.getTabLayout();
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, (tab, position) -> tab.setText(getTabTitle(position)));
        tabLayoutMediator.attach();
    }

    private void setToolbarTitle() {
        String title;
        if (isNewCourse) {
            title = "Add New Course";
        } else {
            title = "Course Information";
        }
        requireActivity().setTitle(title);

    }

    private String getTabTitle(int position) {
        switch (position) {
            case 0:
                return "Course Details";
            case 1:
                return "Notes";
            case 2:
                return "Course Instructors";
        }
        return null;
    }

    /**
     * The interface Tab controller.
     */
    public interface TabController {
        /**
         * Hide tab layout.
         */
        void hideTabLayout();

        /**
         * Show tab layout.
         */
        void showTabLayout();

        /**
         * Gets tab layout.
         *
         * @return the tab layout
         */
        TabLayout getTabLayout();
    }

    private class CoursePageAdapter extends FragmentStateAdapter {

        /**
         * The Top Fragment.
         */
        Fragment top, /**
         * The Note Fragment.
         */
        note, /**
         * The Instructor Fragment.
         */
        instructor;

        /**
         * Instantiates a new Course page adapter.
         *
         * @param fragmentManager the fragment manager
         * @param lifecycle       the lifecycle
         */
        public CoursePageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    if (top == null)
                        top = CourseDetailPagerTopFragment.newInstance();
                    return top;
                case 1:
                    if (note == null) {
                        note = CourseDetailPagerNoteFragment.newInstance();
                    }
                    return note;
                case 2:
                    if (instructor == null) {
                        instructor = CourseDetailPagerInstructorFragment.newInstance();
                    }
                    return instructor;
                default:
                    throw new IllegalStateException("Unexpected value: " + position);
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }


}
