package com.wgu.term_tracker.ui.assessment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.wgu.term_tracker.models.Assessment;


/**
 * The type Assessment list fragment.
 */
public class AssessmentListFragment extends Fragment implements AssessmentListAdapter.AssessmentListAdapterListener {


    private AssessmentViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assessment_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Assessment List");
        viewModel.setSelectedAssessment(null);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * Start assessment detail fragment.
     *
     * @param isNewAssessment the is new assessment
     */
    public void startAssessmentDetail(boolean isNewAssessment) {
        Fragment fragment = AssessmentDetailFragment.newInstance(isNewAssessment);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_assessment, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.assessment_recycler_view);
        AssessmentListAdapter adapter = new AssessmentListAdapter(this);
        viewModel.getAssessmentsForCourse().observe(getViewLifecycleOwner(),
                list -> {
                    adapter.submitList(list);
                    if (adapter.getItemCount() == 0) {
                        Toast.makeText(getContext(),
                                "There are no Assessments. \nAdd an Assessment with the \"+\" button.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onItemClick(Assessment assessment) {
        viewModel.setSelectedAssessment(assessment);
        startAssessmentDetail(false);
    }
}