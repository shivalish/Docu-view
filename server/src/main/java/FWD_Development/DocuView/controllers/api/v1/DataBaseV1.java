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
	
	//public JsonNode flattenResultSet(String TableName, ResultSet rs){
	//	ObjectNode rootNode = objMapper.createObjectNode();
	//	ArrayList list = new ArrayList(50);
	//	List<String> keysList;
	//	// while(targetMap.containsKey())
	//}
	
	// does nothing yet, testing only
	@GetMapping("")
	public JsonNode getDocs(@RequestParam Map<String,String> allRequestParams){
		// ObjectNode rootNode = objMapper.valueToTree(allRequestParams);
		ObjectNode rootNode = objMapper.createObjectNode();
		
		Map<String, String> tableQueries = new HashMap<>();
		for (Map.Entry<String, List<Filter>> set : targetMap.entrySet()) {
			List<String> strLst = new ArrayList<>();
			String query;
			for (Filter filter : set.getValue()){
				if (!allRequestParams.containsKey(filter.getName())){ continue; }
				// data issue, find better way to do this. Make table in hardcodeded?
				String primaryKeyColumn = jdbcTemplate.queryForObject(
						"SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
						"WHERE TABLE_NAME = '"+ filter.getOriginTable() +"' AND CONSTRAINT_NAME = 'PRIMARY'",
						String.class
				);
				query = String.format(
						"SELECT %s FROM %s WHERE %s;", 
						primaryKeyColumn,
						filter.getOriginTable(),
						filter.filteringQueryCondition(allRequestParams.get(filter.getName()))
					);
				List<String> holder = jdbcTemplate.query(query, new RowMapper<String>() {
					public String mapRow(ResultSet rs, int rowNum) throws SQLException {
						return rs.getString(1);
					}
				});
				query = filter.getTargetId() + " IN (\"" + String.join("\", \"", holder) + "\")";
				strLst.add(query);
			}
			//rootNode.put(set.getKey(), String.join(" AND ", strLst));
			if ( !strLst.isEmpty() ) { tableQueries.put(set.getKey(), String.join(" AND ", strLst)); }
		}
		
		Map<String, String> metaQueries = new HashMap<>();
		for (Map.Entry<String, Map<String, String>> set : tableMap.entrySet()){
		 	if (set.getKey() == "ATTACH_PROPOSAL") { continue; }
		 	List<String> strLst = allQueries(set, tableQueries);
		 	if ( !strLst.isEmpty() && tableQueries.containsKey(set.getKey())) { 
		 		metaQueries.put(set.getKey(), tableQueries.get(set.getKey()) + " AND " + String.join(" AND ", strLst)); 
		 	}
		 	else if (tableQueries.containsKey(set.getKey())) {
		 		metaQueries.put( set.getKey(), tableQueries.get(set.getKey()));
		 	}
		 	else if (!strLst.isEmpty()){
		 		metaQueries.put( set.getKey(), String.join(" AND ", strLst));
		 	}
		}
		for (Map.Entry<String, String> set : metaQueries.entrySet()){
			tableQueries.put(set.getKey(), set.getValue());
		}
		// Evil stuff, if someone knows how to do this better pls fix
		System.out.println(tableQueries.toString());
		metaQueries = new HashMap<>();
		for (Map.Entry<String, Map<String, String>> set : tableMap.entrySet()){
			if (set.getKey() != "ATTACH_PROPOSAL") { continue; }
			List<String> strLst = allQueries(set, tableQueries);
		 	if ( !strLst.isEmpty() && tableQueries.containsKey(set.getKey())) { 
		 		metaQueries.put(set.getKey(), tableQueries.get(set.getKey()) + " AND " + String.join(" AND ", strLst)); 
		 	}
		 	else if (tableQueries.containsKey(set.getKey())) {
		 		metaQueries.put( set.getKey(), tableQueries.get(set.getKey()));
		 	}
		 	else if (!strLst.isEmpty()){
		 		metaQueries.put( set.getKey(), String.join(" AND ", strLst));
		 	}
		}
		List<String> holder;
		if ( metaQueries.containsKey("ATTACH_PROPOSAL") ){
			holder = jdbcTemplate.query("SELECT * FROM ATTACH_PROPOSAL WHERE "+ metaQueries.get("ATTACH_PROPOSAL"), new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3);
						}
					});
		}
		else {
			holder = jdbcTemplate.query("SELECT * FROM ATTACH_PROPOSAL", new RowMapper<String>() {
						public String mapRow(ResultSet rs, int rowNum) throws SQLException {
							return rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3);
						}
					});
		}
		rootNode.put("ATTACH_PROPOSAL", holder.toString());
		
		return rootNode;
	}
	
	private List<String> allQueries(Map.Entry<String, Map<String, String>> set, Map<String, String> tableQueries){
		List<String> strLst = new ArrayList<>();
		String query;
		for (Map.Entry<String, String> set2 : set.getValue().entrySet()){
			
	 		if ( !tableQueries.containsKey(set2.getValue()) ) { continue; }
	 		String primaryKeyColumn = jdbcTemplate.queryForObject(
						"SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE " +
						"WHERE TABLE_NAME = '"+ set2.getValue() +"' AND CONSTRAINT_NAME = 'PRIMARY'",
						String.class
				);
			// this should be the standard, will fix
	 		query =  String.format(
					"SELECT %s FROM %s WHERE %s;", 
					primaryKeyColumn,
					set2.getValue(), 
					tableQueries.get(set2.getValue())
				);
			System.out.println(query);
			List<String> holder = jdbcTemplate.query(query, new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString(1);
				}
			});
			
			query = set2.getKey() + " IN (\"" + String.join("\", \"", holder) + "\");";
			strLst.add(query);
	 	}
	 	return strLst;
	}
	
};

//for (Map.Entry<String,String> set : allRequestParams.entrySet()){
//	if (!filterMap.containsKey(set.getKey())) { continue; }
//		rootNode.put(set.getKey(), filterMap.get(set.getKey()).filteringQueryCondition(set.getValue()));
//}
