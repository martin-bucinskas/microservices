package com.martinb.microservices.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.mvc.UriComponentsContributor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@Slf4j
public class ProductRestController {

    @Autowired
    private ProductRepository productRepository;

    @RequestMapping(value = "/products/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resource<Product>> getProduct(@PathVariable("id") String id) {

        Optional<Product> product = productRepository.findById(id);

        if(product.isEmpty()) {
            return new ResponseEntity<Resource<Product>>(HttpStatus.NOT_FOUND);
        }

        Resource<Product> productResource = new Resource<Product>(product.get(),
                linkTo(methodOn(ProductRestController.class)
                        .getProduct(product.get().getId()))
                        .withSelfRel());

        return new ResponseEntity<Resource<Product>>(productResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resources<Resource<Product>>> getAllProducts() {
        List<Product> products = productRepository.findAll();

        Link links[] = {
                linkTo(methodOn(ProductRestController.class).getAllProducts()).withSelfRel(),
                linkTo(methodOn(ProductRestController.class).getAllProducts()).withRel("getAllProducts")
        };

        if(products.isEmpty()) {
            return new ResponseEntity<Resources<Resource<Product>>>(HttpStatus.NOT_FOUND);
        }

        List<Resource<Product>> list = new ArrayList<Resource<Product>>();

        products.forEach(product -> {
            list.add(new Resource<Product>(product,
                    linkTo(methodOn(ProductRestController.class).getProduct(product.getId())).withSelfRel()));
        });

        Resources<Resource<Product>> productResources = new Resources<Resource<Product>>(list, links);

        return new ResponseEntity<Resources<Resource<Product>>>(productResources, HttpStatus.OK);
    }

    @RequestMapping(value = "/products", method = RequestMethod.POST)
    public ResponseEntity<Resource<Product>> postProduct(@RequestBody Product product, UriComponentsBuilder ucBuilder) {
        List<Product> products = productRepository.findByCode(product.getCode());

        if(products.size() > 0) {
            log.info("A product with code {} already exists!", product.getCode());
            return new ResponseEntity<Resource<Product>>(HttpStatus.CONFLICT);
        }

        Product newProduct = productRepository.save(product);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri());

        Resource<Product> productResource = new Resource<Product>(newProduct,
                linkTo(methodOn(ProductRestController.class).getProduct(newProduct.getId()))
                        .withSelfRel());

        return new ResponseEntity<Resource<Product>>(productResource, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Resource<Product>> updateProduct(@PathVariable("id") String id, @RequestBody Product product) {
        Optional<Product> currentProduct = productRepository.findById(id);

        if(currentProduct.isEmpty()) {
            return new ResponseEntity<Resource<Product>>(HttpStatus.NOT_FOUND);
        }

        Product p = currentProduct.get();

        p.setName(product.getName());
        p.setCode(product.getCode());
        p.setTitle(product.getTitle());
        p.setDescription(product.getDescription());
        p.setImageUrl(product.getImageUrl());
        p.setPrice(product.getPrice());
        p.setProductCategoryName(product.getProductCategoryName());

        Product newProduct = productRepository.save(p);

        Resource<Product> productResource = new Resource<Product>(newProduct,
                linkTo(methodOn(ProductRestController.class).getProduct(newProduct.getId())).withSelfRel());

        return new ResponseEntity<Resource<Product>>(productResource, HttpStatus.OK);
    }

    @RequestMapping(value = "/products/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") String id) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            return new ResponseEntity<Product>(HttpStatus.NOT_FOUND);
        }

        productRepository.deleteById(id);

        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/products", method = RequestMethod.DELETE)
    public ResponseEntity<Product> deleteAllProducts() {
        productRepository.deleteAll();
        return new ResponseEntity<Product>(HttpStatus.NO_CONTENT);
    }
}
