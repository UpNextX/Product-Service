package org.upnext.productservice.contracts.ramkits;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.contracts.products.ProductResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class RAMKitResponse extends ProductResponse {
    private String type;
    private Integer capacityGB;
    private Integer modules;
    private Integer speedMHz;
    private Integer casLatency;
    private Boolean ecc;
}