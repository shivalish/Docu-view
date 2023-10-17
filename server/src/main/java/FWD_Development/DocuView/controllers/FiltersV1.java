package FWD_Development.DocuView.controllers;


/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
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
@RequestMapping("/api/v1/filters")
public class FiltersV1 {
    private ObjectMapper objMapper = new ObjectMapper();
    private String[][] filtersArray = {
        {"file_creation", "ISO 8601 (YYYY-MM-DD)"},
        {"document_type", "string"},
        {"file_name", "string"},
        {"customer_name", "string"},
        {"auction_type", "string"},
        {"proposal_type", "string"},
        {"project_type", "string"},
        {"commitment_date_start", "ISO 8601 (YYYY-MM-DD)"},
        {"commitment_date_end", "ISO 8601 (YYYY-MM-DD)"},
        {"auction_date_start", "ISO 8601 (YYYY-MM-DD)"},
        {"auction_date_end", "ISO 8601 (YYYY-MM-DD)"},
        {"proposal_date_start", "ISO 8601 (YYYY-MM-DD)"},
        {"proposals_date_end", "ISO 8601 (YYYY-MM-DD)"},
    };
    

    @GetMapping("")
    public JsonNode filters() {
        ObjectNode rootNode = objMapper.createObjectNode();
        ObjectNode filtersNode = objMapper.createObjectNode();
        rootNode.set("list", filtersNode);
        for (String[] item : filtersArray){
            filtersNode.put(item[0], item[1]);
        }
        

        
        return rootNode;
    }
}
