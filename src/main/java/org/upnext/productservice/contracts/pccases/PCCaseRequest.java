package org.upnext.productservice.contracts.pccases;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.contracts.products.ProductRequest;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PCCaseRequest extends ProductRequest {
    private List<String> formFactorSupport;
    private Integer maxGpuLengthMm;
    private Integer maxCpuCoolerHeightMm;
    private String psuFormFactor;
}