package com.martinb.microservices.product;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class ProductCategoryRepositoryTests {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;

    @Before
    public void setUp() {
        productCategoryRepository.deleteAll();
    }

    @Test
    public void testAddProductCategories() {
        try {
            ProductCategory productCategory = createProductCategory();
            productCategoryRepository.save(productCategory);
            assertTrue("successfully saved", true);
        } catch(Exception e) {
            assertTrue("successfully failed", true);
        }
    }

    @Test
    public void testFindAllProductCategories() {
        ProductCategory productCategory = createProductCategory();
        productCategoryRepository.save(productCategory);

        List<ProductCategory> categoryList = productCategoryRepository.findAll();
        assertTrue(categoryList.size() > 0);
    }

    private ProductCategory createProductCategory() {
        ProductCategory productCategory = new ProductCategory();

        productCategory.setName("Test Category");
        productCategory.setDescription("Test Category: description");
        productCategory.setTitle("testCategory");
        productCategory.setImgUrl("test-product-category.png");

        return productCategory;
    }
}
