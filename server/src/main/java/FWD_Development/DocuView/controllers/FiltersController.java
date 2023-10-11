package FWD_Development.DocuView.controllers;


/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.dao.EmptyResultDataAccessException;

/*
 * {
 *   "timestamp": "2023-10-11T00:28:01.814+00:00",
 *   "status": 404,
 *   "error": "Not Found",
 *   "path": "/api/v1/"
 *}
 */

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/filters")
public class FiltersController {

    @GetMapping("/list")
    public JsonNode listFilters(@PathVariable String filter_name) {
        ObjectMapper objMapper = new ObjectMapper();
        ObjectNode jsonResponse = objMapper.createObjectNode();
        List<ObjectNode> filters = new ArrayList<ObjectNode>();

        try {
            jsonResponse.putArray("list").addAll(filters);
        } catch (EmptyResultDataAccessException e) {
            jsonResponse.put("timestamp", LocalDateTime.now().toString());
            jsonResponse.put("status", 417);
            jsonResponse.put("error", "docu pong!!");
        }

        return jsonResponse;
    }

    @GetMapping("/values/{filter_name}")
    public JsonNode getFilterValues(@PathVariable String filter_name) {
        ObjectMapper objMapper = new ObjectMapper();
        ObjectNode jsonResponse = objMapper.createObjectNode();
        List<ObjectNode> filterValues = new ArrayList<ObjectNode>();
        try {
            jsonResponse.put("filter_name", filter_name);
            jsonResponse.putArray("list").addAll(filterValues);
        } catch (EmptyResultDataAccessException e) {
            jsonResponse.put("timestamp", LocalDateTime.now().toString());
            jsonResponse.put("status", 417);
            jsonResponse.put("error", "docu pong!!");
        }

        return jsonResponse;
    }
}
