package org.upnext.productservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "motherboards")
public class Motherboard extends Product {
    private String socket;
    private String chipset;
    private String formFactor; // ATX, Micro-ATX, Mini-ITX
    private String ramType;    // DDR4, DDR5
    private Integer ramSlots;
    private Integer maxMemorySpeedMHz;
    private String pcieVersion;
    private Integer m2Slots;
    private Boolean wifi;
}
