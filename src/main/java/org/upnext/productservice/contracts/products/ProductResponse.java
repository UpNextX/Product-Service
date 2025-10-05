package org.upnext.productservice.contracts.products;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    Long id;
    String name;
    String description;
    String imageUrl;
    Integer stock;
    Double price;
    Long categoryId;
}
