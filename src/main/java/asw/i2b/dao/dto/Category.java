package asw.i2b.dao.dto;

import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Pineirin on 30/03/2017.
 */
@Document(collection = "categories")
public class Category {

    private String name;
    private int minimalSupport;
    private List<String> invalidWords;

    public Category(){

    }

    public Category(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMinimalSupport() {
        return minimalSupport;
    }

    public void setMinimalSupport(int minimalSupport) {
        this.minimalSupport = minimalSupport;
    }

    public List<String> getInvalidWords() {
        return invalidWords;
    }

    public void setInvalidWords(List<String> invalidWords) {
        this.invalidWords = invalidWords;
    }
}
