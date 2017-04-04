package asw.i2b.model;

import asw.i2b.producers.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class ProposalRestrictions {

    @Autowired
    private KafkaProducer kafkaProducer;
    private Date activeDate;
    private List<String> notAllowedWords;
    private static ProposalRestrictions instance = null;

    public ProposalRestrictions(Date activeDate, List<String> notAllowedWords) {
        this.activeDate = activeDate;
        this.notAllowedWords = notAllowedWords;
    }

    public static ProposalRestrictions getInstance(List<String> categories, Date activeDate, List<String> notAllowedWords) {
        if(instance == null) {
            instance = new ProposalRestrictions(activeDate, notAllowedWords);
        }
        return instance;
    }

    public static ProposalRestrictions getInstance() {
        if(instance == null) {
            instance = new ProposalRestrictions(new Date(), new ArrayList<>());
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
