package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.upnext.productservice.contracts.cpus.CPURequest;
import org.upnext.productservice.contracts.cpus.CPUResponse;
import org.upnext.productservice.entities.CPU;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CPUMapper extends AbstractProductMapper<CPU, CPURequest, CPUResponse>{

}