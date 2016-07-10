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
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.App;
import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.ViewGroupFragment;

/**
 * Created by angja_000 on 9/6/2016.
 *
 * adapter for card views of groupData details
 * data set in the form of GroupData objects
 */
public class CardViewGroupAdapter extends RecyclerView.Adapter<CardViewGroupAdapter.CardViewHolder>{

    private static final String GET_GROUP_KEY = "GroupData Object";
    private ArrayList<GroupData> list;
    private FragmentManager fragmentManager;
    private GroupData groupData;

    public CardViewGroupAdapter(ArrayList<GroupData> list, FragmentManager manager, GroupData groupData) {
        this.list  = list;
        this.fragmentManager = manager;
        this.groupData = groupData;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_groups_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        GroupData groupData = list.get(position);
        String groupName = groupData.getName();
        holder.image.setImageDrawable(groupData.getImage());

        holder.textName.setText(groupName);
        holder.textDesc.setText(groupData.getDescriptionShort());
        holder.groupName = groupName;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //clears the recyclerview by clearing list of info
    public void clearView() {
        list.clear();
        notifyDataSetChanged();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView textName, textDesc;
        private View view;
        private String groupName;

        public CardViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.groupImage);
            textName = (TextView) itemView.findViewById(R.id.valueGroupName);
            textDesc = (TextView) itemView.findViewById(R.id.valueGroupDescription);
            view = itemView;

            //redirects onClick
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(App.getContext(), "clicked " + groupName, Toast.LENGTH_LONG).show();

                    if (fragmentManager != null) {
                        // add all fragment into backstack
                        FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

                        ViewGroupFragment viewGroupFragment = ViewGroupFragment.newInstance();
                        if (groupData != null) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(GET_GROUP_KEY, groupData);
                            viewGroupFragment.setArguments(bundle);
                        }

                        transaction.replace(R.id.content_fragment, viewGroupFragment);
                        transaction.commit();
                    }
                }
            });
        }

    }
}
