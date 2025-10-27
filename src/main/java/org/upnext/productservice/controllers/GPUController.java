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
import org.upnext.productservice.contracts.gpus.GPURequest;
import org.upnext.productservice.contracts.gpus.GPUResponse;
import org.upnext.productservice.services.GPUServices;
import org.upnext.sharedlibrary.Errors.Error;
import org.upnext.sharedlibrary.Errors.Result;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/gpus")
@RequiredArgsConstructor
public class GPUController {

    private final GPUServices gpuServices;

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Result<GPUResponse> result = gpuServices.findById(id);
        return result.isSuccess()
                ? ResponseEntity.ok(result.getValue())
                : response(result.getError());
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> create(
            @Valid @RequestPart GPURequest gpu,
            @RequestPart MultipartFile image,
            HttpServletRequest request,
            UriComponentsBuilder urb,
            Authentication authentication
    ) {
        Result<URI> result = gpuServices.create(gpu, image, request, urb);
        return result.isSuccess()
                ? ResponseEntity.created(result.getValue()).build()
                : response(result.getError());
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestPart GPURequest gpu,
            @RequestPart MultipartFile image,
            HttpServletRequest request
    ) {
        Result result = gpuServices.update(id, gpu, image, request);
        return result.isSuccess()
                ? ResponseEntity.noContent().build()
                : response(result.getError());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Result result = gpuServices.delete(id);
        return result.isSuccess()
                ? ResponseEntity.noContent().build()
                : response(result.getError());
    }

    @GetMapping
    public ResponseEntity<List<GPUResponse>> findAll(Pageable pageable) {
        Result<List<GPUResponse>> result = gpuServices.findAll(pageable);
        return ResponseEntity.ok(result.getValue());
    }

    private ResponseEntity<?> response(Error error) {
        return ResponseEntity.status(error.getStatusCode()).body(error.getMessage());
    }
}
