package FWD_Development.DocuView.controllers.api.v1;


/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.api.services.drive.Drive;

import org.springframework.dao.EmptyResultDataAccessException;

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
@RequestMapping("/api/v1/fileshare")
public class FileShareV1 {

    //private final GoogleDriveService googleDriveService;
//
    //@Autowired
    //public FileShareV1(GoogleDriveService googleDriveService) {
    //    this.googleDriveService = googleDriveService;
    //}

};
