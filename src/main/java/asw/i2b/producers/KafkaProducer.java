package asw.i2b.producers;

import asw.i2b.dao.dto.Comment;
import asw.i2b.dao.dto.Proposal;
import asw.i2b.util.Views;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Iterables;
import kafka.Kafka;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by herminio on 26/12/16.
 *
 * @author MIGUEL
 * @since 02/04/2017
 */
@ManagedBean
public class KafkaProducer {

    private static final Logger logger = Logger.getLogger(KafkaProducer.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;    //Is this getting a KafkaTemplate with the configuration
                                                            //from KafkaProducerFactory already set into it?

    @PostConstruct
    public void init(){
        mapper.disable(MapperFeature.DEFAULT_VIEW_INCLUSION);
    }

    public void send(String topic, String data) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, data);
        future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                logger.info("Success on sending message \"" + data + "\" to topic " + topic);
            }

            @Override
            public void onFailure(Throwable ex) {
                logger.error("Error on sending message \"" + data + "\", stacktrace " + ex.getMessage());
            }
        });
    }

    private String writer(Object o){
        String result = null;
        try {
            result = mapper.writerWithView(Views.Public.class).writeValueAsString(o);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void sendCreateProposal(Proposal proposal) {
        send("createProposal", writer(proposal));
    }

    public void sendCreateComment(Comment comment, String proposalId) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"proposalId\":\"");
        sb.append(proposalId);
        sb.append("\",\"comment:");
        sb.append(writer(comment));
        sb.append("}");
        send("createComment", sb.toString());
    }

    public void sendVoteProposal(Proposal proposal) {
        StringBuilder sb = new StringBuilder();
        sb.append(proposal.get_id());
        sb.append(";");
        sb.append(Iterables.getLast(proposal.getVotedUsernames()));
        send("voteProposal", sb.toString());
    }

    public void sendVoteComment(Comment comment, Proposal proposal) {
        StringBuilder sb = new StringBuilder();
        sb.append(proposal.get_id());
        sb.append(";");
        sb.append(comment.getNum());
        sb.append(";");
        sb.append(Iterables.getLast(comment.getVotedUsernames()));
        send("voteComment", sb.toString());
    }

}
