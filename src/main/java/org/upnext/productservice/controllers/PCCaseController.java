package org.upnext.productservice.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import org.upnext.productservice.contracts.pccases.PCCaseRequest;
import org.upnext.productservice.contracts.pccases.PCCaseResponse;
import org.upnext.productservice.services.PCCaseServices;
import org.upnext.sharedlibrary.Errors.Error;
import org.upnext.sharedlibrary.Errors.Result;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/pccases")
@RequiredArgsConstructor
public class PCCaseController {

    private final PCCaseServices pcCaseServices;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Result<PCCaseResponse> result = pcCaseServices.findById(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result.getValue())
                : response(result.getError());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(
            @Valid @RequestPart PCCaseRequest pcCase,
            @RequestPart MultipartFile image,
            HttpServletRequest request,
            UriComponentsBuilder urb,
            Authentication authentication
    ) {
        Result<URI> result = pcCaseServices.create(pcCase, image, request, urb);
        return result.isSuccess()
                ? ResponseEntity.created(result.getValue()).build()
                : response(result.getError());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestPart PCCaseRequest pcCase,
            @RequestPart MultipartFile image,
            HttpServletRequest request
    ) {
        Result result = pcCaseServices.update(id, pcCase, image, request);
        return result.isSuccess()
                ? ResponseEntity.noContent().build()
                : response(result.getError());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Result result = pcCaseServices.delete(id);
        return result.isSuccess()
                ? ResponseEntity.noContent().build()
                : response(result.getError());
    }

    @GetMapping
    public ResponseEntity<List<PCCaseResponse>> findAll(Pageable pageable) {
        Result<List<PCCaseResponse>> result = pcCaseServices.findAll(pageable);
        return ResponseEntity.ok(result.getValue());
    }

    private ResponseEntity<?> response(Error error) {
        return ResponseEntity.status(error.getStatusCode()).body(error.getMessage());
    }
}
