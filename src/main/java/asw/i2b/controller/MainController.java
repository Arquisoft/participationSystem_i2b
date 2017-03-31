package asw.i2b.controller;


import asw.i2b.dao.dto.Proposal;
import asw.i2b.model.Message;
import asw.i2b.model.ProposalCreation;
import asw.i2b.producers.KafkaProducer;
import asw.i2b.service.CategoryService;
import asw.i2b.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;

@Controller
public class MainController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/")
    public ModelAndView landing(Model model) {
        return new ModelAndView("redirect:" + "/user/home");
    }

    @GetMapping("/login")
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
        model.addAttribute("categories", categoryService.getAllCategories());
        return "user/home";
    }

    @PostMapping("/voteProposal/{id}")
    public String voteProposal(Model model, @PathVariable("id") String id) {
        // TODO vote proposal
        System.out.println("Vote proposal: " + id);
        return "redirect:/user/home";
    }

    @RequestMapping("/user/createProposal")
    public String createProposal(Model model, @ModelAttribute ProposalCreation createProposal){
        System.out.println("Create proposal: " + createProposal.getTitle());
        String author = SecurityContextHolder.getContext().getAuthentication().getName();
        Proposal proposal = new Proposal(createProposal.getCategory(), createProposal.getTitle(), createProposal.getBody(), 0, author, new Date());
        proposalService.createProposal(proposal);
        return "redirect:/user/home";
    }

    @RequestMapping("/user/proposal/{id}")
    public String proposal(Model model, @PathVariable("id") String id) {
        System.out.println("View proposal: " + id);
        return "user/proposal";
    }

}