package org.upnext.productservice.contracts.ramkits;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.contracts.products.ProductRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class RAMKitRequest extends ProductRequest {
    private String type;
    private Integer capacityGB;
    private Integer modules;
    private Integer speedMHz;
    private Integer casLatency;
    private Boolean ecc;
}