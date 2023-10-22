package FWD_Development.DocuView.controllers.api.v1;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;

import FWD_Development.DocuView.controllers.api.v1.DataBaseTree.DataBaseNode;

//  ] upper bound (incl) , [ lower bound (incl), ) upper bound, ( lower bound, = equal, ! not equal, * contains (default), ^ upper wildcard, . lower wildcard
	//"SELECT DISTINCT auction_type FROM AUC_TYPE"  "SELECT DISTINCT attachment_type FROM ATTACH_TYPE" , new String[]{".pdf",".csv",".xlsx",".docs"}
public class Filter {

	    private String name;		// name of filter
	    private String type;		// filter datatype
		private DataBaseNode originTable;
		private String originId;	
		private DataBaseNode targetTable;
		private String targetId;
		private boolean finiteStates;

		private char compType = '*';
		private String finiteStatesQuery;
		private String[] finiteStatesArray;

		private String filteringQuerySQLFormat;
	    	
	    	public Filter(String _name, String _type, String _originId, DataBaseNode _targetTable, String _targetId, boolean _finiteStates){
	    		this.name = _name;
	    		this.type = _type;
				this.originTable = _targetTable.getConnectedId(_targetId);
				this.originId = _originId;
				this.targetTable = _targetTable;
				this.targetId = _targetId;
				this.finiteStates = _finiteStates;
				this.filteringQuerySQLFormat =  generatefilteringQueryFormat();

				this.finiteStatesQuery =  String.format("SELECT DISTINCT %s FROM %s", originId, originTable.getName());
	    	}

			public Filter(String _name, String _type, String _originId, DataBaseNode _targetTable, String _targetId, boolean _finiteStates, char _compType){
	    		this.name = _name;
	    		this.type = _type;
				this.originTable = _targetTable.getConnectedId(_targetId);
				this.originId = _originId;
				this.targetTable = _targetTable;
				this.targetId = _targetId;
				this.finiteStates = _finiteStates;
				this.compType = _compType;
        
				this.filteringQuerySQLFormat =  generatefilteringQueryFormat();

				this.finiteStatesQuery =  String.format("SELECT DISTINCT %s FROM %s", originId, originTable.getName());
	    	}

			public Filter(String _name, String _type,  String _originId, DataBaseNode _targetTable, String _targetId, String[] _finiteStatesArray){
	    		this.name = _name;
	    		this.type = _type;
				this.originTable = _targetTable.getConnectedId(_targetId);
				this.originId = _originId;
				this.targetTable = _targetTable;
				this.targetId = _targetId;
				this.finiteStates = true;

				this.filteringQuerySQLFormat =  generatefilteringQueryFormat();

				this.finiteStatesArray = _finiteStatesArray;
	    	}

			public Filter(String _name, String _type, String _originId, DataBaseNode _targetTable, String _targetId, String[] _finiteStatesArray, char _compType){
	    		this.name = _name;
	    		this.type = _type;
				this.originTable = _targetTable.getConnectedId(_targetId);
				this.originId = _originId;
				this.targetTable = _targetTable;
				this.targetId = _targetId;
				this.finiteStates = true;
				this.compType = _compType;
        
				this.filteringQuerySQLFormat =  generatefilteringQueryFormat();

				this.finiteStatesArray = _finiteStatesArray;
	    	}

			// 
			// SELECT (SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME="ATTACHMENT_FILE") ATTACHMENT_FILE WHERE file_name LIKE .bmp
			// todo. pre-generated then format
			public String filteringQueryCondition(String param){
				String filteringQuerySQL;
				switch(compType){
					case '(':
						filteringQuerySQL = this.originId + " > " + "\'" + param + "\'";
						break;
					case ')':
						filteringQuerySQL = this.originId + " < " + "\'" + param + "\'";
						break;
					case '[':
						filteringQuerySQL = this.originId + " >= " + "\'" + param + "\'";
						break;
					case ']':
						filteringQuerySQL = this.originId + " <= " + "\'" + param + "\'";
						break;
					case '!':
						filteringQuerySQL = this.originId + " <> " + "\'" + param + "\'";
						break;
					case '=':
						filteringQuerySQL = this.originId + " = " + "\'" + param + "\'";
						break;

					case '^':
						filteringQuerySQL = this.originId + " LIKE \'%" + param + "\'";
						break;
					case '.':
						filteringQuerySQL = this.originId + " LIKE \'" + param + "%\'";
						break;
					case '*':
					default:
						filteringQuerySQL = this.originId + " LIKE \'%" + param + "%\'";
				}
				return filteringQuerySQL;
			}

			private String generatefilteringQueryFormat(){
				String filteringQuerySQL;
				switch(compType){
					case '(':
						filteringQuerySQL = this.originId + " > " + "\'%s\'";
						break;
					case ')':
						filteringQuerySQL = this.originId + " < " + "\'%s\'";
						break;
					case '[':
						filteringQuerySQL = this.originId + " >= " + "\'%s\'";
						break;
					case ']':
						filteringQuerySQL = this.originId + " <= " + "\'%s\'";
						break;
					case '!':
						filteringQuerySQL = this.originId + " <> " + "\'%s\'";
						break;
					case '=':
						filteringQuerySQL = this.originId + " = " + "\'%s\'";
						break;

					case '^':
						filteringQuerySQL = this.originId + " LIKE \'%%%s\'";
						break;
					case '.':
						filteringQuerySQL = this.originId + " LIKE \'%s%%\'";
						break;
					case '*':
					default:
						filteringQuerySQL = this.originId + " LIKE \'%%%s%%\'";
				}
				return filteringQuerySQL;
			}

			public String filteringQueryFormat(){
				return filteringQuerySQLFormat;
			}
			public String getName(){
				return this.name;
			}

			public String getType(){
				return this.type;
			}

			public DataBaseNode getOriginTable(){
				return this.originTable;
			}

			public String getOriginId(){
				return this.originId;
			}

			public DataBaseNode getTargetTable(){
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
