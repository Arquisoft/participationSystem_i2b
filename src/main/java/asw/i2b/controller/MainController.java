package asw.i2b.controller;


import asw.i2b.dao.dto.Comment;
import asw.i2b.dao.dto.Proposal;
import asw.i2b.model.CommentCreation;
import asw.i2b.model.Message;
import asw.i2b.model.ProposalCreation;
import asw.i2b.producers.KafkaProducer;
import asw.i2b.service.CategoryService;
import asw.i2b.service.CommentService;
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
    private CommentService commentService;

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

    @GetMapping("/user/home")
    public String send(Model model) {
        model.addAttribute("proposals", proposalService.getProposalsByPopularity());
        model.addAttribute("createProposal", new ProposalCreation());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "user/home";
    }

    @PostMapping("/voteProposal/{id}")
    public String voteProposal(Model model, @PathVariable("id") String id) {
        Proposal proposal = proposalService.findProposalById(id);
        if(proposal != null) {
            proposalService.vote(proposal);
        }
        return "redirect:/user/home";
    }

    @RequestMapping("/user/createProposal")
    public String createProposal(Model model, @ModelAttribute ProposalCreation createProposal){
        String author = SecurityContextHolder.getContext().getAuthentication().getName();
        Proposal proposal = new Proposal("author", createProposal.getCategory(), createProposal.getTitle(), createProposal.getBody(), 0);
        proposalService.createProposal(proposal);
        return "redirect:/user/home";
    }

    @RequestMapping("/user/createComment")
    public String createComment(Model model, @ModelAttribute CommentCreation createComment){
        System.out.println("Create comment");
        String author = SecurityContextHolder.getContext().getAuthentication().getName();
        Comment comment = new Comment(null, author, createComment.getBody());
        commentService.createCommnet(comment);
        return "redirect:/user/home";
    }

    @RequestMapping("/user/proposal/{id}")
    public String proposal(Model model, @PathVariable("id") String id) {
        System.out.println("View proposal: " + id);
        return "user/proposal";
    }

}