package org.upnext.productservice.helpers;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Root;
import org.upnext.productservice.entities.Product;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;


public class ProductSpecification {

    public static Specification<Product> hasWord(String searchWord) {
        return (root, query, cb) ->
        {
            if (searchWord == null || searchWord.trim().isEmpty()) {
                return cb.conjunction();
            }

//            if (searchWord.contains(" ") || searchWord.length() >= 3)
//                return hasWordFullText(searchWord).toPredicate(root, query, cb);

            String pattern = "%" + searchWord.toLowerCase() + "%";
            return cb.or(cb.like(cb.lower(root.get("name")), pattern), cb.like(cb.lower(root.get("description")), pattern));
        };
    }

    private static Specification<Product> hasWordFullText(String searchWord) {
        return (root, query, cb) -> {
            // Return true predicate if keyword is null or empty
            if (searchWord == null || searchWord.trim().isEmpty()) {
                return cb.conjunction();
            }

            // Convert keyword to tsquery format
            String formattedTsQuery = formatTsQuery(searchWord);

            // Create combined expression of name and description
            Expression<String> nameAndDescription = createNameAndDescriptionExpression(root, cb);

            // Create full-text search predicate
            return createFullTextSearchPredicate(cb, nameAndDescription, formattedTsQuery);
        };
    }

    // Format keyword into PostgreSQL tsquery format
    private static String formatTsQuery(String searchKeyword) {
        return searchKeyword.trim()
                .replaceAll("[^\\w\\s]", "") // Remove special characters
                .replaceAll("\\s+", " & ");  // Join words with AND operator
    }

    // Combine name and description fields for full-text search
    private static Expression<String> createNameAndDescriptionExpression(
            Root<Product> root,
            CriteriaBuilder cb
    ) {
        return cb.concat(
                cb.coalesce(root.get("name"), ""),
                cb.concat(
                        cb.literal(" "),
                        cb.coalesce(root.get("description"), "")
                )
        );
    }

    // Create predicate for full-text search matching
    private static Predicate createFullTextSearchPredicate(
            CriteriaBuilder cb,
            Expression<String> nameAndDescription,
            String formattedTsQuery
    ) {
        Expression<Boolean> fullTextMatch = cb.equal(
                cb.function(
                        "@@",
                        Boolean.class,
                        cb.function(
                                "to_tsvector",
                                String.class,
                                cb.literal("english"),
                                nameAndDescription
                        ),
                        cb.function(
                                "to_tsquery",
                                String.class,
                                cb.literal("english"),
                                cb.literal(formattedTsQuery)
                        )
                ),
                cb.literal(true)
        );
        return (Predicate) fullTextMatch;
    }

    public static Specification<Product> hasCategory(String category) {
        return (root, query, cb) -> {
            if  (category == null || category.trim().isEmpty()) {
                return cb.conjunction();
            }

            return cb.equal(root.get("category").get("name"), category);
        };
    }
    public static Specification<Product> hasBrand(String brand) {
        return (root, query, cb) -> {
            if  (brand == null || brand.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("brand").get("name"), brand);
        };
    }

    public static Specification<Product> hasMinPrice(Double price) {
        return (root, query, cb) -> {
            if  (price == null || price < 0) {
                return cb.conjunction();
            }

            return cb.greaterThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Product> hasMaxPrice(Double price) {
        return (root, query, cb) -> {
            if  (price == null || price < 0) {
                return cb.conjunction();
            }

            return cb.lessThanOrEqualTo(root.get("price"), price);
        };
    }

    public static Specification<Product> filters(String word, String category, String brand, Double minPrice, Double maxPrice) {
        return Specification.allOf(
                hasWord(word),
                hasCategory(category),
                hasBrand(brand),
                hasMinPrice(minPrice),
                hasMaxPrice(maxPrice)
        );
    }
}
