package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.upnext.productservice.contracts.ramkits.RAMKitRequest;
import org.upnext.productservice.contracts.ramkits.RAMKitResponse;
import org.upnext.productservice.entities.RAMKit;

@Mapper(componentModel = "spring")
public interface RAMKitMapper extends AbstractProductMapper<RAMKit, RAMKitRequest, RAMKitResponse> {
}
