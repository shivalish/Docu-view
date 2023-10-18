package FWD_Development.DocuView.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

public interface Hardcoded{
	
	public class Filter {
			

	    	private String name;		// name of filter
	    	private String type;		// filter datatype
	    	private boolean hasFiniteStates;	// has finite states?
	    	private boolean preDefinedStates;
	    	private String[] finiteStatesList;
	    	private String finiteStatesQuery;
	    	
	    	// Use this if there is no finite state (i.e Search bar)
	    	Filter(String _name, String _type){
	    		this.name = _name;
	    		this.type = _type;
	    		this.hasFiniteStates = false;
	    		this.preDefinedStates = false;
	    	}
	    	
	    	
	    	// Use this if finite states are defined by developer instead of DB
	    	Filter(String _name, String _type, String[] _finiteStates){
	    		this.name = _name;
	    		this.type = _type;
	    		this.hasFiniteStates = true;
	    		this.preDefinedStates = true;
	    		this.finiteStatesList = _finiteStates;
	    	}
	    	
	    	// Use this if finite stated are defined by DB
	    	Filter(String _name, String _type, String _finiteStatesQuery){
	    		this.name = _name;
	    		this.type = _type;
	    		this.hasFiniteStates = true;
	    		this.preDefinedStates = false;
	    		this.finiteStatesQuery = _finiteStatesQuery;
	    	}
	    	
	    	// only getters (READONLY FUNC)
	    	public String getName(){
	    		return this.name;
	    	}
	    	public String getType(){
	    		return this.type;
	    	}
	    	public boolean getPreDefinedStates(){
	    		return this.preDefinedStates;
	    	}
	    	public boolean getHasFiniteStates(){
	    		return this.hasFiniteStates;
	    	}
	    	//////////////////////////////
	    	
	    	private List<String> queryExecutor(JdbcTemplate jdbcTemplate){
	    		// temp
				List<String> temp = jdbcTemplate.queryForList(finiteStatesQuery,String.class);
	    		return temp;
	    	};
	    	
	    	//temp for skeleton
	    	public List<String> getFiniteStatesQuery(JdbcTemplate jdbcTemplate){
	    		if (!this.hasFiniteStates) { 
	    			return Arrays.asList(new String[]{});
	    		}
	    		if (this.preDefinedStates) {
	    			return Arrays.asList(finiteStatesList);
	    		}
	    		return queryExecutor(jdbcTemplate);
	    	}
	    }

	static final Filter[] filterArrray = new Filter[] {
		new Filter("file_creation", "ISO 8601"),
		new Filter("document_type", "string", new String[]{".pdf",".csv",".xlsx",".docs"}),
		new Filter("filename", "string"),
		new Filter("customer_name", "string"),
		new Filter("auction_type", "string", "SELECT DISTINCT auction_type FROM AUC_TYPE"),
		new Filter("attachment_type", "string", "SELECT DISTINCT attachment_type FROM ATTACH_TYPE"),
		new Filter("commitment_date_start", "ISO 8601"),
		new Filter("commitment_date_end", "ISO 8601"),
		new Filter("auction_date_start", "ISO 8601"),
		new Filter("auction_date_end", "ISO 8601"),
		new Filter("proposal_date_start", "ISO 8601"),
		new Filter("proposals_date_end", "ISO 8601"),
	};
	static final Map<String, Filter> filterMap = new HashMap<String, Filter>(){{
		for (Filter elem : filterArrray){
			put(elem.getName(), elem);
        	}
	}};
}		
