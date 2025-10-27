package org.upnext.productservice.entities;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.upnext.productservice.entities.Product;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pc_cases")
public class PCCase extends Product {
    @ElementCollection
    private List<String> formFactorSupport; // ["ATX", "Micro-ATX", "Mini-ITX"]

    private Integer maxGpuLengthMm;
    private Integer maxCpuCoolerHeightMm;
    private String psuFormFactor; // ATX, SFX, SFX-L
}