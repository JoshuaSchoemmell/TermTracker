package com.wgu.term_tracker.models;

import androidx.annotation.NonNull;

/**
 * The enum Course status.
 */
public enum CourseStatus {
    /**
     * The In progress.
     */
    IN_PROGRESS {
        @NonNull
        @Override
        public String toString() {
            return "In Progress";
        }
    },
    /**
     * The Completed.
     */
    COMPLETED {
        @NonNull
        @Override
        public String toString() {
            return "Completed";
        }
    },
    /**
     * The Dropped.
     */
    DROPPED {
        @NonNull
        @Override
        public String toString() {
            return "Dropped";
        }
    },
    /**
     * The Plan to take.
     */
    PLAN_TO_TAKE {
        @NonNull
        @Override
        public String toString() {
            return "Plan to Take";
        }
    }
}
