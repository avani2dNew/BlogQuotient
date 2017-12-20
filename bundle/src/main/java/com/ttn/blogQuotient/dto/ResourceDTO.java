package com.ttn.blogQuotient.dto;

import java.util.Date;

public class ResourceDTO {

    String name;
    String path;
    String resourceType;
    String resourceSuperType;
    String author;
    Date publishDate;
    String publishDateString;

    public ResourceDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceSuperType() {
        return resourceSuperType;
    }

    public void setResourceSuperType(String resourceSuperType) {
        this.resourceSuperType = resourceSuperType;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishDateString() {
        return publishDateString;
    }

    public void setPublishDateString(String publishDateString) {
        this.publishDateString = publishDateString;
    }
}
