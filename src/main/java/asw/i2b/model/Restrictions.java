package asw.i2b.model;

import asw.i2b.dao.dto.InvalidWord;
import asw.i2b.producers.KafkaProducer;
import asw.i2b.service.InvalidWordsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class Restrictions {

    @Autowired
    private InvalidWordsService invalidWordsService;

    private Date activeDate;
    private List<String> notAllowedWords;
    private static Restrictions instance = null;

    public Restrictions(Date activeDate) {
        this.activeDate = activeDate;
        this.notAllowedWords = invalidWordsService.getAllInvalidWords().stream().map(InvalidWord::getWord).collect(Collectors.toList());
    }

    public static Restrictions getInstance(List<String> categories, Date activeDate) {
        if(instance == null) {
            instance = new Restrictions(activeDate);
        }
        return instance;
    }

    public static Restrictions getInstance() {
        if(instance == null) {
            instance = new Restrictions(new Date());
        }
        return instance;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public void setNotAllowedWords(List<String> notAllowedWords) {
        this.notAllowedWords = notAllowedWords;
    }


    public Date getActiveDate() {
        return activeDate;
    }

    public List<String> getNotAllowedWords() {
        return notAllowedWords;
    }
}
