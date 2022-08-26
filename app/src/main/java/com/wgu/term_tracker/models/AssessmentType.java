package com.wgu.term_tracker.models;

import androidx.annotation.NonNull;

/**
 * The enum Assessment type.
 */
public enum AssessmentType {
    /**
     * The Performance.
     */
    PERFORMANCE {
        @NonNull
        @Override
        public String toString() {
            return "Performance";
        }
    },
    /**
     * The Objective.
     */
    OBJECTIVE {
        @NonNull
        @Override
        public String toString() {
            return "Objective";
        }
    }
}
