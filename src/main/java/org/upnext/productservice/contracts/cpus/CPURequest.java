package org.upnext.productservice.contracts.cpus;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.contracts.products.ProductRequest;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class CPURequest extends ProductRequest {
    private String socket;
    private Integer cores;
    private Integer threads;
    private Double baseClockGHz;
    private Double boostClockGHz;
    private Integer tdpW;
    private List<String> supportedMemoryTypes;
    private Integer maxMemorySpeedMHz;
    private String igpu;
    private String generation;
}
