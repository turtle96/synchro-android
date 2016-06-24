package sg.edu.nus.comp.orbital.synchro;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import sg.edu.nus.comp.orbital.synchro.DataHolders.GroupInfo;
import sg.edu.nus.comp.orbital.synchro.DataHolders.ModuleList;

/**
 * Created by angja_000 on 17/6/2016.
 *
 * Handler class to load data from SynchroAPI and store for ease of access by fragments
 * stored for duration of app usage
 */
public class SynchroDataLoader {
    private static JsonObject profile;
    private static JsonArray modulesJsonArray;
    private static ArrayList<ModuleList> moduleLists;

    private static JsonArray groupsJsonArray;
    private static ArrayList<GroupInfo> groupInfos;

    ////////// Getters //////////////////////

    public static JsonObject getProfile() {return profile;}
    public static JsonArray getModulesJsonArray() {return modulesJsonArray;}
    public static ArrayList<ModuleList> getModuleLists() {return moduleLists;}

    public static JsonArray getGroupsJsonArray() {return groupsJsonArray;}
    public static ArrayList<GroupInfo> getGroupInfos() {return groupInfos;}

    //////////////////////////////////////////

    /****** Load Methods *******/

    //loads and stores profile data
    //user details + modules
    //modules are parsed to ModuleLists
    public static void loadProfileData() {
        profile = SynchroAPI.getInstance().getMe();
        modulesJsonArray = SynchroAPI.getInstance().getMeModules();
        moduleLists = ModuleList.parseModules(modulesJsonArray);
    }

    //loads details of the groups user has joined
    //group details parsed to GroupInfo
    public static void loadGroupsJoinedData(int id) {
        groupsJsonArray = SynchroAPI.getInstance().getGroupsByUserId(id);
        groupInfos = GroupInfo.parseGroupInfo(groupsJsonArray);
    }

    //loads data of a group the user wants to view
    public static JsonArray loadViewGroupData(int id) {
        return SynchroAPI.getInstance().getUsersByGroupId(id);
    }

}
