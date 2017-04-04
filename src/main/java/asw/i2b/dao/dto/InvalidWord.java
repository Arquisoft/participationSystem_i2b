package asw.i2b.dao.dto;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by nicolas on 4/04/17 for participationSystem0.
 */
@Document(collection = "invalidWords")
public class InvalidWord {
    String word;

    public InvalidWord(String word){
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
