package sg.edu.nus.comp.orbital.synchro.DataHolders;

import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
 */
public class GroupData {
    private String id, name, type, description, descriptionShort, venue;
    private String dateYear, dateMonth, dateDay;    //this is for the date
    private int timeHour, timeMinute;
    private String tagsStr;             //for create group (app --> server)
    private ArrayList<String> tagsArr;  //for display group (server --> app)
    private TextDrawable image;         //if group has no image
    private boolean isAdmin;

    //parameters: id, name, type, description, dateYear, dateMonth, dateDay, timeHr, timeMinute, venue
    //tags can be sent in as string or array, put in null if not applicable
    //be careful not to mess up ORDER!
    public GroupData(String id, String name, String type, String desc, String dateYear, String dateMonth,
                     String dateDay, int timeHour, int timeMinute, String venue,
                     String tagsStr, ArrayList<String> tagsArr, boolean isAdmin) {
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

        if (dateYear==null && dateMonth==null && dateDay==null) {
            this.dateYear = "0000";
            this.dateMonth = "00";
            this.dateDay = "00";
        }
        else {
            this.dateYear = dateYear;
            this.dateMonth = dateMonth;
            this.dateDay = dateDay;
        }

        this.timeHour = timeHour;
        this.timeMinute = timeMinute;
        this.venue = venue;
        this.tagsStr = tagsStr;
        this.tagsArr = tagsArr;
        this.isAdmin = isAdmin;

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
    public String getTime() {return formatTime(timeHour, timeMinute);}
    public String getTimeServerFormat() {return formatTimeServer(timeHour, timeMinute);}
    public String getVenue() {return venue;}
    public ArrayList<String> getTagsArr() {return tagsArr;}
    public TextDrawable getImage() {return image;}
    public boolean isAdmin() {return isAdmin;}

    ////////// Setters /////////////

    ///////// Formatters ///////////

    // dd/mm/yyyy
    public static String formatDate(String year, String month, String day) {
        return day + "/" + month + "/" + year;
    }

    // yyyy-mm-dd
    public static String formatDateServer(String year, String month, String day) {
        return year + "-" + month + "-" + day;
    }

    //static method
    //needs params to get formatted string with 12 hr format + AM/PM
    //hh:mm AM/PM
    public static String formatTime(int hourOfDay, int minute) {
        String time, minuteStr;

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

        return time;
    }

    //server time format hh:mm:ss
    public static String formatTimeServer(int hourOfDay, int minute) {
        return hourOfDay + ":" + minute + ":00";
    }

    //////////// Parse Methods////////////

    //converts JsonObject of group details to GroupData object and returns
    public static GroupData parseSingleGroup(JsonObject group) {
        if (group == null) {
            System.out.println("Null group json");
            return null;
        }

        String type, description, dateYear, dateMonth, dateDay, venue;
        int hourOfDay, minute;
        ArrayList<String> tagsArr = null;
        boolean isAdmin = false;

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

        tokens = serverTime.split(":");
        hourOfDay = Integer.valueOf(tokens[0]);
        minute = Integer.valueOf(tokens[1]);

        if (group.has("tags")) {
            tagsArr = parseTags(group.get("tags").getAsJsonArray());
        }
        if (group.has("is_member") && group.get("is_member").getAsBoolean()) {
            isAdmin = true;
        }

        return new GroupData(group.get("id").getAsString(), group.get("name").getAsString(), type,
                description, dateYear, dateMonth, dateDay, hourOfDay, minute, venue, null, tagsArr, isAdmin);
    }

    // static method
    // takes in JsonArray of tags (should be from server) and parses to ArrayList of strings for display
    public static ArrayList<String> parseTags(JsonArray tagsJsonArray) {
        ArrayList<String> tagsArr = new ArrayList<>();
        JsonElement element;
        JsonObject object;

        for (int i=0; i<tagsJsonArray.size(); i++) {
            if (tagsJsonArray.get(i).isJsonPrimitive()) {
                element = tagsJsonArray.get(i);
                tagsArr.add(element.getAsString());
            }
            else if (tagsJsonArray.get(i).isJsonObject()) {
                object = tagsJsonArray.get(i).getAsJsonObject();
                tagsArr.add(object.get("name").getAsString());
            }

        }

        return tagsArr;
    }

    /*
        static
        takes in JsonArray of group details called from server and parses to GroupData objects
        returns ArrayList
    */
    public static ArrayList<GroupData> parseGroups(JsonArray groupsJsonArray) {
        if (groupsJsonArray == null) {
            System.out.println("Null group json array");
            return null;
        }

        ArrayList<GroupData> groupDatas = new ArrayList<>();
        JsonObject object;

        for (int i=0; i<groupsJsonArray.size(); i++) {
            object = groupsJsonArray.get(i).getAsJsonObject();
            groupDatas.add(parseSingleGroup(object));
        }

        return groupDatas;
    }

    /*  temporary for recommendations preview only
        takes in JsonArray of group details called from server and parses to GroupData objects
        returns ArrayList
    */
    public static ArrayList<GroupData> parseGroupsByModuleCount(JsonArray groupsJsonArray) {
        ArrayList<GroupData> groupDatas = new ArrayList<>();
        JsonObject object;
        for (int i=0; i<groupsJsonArray.size(); i++) {
            object = groupsJsonArray.get(i).getAsJsonObject();
            if (object.get("matching_modules_count").getAsInt() > 0) {
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
        groupJson.addProperty("date_happening", getDateServerFormat() + " " + getTimeServerFormat());
        groupJson.addProperty("venue", getVenue());
        groupJson.addProperty("tags", tagsStr);

        return groupJson;
    }
}
