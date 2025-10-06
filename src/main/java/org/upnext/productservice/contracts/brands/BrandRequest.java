package org.upnext.productservice.contracts.brands;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {
    @NotNull(message = "The name Should Not Be Null")
    String name;
}
