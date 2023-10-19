package FWD_Development.DocuView.controllers;


/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RequestParam;
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


// @CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/database")
public class DataBaseV1 implements Hardcoded{

	@Autowired
    private JdbcTemplate jdbcTemplate;

	private static final ObjectMapper objMapper = new ObjectMapper();
	
	@GetMapping("")
	public JsonNode getDocs(@RequestParam Map<String,String> allRequestParams){
		// ObjectNode rootNode = objMapper.valueToTree(allRequestParams);
		ObjectNode rootNode = objMapper.createObjectNode();
		for (Map.Entry<String, List<Filter>> set : targetMap.entrySet()) {
			List<String> strLst = Arrays.asList("");
			String acc = "";
			for (Filter filter : set.getValue()){
				if (!allRequestParams.containsKey(filter.getName())){ continue; }
				String primaryKeyColumn = jdbcTemplate.queryForObject(
						"SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
						"WHERE TABLE_NAME = '"+ filter.getOriginTable() +"' AND CONSTRAINT_NAME = 'PRIMARY'",
						String.class
				);
				String query = String.format(
						"SELECT %s FROM %s;", 
						primaryKeyColumn, 
						filter.filteringQueryCondition(allRequestParams.get(filter.getName()))
					);
				strLst = jdbcTemplate.query(query, new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString(1);
					}
				});
				query = "SELECT * FROM " + filter.getTargetTable() + " WHERE " + filter.getTargetId() + " IN (\"" + String.join("\", \"", strLst) + "\");";
				strLst = jdbcTemplate.query(query, new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3);
					}
				});
				System.out.println(strLst.toString());
			}
			rootNode.put(set.getKey(), "");
		}
		// QUERIES BASED ON HIERARCHY
		return rootNode;
	}
	
};

//for (Map.Entry<String,String> set : allRequestParams.entrySet()){
//	if (!filterMap.containsKey(set.getKey())) { continue; }
//		rootNode.put(set.getKey(), filterMap.get(set.getKey()).filteringQueryCondition(set.getValue()));
//}
