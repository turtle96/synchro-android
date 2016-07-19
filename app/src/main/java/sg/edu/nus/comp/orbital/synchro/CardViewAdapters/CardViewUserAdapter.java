package sg.edu.nus.comp.orbital.synchro.CardViewAdapters;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.DataHolders.User;
import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.ViewUserFragment;

/**
 * Created by angja_000 on 13/6/2016.
 *
 * adaptor for card views that display username and profile image (TextDrawable for now)
 * data set as User objects
 * onClick redirects to respective user profiles
 *
 */
public class CardViewUserAdapter extends RecyclerView.Adapter<CardViewUserAdapter.CardViewHolder> {

    private ArrayList<User> list;
    private FragmentManager fragmentManager;

    public CardViewUserAdapter(ArrayList<User> list, FragmentManager manager) {
        this.list  = list;
        this.fragmentManager = manager;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_member_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        User member = list.get(position);

        holder.image.setImageDrawable(member.getProfileImage());
        holder.textUserName.setText(member.getName());
        holder.memberName = member.getName();
        holder.userId = member.getId();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder {

        private static final String GET_USER_ID = "User Id";

        private String userId = "default id";
        private ImageView image;
        private TextView textUserName;
        private View view;
        private String memberName;  //for toast only, delete if toast not used

        public CardViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.profileImage);
            textUserName = (TextView) itemView.findViewById(R.id.valueMemberName);
            view = itemView;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(App.getContext(), "clicked " + memberName, Toast.LENGTH_LONG).show();
                    if (fragmentManager != null) {
                        // add all fragment into backstack
                        FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out);

                        ViewUserFragment viewUserFragment = ViewUserFragment.newInstance();

                        if (userId.equals("default id")) {
                            System.out.println("Error retrieving user id");
                        }
                        else {
                            Bundle bundle = new Bundle();
                            bundle.putString(GET_USER_ID, userId);
                            viewUserFragment.setArguments(bundle);
                        }

                        transaction.replace(R.id.content_fragment, viewUserFragment);
                        transaction.commit();
                    }
                }
            });
        }
    }
}
