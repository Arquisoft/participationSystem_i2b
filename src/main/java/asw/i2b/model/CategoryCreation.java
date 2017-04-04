package asw.i2b.model;

/**
 * Created by nicolas on 4/04/17 for participationSystem0.
 */
public class CategoryCreation {

    private String name;
    private int minimalSupport;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinimalSupport() {
        return minimalSupport;
    }

    public void setMinimalSupport(int minimalSupport) {
        this.minimalSupport = minimalSupport;
    }
}
