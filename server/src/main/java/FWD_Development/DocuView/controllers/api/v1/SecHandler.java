package FWD_Development.DocuView.controllers.api.v1;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.bind.annotation.RequestMethod;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class SecHandler {

    public static boolean checkToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();

        // Skip token validation for /api/v1/auth and its children

        String token = request.getHeader("Authorization");
        //"{\"error\": \"Unauthorized: Invalid or missing token\"}"
        if (token == null || !JWTHandler.validateToken(token)) {
            return false;
        }
        return true;
    }
    //if(!SecHandler.checkToken())
	//	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
}