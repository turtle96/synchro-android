package sg.edu.nus.comp.orbital.synchro;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment {

    //TOdo should extract all constants for key into external class

    private static final int DESC_MAX_LENGTH = 1000;
    private static final int NAME_MAX_LENGTH = 50;
    private static final String[] GROUP_TYPES_LIST = {"Study", "Project", "Misc"};
    private static final String GET_GROUP_ID = "Group Id";

    private static EditText editTextDate;
    private static EditText editTextTime;

    private static String dateYear, dateMonth, dateDay;
    private static int timeHour, timeMinute;

    public CreateGroupFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     */
    public static CreateGroupFragment newInstance() {
        CreateGroupFragment fragment = new CreateGroupFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_group, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // group type spinner
        final Spinner spinnerGroupType = (Spinner) view.findViewById(R.id.spinnerGroupType);
        ArrayAdapter<String> adapterGroupType = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, GROUP_TYPES_LIST);
        spinnerGroupType.setAdapter(adapterGroupType);

        //name error handling, max 50 characters
        final TextInputLayout layoutName = (TextInputLayout) view.findViewById(R.id.input_layout_group_name);
        layoutName.setErrorEnabled(true);
        final EditText editTextName = (EditText) view.findViewById(R.id.inputGroupName);

        editTextName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > NAME_MAX_LENGTH) {
                    layoutName.setError("Exceeded max character limit of " + NAME_MAX_LENGTH);
                }
                else {
                    layoutName.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // description error handling, max 1000 chars
        final TextInputLayout layoutDesc = (TextInputLayout) view.findViewById(R.id.input_layout_group_description);
        layoutDesc.setErrorEnabled(true);
        final EditText editTextDesc = (EditText) view.findViewById(R.id.inputGroupDesc);

        editTextDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > DESC_MAX_LENGTH) {
                    layoutDesc.setError("Exceeded max character limit of " + DESC_MAX_LENGTH);
                }
                else {
                    layoutDesc.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // date and time selectors
        editTextDate = (EditText) view.findViewById(R.id.inputGroupDate);
        editTextTime = (EditText) view.findViewById(R.id.inputGroupTime);

        ImageButton buttonCalendar = (ImageButton) view.findViewById(R.id.calendar_button);
        ImageButton buttonTime = (ImageButton) view.findViewById(R.id.time_button);
        Button buttonCreate = (Button) view.findViewById(R.id.create_group_button);

        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {showDatePickerDialog(view);
            }
        });
        buttonTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePickerDialog(view);
            }
        });

        final EditText editTextVenue = (EditText) view.findViewById(R.id.inputGroupVenue);
        final EditText editTextTags = (EditText) view.findViewById(R.id.inputGroupTags);

        // create button handling
        // field validation: basic only, checks all fields are filled
        // post group + join group
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String groupName, groupType, groupDesc, groupDate, groupTime, groupVenue, groupTags;
                groupName = editTextName.getText().toString().trim();
                groupType = spinnerGroupType.getSelectedItem().toString().trim();
                groupDesc = editTextDesc.getText().toString().trim();
                groupDate = editTextDate.getText().toString().trim();
                groupTime = editTextTime.getText().toString().trim();
                groupVenue = editTextVenue.getText().toString().trim();
                groupTags = editTextTags.getText().toString().trim();

                String[] fields = {groupName, groupType, groupDesc, groupDate, groupTime, groupVenue};

                //todo: will probably need some validation for groupname to ensure no duplicate names
                if (!checkFields(fields)) {
                    Snackbar checkFields = Snackbar.make(view, "Please make sure all fields are filled in :D",
                            Snackbar.LENGTH_LONG);
                    checkFields.setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    checkFields.show();
                    return;     //ensures code does not proceed to creating group
                }

                GroupData newGroupData = new GroupData(null, groupName, groupType, groupDesc, dateYear,
                        dateMonth, dateDay, timeHour, timeMinute, groupVenue, groupTags, null);

                String groupId = SynchroAPI.getInstance().postNewGroup(newGroupData);

                if (groupId.equals("default id")) {
                    System.out.println("you shall not pass");
                    return;     //user needs to try again
                }

                newGroupData.setId(groupId);    //impt to set the group id after server returns
                SynchroAPI.getInstance().postJoinGroup(groupId);

                // redirects to new group page
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out,
                        R.anim.fragment_fade_in, R.anim.fragment_fade_out);

                ViewGroupFragment viewGroupFragment = ViewGroupFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putString(GET_GROUP_ID, groupId);
                viewGroupFragment.setArguments(bundle);

                transaction.replace(R.id.content_fragment, viewGroupFragment);
                transaction.commit();
            }
        });

    }

    //checks all the given strings in array for empty fields, will reject space characters only strings
    //meaning if a field just has space without other characters will reject
    //note this does not check Group Tags, user can leave tags blank
    private boolean checkFields(String[] fields) {
        for (String f: fields) {
            if (f.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    //calendar dialog
    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //time dialog
    private void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    /////////// Date Picker Fragment //////////////

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        public DatePickerFragment() {}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month += 1;
            dateYear = Integer.toString(year);
            dateMonth = Integer.toString(month);
            dateDay = Integer.toString(day);

            editTextDate.setText(GroupData.formatDate(dateYear, dateMonth, dateDay));
        }
    }

    /////////// Time Picker Fragment ///////////////

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        public TimePickerFragment() {}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeHour = hourOfDay;
            timeMinute = minute;

            editTextTime.setText(GroupData.formatTime(hourOfDay, minute));
        }
    }

}
