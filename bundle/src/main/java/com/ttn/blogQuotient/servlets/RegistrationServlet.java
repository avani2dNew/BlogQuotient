package com.ttn.blogQuotient.servlets;

import com.ttn.blogQuotient.dto.CommonResponseDTO;
import com.ttn.blogQuotient.dto.UserDetails;

import java.io.IOException;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.jcr.PropertyType;
import javax.jcr.Session;
import javax.servlet.ServletException;

import com.ttn.blogQuotient.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SlingServlet(paths = {"/bin/servlet/register"}, methods = {"POST"}, name = "Registration Servlet")
public class RegistrationServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Reference
    UserService userService;

    private static Logger logger = LoggerFactory.getLogger(RegistrationServlet.class);

    public void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException, ServletException {
        CommonResponseDTO responseDTO = userService.registerUser(getUserDetails(request));
        request.setAttribute("msg", responseDTO.getMessage());
        if (responseDTO.getStatus()) {
            request.getRequestDispatcher("/content/blogQuotient/welcome.html").forward(request, response);
        } else {
            request.getRequestDispatcher("/content/blogQuotient/home.html").forward(request, response);
        }
    }

    UserDetails getUserDetails(SlingHttpServletRequest request) {
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(request.getParameter("firstName"));
        userDetails.setLastName(request.getParameter("lastName"));
        userDetails.setUsername(request.getParameter("username"));
        userDetails.setGroupname(request.getParameter("groupname"));
        userDetails.setPassword(request.getParameter("password"));
        userDetails.setEmail(request.getParameter("email"));
        userDetails.setGender(request.getParameter("gender"));
        userDetails.setDateOfBirth(request.getParameter("dateOfBirth"));
        return userDetails;
    }

    protected final void doGet(final SlingHttpServletRequest request,
                               final SlingHttpServletResponse response) throws IOException, ServletException {
        doPost(request, response);
    }
}