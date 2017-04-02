package asw.i2b.controller;


import asw.i2b.dao.dto.Comment;
import asw.i2b.dao.dto.Proposal;
import asw.i2b.model.CommentCreation;
import asw.i2b.model.Message;
import asw.i2b.model.ProposalCreation;
import asw.i2b.model.UserModel;
import asw.i2b.producers.KafkaProducer;
import asw.i2b.service.CategoryService;
import asw.i2b.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/")
    public ModelAndView landing(Model model) {
        return new ModelAndView("redirect:" + "/user/home");
    }

    @GetMapping("/login")   //Spring-boot automatically applies a filter on this request to /login
    public String login(Model model) {
        return "login";
    }

    @PostMapping("/send")
    public String send(Model model, @ModelAttribute Message message) {
        kafkaProducer.send("exampleTopic", message.getMessage());
        return "redirect:/";
    }

    @GetMapping("/user/home")
    public String send(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("proposals", proposalService.getProposalsByPopularity());
        model.addAttribute("createProposal", new ProposalCreation());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "user/home";
    }

    @PostMapping("/voteProposal/{id}")
    public String voteProposal(Model model, @PathVariable("id") String id) {
        Proposal proposal = proposalService.findProposalById(id);
        if (proposal != null) {
            proposalService.vote(proposal);
        }
        return "redirect:/user/home";
    }

    @PostMapping("/deleteProposal/{id}")
    public String deleteProposal(Model model, @PathVariable("id") String id) {
        Proposal proposal = proposalService.findProposalById(id);
        if (proposal != null) {
            proposalService.delete(proposal);
        }
        return "redirect:/user/home";
    }

    @PostMapping("/user/createProposal")
    public String createProposal(Model model, @ModelAttribute ProposalCreation createProposal) {
        String author = ((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        Proposal proposal = new Proposal("author", createProposal.getCategory(), createProposal.getTitle(), createProposal.getBody(), 0);
        proposalService.createProposal(proposal);
        return "redirect:/user/home";
    }

    @PostMapping("/user/voteComment/{id}")//user/voteComment/2?proposalId=58dbfd8abff2304aa268bada
    public String voteComment(Model model, @RequestParam(value = "proposalId") String proposalId, @PathVariable("id") String id) {
        String author = ((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        Proposal proposal = proposalService.findProposalById(proposalId);
        Comment comment = proposal.getComment(Long.parseLong(id));
        if (comment != null) {
            comment.vote(author);
        }
        proposalService.save(proposal);
        return "redirect:/user/home";
    }

    @PostMapping("/user/createComment/{id}")
    public String createComment(Model model, @ModelAttribute CommentCreation createComment, @PathVariable("id") String id) {
        String author = ((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        Proposal proposal = proposalService.findProposalById(id);
        Comment comment = new Comment(author, createComment.getBody());
        proposal.comment(comment);
        proposalService.save(proposal);
        return "redirect:/user/proposal/" + id;
    }

    @GetMapping("/user/proposal/{id}")
    public String proposal(Model model, @PathVariable("id") String id) {
        System.out.println("View proposal: " + id);
        Proposal selectedProposal = proposalService.findProposalById(id);
        System.out.print(selectedProposal);
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("selectedProposal", selectedProposal);
        model.addAttribute("createComment", new CommentCreation());
        return "user/proposal";
    }

}