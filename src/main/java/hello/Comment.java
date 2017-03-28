package hello;

/**
 * Created by Pineirin on 28/03/2017.
 */
public class Comment {
    private String text;
    private int number;
    private int upVotes;
    private int downVotes;

    public Comment(String text, int number) {
        this.text = text;
        this.number = number;
        this.upVotes = 0;
        this.downVotes = 0;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setUpVotes(int upVotes) {
        this.upVotes = upVotes;
    }

    public void setDownVotes(int downVotes) {
        this.downVotes = downVotes;
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }

    public int getUpVotes() {
        return upVotes;
    }

    public int getDownVotes() {
        return downVotes;
    }
}
