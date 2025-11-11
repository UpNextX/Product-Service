package org.upnext.productservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "ram_kits")
public class RAMKit extends Product {
    private String type;
    private Integer capacityGB;
    private Integer modules;
    private Integer speedMHz;
    private Integer casLatency;
    private Boolean ecc;
}