package org.upnext.productservice.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "psus")
public class PSU extends Product {
    private Integer wattage;
    private String efficiency;
    private String modularity;
    private String formFactor;
}