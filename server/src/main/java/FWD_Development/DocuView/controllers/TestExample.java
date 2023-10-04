package FWD_Development.DocuView.controllers;

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

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
public class TestExample {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/docuPing")
    public JsonNode docuPing() {
        ObjectMapper objMapper = new ObjectMapper();
        ObjectNode exampleResponse = objMapper.createObjectNode();

        String sqlQuery = "SELECT exampleColumn2 FROM exampletable WHERE exampleColumn1 = ?";
        try {
            String value = jdbcTemplate.queryForObject(sqlQuery, new RowMapper<String>() {
                @Override
                public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return rs.getString("exampleColumn2");
                }
            }, 50);
            exampleResponse.put("databasePong", value); // adding the fetched value to the response
            exampleResponse.put("serverPong", "docu pong!!");
        } catch (EmptyResultDataAccessException e) {
            exampleResponse.put("serverPong", "docu pong!!");
        }

        return exampleResponse;
    }
}
