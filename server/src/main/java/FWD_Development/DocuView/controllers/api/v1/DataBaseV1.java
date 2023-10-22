package FWD_Development.DocuView.controllers.api.v1;


/* CUSTOM ADDED LIBS */
import java.util.List;
import java.util.Map;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

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

// @CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/database")
public class DataBaseV1 {

	@Autowired
    	private JdbcTemplate jdbcTemplate;

	@GetMapping("")
	public List<Map<String, Object>> getDocs(@RequestParam Map<String,String> allRequestParams){
		String filters =   Hardcoded.dataBaseTree.generateFilterQuery(allRequestParams);
		if (filters.equals("")) {filters = "TRUE";}
		String query = Hardcoded.dataBaseTree.getTreeInnerJoin() + " WHERE " + filters;
		//List<Map<String,Object>> holder = 
		return jdbcTemplate.queryForList(query);
	}
	
};
