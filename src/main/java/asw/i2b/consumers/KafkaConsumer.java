package asw.i2b.consumers;

import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MIGUEL
 * @since 04/04/2017
 */
public class KafkaConsumer {

    private List<String> createProposal = new ArrayList<>();
    private List<String> createComment = new ArrayList<>();
    private List<String> voteProposal = new ArrayList<>();
    private List<String> voteComment = new ArrayList<>();
    private List<String> unvoteProposal = new ArrayList<>();
    private List<String> unvoteComment = new ArrayList<>();

    public List<String> getCreateProposal() {
        return createProposal;
    }

    public List<String> getCreateComment() {
        return createComment;
    }

    public List<String> getVoteProposal() {
        return voteProposal;
    }

    public List<String> getVoteComment() {
        return voteComment;
    }

    public List<String> getUnvoteProposal() {
        return unvoteProposal;
    }

    public List<String> getUnvoteComment() {
        return unvoteComment;
    }

    @KafkaListener(topics = "CREATE_POST")
    public void listenCreateProposal(String s){
        createProposal.add(s);
    }

    @KafkaListener(topics = "CREATE_COMMENT")
    public void listenCreateComment(String s){
        createComment.add(s);
    }

    @KafkaListener(topics = "VOTE_POST")
    public void listenVoteProposal(String s){
        voteProposal.add(s);
    }

    @KafkaListener(topics = "VOTE_COMMENT")
    public void listenVoteComment(String s){
        voteComment.add(s);
    }

    @KafkaListener(topics = "UNVOTE_POST")
    public void listenUnvoteProposal(String s){
        unvoteProposal.add(s);
    }

    @KafkaListener(topics = "UNVOTE_COMMENT")
    public void listenUnvoteComment(String s){
        unvoteComment.add(s);
    }

}
