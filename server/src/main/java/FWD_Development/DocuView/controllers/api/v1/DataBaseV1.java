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
import java.util.Date;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.MultiValueMap;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
import com.fasterxml.jackson.core.JsonProcessingException;

import FWD_Development.DocuView.controllers.api.v1.DataBaseTree.DataBaseNode;

import java.sql.ResultSetMetaData;

import org.springframework.dao.EmptyResultDataAccessException;

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/database")
public class DataBaseV1 {

	@Autowired
    	private JdbcTemplate jdbcTemplate;
    	
    	private ObjectMapper objectMapper = new ObjectMapper();
    	
    	private String sqlQuery = "SELECT x.attach_proposal_attachment_id AS AttachmentId, x.attach_proposal_attachment_type AS AttachmentType, x.attach_proposal_proposal_id AS ProposalId, x.attach_proposal_proposal_id_customer_id AS CustomerId, x.attach_proposal_proposal_id_period_id AS PeriodId, x.attach_proposal_proposal_id_project_id AS ProjectId, x.attach_proposal_proposal_id_project_type AS ProjectType, x.attach_proposal_proposal_id_resource_id AS ResourceId, x.attach_proposal_proposal_id_project_type_project_type_desc AS ProjectTypeDescription, x.attach_proposal_proposal_id_project_id_project_name AS ProjectIdProjectName, x.attach_proposal_proposal_id_resource_id_resource_id AS ResourceIdResourceId, x.attach_proposal_proposal_id_resource_id_resource_name AS ResourceIdResourceName, x.attach_proposal_proposal_id_resource_id_resource_type AS ResourceIdResourceType, x.attach_proposal_proposal_id_resource_id_resource_type_resource_type_desc AS ResourceIdResourceTypeDescription, x.attach_proposal_proposal_id_auction_id_auction_begin_date AS AuctionBeginDate, x.attach_proposal_proposal_id_auction_id_auction_end_date AS AuctionEndDate, x.attach_proposal_proposal_id_auction_id_auction_id AS AuctionId, x.attach_proposal_proposal_id_auction_id_auction_period_id AS AuctionPeriodId, x.attach_proposal_proposal_id_auction_id_auction_type AS AuctionType, x.attach_proposal_proposal_id_auction_id_commitment_period_id AS CommitmentPeriodId, x.attach_proposal_proposal_id_auction_id_auction_type_auction_type AS AuctionTypeDescription, x.attach_proposal_proposal_id_auction_id_commitment_period_id_begin_date AS CommitmentPeriodBeginDate, x.attach_proposal_proposal_id_auction_id_commitment_period_id_description AS CommitmentPeriodDescription, x.attach_proposal_proposal_id_auction_id_commitment_period_id_end_date AS CommitmentPeriodEndDate, x.attach_proposal_proposal_id_auction_id_commitment_period_id_parent_period_id AS CommitmentPeriodParentPeriodId, x.attach_proposal_proposal_id_auction_id_commitment_period_id_period_id AS CommitmentPeriodPeriodId, x.attach_proposal_proposal_id_auction_id_commitment_period_id_period_type AS CommitmentPeriodPeriodType, x.attach_proposal_proposal_id_period_id_begin_date AS PeriodBeginDate, x.attach_proposal_proposal_id_period_id_description AS PeriodDescription, x.attach_proposal_proposal_id_period_id_end_date AS PeriodEndDate, x.attach_proposal_proposal_id_period_id_parent_period_id AS PeriodParentPeriodId, x.attach_proposal_proposal_id_period_id_period_id AS PeriodPeriodId, x.attach_proposal_proposal_id_period_id_period_type AS PeriodPeriodType, x.attach_proposal_proposal_id_customer_id_customer_id AS CustomerIdCustomerId, x.attach_proposal_proposal_id_customer_id_customer_name AS CustomerIdCustomerName, x.attach_proposal_attachment_type_application_category_type AS AttachmentTypeApplicationCategoryType, x.attach_proposal_attachment_type_attachment_type AS AttachmentTypeAttachmentType, x.attach_proposal_attachment_type_description AS AttachmentTypeDescription, x.attach_proposal_attachment_id_attachment_id AS AttachmentIdAttachmentId, x.attach_proposal_attachment_id_create_date AS AttachmentIdCreateDate, x.attach_proposal_attachment_id_description AS AttachmentIdDescription, x.attach_proposal_attachment_id_file_name AS AttachmentIdFileName, x.attach_proposal_attachment_id_file_path AS AttachmentIdFilePath";

	@GetMapping("")
	public ResponseEntity<List<Map<String, Object>>> getDocs(@RequestParam Map<String,String> allRequestParams) throws JsonProcessingException{
		String innerJoin = Hardcoded.dataBaseTree.getTreeInnerJoin();
		String filters = Hardcoded.dataBaseTree.generateFilterQuery(allRequestParams);
		String query = sqlQuery + " FROM (" + innerJoin + " WHERE " + filters + ") AS x;";
		try{
			var data = jdbcTemplate.queryForList(query); //.queryForList(query);
			return new ResponseEntity<>(data, HttpStatus.OK);
		}
		catch (Exception e){
			System.out.println(e);
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
		}
	}
	
};
