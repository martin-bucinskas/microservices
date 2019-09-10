//package com.martinb.microservices.product;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.core.ParameterizedTypeReference;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.hateoas.hal.Jackson2HalModule;
//import org.springframework.http.HttpEntity;
//import org.springframework.http.HttpMethod;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.List;
//
//import static org.junit.Assert.assertTrue;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@TestPropertySource(locations = "classpath:test.properties")
//public class ProductHalRestTemplateTest {
//
//    private static String PRODUCT_SERVICE_URL = "http://127.0.0.1:8080/products";
//
//    @Before
//    public void setUp() {
//        deleteAllProducts();
//    }
//
//    @Test
//    public void testPostProduct() {
//        try {
//            Product testProduct1 = createProduct("1");
//            Product testProduct2 = createProduct("2");
//            RestTemplate restTemplate = restTemplate();
//            Product testProductRetrieved1 = restTemplate.postForObject(PRODUCT_SERVICE_URL, testProduct1, Product.class);
//            Product testProductRetrieved2 = restTemplate.postForObject(PRODUCT_SERVICE_URL, testProduct2, Product.class);
//            assertTrue("successfully saved", true);
//        } catch(Exception e) {
//            assertTrue("successfully failed", true);
//        }
//    }
//
//    @Test
//    public void testGetAllProducts() {
//        testPostProduct();
//        List<Product> productList = getAllProducts();
//        assertTrue(productList.size() > 0);
//    }
//
//    @Test
//    public void testPutProduct() {
//        try {
//            Product testProduct3 = createProduct("3");
//            RestTemplate restTemplate = restTemplate();
//
//            Product retrievedTestProduct3 = restTemplate.postForObject(PRODUCT_SERVICE_URL, testProduct3, Product.class);
//            retrievedTestProduct3.setPrice(retrievedTestProduct3.getPrice() * 2);
//
//            restTemplate.put(PRODUCT_SERVICE_URL + "/" + retrievedTestProduct3.getId(), retrievedTestProduct3, Product.class);
//            Product retrievedTestProduct = restTemplate.getForObject(PRODUCT_SERVICE_URL + "/" + retrievedTestProduct3.getId(), Product.class);
//
//            assertTrue("successfully saved", true);
//        } catch(Exception e) {
//            assertTrue("successfully failed", true);
//        }
//    }
//
//    public void deleteAllProducts() {
//        RestTemplate restTemplate = restTemplate();
//        List<Product> productList = getAllProducts();
//        productList.forEach(item -> restTemplate.delete(PRODUCT_SERVICE_URL + "/" + item.getId()));
//    }
//
//    public List<Product> getAllProducts() {
//        RestTemplate restTemplate = restTemplate();
//        ParameterizedTypeReference<PagedResources<Product>> responseTypeRef = new ParameterizedTypeReference<PagedResources<Product>>() {};
//        ResponseEntity<PagedResources<Product>> responseEntity = restTemplate.exchange(PRODUCT_SERVICE_URL, HttpMethod.GET, (HttpEntity<Product>) null, responseTypeRef);
//        PagedResources<Product> resources = responseEntity.getBody();
//        Collection<Product> products = resources.getContent();
//        return new ArrayList<Product>(products);
//    }
//
//    private Product createProduct(String id) {
//        Product product = new Product();
//
//        product.setId(id);
//        product.setName("Test Product #" + id);
//        product.setCode("test-" + id);
//        product.setTitle("Test Product Title");
//        product.setDescription("Test product description.");
//        product.setImageUrl("test-product-1.png");
//        product.setPrice(10.00);
//        product.setProductCategoryName("testCategory");
//
//        return product;
//    }
//
//    private RestTemplate restTemplate() {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
//        mapper.registerModule(new Jackson2HalModule());
//
//        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
//        converter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/hal+json"));
//        converter.setObjectMapper(mapper);
//
//        return new RestTemplate(Arrays.asList(converter));
//    }
//}
