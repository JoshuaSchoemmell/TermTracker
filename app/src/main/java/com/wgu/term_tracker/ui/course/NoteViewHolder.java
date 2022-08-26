package com.wgu.term_tracker.ui.course;

import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Note;

/**
 * The type Note view holder.
 */
public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
    private final TextView noteTextInput;

    /**
     * Instantiates a new Note view holder.
     *
     * @param itemView the item view
     */
    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);
        noteTextInput = itemView.findViewById(R.id.note);
        itemView.setOnCreateContextMenuListener(this);
    }

    /**
     * Bind.
     *
     * @param note the note
     */
    public void bind(Note note) {
        noteTextInput.setText(note.getContents());
    }


    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = new MenuInflater(v.getContext());
        inflater.inflate(R.menu.note_context_menu, menu);
    }
}
