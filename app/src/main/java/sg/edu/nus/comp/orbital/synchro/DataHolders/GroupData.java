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
 */
public class GroupData {
    private String id, name, type, description, descriptionShort, time, time24Hour, venue;
    private String dateYear, dateMonth, dateDay;    //this is for the date
    private TextDrawable image;

    //parameters: id, name, type, description, dateYear, dateMonth, dateDay, time, time24hr, venue
    //be careful not to mess up ORDER!
    public GroupData(String id, String name, String type, String desc, String dateYear, String dateMonth,
                     String dateDay, String time, String time24Hour, String venue) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.description = desc;

        if (description.length() >= 50) {
            this.descriptionShort = description.substring(0, 49) + "...";
        }
        else {
            this.descriptionShort = description;
        }

        this.dateYear = dateYear;
        this.dateMonth = dateMonth;
        this.dateDay = dateDay;
        this.time = time;
        this.time24Hour = time24Hour;
        this.venue = venue;

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        this.image = TextDrawable.builder()
                .buildRound(this.name.substring(0, 1), color);
    }

    ////////// Getters ////////////////
    public String getId() {return id;}
    public String getName() {return name;}
    public String getType() {return type;}
    public String getDescription() {return description;}
    public String getDescriptionShort() {return descriptionShort;}
    public String getDate() {return formatDate(dateYear, dateMonth, dateDay);}
    public String getDateServerFormat() {return formatDateServer(dateYear, dateMonth, dateDay);}
    public String getTime() {return time;}
    public String getTime24Hour() {return time24Hour;}
    public String getVenue() {return venue;}
    public TextDrawable getImage() {return image;}

    public static String formatDate(String year, String month, String day) {
        return day + "/" + month + "/" + year;
    }

    public static String formatDateServer(String year, String month, String day) {
        return year + "-" + month + "-" + day;
    }

    ////////// Setters /////////////
    public void setId(String id) {
        this.id = id;
    }

    //////////// Parse Methods////////////

    public static GroupData parseSingleGroup(JsonObject group) {
        String type, description, dateYear, dateMonth, dateDay, time, time24Hour, venue;

        //dummy data for server data that are blank
        type = "default type";
        description = App.getContext().getResources().getString(R.string.medium_text);
        venue = "default venue";

        if (!group.get("type").getAsString().equals("")) {
            type = group.get("type").getAsString();
        }
        if (!group.get("description").getAsString().equals("")) {
            description = group.get("description").getAsString();
        }
        if (!group.get("venue").getAsString().equals("")) {
            venue = group.get("venue").getAsString();
        }

        String[] tokens;    //tokens holder for splitting strings (will be re-used)

        tokens = group.get("date_happening").getAsString().split(" ");
        String serverDate = tokens[0];
        String serverTime = tokens[1];

        tokens = serverDate.split("-");
        dateYear = tokens[0];
        dateMonth = tokens[1];
        dateDay = tokens[2];

        time24Hour = serverTime;
        tokens = serverTime.split(":");
        int hourOfDay, minute;
        hourOfDay = Integer.valueOf(tokens[0]);
        minute = Integer.valueOf(tokens[1]);

        String minuteStr;
        if (minute < 10) {
            minuteStr = "0" + minute;
        }
        else {
            minuteStr = "" + minute;
        }

        if (hourOfDay<12 && hourOfDay!=0) {
            time = hourOfDay + ":" + minuteStr + " am";
        }
        else if (hourOfDay>12){
            time = (hourOfDay - 12) + ":" + minuteStr + " pm";
        }
        else if (hourOfDay == 12) {
            time = hourOfDay + ":" + minuteStr + " pm";
        }
        else {  //24:00
            time = 12 + ":" + minuteStr + " am";
        }

        return new GroupData(group.get("id").getAsString(), group.get("name").getAsString(), type,
                description, dateYear, dateMonth, dateDay, time, time24Hour, venue);
    }

    /*
        static
        takes in JsonArray of group details called from server and parses to GroupData objects
        automatically adds in default placeholder string for descriptions
        returns ArrayList
    */
    public static ArrayList<GroupData> parseGroups(JsonArray groupsJsonArray) {
        ArrayList<GroupData> groupDatas = new ArrayList<>();

        for (int i=0; i<groupsJsonArray.size(); i++) {
            JsonObject object = groupsJsonArray.get(i).getAsJsonObject();
            groupDatas.add(parseSingleGroup(object));
        }

        return groupDatas;
    }

    /*  temporary for preview only
        filter version: only returns list of all group names containing given string
        static
        takes in JsonArray of group details called from server and parses to GroupData objects
        automatically adds in default placeholder string for descriptions
        returns ArrayList
    */
    public static ArrayList<GroupData> parseAndFilterGroups(JsonArray groupsJsonArray, String filterStr) {
        ArrayList<GroupData> groupDatas = new ArrayList<>();

        for (int i=0; i<groupsJsonArray.size(); i++) {
            JsonObject object = groupsJsonArray.get(i).getAsJsonObject();
            if (object.get("name").toString().contains(filterStr)) {
                groupDatas.add(parseSingleGroup(object));
            }
        }

        return groupDatas;
    }

    //takes current instantiation of GroupData and parse to JsonObject body for post group call
    public JsonObject parseToPostGroupJson() {
        JsonObject groupJson = new JsonObject();

        groupJson.addProperty("name", getName());
        groupJson.addProperty("type", getType());
        groupJson.addProperty("description", getDescription());
        groupJson.addProperty("date_happening", getDateServerFormat() + " " + getTime24Hour());
        groupJson.addProperty("venue", getVenue());
        groupJson.addProperty("tags", getType());

        return groupJson;
    }
}
