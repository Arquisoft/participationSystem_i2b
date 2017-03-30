package asw.i2b.controller;


import asw.i2b.dao.ProposalsRepository;
import asw.i2b.model.Message;
import asw.i2b.model.ProposalCreation;
import asw.i2b.producers.KafkaProducer;
import asw.i2b.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private ProposalsRepository repository;

    @RequestMapping("/")
    public ModelAndView landing(Model model) {
        return new ModelAndView("redirect:" + "/user/home");
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }
    
    @RequestMapping("/send")
    public String send(Model model, @ModelAttribute Message message) {
        kafkaProducer.send("exampleTopic", message.getMessage());
        return "redirect:/";
    }

    @RequestMapping("/user/home")
    public String send(Model model) {
        model.addAttribute("proposals", proposalService.getProposalsByPopularity());
        model.addAttribute("createProposal", new ProposalCreation());
        return "user/home";
    }

    @RequestMapping(value = "/user/createProposal", method = RequestMethod.POST)
    public String createProposal(Model model, @ModelAttribute ProposalCreation pC){
        // TODO: leo pC y creo proposal
        return "redirect:/user/home";
    }

    @RequestMapping("/user/proposal/{id}")
    public String proposal(Model model, @PathVariable("id") String id) {
        System.out.println(id);
        return "user/proposal";
    }

}