package FWD_Development.DocuView.controllers.api.v1;


/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.sql.rowset.spi.SyncResolver;

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
import FWD_Development.DocuView.controllers.api.v1.DataBaseTree.Query;

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

	private static String rename = "x.attach_proposal_attachment_type AS attachmentType, "
		+ "x.attach_proposal_proposal_id_project_type AS projectType, "
		+ "x.attach_proposal_proposal_id_proposal_label AS proposalLabel, "
		+ "x.attach_proposal_proposal_id_project_type_project_type AS projectType, "
		+ "x.attach_proposal_proposal_id_project_id_project_name AS projectName, "
		+ "x.attach_proposal_proposal_id_resource_id_resource_name AS resourceName, "
		+ "x.attach_proposal_proposal_id_resource_id_resource_type AS resourceType, "
		+ "x.attach_proposal_proposal_id_auction_id_auction_begin_date AS auctionBeginDate, "
		+ "x.attach_proposal_proposal_id_auction_id_auction_end_date AS auctionEndDate, "
		+ "x.attach_proposal_proposal_id_auction_id_auction_type AS auctionType, "
		+ "x.attach_proposal_proposal_id_auction_id_commitment_period_id_begin_date AS commitmentPeriodBeginDate, "
		+ "x.attach_proposal_proposal_id_auction_id_commitment_period_id_end_date AS commitmentPeriodEndDate, "
		+ "x.attach_proposal_proposal_id_auction_id_commitment_period_id_description AS commitmentPeriod, "
		+ "x.attach_proposal_proposal_id_auction_id_auction_period_id_begin_date AS auctionPeriodBeginDate, "
		+ "x.attach_proposal_proposal_id_auction_id_auction_period_id_description AS auctionPeriod, "
		+ "x.attach_proposal_proposal_id_auction_id_auction_period_id_end_date AS auctionPeriodEndDate, "
		+ "x.attach_proposal_proposal_id_customer_id_customer_id AS customerId, "	
		+ "x.attach_proposal_proposal_id_customer_id_customer_name AS customerName, "
		+ "x.attach_proposal_proposal_id_period_id_begin_date AS proposalPeriodBeginDate, "
		+ "x.attach_proposal_proposal_id_period_id_description AS proposalProposal, "
		+ "x.attach_proposal_proposal_id_period_id_end_date AS proposalPeriod, "
		+ "x.attach_proposal_attachment_type_application_category_type AS applicationCategoryType, " 
		+ "x.attach_proposal_attachment_type_attachment_type AS attachmentType, "
		+ "x.attach_proposal_attachment_type_description AS typeDescription, "
		+ "x.attach_proposal_attachment_id_attachment_id AS attachmentId, "
		+ "x.attach_proposal_attachment_id_file_name AS attachmentFileName, "
		+ "SUBSTRING_INDEX(x.attach_proposal_attachment_id_file_name, '.', -1) AS fileExtension, "
		+ "x.attach_proposal_attachment_id_create_date AS createDate, "
		+ "x.attach_proposal_attachment_id_description AS description";

	@GetMapping({"", "/infinite"})
	public ResponseEntity<List<Map<String, Object>>> getDocs(@RequestParam MultiValueMap<String,String> allRequestParams){
		var query =  Hardcoded.dataBaseTree.generateQuery(allRequestParams);
		
		return new ResponseEntity<>(
			jdbcTemplate.queryForList("SELECT " + rename +" FROM (" + query.parametrized + ") AS x;", query.params), 
			HttpStatus.OK
		);
	}

	@GetMapping("/pages")
	public ResponseEntity<List<Map<String, Object>>> getPages(
		@RequestParam MultiValueMap<String,String> allRequestParams, 
		@RequestParam(name = "perPage", required = false, defaultValue = "50") int perPage,
		@RequestParam(name = "page", required = false, defaultValue = "1") int page
		){
		perPage = Math.min(Math.max(1, perPage), 100);
		page = Math.max(1, page);
		var query = Hardcoded.dataBaseTree.generateQuery(allRequestParams);

		String sql = "SELECT " + rename +" FROM (" 
			+ query.parametrized + ") AS x LIMIT " 
			+ perPage + " OFFSET " 
			+ ((page-1) * perPage) + ";";
        return new ResponseEntity<>(
			jdbcTemplate.queryForList(sql, query.params), 
			HttpStatus.OK
		);
	}

	@GetMapping("/pages/content")
	public ResponseEntity<Map<String, Object>> getPagesContent(@RequestParam MultiValueMap<String,String> allRequestParams, 
		@RequestParam(name = "perPage", required = false, defaultValue = "50") int perPage,
		@RequestParam(name = "page", required = false, defaultValue = "1") int page
		){
		perPage = Math.min(Math.max(1, perPage), 100);
		page = Math.max(1, page);
		var query = Hardcoded.dataBaseTree.generateQuery(allRequestParams);
		String sql = "SELECT COUNT(*) FROM (" + query.parametrized + ") AS x;";
		Map<String, Object> data = jdbcTemplate.queryForList(sql, query.params).get(0); //.queryForList(query);
		if (data.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
		data.put("count", data.remove("COUNT(*)"));
        data.put("page", page);
        data.put("per_page", perPage);
        data.put("total_pages", (((Long) data.get("count"))/perPage) + 1);
        return new ResponseEntity<>(data, HttpStatus.OK);
	}
	
	@GetMapping("/help")
	public ResponseEntity<String> getHelp(@RequestParam Map<String,String> allRequestParams){
		return new ResponseEntity<>(Hardcoded.dataBaseTree.getURIquery("/api/v1/database"), HttpStatus.OK);
	}
	
};
