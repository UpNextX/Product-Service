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
import org.upnext.productservice.contracts.psus.PSURequest;
import org.upnext.productservice.contracts.psus.PSUResponse;
import org.upnext.productservice.services.PSUServices;
import org.upnext.sharedlibrary.Errors.Error;
import org.upnext.sharedlibrary.Errors.Result;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/psus")
@RequiredArgsConstructor
public class PSUController {

    private final PSUServices psuServices;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Result<PSUResponse> result = psuServices.findById(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result.getValue())
                : response(result.getError());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(
            @Valid @RequestPart PSURequest psu,
            @RequestPart MultipartFile image,
            HttpServletRequest request,
            UriComponentsBuilder urb,
            Authentication authentication
    ) {
        Result<URI> result = psuServices.create(psu, image, request, urb);
        return result.isSuccess()
                ? ResponseEntity.created(result.getValue()).build()
                : response(result.getError());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestPart PSURequest psu,
            @RequestPart(required = false) MultipartFile image,
            HttpServletRequest request
    ) {
        Result result = psuServices.update(id, psu, image, request);
        return result.isSuccess()
                ? ResponseEntity.noContent().build()
                : response(result.getError());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Result result = psuServices.delete(id);
        return result.isSuccess()
                ? ResponseEntity.noContent().build()
                : response(result.getError());
    }

    @GetMapping
    public ResponseEntity<List<PSUResponse>> findAll(Pageable pageable) {
        Result<List<PSUResponse>> result = psuServices.findAll(pageable);
        return ResponseEntity.ok(result.getValue());
    }

    private ResponseEntity<?> response(Error error) {
        return ResponseEntity.status(error.getStatusCode()).body(error.getMessage());
    }
}
