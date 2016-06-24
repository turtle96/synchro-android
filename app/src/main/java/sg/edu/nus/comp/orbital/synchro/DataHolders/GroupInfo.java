package sg.edu.nus.comp.orbital.synchro.DataHolders;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.App;
import sg.edu.nus.comp.orbital.synchro.R;

/**
 * Created by angja_000 on 20/6/2016.
 *
 * contains details of group to be displayed on card views
 * also creates a image with the first letter of each group's name + random colour using TextDrawable
 * first 50 characters of description string is taken
 */
public class GroupInfo {
    private String name, description;
    private TextDrawable image;

    public GroupInfo(String name, String description) {
        this.name = name.replaceAll("\"", "");
        this.description = description.substring(0, 51) + "...";

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        this.image = TextDrawable.builder()
                .buildRound(this.name.substring(0, 1), color);
    }

    public String getName() {return name;}
    public String getDescription() {return description;}
    public TextDrawable getImage() {return image;}

    /*
        static
        takes in JsonArray of group details called from server and parses to GroupInfo objects
        automatically adds in default placeholder string for descriptions
        returns ArrayList
    */
    public static ArrayList<GroupInfo> parseGroupInfo(JsonArray groupsJsonArray) {
        ArrayList<GroupInfo> groups = new ArrayList<>();

        for (int i=0; i<groupsJsonArray.size(); i++) {
            JsonObject object = groupsJsonArray.get(i).getAsJsonObject();
            groups.add(new GroupInfo(object.get("name").toString(),
                    App.getContext().getResources().getString(R.string.small_text)));
        }

        return groups;
    }

    /*  filter version: only returns list of all group names containing given string
        static
        takes in JsonArray of group details called from server and parses to GroupInfo objects
        automatically adds in default placeholder string for descriptions
        returns ArrayList
    */
    public static ArrayList<GroupInfo> parseAndFilterGroupInfo(JsonArray groupsJsonArray, String filterStr) {
        ArrayList<GroupInfo> groups = new ArrayList<>();

        for (int i=0; i<groupsJsonArray.size(); i++) {
            JsonObject object = groupsJsonArray.get(i).getAsJsonObject();
            if (object.get("name").toString().contains(filterStr)) {
                groups.add(new GroupInfo(object.get("name").toString(),
                        App.getContext().getResources().getString(R.string.small_text)));
            }
        }

        return groups;
    }
}
