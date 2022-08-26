package com.wgu.term_tracker.ui.course;

import static com.wgu.term_tracker.TermTrackerApplication.ARG_FOREIGN_KEY;
import static com.wgu.term_tracker.TermTrackerApplication.ARG_PRIMARY_KEY;
import static com.wgu.term_tracker.utilities.AlertsHelper.setRadioGroupEnabled;
import static com.wgu.term_tracker.utilities.AlertsHelper.showErrorToast;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
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
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputEditText;
import com.wgu.term_tracker.AlarmReceiver;
import com.wgu.term_tracker.R;
import com.wgu.term_tracker.models.Course;
import com.wgu.term_tracker.models.CourseStatus;
import com.wgu.term_tracker.ui.assessment.AssessmentActivity;
import com.wgu.term_tracker.utilities.AlertsHelper;
import com.wgu.term_tracker.utilities.DateHelper;
import com.wgu.term_tracker.utilities.ValidationHelper;

import java.util.Calendar;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseDetailPagerTopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseDetailPagerTopFragment extends Fragment {

    /**
     * The end date cal.
     */
    protected final Calendar mEndDateCal = Calendar.getInstance();
    /**
     * The start date cal.
     */
    protected final Calendar mStartDateCal = Calendar.getInstance();
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
     * The Dropped radio.
     */
    RadioButton droppedRadio;
    /**
     * The Plan to take radio.
     */
    RadioButton planToTakeRadio;
    /**
     * The Is new course.
     */
    boolean isNewCourse;
    private RadioButton inProgressRadio;
    private RadioButton completedRadio;
    private CourseStatus mSelectedCourseStatus = CourseStatus.PLAN_TO_TAKE;
    private Course mCourse;
    private CourseViewModel courseViewModel;

    /**
     * New instance course detail pager top fragment.
     *
     * @return the course detail pager top fragment
     */
    public static CourseDetailPagerTopFragment newInstance() {
        CourseDetailPagerTopFragment fragment = new CourseDetailPagerTopFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        courseViewModel = new ViewModelProvider(requireActivity()).get(CourseViewModel.class);
        isNewCourse = courseViewModel.getSelectedCourseId() == -1L;
        if (!isNewCourse) mCourse = courseViewModel.getSelectedCourse();
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_course_detail_pager_top, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRadioButtons(view);

        mTitle = view.findViewById(R.id.detail_item_title);
        mStartDate = view.findViewById(R.id.detail_item_start_date);
        mEndDate = view.findViewById(R.id.detail_item_end_date);
        mOneToManyTitle = view.findViewById(R.id.one_to_many);
        mOneToManyCount = view.findViewById(R.id.one_to_many_count);
        mOneToManyButton = view.findViewById(R.id.one_to_many_button);
        mCancel = view.findViewById(R.id.cancel_button);
        mSave = view.findViewById(R.id.save_button);

        setupCourseFields();
        if (!isNewCourse) {
            setExistingCourseFields();
        }
    }


    private void setExistingCourseFields() {
        courseViewModel.getAssessmentCountForCourse().observe(getViewLifecycleOwner(), integer -> {
            if (integer != null) mOneToManyCount.setText(String.valueOf(integer));
        });
        mTitle.setText(mCourse.getTitle());
        mStartDateCal.setTimeInMillis(mCourse.getStartDate().getTime());
        mStartDate.setText(DateHelper.formatDate(mCourse.getStartDate()));
        mEndDateCal.setTimeInMillis(mCourse.getEndDate().getTime());
        mEndDate.setText(DateHelper.formatDate(mCourse.getEndDate()));
        switch (mCourse.getCourseStatus()) {
            case IN_PROGRESS:
                inProgressRadio.setChecked(true);
                break;
            case COMPLETED:
                completedRadio.setChecked(true);
                break;
            case DROPPED:
                droppedRadio.setChecked(true);
                break;
            case PLAN_TO_TAKE:
                planToTakeRadio.setChecked(true);
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void setupRadioButtons(View view) {
        RadioGroup courseStatusRadioGroup = view.findViewById(R.id.courseStatusRadioGroup);
        inProgressRadio = view.findViewById(R.id.inProgressRadio);
        completedRadio = view.findViewById(R.id.completedRadio);
        droppedRadio = view.findViewById(R.id.droppedRadio);
        planToTakeRadio = view.findViewById(R.id.planToTakeRadio);

        courseStatusRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.inProgressRadio:
                    mSelectedCourseStatus = CourseStatus.IN_PROGRESS;
                    break;
                case R.id.completedRadio:
                    mSelectedCourseStatus = CourseStatus.COMPLETED;
                    break;
                case R.id.droppedRadio:
                    mSelectedCourseStatus = CourseStatus.DROPPED;
                    break;
                case R.id.planToTakeRadio:
                    mSelectedCourseStatus = CourseStatus.PLAN_TO_TAKE;
                    break;
            }
        });

    }

    private void setupCourseFields() {
        mStartDate.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                if (mStartDate.getError() == ValidationHelper.INVALID_DATE_FORMAT)
                    mStartDate.setError(null);
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        v.getContext(),
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        v.getContext(),
                        (view, y, m, d) ->
                                DateHelper.setDateFromDatePicker(mEndDateCal, mEndDate, y, m, d),
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

        mOneToManyButton.setOnClickListener(v -> {
            if (isNewCourse) {
                AlertsHelper.showCourseNeedsToBeSavedFirstToast(v.getContext());
            } else {
                goToAssessmentList();
            }
        });
        mCancel.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        mSave.setOnClickListener(v -> processSaveCourse());
    }

    /**
     * Process save course.
     */
    public void processSaveCourse() {
        clearKeyboard();
        if (courseIsValid()) {
            saveCourse();
            showCourseSavedToast();
            setToolbarTitle();
        } else {
            showErrorToast(getContext());
        }
    }

    private void setToolbarTitle() {
        String title;
        if (isNewCourse) {
            title = "Add New Course";
        } else {
            title = "Course Information";
        }
        requireActivity().setTitle(title);
    }

    private void clearKeyboard() {
        mTitle.clearFocus();
        mStartDate.clearFocus();
        mEndDate.clearFocus();

        InputMethodManager manager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(requireView().getWindowToken(), 0);
    }

    private void saveCourse() {
        if (isNewCourse) {
            createNewCourse();
            courseViewModel.insert(mCourse);
            isNewCourse = false;
        } else {
            updateCourseFromFields();
        }
    }

    private void createNewCourse() {
        mCourse = new Course(String.valueOf(mTitle.getText()),
                mStartDateCal.getTime(),
                mEndDateCal.getTime(),
                mSelectedCourseStatus,
                courseViewModel.getSelectedTermId());
    }

    private void showCourseSavedToast() {
        Toast.makeText(getContext(), "Course Saved!", Toast.LENGTH_SHORT).show();
    }


    private void goToAssessmentList() {
        Intent intent = new Intent(getContext(), AssessmentActivity.class);
        intent.putExtra(ARG_FOREIGN_KEY, courseViewModel.getSelectedCourseId());
        startActivity(intent);
    }


    /**
     * Sets new course.
     *
     * @param newCourse the new course
     */
    public void setNewCourse(boolean newCourse) {
        isNewCourse = newCourse;
    }

    /**
     * Course is valid boolean.
     *
     * @return the boolean
     */
    public boolean courseIsValid() {
        if (ValidationHelper.validateForm(mTitle, mStartDate, mEndDate)) {
            DateHelper.setCalendarFromText(mStartDateCal, Objects.requireNonNull(mStartDate.getText()).toString());
            DateHelper.setCalendarFromText(mEndDateCal, Objects.requireNonNull(mEndDate.getText()).toString());
            if (ValidationHelper.validateStartBeforeEndDates(mStartDateCal, mEndDateCal)) {
                return true;
            }
            mStartDate.setError(ValidationHelper.START_END_DATE_CHRONO_ERROR);
            mEndDate.setError(ValidationHelper.START_END_DATE_CHRONO_ERROR);
        }
        return false;
    }

    /**
     * Update course from fields.
     */
    public void updateCourseFromFields() {
        mCourse.setTitle(Objects.requireNonNull(mTitle.getText()).toString());
        mCourse.setStartDate(mStartDateCal.getTime());
        mCourse.setEndDate(mEndDateCal.getTime());
        mCourse.setStatus(mSelectedCourseStatus);
        mCourse.setTermId(courseViewModel.getSelectedTermId());
        if (mCourse.alertInfo.isStartAlert()) {
            setAlert(AlarmReceiver.ARG_ALERT_DATE_START, AlarmReceiver.ARG_ALERT_ACTION_SET);
        }
        if (mCourse.alertInfo.isEndAlert()) {
            setAlert(AlarmReceiver.ARG_ALERT_DATE_END, AlarmReceiver.ARG_ALERT_ACTION_SET);
        }
        courseViewModel.updateCourse(mCourse);
    }

    /**
     * Show set alerts popup.
     */
    void showSetAlertsPopup() {
        @SuppressLint("InflateParams")
        View popupView = getLayoutInflater().inflate(R.layout.popup_set_alerts, null);
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

        startSwitch.setChecked(mCourse.alertInfo.isStartAlert());
        endSwitch.setChecked(mCourse.alertInfo.isEndAlert());

        for (int i = 0; i < startRadioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) startRadioGroup.getChildAt(i);
            if (button.getId() == R.id.start_date_alert_day_of) {
                button.setChecked(mCourse.alertInfo.isStartAlertDayOf());
            } else {
                button.setChecked(!mCourse.alertInfo.isStartAlertDayOf());
            }
        }

        for (int i = 0; i < endRadioGroup.getChildCount(); i++) {
            RadioButton button = (RadioButton) endRadioGroup.getChildAt(i);
            if (button.getId() == R.id.end_date_alert_day_of) {
                button.setChecked(mCourse.alertInfo.isEndAlertDayOf());
            } else {
                button.setChecked(!mCourse.alertInfo.isEndAlertDayOf());
            }
        }

        TimePicker timePicker = popupView.findViewById(R.id.time_picker);
        if (mCourse.alertInfo.getAlertHour() != null && mCourse.alertInfo.getAlertMinute() != null) {
            timePicker.setHour(mCourse.alertInfo.getAlertHour());
            timePicker.setMinute(mCourse.alertInfo.getAlertMinute());
        }

        popupView.findViewById(R.id.popup_done_button).setOnClickListener(v -> {
            mCourse.alertInfo.setStartAlert(startSwitch.isChecked());
            mCourse.alertInfo.setEndAlert(endSwitch.isChecked());
            if (startSwitch.isChecked() || endSwitch.isChecked()) {
                mCourse.alertInfo.setAlertHour(timePicker.getHour());
                mCourse.alertInfo.setAlertMinute(timePicker.getMinute());
                if (startSwitch.isChecked()) {
                    mCourse.alertInfo.setStartAlertDayOf(startRadioGroup.getCheckedRadioButtonId() == R.id.start_date_alert_day_of);
                }
                if (endSwitch.isChecked()) {
                    mCourse.alertInfo.setEndAlertDayOf(endRadioGroup.getCheckedRadioButtonId() == R.id.end_date_alert_day_of);
                }
            }
            if (!startSwitch.isChecked()) {
                setAlert(AlarmReceiver.ARG_ALERT_DATE_START, AlarmReceiver.ARG_ALERT_ACTION_CANCEL);
            }
            if (!endSwitch.isChecked()) {
                setAlert(AlarmReceiver.ARG_ALERT_DATE_END, AlarmReceiver.ARG_ALERT_ACTION_CANCEL);
            }
            processSaveCourse();
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
        bundle.putParcelable(AlarmReceiver.KEY_ITEM_OBJECT, mCourse);
        intent.setAction("com.wgu.Term_Tracker.SET_ALARM_NOTIFICATION");
        intent.putExtra("bundle", bundle);
        Calendar alertDate;
        PendingIntent pendingIntent;

        // Start date
        if (arg_date.equals(AlarmReceiver.ARG_ALERT_DATE_START)) {
            pendingIntent = PendingIntent.getBroadcast(getContext(), (int) (mCourse.getId() + mCourse.getClass().hashCode()), intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            if (arg_action.equals(AlarmReceiver.ARG_ALERT_ACTION_SET)) {
                alertDate = AlertsHelper.getAlertDate(mStartDateCal, mCourse.alertInfo.getAlertHour(), mCourse.alertInfo.getAlertMinute());
                if (!mCourse.alertInfo.isStartAlertDayOf()) {
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
            pendingIntent = PendingIntent.getBroadcast(getContext(), (int) (mCourse.getId() + mCourse.getClass().hashCode()) + 1, intent, PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
            if (arg_action.equals(AlarmReceiver.ARG_ALERT_ACTION_SET)) {
                alertDate = AlertsHelper.getAlertDate(mEndDateCal, mCourse.alertInfo.getAlertHour(), mCourse.alertInfo.getAlertMinute());
                if (!mCourse.alertInfo.isEndAlertDayOf()) {
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
}