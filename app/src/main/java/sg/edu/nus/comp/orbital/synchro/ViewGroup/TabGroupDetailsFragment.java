package sg.edu.nus.comp.orbital.synchro.ViewGroup;

import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.R;
import sg.edu.nus.comp.orbital.synchro.ViewGroupFragment;

/**
 * Created by angja_000 on 12/6/2016.
 *
 * Tab fragment for view groupData display
 * Displays all groupData details
 */
public class TabGroupDetailsFragment extends Fragment{

    private GroupData groupData;

    public TabGroupDetailsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_group_details_tab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewGroupFragment viewGroupFragment = (ViewGroupFragment) getParentFragment();
        groupData = viewGroupFragment.getGroupData();

        TextView groupType = (TextView) view.findViewById(R.id.valueGroupType);
        TextView groupDesc = (TextView) view.findViewById(R.id.valueDescription);
        TextView date = (TextView) view.findViewById(R.id.valueDate);
        TextView time = (TextView) view.findViewById(R.id.valueTime);
        TextView venue = (TextView) view.findViewById(R.id.valueVenue);

        groupType.setText(groupData.getType());
        groupDesc.setText(groupData.getDescription());
        date.setText(groupData.getDate());
        time.setText(groupData.getTime());
        venue.setText(groupData.getVenue());

        if (groupData.getTagsArr() != null) {
            ArrayList<String> tagsArr = groupData.getTagsArr();
            TextView tagsTextView = (TextView) view.findViewById(R.id.valueTags);
            String htmlStr, hexColor;
            ColorGenerator generator = ColorGenerator.MATERIAL;
            int color;
            for (String tag: tagsArr) {
                color = generator.getRandomColor();
                hexColor = "#" + Integer.toHexString(color).substring(2);
                htmlStr = "<font color='" + hexColor + "'>" + tag + " " +"</font>";
                tagsTextView.append(Html.fromHtml(htmlStr));
            }
        }

        if (groupData.getDateServerFormat().equals("0000-00-00")) {
            view.findViewById(R.id.labelDate).setVisibility(View.GONE);
            view.findViewById(R.id.valueDate).setVisibility(View.GONE);
            view.findViewById(R.id.labelTime).setVisibility(View.GONE);
            view.findViewById(R.id.valueTime).setVisibility(View.GONE);
            view.findViewById(R.id.labelVenue).setVisibility(View.GONE);
            view.findViewById(R.id.valueVenue).setVisibility(View.GONE);
        }
    }

}
