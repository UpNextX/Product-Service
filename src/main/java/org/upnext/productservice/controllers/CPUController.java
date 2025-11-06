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
import org.upnext.productservice.contracts.cpus.CPURequest;
import org.upnext.productservice.contracts.cpus.CPUResponse;
import org.upnext.productservice.services.CPUServices;
import org.upnext.sharedlibrary.Errors.Error;
import org.upnext.sharedlibrary.Errors.Result;

import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/cpus")
@RequiredArgsConstructor
public class CPUController {
    private final CPUServices cpuServices;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Result<CPUResponse> result = cpuServices.findById(id);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result.getValue());
        }

        return response(result.getError());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity save
            (
                    @Valid @RequestPart CPURequest cpu,
                    @RequestPart MultipartFile image,
                    HttpServletRequest request,
                    UriComponentsBuilder urb,
                    Authentication authentication
            )
    {
        Result<URI> result = cpuServices.create(cpu, image, request, urb);
        if (result.isSuccess()) {
            return ResponseEntity.created(result.getValue()).build();
        }

        return response(result.getError());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        Result result = cpuServices.delete(id);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }

        return response(result.getError());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity update(@PathVariable Long id, @Valid @RequestPart CPURequest cpu, @RequestPart(required = false) MultipartFile image, HttpServletRequest request) {
        Result result = cpuServices.update(id, cpu, image, request);
        if (result.isSuccess()) {
            return ResponseEntity.noContent().build();
        }
        return response(result.getError());
    }


    @GetMapping
    public ResponseEntity<List<CPUResponse>> findAll(
//            @RequestParam(required = false) String word,
//            @RequestParam(required = false) String category,
//            @RequestParam(required = false) String brand,
//            @RequestParam(required = false) Double minPrice,
//            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        Result<List<CPUResponse>> result = cpuServices.findAll(pageable);

        return ResponseEntity.ok(result.getValue());
    }


    private ResponseEntity<?> response(Error error) {
        int status = error.getStatusCode();
        String errorMessage = error.getMessage();
        return ResponseEntity.status(status).body(errorMessage);
    }
}
