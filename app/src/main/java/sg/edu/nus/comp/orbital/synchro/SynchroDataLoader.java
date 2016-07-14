package sg.edu.nus.comp.orbital.synchro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupData;
import sg.edu.nus.comp.orbital.synchro.DataHolders.ModuleList;
import sg.edu.nus.comp.orbital.synchro.DataHolders.User;

/**
 * Created by angja_000 on 17/6/2016.
 *
 * Handler class to load data from SynchroAPI and store for ease of access by fragments
 * stored for duration of app usage
 * data that will be used frequently and unlikely to change for duration of app
 * NOTE: if user joins group might need to refresh the groups joined page (not tested yet)
 */
public class SynchroDataLoader {
    private static JsonObject profileJson;
    private static User userProfile;

    private static JsonArray modulesJsonArray;
    private static ArrayList<ModuleList> moduleLists;

    private static JsonArray groupsJsonArray;
    private static ArrayList<GroupData> groupDatas;

    ////////// Getters //////////////////////

    public static JsonObject getProfileJson() {return profileJson;}
    public static User getUserProfile() {return userProfile;}

    public static JsonArray getModulesJsonArray() {return modulesJsonArray;}
    public static ArrayList<ModuleList> getModuleLists() {return moduleLists;}

    public static JsonArray getGroupsJsonArray() {return groupsJsonArray;}
    public static ArrayList<GroupData> getGroupDatas() {return groupDatas;}

    //////////////////////////////////////////

    /****** Load Methods *******/

    //loads and stores profile data
    //user details
    public static void loadProfileData() {
        profileJson = SynchroAPI.getInstance().getMe();
        userProfile = User.parseSingleUser(profileJson);
    }

    //loads and stores modules
    //modules are parsed to ModuleLists
    public static void loadModules() {
        modulesJsonArray = SynchroAPI.getInstance().getMeModules();
        moduleLists = ModuleList.parseModules(modulesJsonArray);
    }

    //loads details of the groups user has joined
    //group details parsed to GroupData
    public static void loadGroupsJoinedData() {
        groupsJsonArray = SynchroAPI.getInstance().getMeGroups();
        groupDatas = GroupData.parseGroups(groupsJsonArray);
    }

}
