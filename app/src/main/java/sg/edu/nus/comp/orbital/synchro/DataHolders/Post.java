package sg.edu.nus.comp.orbital.synchro.DataHolders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by angja_000 on 21/7/2016.
 */
public class Post {

    private String groupId, userName, message, date, time;

    //note: leave username, date, time null if only sending data to server
    public Post(String groupId, String userName, String message, String date, String time) {
        this.groupId = groupId;
        this.userName = userName;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getUserName() {return userName;}
    public String getMessage() {return message;}
    public String getDate() {return date;}
    public String getTime() {return time;}

    //given a single JsonObject containing post details, parses to Post and returns it
    public static Post parseSinglePost(JsonObject postJson) {
        if (postJson == null) {
            System.out.println("Null post json");
            return null;
        }

        String[] tokens;
        String serverDate, serverTime, dateYear, dateMonth, dateDay;
        int hourOfDay, minute;

        tokens = postJson.get("created_at").getAsString().split(" ");
        serverDate = tokens[0];
        serverTime = tokens[1];

        tokens = serverDate.split("-");
        dateYear = tokens[0];
        dateMonth = tokens[1];
        dateDay = tokens[2];

        tokens = serverTime.split(":");
        hourOfDay = Integer.valueOf(tokens[0]);
        minute = Integer.valueOf(tokens[1]);

        //System.out.println(postJson.get("name").getAsString() + " " + serverTime);

        String date = GroupData.formatDate(dateYear, dateMonth, dateDay);
        String time = GroupData.formatTime(hourOfDay, minute);

        String groupId = null;

        if (postJson.has("group_id")) {
            groupId = postJson.get("group_id").getAsString();
        }

        return new Post(groupId, postJson.get("name").getAsString(),
                postJson.get("content").getAsString(), date, time);
    }

    //given JsonArray of posts, parses and returns ArrayList of Post objects
    public static ArrayList<Post> parsePosts(JsonArray postJsonArray) {
        if (postJsonArray == null) {
            System.out.println("Null posts json array");
            return null;
        }

        ArrayList<Post> posts = new ArrayList<>();
        JsonObject object;

        for (int i=0; i<postJsonArray.size(); i++) {
            object = postJsonArray.get(i).getAsJsonObject();
            posts.add(parseSinglePost(object));
        }

        return posts;
    }

    //returns JsonObject of post details for posting to server
    public JsonObject parseToPostJson() {
        JsonObject postJson = new JsonObject();

        postJson.addProperty("group_id", groupId);
        postJson.addProperty("content", message);

        return postJson;
    }

}
