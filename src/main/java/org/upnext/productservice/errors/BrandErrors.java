package org.upnext.productservice.errors;

import org.springframework.http.HttpStatus;
import org.upnext.sharedlibrary.Errors.Error;

public class BrandErrors {
    public static final Error BrandNotFound = new Error("Brand.NotFound",
            "No Brand was found with given id",
            HttpStatus.NOT_FOUND.value());

    public static final Error DuplicatedBrandName =
            new Error("Brand.DuplicatedName", "Another Brand with the same Name is already exists", HttpStatus.CONFLICT.value());
}
