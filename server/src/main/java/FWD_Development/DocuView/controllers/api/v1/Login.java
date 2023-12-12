package FWD_Development.DocuView.controllers.api.v1;

/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Iterator;
import java.util.HashMap;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.bind.annotation.RequestParam;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import com.fasterxml.jackson.databind.node.TextNode;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/v1/auth")
public class Login {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static String getBearerTokenHeader() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
          return null;
        }
        return requestAttributes.getRequest().getHeader(AUTHORIZATION_HEADER);
    }

    
    @PostMapping("/login")
    public ResponseEntity<JsonNode> login(@RequestParam String username, @RequestParam String password) throws NoSuchAlgorithmException {
        String jwt; 

        String sqlQuery = "SELECT * FROM users WHERE username = ?;";
        try {
            Map<String, Object> user = new HashMap<String, Object>();
            user = jdbcTemplate.queryForMap(sqlQuery, username);
        } catch (EmptyResultDataAccessException e) {
            ObjectNode json = mapper.createObjectNode();
            json.put("message", "Invalid username");
            json.put("authToken", "");
            return ResponseEntity
                .status(401)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
        }
        
        try {
            Map<String, Object> user = new HashMap<String, Object>();
            user = jdbcTemplate.queryForMap(sqlQuery, username);
            jwt = JWTHandler.createJWT(username,"user", 1800000);
            String encodedPassword = (String) user.get("password");
            if (passwordEncoder().matches(password, encodedPassword)) {
                ObjectNode json = mapper.createObjectNode();
                json.put("message", "Login successful");
                json.put("authToken", jwt);
                return ResponseEntity
                    .status(200)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
            }
            else {
                ObjectNode json = mapper.createObjectNode();
                json.put("message", "Invalid password");
                json.put("authToken", "");
                return ResponseEntity
                    .status(401)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json);
            }
        } catch (EmptyResultDataAccessException e) {
            ObjectNode json = mapper.createObjectNode();
            json.put("message", "Invalid password");
            json.put("token", "");
            return ResponseEntity
                .status(401)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<JsonNode> signup(@RequestParam String username, @RequestParam String password) throws NoSuchAlgorithmException {
        String sqlQuery = "SELECT * FROM users WHERE username = ?;";

        try {
            Map<String, Object> user = new HashMap<String, Object>();
            user = jdbcTemplate.queryForMap(sqlQuery, username);
            ObjectNode json = mapper.createObjectNode();
            json.put("message", "Username already exists");
            return ResponseEntity
                .status(401)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
        } catch (EmptyResultDataAccessException e) {
            String sqlQuery1 = "INSERT INTO users (username, password) VALUES (?, ?);";
            String encodedPassword = passwordEncoder().encode(password);
            jdbcTemplate.update(sqlQuery1, username, encodedPassword);
            ObjectNode json = mapper.createObjectNode();
            json.put("message", "Signup successful");
            return ResponseEntity
                .status(200)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
        }
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JsonNode> refresh(@RequestHeader("Authorization") String auth) throws NoSuchAlgorithmException {
        if(!SecHandler.checkToken())
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String jwt = JWTHandler.refreshJWT(auth);
        if (jwt == null) {
            ObjectNode json = mapper.createObjectNode();
            json.put("message", "Something went wrong");
            json.put("authToken", "");
            return ResponseEntity
                .status(401)
                .contentType(MediaType.APPLICATION_JSON)
                .body(json);
        }
        ObjectNode json = mapper.createObjectNode();
        json.put("message", "Refresh successful");
        json.put("authToken", jwt);
        return ResponseEntity
            .status(200)
            .contentType(MediaType.APPLICATION_JSON)
            .body(json);
    }

}
