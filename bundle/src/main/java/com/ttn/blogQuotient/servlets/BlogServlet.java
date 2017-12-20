package com.ttn.blogQuotient.servlets;

import com.ttn.blogQuotient.service.BlogService;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import java.io.IOException;

@SlingServlet(paths = {"/bin/servlet/blog"}, methods = {"GET"}, name = "Blog Servlet")
public class BlogServlet extends SlingSafeMethodsServlet {

    @Reference
    BlogService blogService;

    private static Logger logger = LoggerFactory.getLogger(BlogServlet.class);

    public void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {
        try {
            response.getWriter().print(blogService.getTag("blogQuotient:entertainment/movies"));
        } catch (Exception e) {
            logger.debug("Error while getting tag.");
        }
    }
}