package com.martinb.microservices.product;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "products", path = "products")
public interface ProductRepository extends MongoRepository<Product, String> {

    public List<Product> findByProductCategoryName(@Param("productCategory") String productCategoryName);

    public List<Product> findByCode(@Param("code") String code);
}
