package tyhjis.seasonapp;

/**
 * Created by khansson on 16/02/17.
 */

public class Vegetable implements APIObject {
    private int id;
    private String name;

    public Vegetable(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
