package FWD_Development.DocuView.controllers.api.v1;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
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
import jakarta.annotation.PostConstruct;

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

	private static final ObjectMapper objMapper = new ObjectMapper();

	static DataBaseTree dataBaseTree;
	static ArrayNode outerArray;

	

	@PostConstruct
	public void hardcodedSetUp() {
		initializeDataBaseTree(jdbcTemplate);
		initializeOuterArray();
	}

	public static void initializeDataBaseTree(JdbcTemplate jdbcTemplate){
		dataBaseTree = new DataBaseTree("ATTACH_PROPOSAL", jdbcTemplate);
		DataBaseNode root = Hardcoded.dataBaseTree.getRoot();
		
		// future automatize updates
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
			new Filter("file_creation", "ISO 8601","create_date", root, "attachment_id", false),
			new Filter("file_extension", "string","file_name", root, "attachment_id", new String[]{".bmp", ".doc", ".docx", ".htm", ".html", ".jpg", ".msg", ".pdf", ".txt", ".xlsm", ".xlsx", ".zip", ".zipx"}, '^'),
			new Filter("filename", "string","file_name", root, "attachment_id", false),
			new Filter("attachment_type", "string", "attachment_type", root, "attachment_type", true),

			new Filter("customer_name", "string", "customer_name", holder,"customer_id", false),

			
			new Filter("commitment_date_start", "ISO 8601", "begin_date", holder1, "commitment_period_id",false, '['),
			new Filter("commitment_date_end", "ISO 8601", "end_date", holder1, "commitment_period_id",false, ']'),
			new Filter("auction_type", "string", "auction_type", holder1, "auction_type", true),

			new Filter("resource_type", "string", "resource_type", holder2, "resource_type", true),
	
			
			
			new Filter("auction_date_start", "ISO 8601", "auction_begin_date", holder1, "auction_period_id",false, '['),
			new Filter("auction_date_end", "ISO 8601", "auction_end_date", holder1, "auction_period_id",false, ']'),
	
			new Filter("proposal_date_start", "ISO 8601", "begin_date", holder, "period_id",false, '['),
			new Filter("proposals_date_end", "ISO 8601", "end_date", holder, "period_id",false, ']'),
		};

		Hardcoded.dataBaseTree.addFilters(filterArray);
	}
  
	private void initializeOuterArray(){
		outerArray = objMapper.createArrayNode();
	   for (Map.Entry<String, Filter> entry : Hardcoded.dataBaseTree.getFilterMap().entrySet()) {
		   String name = entry.getKey();
		   Filter filter = entry.getValue();
		   ObjectNode currentJson = objMapper.createObjectNode();
		   currentJson.put("name", name);
		   currentJson.put("type", filter.getType());
		   currentJson.put("has_finite_states", filter.getFiniteStates());
		   ArrayNode finiteStatesArray = currentJson.putArray("finite_states");
		   for (String elem : filter.finite_states_array()) {
			   finiteStatesArray.add(elem);
		   }
		   outerArray.add(currentJson);
	   }
   }

	
}		
