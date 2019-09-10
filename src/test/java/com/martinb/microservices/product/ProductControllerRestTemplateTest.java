package com.martinb.microservices.product;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test.properties")
public class ProductControllerRestTemplateTest {

    private static String PRODUCT_SERVICE_URL = "http://127.0.0.1:8080/products";

    @Before
    public void setUp() {
//        deleteAllProducts();
    }

    @After
    public void clean() {
//        deleteAllProducts();
    }

    @Test
    public void testPostProduct() {
        Product newProduct = createProduct("1");
        RestTemplate restTemplate = restTemplate();
        Product retrievedProduct = restTemplate.postForObject(PRODUCT_SERVICE_URL, newProduct, Product.class);
    }

    @Test
    public void testGetAProduct() {
        Product product = createProduct("2");
        RestTemplate restTemplate = restTemplate();
        Product retrievedProduct = restTemplate.postForObject(PRODUCT_SERVICE_URL, product, Product.class);
        String uri = PRODUCT_SERVICE_URL + "/" + retrievedProduct.getId();
        Product retrievedProduct2 = restTemplate.getForObject(uri, Product.class);
        assertEquals(retrievedProduct, retrievedProduct2);
    }

    @Test
    public void testPutProduct() {
        Product product = createProduct("3");
        RestTemplate restTemplate = restTemplate();

        Product retrievedProduct = restTemplate.postForObject(PRODUCT_SERVICE_URL, product, Product.class);
        retrievedProduct.setPrice(retrievedProduct.getPrice() * 3);
        restTemplate.put(PRODUCT_SERVICE_URL + "/" + retrievedProduct.getId(), retrievedProduct, Product.class);

        Product testRetrievedProduct = restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + retrievedProduct.getId(), Product.class);
        assertEquals(testRetrievedProduct.getPrice(), retrievedProduct.getPrice(), 1);
    }

    @Test
    public void testDeleteAllProducts() {
        RestTemplate restTemplate = restTemplate();
        Product product = createProduct("4");
        Product product2 = createProduct("5");
        Product retrievedProduct1 = restTemplate.postForObject(PRODUCT_SERVICE_URL, product, Product.class);
        Product retrievedProduct2 = restTemplate.postForObject(PRODUCT_SERVICE_URL, product2, Product.class);

        restTemplate.delete(PRODUCT_SERVICE_URL);
    }

    public void deleteAllProducts() {
        RestTemplate restTemplate = restTemplate();
        List<Product> productList = getAllProducts();
        productList.forEach(item -> restTemplate.delete(PRODUCT_SERVICE_URL + "/" + item.getId()));
    }

    public List<Product> getAllProducts() {
        RestTemplate restTemplate = restTemplate();
        ParameterizedTypeReference<PagedResources<Product>> responseTypeRef = new ParameterizedTypeReference<PagedResources<Product>>() {};
        ResponseEntity<PagedResources<Product>> responseEntity = restTemplate.exchange(PRODUCT_SERVICE_URL, HttpMethod.GET, (HttpEntity<Product>) null, responseTypeRef);
        PagedResources<Product> resources = responseEntity.getBody();
        Collection<Product> products = resources.getContent();
        return new ArrayList<Product>(products);
    }

    private RestTemplate restTemplate() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new Jackson2HalModule());

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json, application/json"));
        converter.setObjectMapper(mapper);

        return new RestTemplate(Arrays.asList(converter));
    }

    private Product createProduct(String id) {
        Product product = new Product();

        product.setId(id);
        product.setName("Test Product #" + id);
        product.setCode("test-" + id);
        product.setTitle("Test Product Title");
        product.setDescription("Test product description.");
        product.setImageUrl("test-product-1.png");
        product.setPrice(10.00);
        product.setProductCategoryName("testCategory");

        return product;
    }
}
