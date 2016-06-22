package sg.edu.nus.comp.orbital.synchro.ViewGroup;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.App;
import sg.edu.nus.comp.orbital.synchro.R;

/**
 * Created by angja_000 on 13/6/2016.
 *
 * adaptor for card views that display various users' names and profile image (TextDrawable for now)
 * onClick should redirect to respective user profiles (dummy onClick for now)
 */
public class CardViewUserAdapter extends RecyclerView.Adapter<CardViewUserAdapter.CardViewHolder> {

    private ArrayList<String> list;

    public CardViewUserAdapter(ArrayList<String> list) {
        this.list  = list;
    }


    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_members_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {

        String memberName = list.get(position);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(memberName.substring(0, 1), color);

        holder.image.setImageDrawable(drawable);
        holder.textUserName.setText(memberName);
        holder.memberName = memberName;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clearView() {
        list.clear();
        notifyDataSetChanged();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView textUserName;
        private View view;
        private String memberName;

        public CardViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.profileImage);
            textUserName = (TextView) itemView.findViewById(R.id.valueMemberName);
            view = itemView;

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(App.getContext(), "clicked " + memberName, Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}
