package com.ttn.blogQuotient;

import com.adobe.cq.sightly.WCMUsePojo;
import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.ttn.blogQuotient.dto.Blog;
import com.ttn.blogQuotient.enums.ListType;
import org.apache.felix.scr.annotations.Component;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.text.SimpleDateFormat;
import java.util.*;

@Component(immediate = true)
public class TagList extends WCMUsePojo {

    private static Logger logger = LoggerFactory.getLogger(TagList.class);
    private List<Tag> tags;

    @Override
    public void activate() {
        String resourcePath = get("resourcePath", String.class);

        ResourceResolver resolver = getResourceResolver();
        TagManager tagManager = resolver.adaptTo(TagManager.class);
        Tag[] tagArray = tagManager.getTagsForSubtree(resolver.getResource(resourcePath), true);
        tags = Arrays.asList(tagArray);
    }

    public List<Tag> getTags() {
        return tags;
    }
}
