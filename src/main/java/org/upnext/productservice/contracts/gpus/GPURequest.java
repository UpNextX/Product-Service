package org.upnext.productservice.contracts.gpus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.contracts.products.ProductRequest;

@Data
@EqualsAndHashCode(callSuper = true)
public class GPURequest extends ProductRequest {
    private Integer vramGB;
    private Integer tdpW;
    private Integer recommendedPSUWatt;
    private String performanceTier;
    private Integer lengthMm;
}