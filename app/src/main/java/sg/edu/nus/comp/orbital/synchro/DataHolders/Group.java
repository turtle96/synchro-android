package sg.edu.nus.comp.orbital.synchro.DataHolders;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.App;
import sg.edu.nus.comp.orbital.synchro.R;

/**
 * Created by angja_000 on 20/6/2016.
 *
 * contains details of group to be displayed on card views and display
 * also creates a image with the first letter of each group's name + random colour using TextDrawable
 * descriptionShort contains first 50 characters of description for display on cardviews
 *
 * TODO: to consider, if name can be changed, hook each group object to a group id??? or isit already hooked to id
 * TODO: should have list of members as well, User objects
 */
public class Group implements Serializable {
    private String name, type, description, descriptionShort, date, time, venue;
    private TextDrawable image;

    //parameters: name, type, description, date, time, venue
    //be careful not to mess up ORDER!
    public Group(String name, String type, String desc, String date, String time, String venue) {
        this.name = name.replaceAll("\"", "");
        this.type = type;
        this.description = desc.replaceAll("\"", "");

        if (description.length() >= 50) {
            this.descriptionShort = description.substring(0, 49) + "...";
        }
        else {
            this.descriptionShort = description;
        }

        this.date = date;
        this.time = time;
        this.venue = venue;

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        this.image = TextDrawable.builder()
                .buildRound(this.name.substring(0, 1), color);
    }

    ////////// Getters ////////////////
    public String getName() {return name;}
    public String getType() {return type;}
    public String getDescription() {return description;}
    public String getDescriptionShort() {return descriptionShort;}
    public String getDate() {return date;}
    public String getTime() {return time;}
    public String getVenue() {return venue;}
    public TextDrawable getImage() {return image;}

    //////////// Parse Methods////////////
    /*
        static
        takes in JsonArray of group details called from server and parses to Group objects
        automatically adds in default placeholder string for descriptions
        returns ArrayList
    */
    public static ArrayList<Group> parseGroups(JsonArray groupsJsonArray) {
        ArrayList<Group> groups = new ArrayList<>();

        for (int i=0; i<groupsJsonArray.size(); i++) {
            JsonObject object = groupsJsonArray.get(i).getAsJsonObject();
            groups.add(new Group(object.get("name").toString(), "default type",
                    App.getContext().getResources().getString(R.string.medium_text),
                    "default date", "default time", "default venue"));
        }

        return groups;
    }

    /*  filter version: only returns list of all group names containing given string
        static
        takes in JsonArray of group details called from server and parses to Group objects
        automatically adds in default placeholder string for descriptions
        returns ArrayList
    */
    public static ArrayList<Group> parseAndFilterGroups(JsonArray groupsJsonArray, String filterStr) {
        ArrayList<Group> groups = new ArrayList<>();

        for (int i=0; i<groupsJsonArray.size(); i++) {
            JsonObject object = groupsJsonArray.get(i).getAsJsonObject();
            if (object.get("name").toString().contains(filterStr)) {
                groups.add(new Group(object.get("name").toString(), "default type",
                        App.getContext().getResources().getString(R.string.medium_text),
                        "default date", "default time", "default venue"));
            }
        }

        return groups;
    }
}
