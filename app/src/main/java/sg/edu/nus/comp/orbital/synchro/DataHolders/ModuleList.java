package sg.edu.nus.comp.orbital.synchro.DataHolders;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by angja_000 on 10/6/2016.
 *
 * organizes modules by year and sem
 * each ModuleList object contains a list of modules for each year
 * split into 2 strings, under sem 1 & 2 respectively
 * automatically removes quote marks from JsonObjects parsed
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

    ///// Getters ////////////////////
    public String getListSem1() {return listSem1;}
    public String getListSem2() {return listSem2;}
    public String getYear() {return year;}

    /****** Methods to Add to Lists *******/

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

    //////////////////////////////////////

    //static method
    //parses JsonArray of module info into ArrayList of ModuleList objects for display
    public static ArrayList<ModuleList> parseModules(JsonArray modulesJsonArray) {
        ArrayList<ModuleList> moduleLists = new ArrayList<>();

        int yearCounter = 0;    //index counter for modulesByYear
        String yearTracker = modulesJsonArray.get(0).getAsJsonObject().get("year_taken").toString();
        String semTracker = modulesJsonArray.get(0).getAsJsonObject().get("semester_taken").toString();
        moduleLists.add(new ModuleList(yearTracker));   //initialize

        for (int i = 0; i< modulesJsonArray.size(); i++) {
            JsonObject object = modulesJsonArray.get(i).getAsJsonObject();

            if (!object.get("year_taken").toString().equals(yearTracker)) {
                yearTracker = object.get("year_taken").toString();
                yearCounter++;
                moduleLists.add(new ModuleList(yearTracker));   //initialize
            }
            if (!object.get("semester_taken").toString().equals(semTracker)) {
                semTracker = object.get("semester_taken").toString();
            }

            JsonObject moduleObj = object.getAsJsonObject("module");

            //sort info into correct sem
            //each sem string contains the entire module list
            if (semTracker.replaceAll("\"", "").equals("1")) {
                moduleLists.get(yearCounter).addToListSem1(moduleObj.get("module_code").toString()
                        + ": " + moduleObj.get("module_title").toString() + "\n");
            }
            else {
                moduleLists.get(yearCounter).addToListSem2(moduleObj.get("module_code").toString()
                        + ": " + moduleObj.get("module_title").toString() + "\n");
            }

        }

        return moduleLists;
    }

}
