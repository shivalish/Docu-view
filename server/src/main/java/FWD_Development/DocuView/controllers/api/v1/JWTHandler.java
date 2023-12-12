package FWD_Development.DocuView.controllers.api.v1;

import java.util.Date;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import jakarta.xml.bind.DatatypeConverter;

import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JWTHandler {

    private static String SECRET_KEY = "rd1jbSdKCYQIskCbCjlIG3qGEt50fIz06XfX7c2rJSvVhplewnEcQNUJySeiWlm6lcCQiFPU1Sm0EtZfZCf7LzgVIRCdPrUR1ypQrCOKFcDpPRDBj9YvvHTMpWwQnoaEUMQdp31mGYfathatNPpbLezuTKs1xxiZRk1enj9k5qkEDDgWZdIlImKTka5yJiXk1vdSBJ9BlO4XzlgGO1nApTFHOOTFzE2ukMdRXpPLRLTGURDxzcL4NWWHAhiB5WCK";
    private static byte[] keyBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
    private static SecretKey key = Keys.hmacShaKeyFor(keyBytes);
    private static String ISSUER = "FWD_Development.DocuView";

    public static String createJWT(String id, String subject) throws NoSuchAlgorithmException {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        
        String jwt = Jwts.builder()
                .id(id)
                .issuedAt(now)
                .subject(subject)
                .issuer(ISSUER)
                .signWith(key)
                .compact();
                
        assert getPayload(jwt).getId().equals(id);
        return jwt;
    }

    public static String createJWT(String id, String subject, long ttlMillis) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .id(id)
                .issuedAt(now)
                .subject(subject)
                .issuer(ISSUER)
                .signWith(key);
        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.expiration(exp);
        }  
  
        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public static Boolean validateToken(String jwt) {
        if (jwt.startsWith("Bearer ")) jwt = jwt.substring(7);
        try {
            //This line will throw an exception if it is not a signed JWS (as expected)
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
            if (claims.getExpiration().after(new Date(System.currentTimeMillis()))) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static Claims getPayload(String jwt) {
        if (jwt.startsWith("Bearer ")) jwt = jwt.substring(7);
        if (!validateToken(jwt)) return null;
        Claims claims = (Claims)Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload();
        return claims;
    }

    public static String refreshJWT(String jwt) throws NoSuchAlgorithmException{
        if (jwt.startsWith("Bearer ")) jwt = jwt.substring(7);
        if (!validateToken(jwt)) return null; // will prevent NoSuchAlgorithmException
        Claims claims = getPayload(jwt);
        if (claims.getExpiration().after(new Date(System.currentTimeMillis()))) {
            // do not simplify, for some reason it will be null if simplified
            String id = claims.getId();
            String subject = claims.getSubject();
            return createJWT(id, subject, 1800000);
        }
        return null;
    }

    
    
}