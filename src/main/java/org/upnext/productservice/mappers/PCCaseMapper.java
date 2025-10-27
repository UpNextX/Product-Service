package org.upnext.productservice.mappers;

import org.mapstruct.Mapper;
import org.upnext.productservice.contracts.pccases.PCCaseRequest;
import org.upnext.productservice.contracts.pccases.PCCaseResponse;
import org.upnext.productservice.entities.PCCase;

@Mapper(componentModel = "spring")
public interface PCCaseMapper extends AbstractProductMapper<PCCase, PCCaseRequest, PCCaseResponse> {
}
