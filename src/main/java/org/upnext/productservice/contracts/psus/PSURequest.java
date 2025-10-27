package org.upnext.productservice.contracts.psus;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.contracts.products.ProductRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class PSURequest extends ProductRequest {
    private Integer wattage;
    private String efficiency;
    private String modularity;
    private String formFactor;
}
