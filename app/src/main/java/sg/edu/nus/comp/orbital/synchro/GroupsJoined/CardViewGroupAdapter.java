package sg.edu.nus.comp.orbital.synchro.GroupsJoined;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
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

import sg.edu.nus.comp.orbital.synchro.GroupsJoinedFragment;
import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.ViewGroupFragment;

/**
 * Created by angja_000 on 9/6/2016.
 */
public class CardViewGroupAdapter extends RecyclerView.Adapter<CardViewGroupAdapter.CardViewHolder>{

    private ArrayList<GroupInfo> list;
    private Context context;
    private FragmentManager fragmentManager;

    public CardViewGroupAdapter(ArrayList<GroupInfo> list, Context context, FragmentManager manager) {
        this.list  = list;
        this.context = context;
        this.fragmentManager = manager;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_groups_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view, context);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        //holder.image.setImageResource(R.drawable.balloons);
        GroupInfo group = list.get(position);
        String groupName = group.getName();
        holder.image.setImageDrawable(group.getImage());

        holder.textName.setText(groupName);
        holder.textDesc.setText(group.getDescription());
        holder.groupName = groupName;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView textName, textDesc;
        private View view;
        private String groupName;
        private final Context finalContext;

        public CardViewHolder(View itemView, Context context) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.groupPic);
            textName = (TextView) itemView.findViewById(R.id.valueGroupName);
            textDesc = (TextView) itemView.findViewById(R.id.valueGroupDescription);
            view = itemView;
            finalContext = context;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(finalContext, "clicked " + groupName, Toast.LENGTH_LONG).show();

                    // add all fragment into backstack
                    FragmentTransaction transaction = fragmentManager.beginTransaction().addToBackStack(null);
                    transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                    transaction.replace(R.id.content_fragment, ViewGroupFragment.newInstance());
                    transaction.commit();
                }
            });
        }

    }
}
