package asw.i2b.dao.dto;

import asw.i2b.util.Views;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Pineirin on 28/03/2017.
 */
@Document(collection = "comments")
public class Comment {

    @JsonView(Views.Public.class)
    private String author;

    @JsonView(Views.Public.class)
    private Date created;

    @JsonView(Views.Public.class)
    private String body;
    private int votes;
    private List<String> votedUsernames;

    @JsonView(Views.Public.class)
    private long num;


    public Comment() {

    }

    public Comment(String author, String body) {
        this.author = author;
        this.body = body;
        this.created = new Date();
        votes = 0;
        this.votedUsernames = new ArrayList<>();
    }


    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }

    public Date getCreated() {
        return created;
    }

    public int getVotes() {
        return votes;
    }

    public List<String> getVotedUsernames() {
        return votedUsernames;
    }

    public void vote(String voteUsername) {
        votedUsernames.add(voteUsername);
        this.votes++;
    }

    public void unvote(String voteUsername) {
        votedUsernames.remove(voteUsername);
        this.votes--;
    }

    public void setNum(long num) {
        this.num = num;
    }

    public long getNum() {
        return num;
    }

    @Override
    public String toString() {
        return "Comment{" +
                ", author='" + author + '\'' +
                ", created=" + created +
                ", body='" + body + '\'' +
                ", votes=" + votes +
                ", votedUsernames=" + votedUsernames +
                ", num=" + num +
                '}';
    }

    public boolean isValid(List<String> invalidWords) {
        for (String s : invalidWords)
            if (body.contains(s))
                return false;
        return true;
    }
}
