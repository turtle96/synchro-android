package sg.edu.nus.comp.orbital.synchro.CardViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.App;
import sg.edu.nus.comp.orbital.synchro.R;

/**
 * Created by angja_000 on 19/7/2016.
 */
public class CardViewPostAdapter extends RecyclerView.Adapter<CardViewPostAdapter.CardViewHolder> {

    private ArrayList<String> list;

    public CardViewPostAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_post_layout, parent, false);
        CardViewHolder cardViewHolder = new CardViewHolder(view);
        return cardViewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        ColorGenerator generator = ColorGenerator.MATERIAL;
        String htmlStr, hexColor;
        int color = generator.getRandomColor();
        hexColor = "#" + Integer.toHexString(color).substring(2);
        htmlStr = "<font color='" + hexColor + "'>" + list.get(position) + " " +"</font>";

        holder.textName.setText(Html.fromHtml(htmlStr));
        holder.textPost.setText(holder.message);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView textName, textPost;
        private String message = App.getContext().getResources().getString(R.string.very_small_text);

        public CardViewHolder(View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.valueUserName);
            textPost = (TextView) itemView.findViewById(R.id.valueUserPost);
        }
    }
}
