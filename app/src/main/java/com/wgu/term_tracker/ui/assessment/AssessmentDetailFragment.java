package com.wgu.term_tracker.ui.assessment;

import static com.wgu.term_tracker.utilities.AlertsHelper.setRadioGroupEnabled;

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
import com.wgu.term_tracker.models.Assessment;
import com.wgu.term_tracker.models.AssessmentType;
import com.wgu.term_tracker.utilities.AlertsHelper;
import com.wgu.term_tracker.utilities.DateHelper;
import com.wgu.term_tracker.utilities.ValidationHelper;

import java.util.Calendar;
import java.util.Objects;


/**
 * The type Assessment detail fragment.
 */
public class AssessmentDetailFragment extends Fragment {

    /**
     * The constant ARG_IS_NEW.
     */
    public static final String ARG_IS_NEW = "arg_is_new";
    /**
     * The Is new assessment.
     */
    public boolean isNewAssessment;
    /**
     * The M start date cal.
     */
    protected final Calendar mStartDateCal = Calendar.getInstance();
    /**
     * The M end date cal.
     */
    protected final Calendar mEndDateCal = Calendar.getInstance();
    /**
     * The M title.
     */
    protected TextInputEditText mTitle;
    /**
     * The M start date.
     */
    protected TextInputEditText mStartDate;
    /**
     * The M end date.
     */
    protected TextInputEditText mEndDate;
    /**
     * The M cancel.
     */
    protected Button mCancel;
    /**
     * The M save.
     */
    protected Button mSave;
    /**
     * The M foreign key.
     */
    long mForeignKey = -1L;
    /**
     * The Assessment view model.
     */
    AssessmentViewModel assessmentViewModel;
    private RadioGroup mAssessmentTypeGroup;
    private RadioButton mPerformanceRadio;
    private RadioButton mObjectiveRadio;
    private Assessment mAssessment;

    /**
     * New instance assessment detail fragment.
     *
     * @param isNewAssessment the is new assessment
     * @return the assessment detail fragment
     */
    public static AssessmentDetailFragment newInstance(boolean isNewAssessment) {
        AssessmentDetailFragment fragment = new AssessmentDetailFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_NEW, isNewAssessment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        assessmentViewModel = new ViewModelProvider(requireActivity()).get(AssessmentViewModel.class);
        if (getArguments() != null) {
            isNewAssessment = getArguments().getBoolean(ARG_IS_NEW);
        }
        mForeignKey = assessmentViewModel.getSelectedCourseId();
        if (!isNewAssessment) {
            mAssessment = assessmentViewModel.getSelectedAssessment();
        }
        setToolbarTitle();
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assessment_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getTextViewsAndButtons(view);
        prepFieldListeners();
        if (!isNewAssessment) setExistingAssessmentFields();
    }


    private void setToolbarTitle() {
        String title;
        if (isNewAssessment) {
            title = "New Assessment";
        } else {
            title = "Assessment Info";
        }
        requireActivity().setTitle(title);
    }


