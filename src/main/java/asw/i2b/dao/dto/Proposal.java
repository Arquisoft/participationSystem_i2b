package asw.i2b.dao.dto;

import asw.i2b.util.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
@Document(collection = "proposals")
public class Proposal {

    private static final Logger logger = Logger.getLogger(Proposal.class);

    public enum Order {
        date,
        popularity
    }

    private Order orderBy;

    @Id
    private ObjectId _id;

    private String author;
    private Date created;
    private String category;
    private String title;
    private String body;
    private int minimalSupport;
    private int votes;
    private List<String> votedUsernames;
    private List<Comment> comments;

    public Proposal() {

    }

    public Proposal(String author, String category, String title, String body, int minimalSupport) {
        this.author = author;
        this.created = new Date();
        this.category = category;
        this.title = title;
        this.body = body;
        this.minimalSupport = minimalSupport;
        this.votes = 0;
        this.votedUsernames = new ArrayList<>();
        this.comments = new ArrayList<>();
        this.orderBy = Order.date;
    }

    public String getIdString() {        //Just for serialization, to have the appropiate name with the _
        return _id.toHexString();   //and just serializing the id string
    }

    @JsonView(Views.Public.class)
    public ObjectId getId() {
        return _id;
    }

    public String getAuthor() {
        return author;
    }

    public Date getCreated() {
        return created;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public int getMinimalSupport() {
        return minimalSupport;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public List<String> getVotedUsernames() {
        return votedUsernames;
    }

    public void setOrder(Order orderBy) {
        this.orderBy = orderBy;
    }

    public List<Comment> getComments() {
        System.out.println(orderBy);
        switch (orderBy) {
            case date:
                this.comments.sort(
                        Comparator.comparing(Comment::getCreated)
                );
                break;
            case popularity:
                this.comments.sort(
                        (a, b) -> b.getVotes() - a.getVotes()
                );
                break;
            default:
        }
        System.out.println(comments);
        return comments;
    }

    public void vote(String voteUsername) {
        votedUsernames.add(voteUsername);
        this.votes++;
        if (this.votes == this.minimalSupport)
            logger.debug("NOTIFY ADMIN: Proposal with id [" + this._id + "] and title [" + this.title
                    + "] has reached the minimal Support");
    }

    public void unvote(String voteUsername) {
        votedUsernames.remove(voteUsername);
        this.votes--;
    }

    public boolean isSupported() {
        return votes >= minimalSupport;
    }

    public boolean isValid(List<String> invalidWords) {
        for (String s : invalidWords)
            if (body.contains(s) || title.contains(s))
                return false;
        return true;
    }

    public void comment(Comment comment) {
        comment.setNum(getLastCommentId() + 1);
        this.comments.add(comment);
    }

    public long getLastCommentId() {
        long res = 0;
        for (Comment c : comments)
            res = (res < c.getNum()) ? c.getNum() : res;
        return res;
    }

    public Comment getComment(Long num) {
        for (Comment c : comments)
            if (c.getNum() == num) return c;
        return null;
    }

    public void deleteComment(long num) {
        for (int i = 0; i < this.comments.size(); i++)
            if (this.comments.get(i).getNum() == num)
                this.comments.remove(i);
    }
}
