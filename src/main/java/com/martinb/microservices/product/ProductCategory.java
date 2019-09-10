package com.martinb.microservices.product;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class ProductCategory {

    @Id
    private String id;
    private String name;
    private String title;
    private String description;
    private String imgUrl;
}
