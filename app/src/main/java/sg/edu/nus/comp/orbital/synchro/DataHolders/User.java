package sg.edu.nus.comp.orbital.synchro.DataHolders;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.google.gson.JsonObject;

/**
 * Created by angja_000 on 10/7/2016.
 *
 * contains details of users for displays and card views
 * also creates a image with the first letter of user's name + random colour using TextDrawable
 * before displaying majors, use boolean flags to make sure there are majors to display
 */
public class User {

    private String id, name, faculty, firstMajor, secondMajor, matriculationYear;
    private boolean flagFirstMajor = false, flagSecondMajor = false;    //will be true if major is present
    private TextDrawable profileImage;

    public User(String id, String name, String faculty, String firstMajor, String secondMajor, String matriculationYear) {
        this.id = id;
        this.name = name;
        this.faculty = faculty;

        if (!firstMajor.equals("")) {
            flagFirstMajor = true;
            this.firstMajor = firstMajor;
        }

        if (!secondMajor.equals("")) {
            flagSecondMajor = true;
            this.secondMajor = secondMajor;
        }

        this.matriculationYear = matriculationYear;

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getRandomColor();
        this.profileImage = TextDrawable.builder()
                .buildRound(this.name.substring(0, 1), color);
    }

    ///////// Getters /////////
    public String getId() {return id;}
    public String getName() {return name;}
    public String getFaculty() {return faculty;}
    public String getFirstMajor() {return firstMajor;}
    public String getSecondMajor() {return secondMajor;}
    public String getYear() {return matriculationYear;}
    public boolean hasFirstMajor() {return flagFirstMajor;}
    public boolean hasSecondMajor() {return flagSecondMajor;}

    //given one user's details in JsonObject, parses to User and returns
    public static User parseSingleUser(JsonObject userJson) {
        return new User(userJson.get("id").getAsString(), userJson.get("name").getAsString(),
                userJson.get("faculty").getAsString(), userJson.get("first_major").getAsString(),
                userJson.get("second_major").getAsString(), userJson.get("matriculation_year").getAsString());
    }
}
