package sg.edu.nus.comp.orbital.synchro.CardViewAdapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.App;
import sg.edu.nus.comp.orbital.synchro.DataHolders.Post;
import sg.edu.nus.comp.orbital.synchro.R;

/**
 * Created by angja_000 on 19/7/2016.
 */
public class CardViewPostAdapter extends RecyclerView.Adapter<CardViewPostAdapter.CardViewHolder> {

    private ArrayList<Post> list;

    public CardViewPostAdapter(ArrayList<Post> list) {
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
        Post post = list.get(position);

        ColorGenerator generator = ColorGenerator.MATERIAL;
        String htmlStr, hexColor;
        int color = generator.getRandomColor();
        hexColor = "#" + Integer.toHexString(color).substring(2);
        htmlStr = "<font color='" + hexColor + "'>" + post.getUserName() + " " +"</font>";

        holder.textName.setText(Html.fromHtml(htmlStr));
        holder.textPost.setText(post.getMessage());

        String dateAndTime = post.getDate() + " " + post.getTime();
        holder.textDateAndTime.setText(dateAndTime);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    protected class CardViewHolder extends RecyclerView.ViewHolder {
        private TextView textName, textPost, textDateAndTime;

        public CardViewHolder(View itemView) {
            super(itemView);

            textName = (TextView) itemView.findViewById(R.id.valueUserName);
            textPost = (TextView) itemView.findViewById(R.id.valueUserPost);
            textDateAndTime = (TextView) itemView.findViewById(R.id.valueDateAndTime);

            if (android.os.Build.VERSION.SDK_INT < 18) {
                textDateAndTime.setPadding(0, 0, 0, 0);
            }
        }
    }
}
