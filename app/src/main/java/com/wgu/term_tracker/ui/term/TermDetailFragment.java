package com.wgu.term_tracker.ui.term;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.wgu.term_tracker.AlarmReceiver;
import com.wgu.term_tracker.R;
import com.wgu.term_tracker.TermTrackerApplication;
import com.wgu.term_tracker.models.Term;
import com.wgu.term_tracker.ui.course.CourseActivity;
import com.wgu.term_tracker.utilities.AlertsHelper;
import com.wgu.term_tracker.utilities.DateHelper;
import com.wgu.term_tracker.utilities.ValidationHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;


/**
 * The type Term detail fragment.
 */
public class TermDetailFragment extends Fragment {

    /**
     * The title.
     */
    protected TextInputEditText mTitle;
    /**
     * The start date.
     */
    protected TextInputEditText mStartDate;
    /**
     * The end date.
     */
    protected TextInputEditText mEndDate;
    /**
     * The one to many title.
     */
    protected TextView mOneToManyTitle;
    /**
     * The one to many count.
     */
    protected TextView mOneToManyCount;
    /**
     * The cancel.
     */
    protected Button mCancel;
    /**
     * The save.
     */
    protected Button mSave;
    /**
     * The one to many button.
     */
    protected Button mOneToManyButton;
    /**
     * The end date cal.
     */
    protected final Calendar mEndDateCal = Calendar.getInstance();
    /**
     * The start date cal.
     */
    protected final Calendar mStartDateCal = Calendar.getInstance();
    /**
     * The primary key.
     */
    protected long mPrimaryKey;
    private boolean isNewTerm;
    private boolean hasCourses;
    private TermViewModel termViewModel;
    private Term mTerm;

    /**
     * New instance term detail fragment.
     *
     * @param isNewTerm the is new term
     * @return the term detail fragment
     */
    public static TermDetailFragment newInstance(boolean isNewTerm) {
        TermDetailFragment fragment = new TermDetailFragment();
        Bundle args = new Bundle();
        args.putBoolean(AlertsHelper.ARG_IS_NEW_TERM, isNewTerm);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        termViewModel = new ViewModelProvider(requireActivity()).get(TermViewModel.class);
        if (getArguments() != null) {
            isNewTerm = getArguments().getBoolean(AlertsHelper.ARG_IS_NEW_TERM);
        }
        if (!isNewTerm) mTerm = termViewModel.getSelectedTerm();
        setToolbarTitle();
        setHasOptionsMenu(true);
    }

