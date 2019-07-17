package linc.com.alarmclockforprogrammers.ui.fragments.alarmsettings;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;
import android.support.transition.Slide;
import android.support.transition.TransitionManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import linc.com.alarmclockforprogrammers.R;
import linc.com.alarmclockforprogrammers.model.data.Alarm;
import linc.com.alarmclockforprogrammers.presentation.alarmsettings.PresenterAlarmSettings;
import linc.com.alarmclockforprogrammers.presentation.alarmsettings.ViewAlarmSettings;

public class FragmentAlarmSettings extends Fragment implements ViewAlarmSettings,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener{

    private ViewGroup container;
    private Group taskExpand;
    private View view;
    private TimePicker timePicker;
    private EditText alarmLabel;
    private SwitchCompat taskEnable;
    private SwitchCompat alarmEnable;
    private TextView dayPicker;
    private LinearLayout difficultPicker;
    private LinearLayout languagePicker;
    private LinearLayout songPicker;

    private PresenterAlarmSettings presenter;
    private Alarm alarm;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PresenterAlarmSettings(this);

        // todo get by id from room

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_alarm_settings, container, false);
        this.container = container;

        taskExpand = view.findViewById(R.id.alarm_settings__task_expand);
        alarmLabel = view.findViewById(R.id.alarm_settings__alarm_name);
        timePicker = view.findViewById(R.id.alarm_settings__timePicker);
        taskEnable = view.findViewById(R.id.alarm_settings__toggle_task_enable);
        alarmEnable = view.findViewById(R.id.alarm_settings__toggle_alarm_enable);
        dayPicker = view.findViewById(R.id.alarm_settings__days);
        difficultPicker = view.findViewById(R.id.alarm_settings_task_expand__difficult_layout);
        languagePicker = view.findViewById(R.id.alarm_settings_task_expand__language_layout);
        songPicker = view.findViewById(R.id.alarm_settings__song_layout);
        FloatingActionButton saveAlarmButton = view.findViewById(R.id.alarm_settings__save);
        FloatingActionButton cancelButton = view.findViewById(R.id.alarm_settings__cancel);

        dayPicker.setOnClickListener(this);
        difficultPicker.setOnClickListener(this);
        languagePicker.setOnClickListener(this);
        songPicker.setOnClickListener(this);
        saveAlarmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        taskEnable.setOnCheckedChangeListener(this);
        alarmEnable.setOnCheckedChangeListener(this);

        presenter.setAlarmData(this.alarm);

        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch(buttonView.getId()) {
            case R.id.alarm_settings__toggle_task_enable:
                presenter.openExpandedSettings(isChecked);
                break;
            case R.id.alarm_settings__toggle_alarm_enable:
                Toast.makeText(getContext(), "Checked: " + isChecked, Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.alarm_settings__days:
                presenter.showWeekDaysDialog();
                break;
            case R.id.alarm_settings_task_expand__difficult_layout:
                presenter.showRadioButtonDialog(getResources().getStringArray(R.array.difficult_modes));
                break;
            case R.id.alarm_settings_task_expand__language_layout:
                presenter.showRadioButtonDialog(getResources().getStringArray(R.array.programming_languages));
                break;
            case R.id.alarm_settings__song_layout:
                presenter.getSongFile();
                break;
            case R.id.alarm_settings__save:
                presenter.saveAlarm(this.alarm);
                break;
            case R.id.alarm_settings__cancel:
                presenter.closeAlarmSettings();
                break;
        }
    }

    @Override
    public void openExpandedSettings(boolean isChecked) {
        TransitionManager.beginDelayedTransition(
                container,
                new Slide(Gravity.BOTTOM)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setDuration(1000)
        );
        taskExpand.setVisibility(isChecked ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showWeekDaysDialog() {
        final String[] weekDays = getResources().getStringArray(R.array.week_days);
        final boolean[] checkedDays = { false, false, false, false, false, false, false };

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));

        dialogBuilder.setCancelable(true)
                .setMultiChoiceItems(weekDays, checkedDays,
                        (dialog, which, isChecked) -> checkedDays[which] = isChecked)
                .setPositiveButton(R.string.dialog_confirm,
                        (dialog, id) -> {
                            StringBuilder state = new StringBuilder();
                            for (int i = 0; i < weekDays.length; i++) {
                                state.append("" + weekDays[i]);
                                if (checkedDays[i])
                                    state.append(" выбран\n");
                                else
                                    state.append(" не выбран\n");
                            }
                        })

                .setNegativeButton(R.string.dialog_cancel,
                        (dialog, id) -> dialog.cancel());
        dialogBuilder.create()
                .show();
    }

    @Override
    public void showRadioButtonDialog(String[] items) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(getActivity(), R.style.AlertDialogStyle));
        dialogBuilder.setCancelable(true)
                .setPositiveButton(R.string.dialog_confirm,
                        (dialog, id) -> {})
                .setNegativeButton(R.string.dialog_cancel,
                        (dialog, id) -> dialog.cancel())
                .setSingleChoiceItems(items, -1,
                        (dialog, item) -> {});
        dialogBuilder.create()
                .show();
    }

    @Override
    public void askForReadWritePermission() {
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }

    @Override
    public void getSongFile() {
        // todo implement .mp3 file selection
    }

    @Override
    public void setAlarmData(Alarm alarm) {

//        this.timePicker;
//        this.alarmLabel;
//        this.taskEnable;
//        this.alarmEnable;
//        this.dayPicker;
//        this.difficultPicker;
//        this.languagePicker;
//        this.songPicker;

    }

    @Override
    public void closeAlarmSettings() {
        getFragmentManager().popBackStack();
    }

    /**
     *      TODO USE IT TO SAVE ALARM TIME


     if(Build.VERSION.SDK_INT < 23){
     int hour = timePicker.getCurrentHour();
     int minute = timePicker.getCurrentMinute();
     } else{
     int hour = timePicker.getHour();
     int minute = timePicker.getMinute();
     }

     */
}
