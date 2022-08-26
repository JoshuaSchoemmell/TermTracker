package com.wgu.term_tracker.ui.course;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Note;

import java.util.List;

/**
 * The type Note list adapter.
 */
public class NoteListAdapter extends ListAdapter<Note, NoteViewHolder> {

    /**
     * The constant NOTE_ITEM_CALLBACK.
     */
    public static final DiffUtil.ItemCallback<Note> NOTE_ITEM_CALLBACK = new DiffUtil.ItemCallback<Note>() {
        @Override
        public boolean areItemsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Note oldItem, @NonNull Note newItem) {
            return oldItem.getContents().equals(((Note) newItem).getContents());
        }
    };
    private final NoteListener noteListener;
    private int position;

    /**
     * Instantiates a new Note list adapter.
     *
     * @param noteListener the note listener
     */
    public NoteListAdapter(NoteListener noteListener) {
        super(NOTE_ITEM_CALLBACK);
        this.noteListener = noteListener;
        setHasStableIds(true);
    }

    /**
     * Gets position.
     *
     * @return the position
     */
    public int getPosition() {
        return position;
    }

    /**
     * Sets position.
     *
     * @param position the position
     */
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getNoteId();
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_note, parent, false);
        NoteViewHolder viewHolder = new NoteViewHolder(view);

        viewHolder.itemView.setOnClickListener((v) -> {
            int pos = viewHolder.getAdapterPosition();
            noteListener.onClick(pos);
        });
        viewHolder.itemView.setOnLongClickListener((v) -> {
            setPosition(viewHolder.getAdapterPosition());
            return false;
        });
        return viewHolder;
    }

    @Override
    public Note getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = getItem(position);
        holder.bind(note);
    }

    @Override
    public void submitList(@Nullable List<Note> list) {
        super.submitList(list);
    }

    /**
     * The interface Note listener.
     */
    public interface NoteListener extends View.OnCreateContextMenuListener {
        /**
         * On click.
         *
         * @param pos the pos
         */
        void onClick(int pos);
    }
}
