package services;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class SessionHelper {
    public void removeMessageFromSession(){
        String message = null;
        try{
            HttpSession session = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");
//            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
//            message = (String) session.getAttribute("message");
//            session.removeAttribute("message");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