    private void setToolbarTitle() {
        String title;
        if (isNewTerm) {
            title = "Add New Term";
        } else {
            title = "Term Information";
        }
        requireActivity().setTitle(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_term_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTextViewsAndButtons(view);
        prepFieldListeners();
        if (!isNewTerm) {
            setExistingTermFields();
        }
    }


    /**
     * Gets text views and buttons.
     *
     * @param view the view
     */
    protected void getTextViewsAndButtons(View view) {
        mTitle = view.findViewById(R.id.detail_item_title);
        mStartDate = view.findViewById(R.id.detail_item_start_date);
        mEndDate = view.findViewById(R.id.detail_item_end_date);

        mCancel = view.findViewById(R.id.cancel_button);
        mSave = view.findViewById(R.id.save_button);

        mOneToManyTitle = view.findViewById(R.id.one_to_many);
        mOneToManyCount = view.findViewById(R.id.one_to_many_count);
        mOneToManyButton = view.findViewById(R.id.one_to_many_button);
    }

    /**
     * Prep field listeners.
     */
    @SuppressLint("ClickableViewAccessibility")
    protected void prepFieldListeners() {
        // Set date fields
        mStartDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mStartDate.getError() == ValidationHelper.INVALID_DATE_FORMAT)
                    mStartDate.setError(null);
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        (view, y, m, d) ->
                                DateHelper.setDateFromDatePicker(mStartDateCal, mStartDate, y, m, d), year, month, day);
                datePickerDialog.show();
            } else {
                ValidationHelper.isValidDateFormat(mStartDate);
            }
        });

        mStartDate.setOnKeyListener((v, keyCode, event) -> {
            ValidationHelper.isValidDateFormat(mStartDate);
            return false;
        });


        mEndDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mEndDate.getError() != ValidationHelper.INVALID_DATE_FORMAT)
                    mEndDate.setError(null);
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        (view, year1, month1, dayOfMonth) ->
                                DateHelper.setDateFromDatePicker(mEndDateCal, mEndDate, year1, month1, dayOfMonth), year, month, day);
                datePickerDialog.show();
            } else {
                ValidationHelper.isValidDateFormat(mEndDate);
            }
        });

        mEndDate.setOnKeyListener((v, keyCode, event) -> {
            ValidationHelper.isValidDateFormat(mEndDate);
            return false;
        });

        // Set button listeners
        mCancel.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            fragmentManager.popBackStack();
        });
        mSave.setOnClickListener(v -> processSaveTerm());
        mOneToManyButton.setOnClickListener(v -> {
            if (isNewTerm) {
                Toast.makeText(v.getContext(), "Term must be saved first before adding Courses!", Toast.LENGTH_LONG).show();
            } else {
                goToCourseList();
            }
        });
        mTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextInputEditText title = (TextInputEditText) v;
                ValidationHelper.isValidTitle(title);
            }
        });
        termViewModel.termHasCourses().observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean != null) {
                hasCourses = aBoolean;
            }
        });
    }

    private void setExistingTermFields() {
        TermTrackerApplication.singleThreadExecutor.execute(() -> {
            mStartDateCal.setTimeInMillis(mTerm.getStartDate().getTime());
            mEndDateCal.setTimeInMillis(mTerm.getEndDate().getTime());
        });
        termViewModel.getCourseCount().observe(getViewLifecycleOwner(), integer -> {
            if (integer != null) {
                mOneToManyCount.setText(String.valueOf(integer));
            }
        });
        mTitle.setText(mTerm.getTitle());
        mStartDate.setText(DateHelper.formatDate(mTerm.getStartDate()));
        mEndDate.setText(DateHelper.formatDate(mTerm.getEndDate()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                if (!isNewTerm) {
                    if (!has_OneToManyItems_Connected()) {
                        confirmDelete();
                    } else {
                        showStillHasOneToManyError();
                    }
                } else {
                    showNotSavedYetToast();
                }
                return true;
            case R.id.menu_detail_save:
                processSaveTerm();
                return true;
            case android.R.id.home:
                getParentFragmentManager().popBackStack();
                return true;
            case R.id.detail_menu_set_alert:
                if (isNewTerm) {
                    showNotSavedYetToast();
                } else {
                    showSetAlertsPopup();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showNotSavedYetToast() {
        Toast.makeText(getContext(), "Item has not been saved yet!", Toast.LENGTH_SHORT).show();
    }

    private void showSetAlertsPopup() {
        @SuppressLint("InflateParams") View popupView = getLayoutInflater().inflate(R.layout.popup_set_alerts, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                true);
        popupWindow.setElevation(24);
        popupWindow.setAnimationStyle(androidx.transition.R.style.Animation_AppCompat_Dialog);

        RadioGroup startRadioGroup = popupView.findViewById(R.id.start_date_alert_radio_group);
        RadioGroup endRadioGroup = popupView.findViewById(R.id.end_date_alert_radio_group);

        SwitchCompat startSwitch = popupView.findViewById(R.id.switch_start_date);
        startSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> AlertsHelper.setRadioGroupEnabled(startRadioGroup, startSwitch.isChecked()));

        SwitchCompat endSwitch = popupView.findViewById(R.id.switch_end_date);
        endSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> AlertsHelper.setRadioGroupEnabled(endRadioGroup, endSwitch.isChecked()));

        startSwitch.setChecked(mTerm.alertInfo.isStartAlert());
        endSwitch.setChecked(mTerm.alertInfo.isEndAlert());

        for (int i = 0; i < startRadioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) startRadioGroup.getChildAt(i);
            if (button.getId() == R.id.start_date_alert_day_of) {
                button.setChecked(mTerm.alertInfo.isStartAlertDayOf());
            } else {
                button.setChecked(!mTerm.alertInfo.isStartAlertDayOf());
            }
        }

        for (int i = 0; i < endRadioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) endRadioGroup.getChildAt(i);
            if (button.getId() == R.id.end_date_alert_day_of) {
                button.setChecked(mTerm.alertInfo.isEndAlertDayOf());
            } else {
                button.setChecked(!mTerm.alertInfo.isEndAlertDayOf());
            }
        }

        TimePicker timePicker = popupView.findViewById(R.id.time_picker);
        if (mTerm.alertInfo.getAlertHour() != null && mTerm.alertInfo.getAlertMinute() != null) {
            timePicker.setHour(mTerm.alertInfo.getAlertHour());
            timePicker.setMinute(mTerm.alertInfo.getAlertMinute());
        }

        popupView.findViewById(R.id.popup_done_button).setOnClickListener(v -> {
            mTerm.alertInfo.setStartAlert(startSwitch.isChecked());
            mTerm.alertInfo.setEndAlert(endSwitch.isChecked());
            if (startSwitch.isChecked() || endSwitch.isChecked()) {
                mTerm.alertInfo.setAlertHour(timePicker.getHour());
                mTerm.alertInfo.setAlertMinute(timePicker.getMinute());
                if (startSwitch.isChecked()) {
                    mTerm.alertInfo.setStartAlertDayOf(startRadioGroup.getCheckedRadioButtonId() == R.id.start_date_alert_day_of);
                }
                if (endSwitch.isChecked()) {
                    mTerm.alertInfo.setEndAlertDayOf(endRadioGroup.getCheckedRadioButtonId() == R.id.end_date_alert_day_of);
                }
            }
            if (!startSwitch.isChecked()) {
                setAlert(AlarmReceiver.ARG_ALERT_DATE_START, AlarmReceiver.ARG_ALERT_ACTION_CANCEL);
            }
            if (!endSwitch.isChecked()) {
                setAlert(AlarmReceiver.ARG_ALERT_DATE_END, AlarmReceiver.ARG_ALERT_ACTION_CANCEL);
            }
            processSaveTerm();
            popupWindow.dismiss();
        });
        clearKeyboard();
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }


    private void setAlert(String arg_date, String arg_action) {
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity().getApplicationContext(), AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.KEY_ALERT_DATE, arg_date);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AlarmReceiver.KEY_ITEM_OBJECT, mTerm);
        intent.setAction("com.wgu.Term_Tracker.SET_ALARM_NOTIFICATION");
        intent.putExtra("bundle", bundle);
        Calendar alertDate;
        PendingIntent pendingIntent;

        // Start date
        if (arg_date.equals(AlarmReceiver.ARG_ALERT_DATE_START)) {
            pendingIntent = PendingIntent.getBroadcast(getContext(), (int) (mTerm.getId() + mTerm.getClass().hashCode()), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            if (arg_action.equals(AlarmReceiver.ARG_ALERT_ACTION_SET)) {
                alertDate = AlertsHelper.getAlertDate(mStartDateCal, mTerm.alertInfo.getAlertHour(), mTerm.alertInfo.getAlertMinute());
                if (!mTerm.alertInfo.isStartAlertDayOf()) {
                    alertDate.add(Calendar.DAY_OF_YEAR, -1);
                }
                Log.d("ALERT DATE BEFORE START ALARM", alertDate.toString());
                if (System.currentTimeMillis() < alertDate.getTimeInMillis()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertDate.getTimeInMillis(), pendingIntent);
                } else {
                    Toast.makeText(getContext(), "The alert for the Start Date is already passed! No notification will be sent.", Toast.LENGTH_LONG).show();
                }
            } else {
                // Cancel start date alarm
                alarmManager.cancel(pendingIntent);
            }
        } else {
            // END DATE
            pendingIntent = PendingIntent.getBroadcast(getContext(), (int) (mTerm.getId() + mTerm.getClass().hashCode()) + 1, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            if (arg_action.equals(AlarmReceiver.ARG_ALERT_ACTION_SET)) {
                alertDate = AlertsHelper.getAlertDate(mEndDateCal, mTerm.alertInfo.getAlertHour(), mTerm.alertInfo.getAlertMinute());
                if (!mTerm.alertInfo.isEndAlertDayOf()) {
                    alertDate.add(Calendar.DAY_OF_YEAR, -1);
                }
                Log.d("ALERT DATE BEFORE END ALARM", alertDate.toString());
                if (System.currentTimeMillis() < alertDate.getTimeInMillis()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertDate.getTimeInMillis(), pendingIntent);
                } else {
                    Toast.makeText(getContext(), "The alert for the End Date is already passed! No notification will be sent.", Toast.LENGTH_LONG).show();
                }
            } else {
                // Cancel end date alarm
                alarmManager.cancel(pendingIntent);
            }
        }
        Calendar systemTime = Calendar.getInstance();
        systemTime.setTimeInMillis(System.currentTimeMillis());
        Log.d("CURRENT SYSTEM TIME", systemTime.toString());
    }


    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = getString(R.string.dialog_confirm_delete_message);
        message = message.replace("this", "this " + "Term");
        builder.setMessage(message)
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> deleteTerm());
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showItemDeletedToast() {
        Toast.makeText(getContext(), "Term has been deleted!", Toast.LENGTH_SHORT).show();
    }


    /**
     * Process save term.
     */
    protected void processSaveTerm() {
        clearKeyboard();
        if (termIsValid()) {
            saveTerm();
            showTermSavedToast();
            setToolbarTitle();
        } else {
            AlertsHelper.showErrorToast(getContext());
        }
    }

    private void saveTerm() {
        if (isNewTerm) {
            createNewTerm();
            termViewModel.insert(mTerm);
            isNewTerm = false;
        } else {
            updateTermFromFields();
        }
    }


    private boolean termIsValid() {
        if (ValidationHelper.validateForm(mTitle, mStartDate, mEndDate)) {
            DateHelper.setCalendarFromText(mStartDateCal, Objects.requireNonNull(mStartDate.getText()).toString());
            DateHelper.setCalendarFromText(mEndDateCal, Objects.requireNonNull(mEndDate.getText()).toString());
            if (ValidationHelper.validateStartBeforeEndDates(mStartDateCal, mEndDateCal)) {
                return true;
            } else {
                mStartDate.setError(ValidationHelper.START_END_DATE_CHRONO_ERROR);
                mEndDate.setError(ValidationHelper.START_END_DATE_CHRONO_ERROR);
            }
        }
        return false;
    }


    private void createNewTerm() {
        String title = String.valueOf(mTitle.getText());
        Date start = mStartDateCal.getTime();
        Date end = mEndDateCal.getTime();
        mTerm = new Term(title, start, end);
    }

    private boolean has_OneToManyItems_Connected() {
        return hasCourses;
    }


    private void showTermSavedToast() {
        Toast.makeText(getContext(), R.string.toast_term_saved, Toast.LENGTH_LONG).show();
    }

    /**
     * Delete term.
     */
    protected void deleteTerm() {
        if (has_OneToManyItems_Connected()) {
            showStillHasOneToManyError();
        } else {
            termViewModel.delete(mTerm);
            showItemDeletedToast();
            termViewModel.setSelectedTerm(null);
            getParentFragmentManager().popBackStack();
        }

    }

    /**
     * Update term from fields.
     */
    protected void updateTermFromFields() {
        mTerm.setTitle(Objects.requireNonNull(mTitle.getText()).toString());
        mTerm.setStartDate(mStartDateCal.getTime());
        mTerm.setEndDate(mEndDateCal.getTime());
        if (mTerm.alertInfo.isStartAlert()) {
            setAlert(AlarmReceiver.ARG_ALERT_DATE_START, AlarmReceiver.ARG_ALERT_ACTION_SET);
        }
        if (mTerm.alertInfo.isEndAlert()) {
            setAlert(AlarmReceiver.ARG_ALERT_DATE_END, AlarmReceiver.ARG_ALERT_ACTION_SET);
        }
        termViewModel.update(mTerm);
    }

    private void clearKeyboard() {
        mTitle.clearFocus();
        mStartDate.clearFocus();
        mEndDate.clearFocus();

        InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }

    private void showStillHasOneToManyError() {
        Toast.makeText(getContext(), R.string.toast_term_has_assessments_still, Toast.LENGTH_LONG).show();
    }

    private void goToCourseList() {
        Intent intent = new Intent(getContext(), CourseActivity.class);
        intent.putExtra(TermTrackerApplication.ARG_FOREIGN_KEY, mTerm.getId());
        startActivity(intent);
    }

}