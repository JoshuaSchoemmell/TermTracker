package com.wgu.term_tracker.ui.course;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.wgu.term_tracker.TermTrackerApplication.ARG_FOREIGN_KEY;
import static com.wgu.term_tracker.TermTrackerApplication.ARG_PRIMARY_KEY;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Note;
import com.wgu.term_tracker.utilities.AlertsHelper;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailPagerNoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailPagerNoteFragment extends Fragment implements NoteListAdapter.NoteListener {

    /**
     * The Adapter.
     */
    NoteListAdapter adapter;
    private CourseViewModel viewModel;
    private Drawable trash;
    private Drawable share;

    /**
     * New instance course detail pager note fragment.
     *
     * @return the course detail pager note fragment
     */
    public static CourseDetailPagerNoteFragment newInstance() {
        CourseDetailPagerNoteFragment fragment = new CourseDetailPagerNoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        trash = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_delete_bgcolor_24);
        share = ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_ios_share_24);

    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getNotesForCourse().observe(getViewLifecycleOwner(), notes -> adapter.submitList(notes));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_detail_pager_note, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.notes_recycler);
        adapter = new NoteListAdapter(this);
        viewModel.getNotesForCourse().observe(getViewLifecycleOwner(), notes -> adapter.submitList(notes));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        view.findViewById(R.id.add_note).setOnClickListener(v -> showNotePopup(v, inflater, null));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.d(TAG, "onSwiped: " + direction);
                if (direction == ItemTouchHelper.LEFT) {
                    confirmDelete(viewHolder.getItemId(), viewHolder.getAdapterPosition());
                } else if (direction == ItemTouchHelper.RIGHT) {
                    int pos = viewHolder.getAdapterPosition();
                    shareNote(pos);
                    adapter.notifyItemChanged(pos);
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

                // Swiping Left
                if (dX < 0) {
                    ColorDrawable bg = new ColorDrawable();
                    bg.setColor(Color.RED);
                    bg.setBounds(itemView.getRight() + ((int) dX),
                            itemView.getTop(),
                            itemView.getRight(),
                            itemView.getBottom());
                    bg.draw(c);

                    int margin = (itemView.getHeight() - trash.getIntrinsicHeight()) / 2;
                    int left = itemView.getRight() - margin - trash.getIntrinsicWidth();
                    int top = itemView.getTop() + (itemView.getHeight() - trash.getIntrinsicHeight()) / 2;
                    int right = itemView.getRight() - margin;
                    int bottom = top + trash.getIntrinsicHeight();

                    trash.setBounds(left, top, right, bottom);
                    trash.draw(c);

                    // Swiping Right
                } else if (dX > 0) {
                    ColorDrawable bg = new ColorDrawable();
                    bg.setColor(Color.BLUE);
                    bg.setBounds((itemView.getLeft()),
                            itemView.getTop(),
                            itemView.getLeft() + ((int) dX),
                            itemView.getBottom());
                    bg.draw(c);

                    int margin = (itemView.getHeight() - share.getIntrinsicHeight()) / 2;
                    int left = itemView.getLeft() + margin;
                    int top = itemView.getTop() + (itemView.getHeight() - share.getIntrinsicHeight()) / 2;
                    int right = itemView.getLeft() + margin + share.getIntrinsicWidth();
                    int bottom = top + share.getIntrinsicHeight();

                    share.setBounds(left, top, right, bottom);
                    share.draw(c);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLongClickable(true);
        registerForContextMenu(recyclerView);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.note_context_delete:
                confirmDelete(adapter.getItemId(adapter.getPosition()), adapter.getPosition());
                return true;
            case R.id.note_context_share:
                shareNote(adapter.getPosition());
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }


    private void showNotePopup(View v, LayoutInflater inflater, Note note) {
        if (viewModel.getSelectedCourse() == null) {
            AlertsHelper.showCourseNeedsToBeSavedFirstToast(getContext());
        } else {
            @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup_note, null);
            final PopupWindow popupWindow = new PopupWindow(popupView,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    true);
            popupWindow.setElevation(24);
            popupWindow.setAnimationStyle(androidx.transition.R.style.Animation_AppCompat_Dialog);
            popupView.findViewById(R.id.popup_cancel_button).setOnClickListener(v1 -> popupWindow.dismiss());
            TextInputEditText textInputEditText = popupView.findViewById(R.id.note_text_input);
            if (note == null) {
                textInputEditText.setHint("Enter a new note: ");
                popupView.findViewById(R.id.popup_save_button).setOnClickListener(v12 -> {
                    String noteTextContent = Objects.requireNonNull(textInputEditText.getText()).toString();
                    Note newNote = new Note(noteTextContent, viewModel.getSelectedCourseId());
                    viewModel.insertNewNote(newNote);
                    Toast.makeText(v12.getContext(), "Note saved!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                });
            } else {
                textInputEditText.setHint("");
                textInputEditText.setText(note.getContents());
                popupView.findViewById(R.id.popup_save_button).setOnClickListener(v13 -> {
                    note.setContents(Objects.requireNonNull(textInputEditText.getText()).toString());
                    viewModel.insert(note);
                    Toast.makeText(v13.getContext(), "Note saved!", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    popupWindow.dismiss();
                });
            }
            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    }

    private void shareNote(int pos) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String noteMessage =
                "Note for " + viewModel.getSelectedCourse().getTitle() + ":\n'" +
                        adapter.getItem(pos).getContents() + "'";
        sendIntent.putExtra(Intent.EXTRA_TEXT, noteMessage);
        sendIntent.setType("text/plain");
        Intent shareIntent = Intent.createChooser(sendIntent, "Share note");
        startActivity(shareIntent);
    }

    private void confirmDelete(long iD, int adapterPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.dialog_confirm_delete_note))
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> deleteNote(iD))
                .setNegativeButton(R.string.cancel, (dialog, which) -> {
                    dialog.dismiss();
                    adapter.notifyItemChanged(adapterPosition);
                })
                .create()
                .show();
    }

    private void deleteNote(long iD) {
        viewModel.deleteNote(iD);
        Toast.makeText(getContext(), "Deleted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(int pos) {
        showNotePopup(getView(), getLayoutInflater(), adapter.getCurrentList().get(pos));
    }
}