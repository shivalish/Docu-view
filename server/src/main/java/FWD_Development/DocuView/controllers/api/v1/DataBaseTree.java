package FWD_Development.DocuView.controllers.api.v1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.MultiValueMap;

public class DataBaseTree {
    public class DataBaseNode {
        private JdbcTemplate jdbcTemplate;
        private String name;
        private String primaryKey;
        private Map<String, DataBaseNode> connected;
        private List<String> columns;
        private String path;
        private DataBaseTree tree;

        private List<String> _getColumns(){
            List<String> columns = new ArrayList<>();
            for (Map<String, Object> row : jdbcTemplate.queryForList("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '" + name + "';")) {
                columns.add((String)row.get("column_name"));
            }
            return columns;
        }

        private String _getPrimaryKey(){
            List<Map<String, Object>> holder = jdbcTemplate.queryForList("SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE WHERE TABLE_NAME = '" + name + "' AND CONSTRAINT_NAME = 'PRIMARY';");
            if (holder.isEmpty()) { return ""; }
            return (String)holder.get(0).get("COLUMN_NAME");

        }

        public DataBaseNode(String _name, JdbcTemplate jdbcTemplate, String path, DataBaseTree tree) {
            this.jdbcTemplate = jdbcTemplate;
            this.name = _name;
            this.primaryKey = this._getPrimaryKey();
            this.connected = new HashMap<>();
            this.columns = this._getColumns();
            this.path = path;
            this.tree =  tree;
        }

        public String getName() {
            return this.name;
        }

        public String getPath(){
            return this.path;
        }

        public List<String> getColumns(){
            return this.columns;
        }

        public String getPrimaryKey() {
            return this.primaryKey;
        }

        public Map<String, DataBaseNode> getConnected() {
            return this.connected;
        }

        public DataBaseNode getConnectedId(String key) {
            return this.connected.get(key);
        }

        public boolean isLeaf() {
            return this.connected.isEmpty();
        }

        public void add(String refId, String tableName) {
            DataBaseNode node = new DataBaseNode(tableName, this.jdbcTemplate, this.path + "_" + refId, this.tree);
            connected.put(refId, node);
            this.tree.addNode(node);
        }
    }

    private DataBaseNode root;
    private String TreeInnerJoin;
    private Map <String, DataBaseNode> nodes;
    private Map <String, String> paramFormatStrings;
    private Map <String, Filter> filterMap;

    public DataBaseTree(String rootName, JdbcTemplate jdbcTemplate){
        root = new DataBaseNode(rootName, jdbcTemplate, rootName.toLowerCase(), this);
        nodes = new HashMap<>();
        nodes.put(rootName, root);
    }

    public DataBaseNode getRoot(){
        return root;
    }

    public Map<String, Filter> getFilterMap(){
        // do not send original to be modified
        return new HashMap<>(filterMap);
    }

    public void addNode(DataBaseNode node){
        if (node == null) {return ;}
        nodes.put(node.getName(), node);
    }

    public Map <String, DataBaseNode> getNodes(){
        return this.nodes;
    }

    public DataBaseNode getNode(String tableName){
        if (!this.nodes.containsKey(tableName)) {return null;}
        return this.nodes.get(tableName);
    }

    public String generateFilterQuery(Map<String,String> allRequestParams){
        if (paramFormatStrings == null || paramFormatStrings.isEmpty()) { return "TRUE"; }
        List<String> query = new ArrayList<>();
        for (Map.Entry<String, String> set : paramFormatStrings.entrySet()){
            if ( !allRequestParams.containsKey( set.getKey() ) ){ continue; }
            String key = set.getKey();
            List<String> holder = new ArrayList<>();
            for (String elem : allRequestParams.get(key))
                holder.add(set.getValue().replace("{" + key + "}", elem));
            query.add("( " + String.join(" OR ", holder) + " )");
        }
        if (query.isEmpty()) { return "TRUE"; }
        return String.join(" AND ", query);
    }

    public void addFilters(Filter[] filterArray){
        Map <String, String> out = new HashMap<>();
        filterMap = new HashMap<>();
        for (Filter f : filterArray){
            filterMap.put(f.getName(), f);
            out.put(f.getName(), f.getTargetTable().getPath()  + "_" + f.getTargetId() + "_" + String.format(f.filteringQueryFormat(), "{" + f.getName() + "}"));
        }
        this.paramFormatStrings = out;
    }

    public String getTreeInnerJoin(){
        if (TreeInnerJoin == null) {
            List<String> columns_alias = new ArrayList<>();
            for(String column : root.getColumns()){
                columns_alias.add(root.getName() + "." + column + " AS " +  root.getPath() + "_" + column.toLowerCase());
            }
            this.TreeInnerJoin = "SELECT * FROM ( SELECT " + String.join(", ", columns_alias) + " FROM " + root.getName() +") AS" + root.getPath() + "\n"+ this.treeInnerJoinGenerate(root);
        
        }
        return TreeInnerJoin;
    }
    
    private String treeInnerJoinGenerate(DataBaseNode node){
        if (node == null) { return ""; }
        Map<String, DataBaseNode> connectedNodes = node.getConnected();
        List<String> strLst = new ArrayList<>();
        for (Map.Entry<String, DataBaseNode> set: connectedNodes.entrySet()) {
            String alias = set.getValue().getPath();
            List<String> columns_alias = new ArrayList<>();
            for(String column : set.getValue().getColumns()){
                columns_alias.add(set.getValue().getName() + "." + column + " AS " +  alias + "_" + column.toLowerCase());
            }
            String query = "INNER JOIN " + "( SELECT " + String.join(", ", columns_alias) + " FROM " + set.getValue().getName() +")" + " AS " + alias + "_Table"
                + " ON " + node.getPath() + "_" + set.getKey() + " = " + alias + "_" + set.getValue().getPrimaryKey()
                + "\n" + treeInnerJoinGenerate(set.getValue());
            strLst.add(query);
        }
        return String.join("", strLst);
        
    }
}
