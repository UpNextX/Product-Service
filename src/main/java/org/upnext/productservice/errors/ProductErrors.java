package org.upnext.productservice.errors;

import org.springframework.http.HttpStatus;
import org.upnext.sharedlibrary.Errors.Error;

public class ProductErrors {

    public static final Error ProductNotFound =
            new Error(
                    "Product.NotFound",
                    "No Product was found with given id",
                    HttpStatus.NOT_FOUND.value()
            );
}
