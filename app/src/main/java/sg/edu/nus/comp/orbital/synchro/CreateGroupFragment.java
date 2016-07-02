package sg.edu.nus.comp.orbital.synchro;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
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

import sg.edu.nus.comp.orbital.synchro.DataHolders.Group;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateGroupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateGroupFragment extends Fragment {

    //TOdo should extract all constants for key into external class
    private static final String GET_GROUP_KEY = "Group Object";

    private static final int DESC_MAX_LENGTH = 1000;
    private static final String[] GROUP_TYPES_LIST = {"Study", "Project", "Misc"};

    private static EditText editTextDate;
    private static EditText editTextTime;

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
        View rootView = inflater.inflate(R.layout.fragment_create_group, container, false);

        final Spinner spinnerGroupType = (Spinner) rootView.findViewById(R.id.spinnerGroupType);
        ArrayAdapter<String> adapterGroupType = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, GROUP_TYPES_LIST);
        spinnerGroupType.setAdapter(adapterGroupType);

        final TextInputLayout layoutDesc = (TextInputLayout) rootView.findViewById(R.id.input_layout_group_description);
        layoutDesc.setErrorEnabled(true);
        final EditText editTextDesc = (EditText) rootView.findViewById(R.id.inputGroupDesc);

        editTextDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > DESC_MAX_LENGTH) {
                    layoutDesc.setError("Exceeded max character limit of 1000");
                }
                else {
                    layoutDesc.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editTextDate = (EditText) rootView.findViewById(R.id.inputGroupDate);
        editTextTime = (EditText) rootView.findViewById(R.id.inputGroupTime);

        ImageButton buttonCalendar = (ImageButton) rootView.findViewById(R.id.calendar_button);
        ImageButton buttonTime = (ImageButton) rootView.findViewById(R.id.time_button);
        Button buttonCreate = (Button) rootView.findViewById(R.id.create_group_button);

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

        final EditText editTextName = (EditText) rootView.findViewById(R.id.inputGroupName);
        final EditText editTextVenue = (EditText) rootView.findViewById(R.id.inputGroupVenue);

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "created group", Toast.LENGTH_LONG).show();

                String groupName, groupType, groupDesc, groupDate, groupTime, groupVenue;
                groupName = editTextName.getText().toString();
                groupType = spinnerGroupType.getSelectedItem().toString();
                groupDesc = editTextDesc.getText().toString();
                groupDate = editTextDate.getText().toString();
                groupTime = editTextTime.getText().toString();
                groupVenue = editTextVenue.getText().toString();

                Group newGroup = new Group(groupName, groupType, groupDesc, groupDate, groupTime, groupVenue);

                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction().addToBackStack(null);
                transaction.setCustomAnimations(R.anim.fragment_slide_in_right, R.anim.fragment_slide_out_left,
                        R.anim.fragment_slide_in_left, R.anim.fragment_slide_out_right);

                ViewGroupFragment viewGroupFragment = ViewGroupFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putSerializable(GET_GROUP_KEY, newGroup);
                viewGroupFragment.setArguments(bundle);

                transaction.replace(R.id.content_fragment, viewGroupFragment);
                transaction.commit();
            }
        });

        return rootView;
    }

    //calendar dialog
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    //time dialog
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    /////////// Date Picker Fragment //////////////

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

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
            String date = day + "/" + month + "/" + year;
            editTextDate.setText(date);
        }
    }

    /////////// Time Picker Fragment ///////////////

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

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
            String time, minuteStr;
            if (minute < 10) {
                minuteStr = "0" + minute;
            }
            else {
                minuteStr = "" + minute;
            }

            if (hourOfDay<12 && hourOfDay!=0) {
                time = hourOfDay + ":" + minuteStr + " am";
            }
            else if (hourOfDay>12){
                time = (hourOfDay - 12) + ":" + minuteStr + " pm";
            }
            else if (hourOfDay == 12) {
                time = hourOfDay + ":" + minuteStr + " pm";
            }
            else {  //24:00
                time = 12 + ":" + minuteStr + " am";
            }
            editTextTime.setText(time);
        }
    }

}
