package asw.i2b.service;

import asw.i2b.dao.CategoryRepository;
import asw.i2b.dao.ProposalsRepository;
import asw.i2b.dao.dto.Category;
import asw.i2b.dao.dto.Proposal;
import org.bson.types.ObjectId;
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


    public Category findCategoryById(String id) {
        return categoryRepository.findOne(new ObjectId(id));
    }


    public void createCategory(Category category) {
        if (category != null && this.getAllCategories().stream().map(a -> a.getName()).filter(a -> a.equals(category.getName())).count() == 0)
            categoryRepository.insert(category);
    }

    public void delete(Category cat) {
        categoryRepository.delete(cat);
    }
}
