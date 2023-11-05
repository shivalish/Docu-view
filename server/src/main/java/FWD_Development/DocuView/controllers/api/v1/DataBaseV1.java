package FWD_Development.DocuView.controllers.api.v1;


/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Iterator;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
/* CUSTOM ADDED LIBS */

import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import FWD_Development.DocuView.controllers.api.v1.DataBaseTree.DataBaseNode;

import java.sql.ResultSetMetaData;

import org.springframework.dao.EmptyResultDataAccessException;
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

// All this stuff, evil.
// better impletion form, make tree that maps out all TABLES and has data of every table, THEN use that to navigate the database

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/database")
public class DataBaseV1 {

	@Autowired
    	private JdbcTemplate jdbcTemplate;

	@GetMapping("")
	public List<Map<String,Object>> getDocs(@RequestParam MultiValueMap<String,String> allRequestParams){
		String filters =   Hardcoded.dataBaseTree.generateFilterQuery(allRequestParams);
		if (filters.equals("")) {filters = "TRUE";}
		String query = Hardcoded.dataBaseTree.getTreeInnerJoin() + " WHERE " + filters;
		return jdbcTemplate.queryForList(query);
	}

	@GetMapping("/content")
	public ResponseEntity<Map<String, Object>> getContent(@RequestParam MultiValueMap<String,String> allRequestParams){
		allRequestParams.addIfAbsent("per_page", "50");
		allRequestParams.addIfAbsent("page", "1");
		int per_page = Math.min(Math.max(1, Integer.valueOf(allRequestParams.getFirst("per_page"))), 100);
		int page = Integer.valueOf(allRequestParams.getFirst("page"));
		String innerJoin = Hardcoded.dataBaseTree.getTreeInnerJoin();
		String filters = Hardcoded.dataBaseTree.generateFilterQuery(allRequestParams);
		String query = "SELECT COUNT(*) FROM (" + innerJoin + " WHERE " + filters + ") AS x;";
		Map<String, Object> data = jdbcTemplate.queryForList(query).get(0); //.queryForList(query);
		
		if (data.isEmpty()) {
            		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        	}
		data.put("count", data.remove("COUNT(*)"));
        data.put("page", page);
        data.put("per_page", per_page);
        data.put("total_pages", (((Long) data.get("count"))/per_page) + 1);
        return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@GetMapping("/help")
	public ResponseEntity<String> getHelp(@RequestParam Map<String,String> allRequestParams){
		return new ResponseEntity<>(Hardcoded.dataBaseTree.getURIquery("/api/v1/database"), HttpStatus.OK);
	}
	
};