    private void showItemDeletedToast() {
        Toast.makeText(getContext(), "Assessment has been deleted!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Process save assessment.
     */
    protected void processSaveAssessment() {
        clearKeyboard();
        if (assessmentIsValid()) {
            saveAssessment();
            showItemSavedToast();
            setToolbarTitle();
        } else {
            AlertsHelper.showErrorToast(getContext());
        }
    }

    /**
     * Save assessment.
     */
    public void saveAssessment() {
        if (isNewAssessment) {
            createNewAssessment();
            assessmentViewModel.insert(mAssessment);
            isNewAssessment = false;
        } else {
            updateAssessment();
        }
    }

    private void createNewAssessment() {
        mAssessment = new Assessment(
                String.valueOf(mTitle.getText()),
                mStartDateCal.getTime(),
                mEndDateCal.getTime(),
                getAssessmentType(),
                mForeignKey
        );
    }

    private boolean assessmentIsValid() {
        if (ValidationHelper.validateForm(mTitle, mStartDate, mEndDate)) {
            DateHelper.setCalendarFromText(mStartDateCal, Objects.requireNonNull(mStartDate.getText()).toString());
            DateHelper.setCalendarFromText(mEndDateCal, Objects.requireNonNull(mEndDate.getText()).toString());
            return true;
        }
        return false;
    }

    @SuppressLint("NonConstantResourceId")
    private AssessmentType getAssessmentType() {
        switch (mAssessmentTypeGroup.getCheckedRadioButtonId()) {
            case R.id.performanceRadioButton:
                return AssessmentType.PERFORMANCE;
            case R.id.objectiveRadioButton:
                return AssessmentType.OBJECTIVE;
        }
        return null;
    }


    /**
     * Show item saved toast.
     */
    protected void showItemSavedToast() {
        showAssessmentSavedToast();
    }

    private void showAssessmentSavedToast() {
        Toast.makeText(getContext(), R.string.toast_assessment_saved, Toast.LENGTH_LONG).show();
    }

    /**
     * Delete assessment.
     */
    public void deleteAssessment() {
        assessmentViewModel.delete(mAssessment);
        showItemDeletedToast();
        assessmentViewModel.setSelectedAssessment(null);
        getParentFragmentManager().popBackStack();
    }

    /**
     * Update assessment.
     */
    protected void updateAssessment() {
        mAssessment.setTitle(Objects.requireNonNull(mTitle.getText()).toString());
        mAssessment.setStartDate(mStartDateCal.getTime());
        mAssessment.setEndDate(mEndDateCal.getTime());
        mAssessment.setAssessmentType(getAssessmentType());
        if (mAssessment.alertInfo.isStartAlert()) {
            setAlert(AlarmReceiver.ARG_ALERT_DATE_START, AlarmReceiver.ARG_ALERT_ACTION_SET);
        }
        if (mAssessment.alertInfo.isEndAlert()) {
            setAlert(AlarmReceiver.ARG_ALERT_DATE_END, AlarmReceiver.ARG_ALERT_ACTION_SET);
        }
        assessmentViewModel.update(mAssessment);
    }

    private void confirmDelete() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String message = getString(R.string.dialog_confirm_delete_message_assessment);
        builder.setMessage(message)
                .setTitle(R.string.dialog_delete_title)
                .setPositiveButton(R.string.ok, (dialog, which) -> deleteAssessment())
                .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void clearKeyboard() {
        mTitle.clearFocus();
        mEndDate.clearFocus();
        InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
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
        mAssessmentTypeGroup = view.findViewById(R.id.assessmentTypeRadioGroup);
        mPerformanceRadio = view.findViewById(R.id.performanceRadioButton);
        mObjectiveRadio = view.findViewById(R.id.objectiveRadioButton);
        mCancel = view.findViewById(R.id.cancel_button);
        mSave = view.findViewById(R.id.save_button);
    }

    /**
     * Prep field listeners.
     */
    @SuppressLint("ClickableViewAccessibility")
    protected void prepFieldListeners() {
        mStartDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mStartDate.getError() != ValidationHelper.INVALID_DATE_FORMAT)
                    mStartDate.setError(null);
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(),
                        (view, y, m, d) -> DateHelper.setDateFromDatePicker(mStartDateCal, mStartDate, y, m, d),
                        year, month, day);
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
                        (view, y, m, d) -> DateHelper.setDateFromDatePicker(mEndDateCal, mEndDate, y, m, d),
                        year, month, day);
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
        mSave.setOnClickListener(v -> processSaveAssessment());

