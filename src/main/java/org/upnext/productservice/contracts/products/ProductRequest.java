package org.upnext.productservice.contracts.products;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest
{
    @NotNull(message = "Name is required")
    String name;

    @NotNull(message = "Description is required")
    String description;

    @DecimalMin(value = "1.0", inclusive = true, message = "Price must be at least 1.0")
    Double price;

    String imageUrl;

    @Min(value = 1, message = "Stock must be at least 1")
    Integer stock;

    Long categoryId;
    Long brandId;
}
