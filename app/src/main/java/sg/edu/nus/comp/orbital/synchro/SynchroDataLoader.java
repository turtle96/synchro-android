package sg.edu.nus.comp.orbital.synchro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.Profile.ModuleList;

/**
 * Created by angja_000 on 17/6/2016.
 *
 * Handler class to load data from SynchroAPI
 */
public class SynchroDataLoader {
    private static JsonObject profile;
    private static JsonArray modulesJsonArray;  //keep a copy in case
    private static ArrayList<ModuleList> moduleLists;

    private static JsonArray groupsJsonArray;

    private static JsonArray membersJsonArray;

    public static void loadProfileData() {
        profile = SynchroAPI.getInstance().getMe();
        modulesJsonArray = SynchroAPI.getInstance().getMeModules();
        moduleLists = parseModules(modulesJsonArray);
    }

    public static void loadGroupsJoinedData() {
        groupsJsonArray = SynchroAPI.getInstance().getUserGroupsById(1);
    }

    public static void loadViewGroupData() {
        membersJsonArray = SynchroAPI.getInstance().getGroupUsersById(5);
    }

    //parses JsonArray of module info into ModuleList objects for display
    private static ArrayList<ModuleList> parseModules(JsonArray modules) {
        ArrayList<ModuleList> lists = new ArrayList<>();

        int yearCounter = 0;    //index counter for modulesByYear
        String yearTracker = modules.get(0).getAsJsonObject().get("year_taken").toString();
        String semTracker = modules.get(0).getAsJsonObject().get("semester_taken").toString();
        lists.add(new ModuleList(yearTracker));   //initialize

        for (int i=0; i<modules.size(); i++) {
            JsonObject object = modules.get(i).getAsJsonObject();

            if (!object.get("year_taken").toString().equals(yearTracker)) {
                yearTracker = object.get("year_taken").toString();
                yearCounter++;
                lists.add(new ModuleList(yearTracker));   //initialize
            }
            if (!object.get("semester_taken").toString().equals(semTracker)) {
                semTracker = object.get("semester_taken").toString();
            }

            JsonObject moduleObj = object.getAsJsonObject("module");

            //sort info into correct sem
            //each sem string contains the entire module list
            if (semTracker.replaceAll("\"", "").equals("1")) {
                lists.get(yearCounter).addToListSem1(moduleObj.get("module_code").toString()
                        + ": " + moduleObj.get("module_title").toString() + "\n");
            }
            else {
                lists.get(yearCounter).addToListSem2(moduleObj.get("module_code").toString()
                        + ": " + moduleObj.get("module_title").toString() + "\n");
            }

        }

        return lists;
    }

    public static JsonObject getProfile() {return profile;}
    public static JsonArray getModulesJsonArray() {return modulesJsonArray;}
    public static ArrayList<ModuleList> getModuleLists() {return moduleLists;}

    public static JsonArray getGroupsJsonArray() {return groupsJsonArray;}

    public static JsonArray getMembersJsonArray() {return membersJsonArray;}
}
