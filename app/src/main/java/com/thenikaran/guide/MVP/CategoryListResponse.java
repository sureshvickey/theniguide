
package com.thenikaran.guide.MVP;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryListResponse {

    private String categoryId;
    private String categoryName;
    private String category_image;
    private List<Place> place = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return category_image;
    }

    public void setCategoryImage(String categoryImage) {
        this.category_image = categoryImage;
    }

    public List<Place> getPlace() {
        return place;
    }

    public void setPlace(List<Place> place) {
        this.place = place;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
