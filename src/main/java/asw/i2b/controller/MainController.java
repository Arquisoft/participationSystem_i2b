package asw.i2b.controller;


import asw.i2b.dao.dto.Category;
import asw.i2b.dao.dto.Comment;
import asw.i2b.dao.dto.InvalidWord;
import asw.i2b.dao.dto.Proposal;
import asw.i2b.model.*;
import asw.i2b.producers.KafkaProducer;
import asw.i2b.service.CategoryService;
import asw.i2b.service.InvalidWordsService;
import asw.i2b.service.ProposalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.stream.Collectors;

@Controller
public class MainController {

    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private ProposalService proposalService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private InvalidWordsService invalidWordsService;

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
        return "/user/home";
    }

    @GetMapping("/user/admin_settings")
    public String settings(Model model) {
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        model.addAttribute("invalidWords", invalidWordsService.getAllInvalidWords());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("createInvalidWord", new InvalidWordCreation());
        model.addAttribute("createCategory", new CategoryCreation());
        return "/user/admin_settings";
    }

    @PostMapping("/voteProposal/{id}")
    public String voteProposal(Model model, @PathVariable("id") String id) {
        Proposal proposal = proposalService.findProposalById(id);
        if (proposal != null) {
            boolean isAVote = proposalService.vote(proposal);
            kafkaProducer.sendVoteProposal(proposal, isAVote);
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

    @PostMapping("/deleteComment/{id}")
    public String deleteComment(Model model, @RequestParam(value = "proposalId") String proposalId, @PathVariable("id") String id) {
        Proposal proposal = proposalService.findProposalById(proposalId);
        proposal.deleteComment(Long.parseLong(id));
        proposalService.save(proposal);
        return "redirect:/user/proposal/" + proposalId;
    }

    @PostMapping("/deleteInvalidWord/{id}")
    public String deleteInvalidWord(Model model, @PathVariable("id") String id) {
        InvalidWord invalidWord = invalidWordsService.findInvalidWordById(id);
        if (invalidWord != null) {
            invalidWordsService.delete(invalidWord);
        }
        return "redirect:/user/admin_settings";
    }

    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(Model model, @PathVariable("id") String id) {
        Category cat = categoryService.findCategoryById(id);
        if (cat != null) {
            categoryService.delete(cat);
        }
        return "redirect:/user/admin_settings";
    }

    @PostMapping("/user/createProposal")
    public String createProposal(Model model, @ModelAttribute ProposalCreation createProposal) {
        String author = ((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        Category cat = categoryService.findCategoryById(createProposal.getCategory());
        Proposal proposal = new Proposal(author, cat.getName(), createProposal.getTitle(), createProposal.getBody(), cat.getMinimalSupport());
        if(proposal.isValid(invalidWordsService.getAllInvalidWords().stream().map(a -> a.getWord()).collect(Collectors.toList()))){
            proposalService.createProposal(proposal);
            kafkaProducer.sendCreateProposal(proposal);
        }
        return "redirect:/user/home";
    }

    @PostMapping("/user/voteComment/{id}")//user/voteComment/2?proposalId=58dbfd8abff2304aa268bada
    public String voteComment(Model model, @RequestParam(value = "proposalId") String proposalId, @PathVariable("id") String id) {
        String author = ((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        Proposal proposal = proposalService.findProposalById(proposalId);
        Comment comment = proposal.getComment(Long.parseLong(id));
        if (comment != null) {
            if (!comment.getVotedUsernames().contains(author)) {
                comment.vote(author);
            } else {
                comment.unvote(author);
            }
        }
        proposalService.save(proposal);
        kafkaProducer.sendVoteComment(comment, proposal, true);
        return "redirect:/user/proposal/" + proposalId + "?orderBy=date";
    }

    @PostMapping("/user/createComment/{id}")
    public String createComment(Model model, @ModelAttribute CommentCreation createComment, @PathVariable("id") String id) {
        String author = ((UserModel) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getLogin();
        Proposal proposal = proposalService.findProposalById(id);
        Comment comment = new Comment(author, createComment.getBody());
        proposal.comment(comment);
        if(comment.isValid(invalidWordsService.getAllInvalidWords().stream().map(a -> a.getWord()).collect(Collectors.toList()))){
            proposalService.save(proposal);
            kafkaProducer.sendCreateComment(comment, id);
        }

        return "redirect:/user/proposal/" + id + "?orderBy=date";
    }

    @PostMapping("/createInvalidWord")
    public String createInvalidWord(Model model, @ModelAttribute InvalidWordCreation invalidWordCreation) {
        invalidWordsService.createInvalidWord(
                new InvalidWord(
                        invalidWordCreation.getWord()
                )
        );
        return "redirect:/user/admin_settings";
    }

    @PostMapping("/createCategory")
    public String createCategory(Model model, @ModelAttribute CategoryCreation categoryCreation) {
        categoryService.createCategory(
                new Category(
                        categoryCreation.getName(),
                        categoryCreation.getMinimalSupport()
                )
        );
        return "redirect:/user/admin_settings";
    }

    @PostMapping("/addVotes/{id}")
    public String addVotes(Model model, @ModelAttribute("quantity") int quantity, @PathVariable("id") String id){
        Proposal proposal = proposalService.findProposalById(id);
        System.out.println("QUANTITY" + quantity);
        if(proposal != null)
            proposal.setVotes(proposal.getVotes() + quantity);
        proposalService.save(proposal);
        return "redirect:/user/proposal/" + id + "?orderBy=date";
    }

    @GetMapping("/user/proposal/{id}")
    public String proposal(Model model, @PathVariable("id") String id, @RequestParam(value = "orderBy") String orderBy) {
        Proposal selectedProposal = proposalService.findProposalById(id);
        model.addAttribute("user", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        selectedProposal.setOrder(("date".equals(orderBy)) ? Proposal.Order.date : Proposal.Order.popularity);
        model.addAttribute("orderBy", orderBy);
        model.addAttribute("selectedProposal", selectedProposal);
        model.addAttribute("createComment", new CommentCreation());
        return "user/proposal";
    }

}