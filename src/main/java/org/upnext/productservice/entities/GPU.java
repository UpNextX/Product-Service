package org.upnext.productservice.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "gpus")
public class GPU extends Product {
    private Integer vramGB;
    private Integer tdpW;
    private Integer recommendedPSUWatt;
    private String performanceTier;
    private Integer lengthMm;
}
