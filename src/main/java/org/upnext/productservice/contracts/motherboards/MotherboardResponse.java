package org.upnext.productservice.contracts.motherboards;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.contracts.products.ProductResponse;

@Data
@EqualsAndHashCode(callSuper = true)
public class MotherboardResponse extends ProductResponse {
    private String socket;
    private String chipset;
    private String formFactor;
    private String ramType;
    private Integer ramSlots;
    private Integer maxMemorySpeedMHz;
    private String pcieVersion;
    private Integer m2Slots;
    private Boolean wifi;
}