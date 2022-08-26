package com.wgu.term_tracker.ui.course;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Instructor;

/**
 * The type Instructor view holder.
 */
public class InstructorViewHolder extends RecyclerView.ViewHolder {
    private final TextView name;
    private final TextView phone;
    private final TextView email;

    /**
     * Instantiates a new Instructor view holder.
     *
     * @param itemView the item view
     */
    public InstructorViewHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.instructor_name);
        phone = itemView.findViewById(R.id.instructor_phone);
        email = itemView.findViewById(R.id.instructor_email);
    }

    /**
     * Bind.
     *
     * @param instructor the instructor
     */
    public void bind(Instructor instructor) {
        name.setText(instructor.getName());
        phone.setText(instructor.getPhoneNumber());
        email.setText(instructor.getEmail());
    }
}
