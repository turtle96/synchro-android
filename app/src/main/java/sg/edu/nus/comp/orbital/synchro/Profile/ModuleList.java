package sg.edu.nus.comp.orbital.synchro.Profile;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by angja_000 on 10/6/2016.
 *
 * to contain a list of modules for each year, categorized by sem 1 & 2
 * organizes modules by year and sem
 */
public class ModuleList {

    private String listSem1, listSem2, year;

    //initializes both lists
    //will replace "" marks
    public ModuleList(String year) {
        this.year = "Year " + year.replaceAll("\"", "");
        listSem1 = "";
        listSem2 = "";
    }

    public String getListSem1() {return listSem1;}
    public String getListSem2() {return listSem2;}
    public String getYear() {return year;}


    //send in additional module names to concat to existing list of SEM 1
    //will replace "" marks
    public void addToListSem1(String str) {
        listSem1 = listSem1 + str.replaceAll("\"", "");
    }

    //send in additional module names to concat to existing list of SEM 2
    //will replace "" marks
    public void addToListSem2(String str) {
        listSem2 = listSem2 + str.replaceAll("\"", "");
    }

    //parses JsonArray of module info into ArrayList of ModuleList objects for display
    public static ArrayList<ModuleList> parseModules(JsonArray modules) {
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

}
