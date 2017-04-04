package asw.i2b.consumers;

import org.codehaus.groovy.runtime.powerassert.SourceText;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

    @KafkaListener(topics = "createProposal")
    public void listenCreateProposal(String s){
        createProposal.add(s);
    }

    @KafkaListener(topics = "createComment")
    public void listenCreateComment(String s){
        createComment.add(s);
    }

    @KafkaListener(topics = "voteProposal")
    public void listenVoteProposal(String s){
        voteProposal.add(s);
    }

    @KafkaListener(topics = "voteComment")
    public void listenVoteComment(String s){
        voteComment.add(s);
    }

    @KafkaListener(topics = "unvoteProposal")
    public void listenUnvoteProposal(String s){
        unvoteProposal.add(s);
    }

    @KafkaListener(topics = "unvoteComment")
    public void listenUnvoteComment(String s){
        unvoteComment.add(s);
    }

}
