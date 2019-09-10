package com.martinb.microservices.product;

import org.junit.After;
import org.junit.AfterClass;
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
public class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Before
    public void setUp() {
        productRepository.deleteAll();
    }

    @After
    public void clearTestData() {
        productRepository.deleteAll();
    }

    @Test
    public void testAddProduct() {
        productRepository.save(createProduct());
        assertTrue("successfully saved", true);
    }

    @Test
    public void testFindAllProducts() {
        productRepository.save(createProduct());
        List<Product> productList = productRepository.findAll();
        assertTrue(productList.size() > 0);
    }

    @Test
    public void testProductByProductCategory() {
        Product product = createProduct();
        productRepository.save(product);
        List<Product> productList = productRepository.findByProductCategoryName(product.getProductCategoryName());
        assertTrue(productList.size() > 0);
    }

    private Product createProduct() {
        Product product = new Product();

        product.setName("Test Product #1");
        product.setCode("test-1");
        product.setTitle("Test Product #1 Title");
        product.setDescription("Test product description.");
        product.setImageUrl("test-product-1.png");
        product.setPrice(10.00);
        product.setProductCategoryName("testCategory");
        return product;
    }
}
