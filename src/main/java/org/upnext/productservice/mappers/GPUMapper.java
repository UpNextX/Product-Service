package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.upnext.productservice.contracts.gpus.GPURequest;
import org.upnext.productservice.contracts.gpus.GPUResponse;
import org.upnext.productservice.entities.GPU;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GPUMapper extends AbstractProductMapper<GPU, GPURequest, GPUResponse>{

}