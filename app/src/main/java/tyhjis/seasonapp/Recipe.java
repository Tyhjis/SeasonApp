package tyhjis.seasonapp;

/**
 * Created by khansson on 16/02/17.
 */

public class Recipe implements APIObject {

    private int id;
    private String name;

    public Recipe(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getId() {
        return this.id;
    }
}
