package com.wgu.term_tracker.ui.course;

import static com.wgu.term_tracker.TermTrackerApplication.ARG_PRIMARY_KEY;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Instructor;
import com.wgu.term_tracker.utilities.AlertsHelper;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailPagerInstructorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailPagerInstructorFragment extends Fragment implements InstructorListAdapter.InstructorListener {

    /**
     * The Adapter.
     */
    InstructorListAdapter adapter;
    /**
     * The Trash.
     */
    Drawable trash;
    private CourseViewModel viewModel;

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getInstructorsForCourse().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));
    }

    /**
     * New instance course detail pager instructor fragment.
     *
     * @return the course detail pager instructor fragment
     */
    public static CourseDetailPagerInstructorFragment newInstance() {
        CourseDetailPagerInstructorFragment fragment = new CourseDetailPagerInstructorFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        trash = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_bgcolor_24);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_detail_pager_instructor, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.instructor_recycler);
        adapter = new InstructorListAdapter(this);
        viewModel.getInstructorsForCourse().observe(getViewLifecycleOwner(), list -> adapter.submitList(list));

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        view.findViewById(R.id.add_instructor).setOnClickListener(v -> popupInstructor(v, inflater, null));


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(ItemTouchHelper.LEFT, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT) {
                    confirmDelete(viewHolder.getItemId(), viewHolder.getAdapterPosition());
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c,
                                    @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder,
                                    float dX,
                                    float dY,
                                    int actionState,
                                    boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // Swiping from right to left
                if (dX < 0) {

                    ColorDrawable bg = new ColorDrawable();
                    bg.setColor(Color.RED);
                    bg.setBounds(itemView.getRight() + ((int) dX), itemView.getTop(), itemView.getRight(), itemView.getBottom());
                    bg.draw(c);

                    int top = itemView.getTop() + (itemView.getHeight() - trash.getIntrinsicHeight()) / 2;
                    int margin = (itemView.getHeight() - trash.getIntrinsicHeight()) / 2;
                    int left = itemView.getRight() - margin - trash.getIntrinsicWidth();
                    int right = itemView.getRight() - margin;
                    int bottom = top + trash.getIntrinsicHeight();

                    trash.setBounds(left, top, right, bottom);
                    trash.draw(c);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        return view;
    }


    private void confirmDelete(long iD, int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to delete this instructor?")
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    viewModel.deleteInstructor(iD);
                    Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                    adapter.notifyItemChanged(adapterPosition);
                })
                .create()
                .show();
    }


    private void popupInstructor(View v, LayoutInflater inflater, Instructor instructor) {
        if (viewModel.getSelectedCourse() == null) {
            AlertsHelper.showCourseNeedsToBeSavedFirstToast(getContext());
        } else {
            @SuppressLint("InflateParams")
            View popupView = inflater.inflate(R.layout.popup_instructor, null);
            final PopupWindow popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true);
            popupWindow.setElevation(24);
            popupWindow.setAnimationStyle(androidx.transition.R.style.Animation_AppCompat_Dialog);
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
            popupView.findViewById(R.id.popup_cancel_button).setOnClickListener(v1 -> popupWindow.dismiss());

            if (instructor == null) {
                // Setup for New Instructor
                popupView.findViewById(R.id.popup_save_button).setOnClickListener(v12 -> {
                    // Save the new instructor
                    String instructorName = ((EditText) popupView.findViewById(R.id.name_popup)).getText().toString();
                    String instructorPhone = ((EditText) popupView.findViewById(R.id.phone_popup)).getText().toString();
                    String instructorEmail = ((EditText) popupView.findViewById(R.id.email_popup)).getText().toString();
                    Instructor instructor1 = new Instructor(instructorName, instructorPhone, instructorEmail);
                    viewModel.insertInstructor(instructor1);
                    Toast.makeText(v12.getContext(), instructor1 + " saved!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                });
            } else {
                // Put Instructor Information into the fields.
                ((EditText) popupView.findViewById(R.id.name_popup)).setText(instructor.getName());
                ((EditText) popupView.findViewById(R.id.phone_popup)).setText(instructor.getPhoneNumber());
                ((EditText) popupView.findViewById(R.id.email_popup)).setText(instructor.getEmail());

                // Save the changes
                popupView.findViewById(R.id.popup_save_button).setOnClickListener(v13 -> {
                    String instructorName = ((EditText) popupView.findViewById(R.id.name_popup)).getText().toString();
                    String instructorPhone = ((EditText) popupView.findViewById(R.id.phone_popup)).getText().toString();
                    String instructorEmail = ((EditText) popupView.findViewById(R.id.email_popup)).getText().toString();
                    instructor.setName(instructorName);
                    instructor.setPhoneNumber(instructorPhone);
                    instructor.setEmail(instructorEmail);
                    viewModel.insertInstructor(instructor);
                    Toast.makeText(v13.getContext(), instructor + " saved!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                });
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onItemClick(int pos) {
        Instructor instructor = adapter.getCurrentList().get(pos);
        popupInstructor(getView(), getLayoutInflater(), instructor);
    }

    @Override
    public void onItemLongClick(int pos) {
        confirmDelete(adapter.getCurrentList().get(pos).getInstructorId(), pos);
    }

}