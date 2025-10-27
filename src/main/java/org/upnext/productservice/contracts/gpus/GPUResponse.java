package org.upnext.productservice.contracts.gpus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.contracts.products.ProductResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class GPUResponse extends ProductResponse {
    private Integer vramGB;
    private Integer tdpW;
    private Integer recommendedPSUWatt;
    private String performanceTier;
    private Integer lengthMm;
}