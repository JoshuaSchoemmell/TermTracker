package com.wgu.term_tracker.ui.term;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
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
import com.wgu.term_tracker.TermTrackerApplication;
import com.wgu.term_tracker.models.Term;

/**
 * The type Term list fragment.
 */
public class TermListFragment extends Fragment implements TermListAdapter.TermListAdapterListener {

    /**
     * The Adapter.
     */
    TermListAdapter adapter;
    private TermViewModel mViewModel;

    /**
     * New instance term list fragment.
     *
     * @return the term list fragment
     */
    public static TermListFragment newInstance() {
        return new TermListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(requireActivity()).get(TermViewModel.class);
        setHasOptionsMenu(true);
        requireActivity().setTitle("Terms");
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.list_menu_add) {
            startTermDetail(true);
        }
        if (item.getItemId() == android.R.id.home) {
            if (getParentFragmentManager().getBackStackEntryCount() == 0) {
                requireActivity().finish();
            }
            getParentFragmentManager().popBackStack();
        }
        return false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_term_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        adapter = new TermListAdapter(this);
        mViewModel.getAll().observe(getViewLifecycleOwner(),
                list -> {
                    adapter.submitList(list);
                    if (adapter.getCurrentList().isEmpty()) {
                        Handler uiHandler = new Handler(Looper.getMainLooper());
                        TermTrackerApplication.singleThreadExecutor.execute(() -> {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } finally {
                                if (adapter.getCurrentList().isEmpty()) {
                                    new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getContext(), "There are no Terms. \nAdd a Term with the \"+\" button!", Toast.LENGTH_SHORT).show());
                                }
                            }
                        });

                    }
                });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
    }


    @Override
    public void onResume() {
        mViewModel.setSelectedTerm(null);
        super.onResume();
    }

    private void startTermDetail(boolean isNewTerm) {
        Fragment fragment = TermDetailFragment.newInstance(isNewTerm);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_term, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onItemClicked(Term term) {
        mViewModel.setSelectedTerm(term);
        startTermDetail(false);
    }
}