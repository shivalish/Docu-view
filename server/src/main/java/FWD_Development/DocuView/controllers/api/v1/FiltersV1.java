package FWD_Development.DocuView.controllers.api.v1;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/filters")
public class FiltersV1{
    
    @GetMapping("")
    public ResponseEntity<JsonNode> filters() {
        //if(!SecHandler.checkToken())
		//	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       return ResponseEntity.ok().body(Hardcoded.outerArray);
    }
}
