package com.wgu.term_tracker;

import android.app.Application;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The type Term tracker application.
 */
public class TermTrackerApplication extends Application {
    /**
     * The constant singleThreadExecutor.
     */
    public static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    /**
     * The constant ARG_FOREIGN_KEY.
     */
    public static final String ARG_FOREIGN_KEY = "arg_foreign_key";
    /**
     * The constant ARG_PRIMARY_KEY.
     */
    public static final String ARG_PRIMARY_KEY = "arg_primary_key";

}
