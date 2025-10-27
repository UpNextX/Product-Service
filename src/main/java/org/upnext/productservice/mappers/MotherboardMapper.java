package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.upnext.productservice.contracts.motherboards.MotherboardRequest;
import org.upnext.productservice.contracts.motherboards.MotherboardResponse;
import org.upnext.productservice.entities.Motherboard;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MotherboardMapper extends AbstractProductMapper<Motherboard, MotherboardRequest, MotherboardResponse> {

}
