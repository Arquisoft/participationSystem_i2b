package asw.i2b.dao.dto;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by Pineirin on 30/03/2017.
 */
@Document(collection = "categories")
public class Category {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
