package FWD_Development.DocuView.controllers.api.v1;


/* CUSTOM ADDED LIBS */
import java.util.Map;
import java.util.HashMap;
/* CUSTOM ADDED LIBS */

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

// FILTERS
//      file_creation
//      document_type
//      file_name
//      customer_name
//      auction_type
//      proposal_type
//      project_type
//      commitment_date_start
//      commitment_date_end
//      auction_date_start
//      auction_date_end
//      proposal_date_start
//      proposals_date_end


@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/Callback")
public class Callback {

    @GetMapping("")
	public ResponseEntity<Map<String,Object>> getDocs(@RequestParam MultiValueMap<String,String> allRequestParams){
        Map<String,Object> data = new HashMap<String,Object>();
		return new ResponseEntity<Map<String,Object>>(data, HttpStatus.OK);
	}

    //private final GoogleDriveService googleDriveService;
//
    //@Autowired
    //public FileShareV1(GoogleDriveService googleDriveService) {
    //    this.googleDriveService = googleDriveService;
    //}

};
