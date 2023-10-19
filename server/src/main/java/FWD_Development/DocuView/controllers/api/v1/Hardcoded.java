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

// TODO get rid of independant
public interface Hardcoded{
	
	public class Filter {
			

	    	private String name;		// name of filter
	    	private String type;		// filter datatype
			private String originTable;
			private String originId;	
			private String targetTable;
			private String targetId;
			private boolean finiteStates;

			private char compType = '*';
			private String finiteStatesQuery;
			private String[] finiteStatesArray;
	    	
	    	// SearchType filters (must be id in another table)
	    	Filter(String _name, String _type, String _originTable, String _originId, String _targetTable, String _targetId, boolean _finiteStates){
	    		this.name = _name;
	    		this.type = _type;
				this.originTable = _originTable;
				this.originId = _originId;
				this.targetTable = _targetTable;
				this.targetId = _targetId;
				this.finiteStates = _finiteStates;

				this.finiteStatesQuery =  String.format("SELECT DISTINCT %s FROM %s", originId, originTable);
	    	}

			Filter(String _name, String _type, String _originTable, String _originId, String _targetTable, String _targetId, boolean _finiteStates, char _compType){
	    		this.name = _name;
	    		this.type = _type;
				this.originTable = _originTable;
				this.originId = _originId;
				this.targetTable = _targetTable;
				this.targetId = _targetId;
				this.finiteStates = _finiteStates;
				this.compType = _compType;

				this.finiteStatesQuery =  String.format("SELECT DISTINCT %s FROM %s", originId, originTable);
	    	}

			Filter(String _name, String _type, String _originTable, String _originId, String _targetTable, String _targetId, String[] _finiteStatesArray){
	    		this.name = _name;
	    		this.type = _type;
				this.originTable = _originTable;
				this.originId = _originId;
				this.targetTable = _targetTable;
				this.targetId = _targetId;
				this.finiteStates = true;

				this.finiteStatesArray = _finiteStatesArray;
	    	}

			Filter(String _name, String _type, String _originTable, String _originId, String _targetTable, String _targetId, String[] _finiteStatesArray, char _compType){
	    		this.name = _name;
	    		this.type = _type;
				this.originTable = _originTable;
				this.originId = _originId;
				this.targetTable = _targetTable;
				this.targetId = _targetId;
				this.finiteStates = true;
				this.compType = _compType;

				this.finiteStatesArray = _finiteStatesArray;
	    	}

			// todo. pregenrated then format
			private String filteringQuery(String param){
				String filteringQuerySQL;
				switch(compType){
					case '<':
					case '>':
						filteringQuerySQL = "SELECT * FROM " + this.originTable + " WHERE " + this.originId + String.format(" %c ", compType) + param ;
						break;
					case 'g':
						filteringQuerySQL = "SELECT * FROM " + this.originTable + " WHERE " + this.originId + ">=" + param ;
						break;
					case 'l':
						filteringQuerySQL = "SELECT * FROM " + this.originTable + " WHERE " + this.originId + "<=" + param ;
						break;
					case '!':
						filteringQuerySQL = "SELECT * FROM " + this.originTable + " WHERE " + this.originId + " <> " + param ;
						break;
					case '*':
					default:
						filteringQuerySQL = "SELECT * FROM " + this.originTable + " WHERE " + this.originId + " LIKE " + param ;
				}
				return filteringQuerySQL;
			}
			public String getName(){
				return this.name;
			}

			public String getType(){
				return this.type;
			}

			public String getOriginTable(){
				return this.originTable;
			}

			public String getOriginId(){
				return this.originId;
			}

			public String getTargetTable(){
				return this.targetTable;
			}

			public String getTargetId(){
				return this.targetId;
			}

			public boolean getFiniteStates(){
				return this.finiteStates;
			}	    	
	    	//////////////////////////////
	    	
	    	private List<String> queryExecutor(JdbcTemplate jdbcTemplate){
				List<String> temp = jdbcTemplate.queryForList(finiteStatesQuery,String.class);
	    		return temp;
	    	};
	    	
	    	//temp for skeleton
	    	public List<String> getFiniteStatesQuery(JdbcTemplate jdbcTemplate){
	    		if (!this.finiteStates) { return Arrays.asList(new String[] {}); }
				if (this.finiteStatesArray != null) { return Arrays.asList(this.finiteStatesArray); }
	    		return queryExecutor(jdbcTemplate);
	    	}
	    }


	// > greater, < less than, = equal, ! not equal, g greater than inclusive, l less than inclusive, * contains (defualt)
	//"SELECT DISTINCT auction_type FROM AUC_TYPE"  "SELECT DISTINCT attachment_type FROM ATTACH_TYPE" , new String[]{".pdf",".csv",".xlsx",".docs"}
	static final Filter[] filterArrray = new Filter[] {
		new Filter("file_creation", "ISO 8601", "ATTACHMENT_FILE" ,"create_date", "ATTACH_PROPOSAL", "attachment_id", false),
		new Filter("file_extension", "string", "ATTACHMENT_FILE" ,"file_name", "ATTACH_PROPOSAL", "attachment_id", new String[]{".bmp", ".doc", ".docx", ".htm", ".html", ".jpg", ".msg", ".pdf", ".txt", ".xlsm", ".xlsx", ".zip", ".zipx"}),
		new Filter("filename", "string", "ATTACHMENT_FILE" ,"file_name", "ATTACH_PROPOSAL", "attachment_id", false),
		new Filter("customer_name", "string", "CUST_INFO", "customer_name", "PROPOSAL_INFO" ,"customer_id", false),
		new Filter("auction_type", "string", "AUC_TYPE", "auction_type", "AUC_INFO", "auction_type", true),
		new Filter("attachment_type", "string", "ATTACH_TYPE", "attachment_type", "ATTACH_PROPOSAL", "attachment_type", true),

		new Filter("commitment_date_start", "ISO 8601", "PERIOD_INFO", "begin_date", "AUC_INFO", "commitment_period_id",false, '<'),
		new Filter("commitment_date_end", "ISO 8601", "PERIOD_INFO", "end_date", "AUC_INFO", "commitment_period_id",false, '<'),
		new Filter("auction_date_start", "ISO 8601", "AUC_INFO", "auction_begin_date", "AUC_INFO", "auction_period_id",false, '<'),
		new Filter("auction_date_end", "ISO 8601", "AUC_INFO", "auction_end_date", "AUC_INFO", "auction_period_id",false, '<'),

		new Filter("proposal_date_start", "ISO 8601", "PERIOD_INFO", "begin_date", "PROPOSAL_INFO", "period_id",false, '<'),
		new Filter("proposals_date_end", "ISO 8601", "PERIOD_INFO", "end_date", "PROPOSAL_INFO", "period_id",false, '<'),
		
	};
	static final Map<String, Filter> filterMap = new HashMap<String, Filter>(){{
		for (Filter elem : filterArrray){
			put(elem.getName(), elem);
        	}
	}};
}		
