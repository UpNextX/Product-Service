package org.upnext.productservice.entities;


import jakarta.annotation.Nullable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name="cpus")
@Data
public class CPU extends Product {
    private String socket;
    private Integer cores;
    private Integer threads;
    @Nullable
    private Double baseClockGHz;
    @Nullable
    private Double boostClockGHz;
    private Integer tdpW;

    @ElementCollection
    private List<String> supportedMemoryTypes;

    private Integer maxMemorySpeedMHz;
    @Nullable
    private String igpu;
    @Nullable
    private String generation;

}