        mTitle.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                TextInputEditText title = (TextInputEditText) v;
                ValidationHelper.isValidTitle(title);
            }
        });
    }

    private void setExistingAssessmentFields() {
        TermTrackerApplication.singleThreadExecutor.execute(() -> mEndDateCal.setTimeInMillis(mAssessment.getEndDate().getTime()));
        mTitle.setText(mAssessment.getTitle());
        mStartDate.setText(DateHelper.formatDate(mAssessment.getStartDate()));
        mEndDate.setText(DateHelper.formatDate(mAssessment.getEndDate()));
        if (mAssessment.getAssessmentType() == AssessmentType.OBJECTIVE) {
            mObjectiveRadio.setChecked(true);
        } else {
            mPerformanceRadio.setChecked(true);
        }
    }

    /**
     * Process delete.
     */
    public void processDelete() {
        confirmDelete();
    }

    /**
     * Show set alerts popup.
     */
    void showSetAlertsPopup() {
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
        startSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setRadioGroupEnabled(startRadioGroup, startSwitch.isChecked()));

        SwitchCompat endSwitch = popupView.findViewById(R.id.switch_end_date);
        endSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setRadioGroupEnabled(endRadioGroup, endSwitch.isChecked()));

        startSwitch.setChecked(mAssessment.alertInfo.isStartAlert());
        endSwitch.setChecked(mAssessment.alertInfo.isEndAlert());

        for (int i = 0; i < startRadioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) startRadioGroup.getChildAt(i);
            if (button.getId() == R.id.start_date_alert_day_of) {
                button.setChecked(mAssessment.alertInfo.isStartAlertDayOf());
            } else {
                button.setChecked(!mAssessment.alertInfo.isStartAlertDayOf());
            }
        }

        for (int i = 0; i < endRadioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) endRadioGroup.getChildAt(i);
            if (button.getId() == R.id.end_date_alert_day_of) {
                button.setChecked(mAssessment.alertInfo.isEndAlertDayOf());
            } else {
                button.setChecked(!mAssessment.alertInfo.isEndAlertDayOf());
            }
        }

        TimePicker timePicker = popupView.findViewById(R.id.time_picker);
        if (mAssessment.alertInfo.getAlertHour() != null && mAssessment.alertInfo.getAlertMinute() != null) {
            timePicker.setHour(mAssessment.alertInfo.getAlertHour());
            timePicker.setMinute(mAssessment.alertInfo.getAlertMinute());
        }

        popupView.findViewById(R.id.popup_done_button).setOnClickListener(v -> {
            mAssessment.alertInfo.setStartAlert(startSwitch.isChecked());
            mAssessment.alertInfo.setEndAlert(endSwitch.isChecked());
            if (startSwitch.isChecked() || endSwitch.isChecked()) {
                mAssessment.alertInfo.setAlertHour(timePicker.getHour());
                mAssessment.alertInfo.setAlertMinute(timePicker.getMinute());
                if (startSwitch.isChecked()) {
                    mAssessment.alertInfo.setStartAlertDayOf(startRadioGroup.getCheckedRadioButtonId() == R.id.start_date_alert_day_of);
                }
                if (endSwitch.isChecked()) {
                    mAssessment.alertInfo.setEndAlertDayOf(endRadioGroup.getCheckedRadioButtonId() == R.id.end_date_alert_day_of);
                }
            }
            if (!startSwitch.isChecked()) {
                setAlert(AlarmReceiver.ARG_ALERT_DATE_START, AlarmReceiver.ARG_ALERT_ACTION_CANCEL);
            }
            if (!endSwitch.isChecked()) {
                setAlert(AlarmReceiver.ARG_ALERT_DATE_END, AlarmReceiver.ARG_ALERT_ACTION_CANCEL);
            }
            processSaveAssessment();
            popupWindow.dismiss();
        });
        clearKeyboard();
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void setAlert(String arg_date, String arg_action) {
        AlarmManager alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireActivity().getApplicationContext(), AlarmReceiver.class);
        intent.putExtra(AlarmReceiver.KEY_ALERT_DATE, arg_date);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AlarmReceiver.KEY_ITEM_OBJECT, mAssessment);
        intent.setAction("com.wgu.Term_Tracker.SET_ALARM_NOTIFICATION");
        intent.putExtra("bundle", bundle);
        Calendar alertDate;
        PendingIntent pendingIntent;

        // Start date
        if (arg_date.equals(AlarmReceiver.ARG_ALERT_DATE_START)) {
            pendingIntent = PendingIntent.getBroadcast(getContext(),
                    (int) (mAssessment.getId() + mAssessment.getClass().hashCode()),
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            if (arg_action.equals(AlarmReceiver.ARG_ALERT_ACTION_SET)) {
                alertDate = AlertsHelper.getAlertDate(mStartDateCal,
                        mAssessment.alertInfo.getAlertHour(),
                        mAssessment.alertInfo.getAlertMinute());
                if (!mAssessment.alertInfo.isStartAlertDayOf()) {
                    alertDate.add(Calendar.DAY_OF_YEAR, -1);
                }
                Log.d("ALERT DATE BEFORE START ALARM", alertDate.toString());
                if (System.currentTimeMillis() < alertDate.getTimeInMillis()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertDate.getTimeInMillis(), pendingIntent);
                } else {
                    Toast.makeText(getContext(),
                            "The alert for the Start Date is already passed! No notification will be sent.",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                // Cancel start date alarm
                alarmManager.cancel(pendingIntent);
            }
        } else {
            // END DATE
            pendingIntent = PendingIntent.getBroadcast(
                    getContext(),
                    (int) (mAssessment.getId() + mAssessment.getClass().hashCode()) + 1,
                    intent,
                    PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            if (arg_action.equals(AlarmReceiver.ARG_ALERT_ACTION_SET)) {
                alertDate = AlertsHelper.getAlertDate(mEndDateCal,
                        mAssessment.alertInfo.getAlertHour(),
                        mAssessment.alertInfo.getAlertMinute());
                if (!mAssessment.alertInfo.isEndAlertDayOf()) {
                    alertDate.add(Calendar.DAY_OF_YEAR, -1);
                }
                Log.d("ALERT DATE BEFORE END ALARM", alertDate.toString());
                if (System.currentTimeMillis() < alertDate.getTimeInMillis()) {
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alertDate.getTimeInMillis(), pendingIntent);
                } else {
                    Toast.makeText(getContext(),
                            "The alert for the End Date is already passed! No notification will be sent.",
                            Toast.LENGTH_LONG).show();
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
}