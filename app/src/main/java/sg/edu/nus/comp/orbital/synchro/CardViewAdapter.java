package sg.edu.nus.comp.orbital.synchro;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by angja_000 on 9/6/2016.
 */
public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder>{

    private ArrayList<String> list;
    private Context context;

    public CardViewAdapter(ArrayList<String> list, Context context) {
        this.list  = list;
        this.context = context;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view, context);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.image.setImageResource(R.drawable.balloons);
        holder.text.setText(list.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView text;
        private View view;
        private final Context finalContext;

        public CardViewHolder(View itemView, Context context) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.groupPic);
            text = (TextView) itemView.findViewById(R.id.valueGroupName);
            view = itemView;
            finalContext = context;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(finalContext, "clicked", Toast.LENGTH_LONG).show();
                }
            });
        }

    }
}
