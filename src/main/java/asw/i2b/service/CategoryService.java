package asw.i2b.service;

import asw.i2b.dao.CategoryRepository;
import asw.i2b.dao.ProposalsRepository;
import asw.i2b.dao.dto.Category;
import asw.i2b.dao.dto.Proposal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author nokutu
 * @since 28/03/2017.
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

}
