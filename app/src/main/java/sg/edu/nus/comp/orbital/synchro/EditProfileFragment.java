package sg.edu.nus.comp.orbital.synchro;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sg.edu.nus.comp.orbital.synchro.AsyncTasks.AsyncTaskPutMe;
import sg.edu.nus.comp.orbital.synchro.DataHolders.User;

/**
 * Created by angja_000 on 22/7/2016.
 */
public class EditProfileFragment extends Fragment {

    private User user;

    public EditProfileFragment() {}

    public static EditProfileFragment newInstance() {
        EditProfileFragment fragment = new EditProfileFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        user = SynchroDataLoader.getUserProfile();

        if (user != null) {
            return inflater.inflate(R.layout.fragment_edit_profile, container, false);
        }
        else {
            return inflater.inflate(R.layout.error_layout, container, false);
        }
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (user == null) {
            return;
        }

        final EditText editTextDesc = (EditText) view.findViewById(R.id.inputUserDesc);
        editTextDesc.setText(user.getDesc());

        Button buttonSave = (Button) view.findViewById(R.id.save_button);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = editTextDesc.getText().toString().trim();
                if (desc.isEmpty()) {
                    Snackbar checkFields = Snackbar.make(view, "Please make sure all fields are filled in :D",
                            Snackbar.LENGTH_LONG);
                    checkFields.setAction("ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    });
                    checkFields.show();
                }
                else {
                    AsyncTaskPutMe.load(new ProgressDialog(getContext()), desc, getFragmentManager());
                }
            }
        });
    }

    public static void redirectAfterUpdate(boolean result, FragmentManager manager) {
        if (result) {
            SynchroDataLoader.loadProfileData();
            Toast.makeText(App.getContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out,
                    R.anim.fragment_fade_in, R.anim.fragment_fade_out);
            transaction.replace(R.id.content_fragment, ProfileFragment.newInstance());
            transaction.commit();
        }
        else {
            Toast.makeText(App.getContext(), "Error updating profile. Please try again.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
