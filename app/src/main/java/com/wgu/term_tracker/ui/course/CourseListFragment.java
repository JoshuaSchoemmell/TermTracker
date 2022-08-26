package com.wgu.term_tracker.ui.course;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Course;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseListFragment extends Fragment implements CourseListAdapter.CourseListAdapterListener {

    /**
     * The Tab controller.
     */
    CourseDetailFragment.TabController tabController;
    private CourseViewModel courseViewModel;

    /**
     * New instance course list fragment.
     *
     * @return the course list fragment
     */
    public static CourseListFragment newInstance() {
        return new CourseListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        setHasOptionsMenu(true);
        tabController = (CourseDetailFragment.TabController) getActivity();
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_course_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        CourseListAdapter adapter = new CourseListAdapter(this);
        courseViewModel.getCoursesForTerm().observe(getViewLifecycleOwner(),
                list -> {
                    adapter.submitList(list);
                    if (adapter.getItemCount() == 0) {
                        Toast.makeText(getContext(), "There are no Courses. \nAdd a Course with the \"+\" button!", Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onResume() {
        requireActivity().setTitle("Courses");
        tabController.hideTabLayout();
        courseViewModel.setSelectedCourse(null);
        super.onResume();
    }

    /**
     * Start course detail.
     *
     * @param isNewCourse the is new course
     */
    void startCourseDetail(boolean isNewCourse) {
        Fragment fragment = CourseDetailFragment.newInstance(isNewCourse);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_course, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClick(Course course) {
        courseViewModel.setSelectedCourse(course);
        startCourseDetail(false);
    }
}