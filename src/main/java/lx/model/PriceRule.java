package lx.model;

import java.math.BigDecimal;

/** A title keyword and the inclusive price range it represents. */
public class PriceRule {
    private final String keyword;
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;

    public PriceRule(String keyword, BigDecimal minPrice, BigDecimal maxPrice) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("价格规则关键词不能为空");
        }
        if (minPrice == null && maxPrice == null) {
            throw new IllegalArgumentException("价格规则至少需要设置一个价格边界");
        }
        if (minPrice != null && maxPrice != null && minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("价格规则最低价不能高于最高价");
        }
        this.keyword = keyword.trim();
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean matches(String title) {
        return title != null && title.contains(keyword);
    }

    public boolean accepts(BigDecimal price) {
        if (price == null) {
            return false;
        }
        return (minPrice == null || price.compareTo(minPrice) >= 0)
                && (maxPrice == null || price.compareTo(maxPrice) <= 0);
    }

    public String describe() {
        return keyword + " [" + (minPrice == null ? "-" : minPrice)
                + ", " + (maxPrice == null ? "-" : maxPrice) + "]";
    }
}
