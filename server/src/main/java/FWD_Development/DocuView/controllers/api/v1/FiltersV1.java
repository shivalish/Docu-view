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

// @CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/filters")
public class FiltersV1 implements Hardcoded{
	
    private static final ObjectMapper objMapper = new ObjectMapper();
    // will not change during execution, so semi-hardcoded
    private static ObjectNode filtersNode;
    public FiltersV1(){
        filtersNode = objMapper.createObjectNode();
        for (Map.Entry<String, Filter> set : filterMap.entrySet()){
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
        if (!filterMap.containsKey(filter_name)) { return rootNode; }
        Filter get = filterMap.get(filter_name);
        rootNode.put("name", get.getName());
        rootNode.put("has_finite_states", get.getHasFiniteStates());
        ArrayNode finite_states = rootNode.putArray("finite_states");
        for (String elem : get.getFiniteStates()){
        	finite_states.add(elem);
        }
        
        return rootNode;
    }
}
