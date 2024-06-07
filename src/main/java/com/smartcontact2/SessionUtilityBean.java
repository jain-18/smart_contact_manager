package com.smartcontact2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.hibernate.query.sqm.tree.SqmNode.log;

@Component
public class SessionUtilityBean {
    public void removeMessageFromSession() {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            HttpSession session = request.getSession();
            session.removeAttribute("message");
        } catch (RuntimeException ex) {
            // Handle any exceptions
            log.error("Error removing message from session: ", ex);
        }
    }
}
