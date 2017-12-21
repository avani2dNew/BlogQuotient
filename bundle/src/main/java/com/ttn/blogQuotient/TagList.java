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
        String namespacePath = get("namespacePath", String.class);

        ResourceResolver resolver = getResourceResolver();
        TagManager tagManager = resolver.adaptTo(TagManager.class);
        Iterator<Tag> tagIter = tagManager.resolve(namespacePath).listChildren();
        List<Tag> tagList = new ArrayList<Tag>();
        while (tagIter.hasNext()) {
            Tag tag = tagIter.next();
            tagList.add(tag);
            Iterator<Tag> childTagIter = tag.listAllSubTags();
            while (childTagIter.hasNext()) {
                tagList.add(childTagIter.next());
            }
        }
        tags = tagList;
    }

    public List<Tag> getTags() {
        return tags;
    }
}
