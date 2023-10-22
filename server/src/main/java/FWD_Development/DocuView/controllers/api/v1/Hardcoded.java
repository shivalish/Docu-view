package FWD_Development.DocuView.controllers.api.v1;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import FWD_Development.DocuView.controllers.api.v1.DataBaseTree.DataBaseNode;

import org.springframework.dao.EmptyResultDataAccessException;
import java.util.HashMap;



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

// modify here for everything to do with filters

// Hardcoded is a antipattern interface to allow vars to share the same immutable variables,
// allowing the uses to use the same vars.

@Component
public class Hardcoded{

	@Autowired
    	private JdbcTemplate jdbcTemplate;

	static DataBaseTree dataBaseTree = new DataBaseTree();

	@EventListener(ApplicationReadyEvent.class)
	public void doSomethingAfterStartup() {
		initializeDataBaseTree(jdbcTemplate);
	}

	public static void initializeDataBaseTree(JdbcTemplate jdbcTemplate){
		Hardcoded.dataBaseTree.setRoot("ATTACH_PROPOSAL", jdbcTemplate);
		DataBaseNode root = Hardcoded.dataBaseTree.getRoot();

		root.add("attachment_id", "ATTACHMENT_FILE");
		root.add("proposal_id", "PROPOSAL_INFO");
		root.add("attachment_type", "ATTACH_TYPE");

		DataBaseNode holder = root.getConnectedId("proposal_id");
		holder.add("project_id", "PROJ_INFO");
		holder.add("project_type", "PROJ_TYPE");
		holder.add("resource_id", "RES_INFO");
		holder.add("customer_id", "CUST_INFO");
		holder.add("auction_id", "AUC_INFO");
		holder.add("period_id", "PERIOD_INFO");
		
		DataBaseNode holder1 = holder.getConnectedId("auction_id");
		holder1.add("commitment_period_id", "PERIOD_INFO");
		holder1.add("auction_period_id", "PERIOD_INFO");
		holder1.add("auction_type", "AUC_TYPE");
		
		DataBaseNode holder2 = holder.getConnectedId("resource_id");
		holder2.add("resource_type", "RES_TYPE");

		Filter[] filterArray = new Filter[] {
			new Filter("file_creation", "ISO 8601", "ATTACHMENT_FILE" ,"create_date", "ATTACH_PROPOSAL", "attachment_id", false),
			new Filter("file_extension", "string", "ATTACHMENT_FILE" ,"file_name", "ATTACH_PROPOSAL", "attachment_id", new String[]{".bmp", ".doc", ".docx", ".htm", ".html", ".jpg", ".msg", ".pdf", ".txt", ".xlsm", ".xlsx", ".zip", ".zipx"}, '^'),
			new Filter("filename", "string", "ATTACHMENT_FILE" ,"file_name", "ATTACH_PROPOSAL", "attachment_id", false),
			new Filter("customer_name", "string", "CUST_INFO", "customer_name", "PROPOSAL_INFO" ,"customer_id", false),
			new Filter("auction_type", "string", "AUC_TYPE", "auction_type", "AUC_INFO", "auction_type", true),
			new Filter("attachment_type", "string", "ATTACH_TYPE", "attachment_type", "ATTACH_PROPOSAL", "attachment_type", true),
			new Filter("resource_type", "string", "RES_TYPE", "resource_type", "RES_INFO", "resource_type", true),
	
			new Filter("commitment_date_start", "ISO 8601", "PERIOD_INFO", "begin_date", "AUC_INFO", "commitment_period_id",false, '['),
			new Filter("commitment_date_end", "ISO 8601", "PERIOD_INFO", "end_date", "AUC_INFO", "commitment_period_id",false, ']'),
			new Filter("auction_date_start", "ISO 8601", "AUC_INFO", "auction_begin_date", "AUC_INFO", "auction_period_id",false, '['),
			new Filter("auction_date_end", "ISO 8601", "AUC_INFO", "auction_end_date", "AUC_INFO", "auction_period_id",false, ']'),
	
			new Filter("proposal_date_start", "ISO 8601", "PERIOD_INFO", "begin_date", "PROPOSAL_INFO", "period_id",false, '['),
			new Filter("proposals_date_end", "ISO 8601", "PERIOD_INFO", "end_date", "PROPOSAL_INFO", "period_id",false, ']'),	
		};

		Hardcoded.dataBaseTree.addFilters(filterArray);
	}



	
}		
