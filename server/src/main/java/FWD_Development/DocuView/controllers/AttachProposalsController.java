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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.dao.EmptyResultDataAccessException;

/* Structure Draft
{
    "proposal" : {
        "proposal_id" : 1
        "proposal_label" : "CP 2010-11-FCA"
        "project_id" : {
            "project_id" : 2000
            "project_name" : "Gravity Works"
        },
        #NEXT LEVEL?
        "project_type" : "INCREMENTAL"
        "resource" : {
            "resource_id" : 100
            "resource_name" : "Big Generator"
            "resource_type" : "GEN"
        },
        "customer" : {
            "customer_id" : 10
            "customer_name" : "Constant Energy"
        },
        "auction" : {
            "auction_id" : 1
            "commitment_period_id" : {
                "period_id" : 1,
                "period_type" : "COMMITMENT"
                "description" : "2010 - 11"
                "begin_date" : 2010-6-1  4:00:00.000
                "end_date" : 2011-6-1 4:00:00.000
            },
            "auction_period_id" : {
                "period_id" : 1,
                "period_type" : "COMMITMENT"
                "description" : "2010 - 11"
                "begin_date" : 2010-6-1  4:00:00.000
                "end_date" : 2011-6-1 4:00:00.000
            },
            "auction_begin_date" : 2010-6-1  4:00:00.000,
            "auction_end_date" : 2011-6-1 4:00:00.000
            "auction_type" : "FCA"
        },
        "period" : {
            "period_id" : 1,
            "period_type" : "COMMITMENT"
            "description" : "2010 - 11"
            "begin_date" : 2010-6-1  4:00:00.000
            "end_date" : 2011-6-1 4:00:00.000
        }

    }
    "attachment" : {
        "attachment_id" : 1,
        "description" : "A PDF file"
        "extension" : ".pdf"
        "file_name" : "happy.pdf"
        "file_path" : "/FCTS_data/Attachments"
        "create_date" : 2008-02-25 15:16:00.000 # datetime
        #ask if timestamp or string
    }
    "attachment_type" : "QP.DR.PROJECT_DESCRIPTION"
}
*/

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1")
public class AttachProposalsController {

    @GetMapping("/attach_proposals")
    public JsonNode attachProposals() {
        ObjectMapper objMapper = new ObjectMapper();
        ObjectNode jsonResponse = objMapper.createObjectNode();
        try {
            jsonResponse.set("proposal", objMapper.createObjectNode());
            jsonResponse.set("attachment", objMapper.createObjectNode());
            jsonResponse.put("attachment_type", "<attachment_type : STRING>");
        } catch (EmptyResultDataAccessException e) {
            jsonResponse.put("timestamp", LocalDateTime.now().toString());
            jsonResponse.put("status", 417);
            jsonResponse.put("error", "docu pong!!");
        }

        return jsonResponse;
    }
}
