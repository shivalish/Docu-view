package FWD_Development.DocuView.controllers.api.v1;

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

// @CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/filters")
public class FiltersV1{
    
    @Autowired
        private JdbcTemplate jdbcTemplate;
	
    private static final ObjectMapper objMapper = new ObjectMapper();
    
    private ArrayNode outerArray;

    private ArrayNode initializeOuterArray(){
        if ( Hardcoded.dataBaseTree.getRoot() == null) { Hardcoded.initializeDataBaseTree(jdbcTemplate); };
         ArrayNode outerArray = objMapper.createArrayNode();
        for (Map.Entry<String, Filter> entry : Hardcoded.dataBaseTree.getFilterMap().entrySet()) {
            String name = entry.getKey();
            Filter filter = entry.getValue();
            ObjectNode currentJson = objMapper.createObjectNode();
            currentJson.put("name", name);
            currentJson.put("type", filter.getType());
            currentJson.put("has_finite_states", filter.getFiniteStates());

            ArrayNode finiteStatesArray = currentJson.putArray("finite_states");
            List<String> finiteStates = filter.getFiniteStatesQuery(jdbcTemplate);
            for (String elem : finiteStates) {
                finiteStatesArray.add(elem);
            }
            outerArray.add(currentJson);
        }
        return outerArray;
    }
    
    @GetMapping("")
    public JsonNode filters() {
        if (outerArray == null) {outerArray = initializeOuterArray();}
       return outerArray;
    }
}
