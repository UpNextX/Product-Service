package org.upnext.productservice.errors;

import org.springframework.http.HttpStatus;
import org.upnext.sharedlibrary.Errors.Error;

public class CategoryErrors {
    public static final Error CategoryNotFound = new Error("Category.NotFound",
            "No Category was found with given id",
            HttpStatus.NOT_FOUND.value());

    public static final Error DuplicatedCategoryName =
            new Error("Category.DuplicatedName", "Another Category with the same Name is already exists", HttpStatus.CONFLICT.value());
}
