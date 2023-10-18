package FWD_Development.DocuView.controllers;


/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.HashMap;
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
	
    private static ObjectMapper objMapper = new ObjectMapper();

    private class Filter {
    	private String name;		// name of filter
    	private String type;		// filter datatype
    	private boolean hasFiniteStates;	// has finite states?
    	private String finiteStates;
    	
    	Filter(String _name, String _type){
    		this.name = _name;
    		this.type = _type;
    		this.hasFiniteStates = false;
    		this.finiteStates = "";
    	}
    	
    	Filter(String _name, String _type, String _finiteStatesQuery){
    		this.name = _name;
    		this.type = _type;
    		this.hasFiniteStates = true;
    		this.finiteStates = _finiteStatesQuery;
    	}
    	
    	// only getters (READONLY FUNC)
    	public String getName(){
    		return this.name;
    	}
    	public String getType(){
    		return this.type;
    	}
    	public boolean getHasFiniteStates(){
    		return this.hasFiniteStates;
    	}
    	
    	//temp for skeleton
    	public List<String> getFiniteStates(){
    		if (this.hasFiniteStates == false) { return Arrays.asList(new String[]{}); }
    		else { return Arrays.asList(new String[]{}); }
    	}
    }

    
    private final Filter[] m = new Filter[] {
		new Filter("file_creation", "ISO 8601"),
		new Filter("document_type", "string"),
		new Filter("filename", "string"),
		new Filter("customer_name", "string"),
		new Filter("auction_type", "string"),
		new Filter("proposal_type", "string"),
		new Filter("commitment_date_start", "ISO 8601"),
		new Filter("commitment_date_end", "ISO 8601"),
		new Filter("auction_date_start", "ISO 8601"),
		new Filter("auction_date_end", "ISO 8601"),
		new Filter("proposal_date_start", "ISO 8601"),
		new Filter("proposals_date_end", "ISO 8601"),
	};
    private Map<String, Filter> m2;
    // will not change during execution, so semi-hardcoded
    private static ObjectNode filtersNode;
    public FiltersV1(){
	 m2 = new HashMap<String, Filter>(){{
		for (Filter elem : m){
			put(elem.getName(), elem);
        	}
	   }};
        filtersNode = objMapper.createObjectNode();
        for (Map.Entry<String, Filter> set : m2.entrySet()){
            filtersNode.put(set.getKey(), set.getValue().getType());
        }
    }
    
    @GetMapping("")
    public JsonNode filters() {
        ObjectNode rootNode = objMapper.createObjectNode();
        rootNode.set("list", filtersNode);
        return rootNode;
    }
    
    @GetMapping("/{filter_name}")
    public JsonNode filters(@PathVariable String filter_name) {
        ObjectNode rootNode = objMapper.createObjectNode();
        if (!m2.containsKey(filter_name)) { return rootNode; }
        Filter get = m2.get(filter_name);
        rootNode.put("name", get.getName());
        rootNode.put("has_finite_states", get.getHasFiniteStates());
        ArrayNode finite_states = rootNode.putArray("finite_states");
        for (String elem : get.getFiniteStates()){
        	finite_states.add(elem);
        }
        
        return rootNode;
    }
}
