package asw.i2b.dao.dto;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Created by Pineirin on 30/03/2017.
 */
@Document(collection = "categories")
public class Category {

    @Id
    private ObjectId _id;

    private String name;
    private int minimalSupport;

    public Category(){

    }

    public Category(String name, int minimalSupport){
        this.name = name;
        this.minimalSupport = minimalSupport;
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

    public ObjectId getId() {
        return _id;
    }
}
