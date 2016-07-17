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
 *
 * NOTE: for future development will be good to add in variables for special terms
 */
public class ModuleList {

    private String listSem1, listSem2, year;

    //initializes both lists
    public ModuleList(String year) {
        this.year = "Year " + year;
        listSem1 = "";
        listSem2 = "";
    }

    ///// Getters ////////////////////
    public String getListSem1() {return listSem1;}
    public String getListSem2() {return listSem2;}
    public String getYear() {return year;}

    /****** Methods to Add to Lists *******/

    //send in additional module names to concat to existing list of SEM 1
    private void addToListSem1(String str) {
        if (listSem1.equals("")) {
            listSem1 = listSem1 + str;
        }
        else {
            listSem1 = listSem1 + "\n" + str;
        }

    }

    //send in additional module names to concat to existing list of SEM 2
    private void addToListSem2(String str) {

        if (listSem2.equals("")) {
            listSem2 = listSem2 + str;
        }
        else {
            listSem2 = listSem2 + "\n" + str;
        }
    }

    //////////////////////////////////////

    //static method
    //parses JsonArray of module info into ArrayList of ModuleList objects for display
    public static ArrayList<ModuleList> parseModules(JsonArray modulesJsonArray) {
        if (modulesJsonArray == null) {
            System.out.println("Null modules json");
            return null;
        }

        ArrayList<ModuleList> moduleLists = new ArrayList<>();

        int yearCounter = 0;    //index counter for modulesByYear
        String yearTracker = modulesJsonArray.get(0).getAsJsonObject().get("year_taken").getAsString();
        String semTracker = modulesJsonArray.get(0).getAsJsonObject().get("semester_taken").getAsString();
        moduleLists.add(new ModuleList(yearTracker));   //initialize

        for (int i = 0; i< modulesJsonArray.size(); i++) {
            JsonObject object = modulesJsonArray.get(i).getAsJsonObject();

            if (!object.get("year_taken").getAsString().equals(yearTracker)) {
                yearTracker = object.get("year_taken").getAsString();
                yearCounter++;
                moduleLists.add(new ModuleList(yearTracker));   //initialize
            }
            if (!object.get("semester_taken").getAsString().equals(semTracker)) {
                semTracker = object.get("semester_taken").getAsString();
            }

            JsonObject moduleObj = object.getAsJsonObject("module");

            //sort info into correct sem
            //each sem string contains the entire module list
            if (semTracker.equals("1")) {
                moduleLists.get(yearCounter).addToListSem1(moduleObj.get("module_code").getAsString()
                        + ": " + moduleObj.get("module_title").getAsString());
            }
            else if (semTracker.equals("2")) {
                moduleLists.get(yearCounter).addToListSem2(moduleObj.get("module_code").getAsString()
                        + ": " + moduleObj.get("module_title").getAsString());
            }

        }

        return moduleLists;
    }

}
