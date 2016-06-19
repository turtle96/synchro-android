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

    public static void loadProfileData() {
        profile = SynchroAPI.getInstance().getMe();
        modulesJsonArray = SynchroAPI.getInstance().getMeModules();
        moduleLists = ModuleList.parseModules(modulesJsonArray);
    }

    public static void loadGroupsJoinedData() {
        groupsJsonArray = SynchroAPI.getInstance().getUserGroupsById(1);
    }

    public static JsonArray loadViewGroupData(int id) {
        return SynchroAPI.getInstance().getGroupUsersById(id);
    }

    public static JsonObject getProfile() {return profile;}
    public static JsonArray getModulesJsonArray() {return modulesJsonArray;}
    public static ArrayList<ModuleList> getModuleLists() {return moduleLists;}

    public static JsonArray getGroupsJsonArray() {return groupsJsonArray;}

}
