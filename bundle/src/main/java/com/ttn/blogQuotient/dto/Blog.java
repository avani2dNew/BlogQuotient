package com.ttn.blogQuotient.dto;

import java.util.Date;

public class Blog extends ResourceDTO {

    String blogTitle;
    String blogDescription;
    String blogSubTitle;
    String tags;
    Integer viewCount;

    public Blog() {
    }

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogDescription() {
        return blogDescription;
    }

    public void setBlogDescription(String blogDescription) {
        this.blogDescription = blogDescription;
    }

    public String getBlogSubTitle() {
        return blogSubTitle;
    }

    public void setBlogSubTitle(String blogSubTitle) {
        this.blogSubTitle = blogSubTitle;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
}
