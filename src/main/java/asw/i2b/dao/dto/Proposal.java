package asw.i2b.dao.dto;

import asw.i2b.model.ProposalRestrictions;
import asw.i2b.util.ObjectIdSerializer;
import asw.i2b.util.Views;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
@Document(collection = "proposals")
public class Proposal {

    @Id

    private ObjectId _id;

    @JsonView(Views.Public.class)
    private String author;
    @JsonView(Views.Public.class)
    private Date created;
    private String category;
    @JsonView(Views.Public.class)
    private String title;
    @JsonView(Views.Public.class)
    private String body;
    private int minimalSupport;
    @JsonView(Views.Public.class)
    private int votes;
    @JsonView(Views.Public.class)
    private List<String> votedUsernames;
    @JsonView(Views.Public.class)
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

    }
    @JsonView(Views.Public.class)
    public String get_id() {        //Just for serialization, to have the appropiate name with the _
        return _id.toHexString();   //and just serializing the id string
    }

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

    public List<String> getVotedUsernames() {
        return votedUsernames;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void vote(String voteUsername) {
        votedUsernames.add(voteUsername);
        this.votes++;
    }

    public boolean isSupported() {
        if (votes > minimalSupport)
            return true;
        return false;
    }

    public boolean isValid() {
        for (String s : ProposalRestrictions.getInstance().getNotAllowedWords())
            if (body.contains(s))
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

    @Override
    public String toString() {
        return "Proposal{" +
                "_id=" + _id +
                ", author='" + author + '\'' +
                ", created=" + created +
                ", category='" + category + '\'' +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                ", minimalSupport=" + minimalSupport +
                ", votes=" + votes +
                ", votedUsernames=" + votedUsernames +
                ", comments=" + comments +
                '}';
    }

    public void deleteComment(long num) {
        for(int i = 0;i < this.comments.size();i++)
            if(this.comments.get(i).getNum() == num)
                this.comments.remove(i);
    }
}
