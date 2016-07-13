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

    private ArrayList<GroupData> list;
    private FragmentManager fragmentManager;

    public CardViewGroupAdapter(ArrayList<GroupData> list, FragmentManager manager) {
        this.list  = list;
        this.fragmentManager = manager;
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

        holder.groupId = groupData.getId();
        holder.image.setImageDrawable(groupData.getImage());
        holder.textName.setText(groupData.getName());
        holder.textDesc.setText(groupData.getDescriptionShort());
        holder.groupName = groupData.getName();
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
        private static final String GET_GROUP_ID = "Group Id";

        private String groupId = "default id";
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

                    System.out.println(groupName + " group id: " + groupId);

                    if (fragmentManager != null) {
                        // add all fragment into backstack
                        FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
                        transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                                android.R.anim.fade_in, android.R.anim.fade_out);

                        ViewGroupFragment viewGroupFragment = ViewGroupFragment.newInstance();

                        if (groupId.equals("default id")) {
                            System.out.println("Error retrieving group id");
                        }
                        else {
                            Bundle bundle = new Bundle();
                            bundle.putString(GET_GROUP_ID, groupId);
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
