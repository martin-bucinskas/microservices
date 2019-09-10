package com.martinb.microservices.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Product {

    @Id
    private String id;
    private String name;
    private String code;
    private String title;
    private String description;
    private String imageUrl;
    private double price;
    private String productCategoryName;
}
