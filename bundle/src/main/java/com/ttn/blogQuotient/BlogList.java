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
public class BlogList extends WCMUsePojo {

    private static Logger logger = LoggerFactory.getLogger(BlogList.class);
    private List<Blog> blogs;

    @Override
    public void activate() {
        String resourcePath = get("resourcePath", String.class);
        String listingType = get("listingType", String.class);

        ResourceResolver resolver = getResourceResolver();
        PageManager pageManager = resolver.adaptTo(PageManager.class);
        Page page = pageManager.getPage(resourcePath);
        if (listingType.equalsIgnoreCase(ListType.RECENT.toString())) {
            blogs = fetchRecentBlogs(page.listChildren());
        } else if (listingType.equalsIgnoreCase(ListType.POPULAR.toString())) {
            blogs = fetchPopularBlogs(page.listChildren());
        } else if (listingType.equalsIgnoreCase(ListType.RELATED.toString())) {
            TagManager tagManager = resolver.adaptTo(TagManager.class);
            Resource pageContent = page.getContentResource();
            Tag[] tags = tagManager.getTags(pageContent);
            Set<String> nameSet = new HashSet<String>();
            List<Blog> blogList = new ArrayList<Blog>();
            for (Tag tag : tags) {
                Iterator<Resource> it = tag.find();
                while (it.hasNext()) {
                    Resource resource = it.next();
                    if (!resource.getPath().equals(pageContent.getPath()) && !nameSet.contains(resource.getPath())) {
                        blogList.add(bindResourceToBlog(resource));
                        nameSet.add(resource.getPath());
                    }
                }
            }
            blogs = blogList;
        } else if (listingType.equalsIgnoreCase(ListType.BY_USER.toString())) {
            Session session = resolver.adaptTo(Session.class);
            String userId = session.getUserID();
            blogs = fetchBlogsByUser(page.listChildren(), userId);
        } else {
            blogs = fetchAllBlogs(page.listChildren());
        }
    }

    List<Blog> fetchPopularBlogs(Iterator<Page> childPages) {
        List<Blog> blogList = fetchAllBlogs(childPages);
        try {
            Collections.sort(blogList, new Comparator<Blog>() {
                public int compare(Blog o1, Blog o2) {
                    return o2.getViewCount().compareTo(o1.getViewCount());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blogList;
    }

    List<Blog> fetchBlogsByUser(Iterator<Page> childPages, String userId) {
        List<Blog> resources = new ArrayList<Blog>();
        Resource resource;
        ValueMap vm;
        while (childPages.hasNext()) {
            resource = childPages.next().getContentResource();
            vm = resource.getValueMap();
            if (vm.get("jcr:createdBy").toString().equals(userId)) {
                resources.add(bindResourceToBlog(resource));
            }
        }
        return resources;
    }

    List<Blog> fetchRecentBlogs(Iterator<Page> childPages) {
        List<Blog> blogList = fetchAllBlogs(childPages);
        try {
            Collections.sort(blogList, new Comparator<Blog>() {
                public int compare(Blog o1, Blog o2) {
                    return o2.getPublishDate().compareTo(o1.getPublishDate());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blogList;
    }

    List<Blog> fetchAllBlogs(Iterator<Page> childPages) {
        List<Blog> resources = new ArrayList<Blog>();
        while (childPages.hasNext()) {
            resources.add(bindResourceToBlog(childPages.next().getContentResource()));
        }
        return resources;
    }

    Blog bindResourceToBlog(Resource resource) {
        ValueMap valueMap = resource.getValueMap();
        Blog blog = new Blog();
        blog.setName(resource.getName());
        blog.setPath(resource.getParent().getPath());
        blog.setResourceType(resource.getResourceType());
        blog.setResourceSuperType(resource.getResourceSuperType());
        blog.setAuthor(valueMap.get("jcr:createdBy").toString());
        blog.setPublishDate(fetchDate((Calendar) valueMap.get("jcr:created")));
        blog.setPublishDateString(fetchDateString((Calendar) valueMap.get("jcr:created")));
        blog.setBlogTitle(valueMap.get("blogTitle").toString());
        blog.setBlogDescription(valueMap.get("blogDescription").toString());
        blog.setBlogSubTitle(valueMap.get("blogSubTitle").toString());
        blog.setViewCount(Integer.parseInt(valueMap.get("viewCount").toString()));
//        blog.setTags(valueMap.get("Tags").toString());
        return blog;
    }

    Date fetchDate(Calendar cal) {
        try {
            return cal.getTime();
        } catch (Exception e) {
            logger.debug("****Error in parsing blog dates.");
            return new Date();
        }
    }

    String fetchDateString(Calendar cal) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("dd MMM, yyyy HH:mm a");
            return format.format(cal.getTime());
        } catch (Exception e) {
            logger.debug("****Error in parsing blog dates.");
            return new Date().toString();
        }
    }

    public List<Blog> getBlogs() {
        return blogs;
    }
}
