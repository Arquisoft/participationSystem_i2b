package asw.i2b.service;

import asw.i2b.dao.InvalidWordsRepository;
import asw.i2b.dao.dto.InvalidWord;
import asw.i2b.dao.dto.Proposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nicolas on 4/04/17 for participationSystem0.
 */
@Service
public class InvalidWordsService {

    @Autowired
    private InvalidWordsRepository invalidWordsRepository;

    public List<InvalidWord> getAllInvalidWords() {
        return invalidWordsRepository.findAll();
    }
}
