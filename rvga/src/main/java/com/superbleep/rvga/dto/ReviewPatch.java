package com.superbleep.rvga.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

public class ReviewPatch {
    @Range(min = 0, max = 10)
    private Integer rating;
    private String comment;

    public ReviewPatch(Integer rating, String comment) {
        this.rating = rating;
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
