package asw.i2b.service;

import asw.i2b.dao.InvalidWordsRepository;
import asw.i2b.dao.dto.InvalidWord;
import org.bson.types.ObjectId;
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

    public void createInvalidWord(InvalidWord invalidWord) {
        if(this.getAllInvalidWords().stream().map(a -> a.getWord()).filter(a -> a.equals(invalidWord.getWord().trim())).count() == 0 )
            invalidWordsRepository.insert(invalidWord);
    }

    public InvalidWord findInvalidWordById(String id) {
        return invalidWordsRepository.findOne(new ObjectId(id));
    }

    public void delete(InvalidWord invalidWord) {
        invalidWordsRepository.delete(invalidWord);
    }
}
