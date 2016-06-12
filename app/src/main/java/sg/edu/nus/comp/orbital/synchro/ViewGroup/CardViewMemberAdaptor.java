package sg.edu.nus.comp.orbital.synchro.ViewGroup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.R;

/**
 * Created by angja_000 on 13/6/2016.
 */
public class CardViewMemberAdaptor extends RecyclerView.Adapter<CardViewMemberAdaptor.CardViewHolder> {

    private ArrayList<String> list;
    private Context context;

    public CardViewMemberAdaptor(ArrayList<String> list, Context context) {
        this.list  = list;
        this.context = context;
    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_members_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view, context);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.balloons);
        holder.text.setText(list.get(position));
        holder.memberName = list.get(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView text;
        private View view;
        private final Context finalContext;
        private String memberName;

        public CardViewHolder(View itemView, Context context) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.profilePic);
            text = (TextView) itemView.findViewById(R.id.valueMemberName);
            view = itemView;
            finalContext = context;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(finalContext, "clicked " + memberName, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
