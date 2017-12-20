package com.ttn.blogQuotient.impl;

import com.ttn.blogQuotient.BQConstants;
import com.ttn.blogQuotient.dto.CommonResponseDTO;
import com.ttn.blogQuotient.dto.UserDetails;
import com.ttn.blogQuotient.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.*;
import org.apache.jackrabbit.api.security.user.Group;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.PropertyType;
import javax.jcr.Session;
import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
@Component(metatype = false, immediate = true)
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private ResourceResolver resourceResolver;

    @Reference
    ResourceResolverFactory resolverFactory;

    public CommonResponseDTO registerUser(UserDetails userDetails) {
        ResourceResolver adminResolver = null;
        Session adminSession = null;
        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put(ResourceResolverFactory.SUBSERVICE, "userService");
            adminResolver = resolverFactory.getServiceResourceResolver(params);

            adminSession = adminResolver.adaptTo(Session.class);
            final UserManager userManager = adminResolver.adaptTo(UserManager.class);
            String username = userDetails.getUsername();
            if (userManager.getAuthorizable(username) == null) {
                User user = userManager.createUser(username, userDetails.getPassword(), new SimplePrincipal(username), BQConstants.BQ_USER_ROOT);
                user.setProperty("./profile/givenName", adminSession.getValueFactory().createValue(userDetails.getFirstName(), PropertyType.STRING));
                user.setProperty("./profile/familyName", adminSession.getValueFactory().createValue(userDetails.getLastName(), PropertyType.STRING));
                user.setProperty("./profile/email", adminSession.getValueFactory().createValue(userDetails.getEmail(), PropertyType.STRING));
                user.setProperty("./profile/gender", adminSession.getValueFactory().createValue(userDetails.getGender(), PropertyType.STRING));

                Calendar calendar = Calendar.getInstance();
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                calendar.setTime(formatter.parse(userDetails.getDateOfBirth()));
                user.setProperty("./profile/birthday", adminSession.getValueFactory().createValue(calendar));
            } else {
                return new CommonResponseDTO(Boolean.FALSE, "User already exists.", null);
            }
            Group group = (Group) (userManager.getAuthorizable(userDetails.getGroupname()));
            group.addMember(userManager.getAuthorizable(username));

            if (!userManager.isAutoSave()) {
                adminSession.save();
            }
            return new CommonResponseDTO(Boolean.TRUE, "User created successfully.", null);
        } catch (Exception e) {
            logger.debug(e.getMessage());
            return new CommonResponseDTO(Boolean.FALSE, "Error while creating user.", null);
        } finally {
            if (adminResolver != null)
                adminResolver.close();
        }
    }

    private static class SimplePrincipal implements Principal {
        protected final String name;

        public SimplePrincipal(String name) {
            if (StringUtils.isBlank(name)) {
                throw new IllegalArgumentException("Principal name cannot be blank.");
            }
            this.name = name;
        }

        public String getName() {
            return name;
        }

        @Override
        public int hashCode() {
            return name.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Principal) {
                return name.equals(((Principal) obj).getName());
            }
            return false;
        }
    }
}
