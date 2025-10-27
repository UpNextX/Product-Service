package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.upnext.productservice.contracts.psus.PSURequest;
import org.upnext.productservice.contracts.psus.PSUResponse;
import org.upnext.productservice.entities.PSU;

@Mapper(componentModel = "spring")
public interface PSUMapper extends AbstractProductMapper<PSU, PSURequest, PSUResponse> {
}
