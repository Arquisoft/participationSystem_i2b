package asw.i2b.model;

import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class ProposalRestrictions {

    private List<String> categories;
    private Date activeDate;
    private List<String> notAllowedWords;

    public ProposalRestrictions(List<String> categories, Date activeDate, List<String> notAllowedWords) {
        this.categories = categories;
        this.activeDate = activeDate;
        this.notAllowedWords = notAllowedWords;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public void setActiveDate(Date activeDate) {
        this.activeDate = activeDate;
    }

    public void setNotAllowedWords(List<String> notAllowedWords) {
        this.notAllowedWords = notAllowedWords;
    }

    public List<String> getCategories() {
        return categories;
    }

    public Date getActiveDate() {
        return activeDate;
    }

    public List<String> getNotAllowedWords() {
        return notAllowedWords;
    }
}
