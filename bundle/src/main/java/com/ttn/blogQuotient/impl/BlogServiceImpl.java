package com.ttn.blogQuotient.impl;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.ttn.blogQuotient.service.BlogService;
import org.apache.felix.scr.annotations.*;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
@Component(metatype = false, immediate = true)
public class BlogServiceImpl implements BlogService {

    private static Logger logger = LoggerFactory.getLogger(BlogServiceImpl.class);
    private ResourceResolver resourceResolver;

    @Reference
    ResourceResolverFactory resourceResolverFactory;

    @Activate
    public void init() {
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(ResourceResolverFactory.SUBSERVICE, "blogService");
            resourceResolver = resourceResolverFactory.getServiceResourceResolver(params);
        } catch (LoginException e) {
            logger.debug(e.getMessage());
        }
    }

    @Deactivate
    public void kill() {
        resourceResolver = null;
    }

    public Tag getTag(String tagID) {

        Tag myTag = resourceResolver.adaptTo(TagManager.class).resolve(tagID);
        return myTag;
    }
}