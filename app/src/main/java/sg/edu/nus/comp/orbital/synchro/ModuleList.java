package sg.edu.nus.comp.orbital.synchro;

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
}
