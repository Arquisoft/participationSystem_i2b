package asw.i2b.model;

import asw.i2b.producers.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class ProposalRestrictions {

    @Autowired
    private KafkaProducer kafkaProducer;
    private List<String> categories;
    private Date activeDate;
    private List<String> notAllowedWords;
    private static ProposalRestrictions instance = null;

    public ProposalRestrictions(List<String> categories, Date activeDate, List<String> notAllowedWords) {
        this.categories = categories;
        this.activeDate = activeDate;
        this.notAllowedWords = notAllowedWords;
    }

    public static ProposalRestrictions getInstance(List<String> categories, Date activeDate, List<String> notAllowedWords) {
        if(instance == null) {
            instance = new ProposalRestrictions(categories, activeDate, notAllowedWords);
        }
        return instance;
    }

    public static ProposalRestrictions getInstance() {
        if(instance == null) {
            instance = new ProposalRestrictions(new ArrayList<>(), new Date(), new ArrayList<>());
        }
        return instance;
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

    @RequestMapping("/")
    public String landing(Model model) {
        model.addAttribute("message", new Message());
        return "index";
    }

    @RequestMapping("/send")
    public String send(Model model, @ModelAttribute Message message) {
        kafkaProducer.send("exampleTopic", message.getMessage());
        return "redirect:/";
    }
}
