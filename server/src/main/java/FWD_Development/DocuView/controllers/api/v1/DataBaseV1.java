package FWD_Development.DocuView.controllers.api.v1;


/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Iterator;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Date;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.MultiValueMap;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonView;

import FWD_Development.DocuView.controllers.api.v1.DataBaseTree.DataBaseNode;

import java.sql.ResultSetMetaData;

import org.springframework.dao.EmptyResultDataAccessException;

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/database")
public class DataBaseV1 {

	public class Views {
	    public static class Public {
	    }
	    
	    public static class Private {
	    }
	}

	public class ResultDTO {
		@JsonView(Views.Public.class)
		private Long attachmentId;
		@JsonView(Views.Public.class)
		private String attachmentType;
		@JsonView(Views.Public.class)
		private Long proposalId;
		@JsonView(Views.Public.class)
		private Long customerId;
		@JsonView(Views.Public.class)
		private Long periodId;
		@JsonView(Views.Public.class)
		private Long projectId;
		@JsonView(Views.Public.class)
		private String projectType;
		@JsonView(Views.Public.class)
		private Long resourceId;
		@JsonView(Views.Public.class)
		private String projectTypeDescription;
		 @JsonView(Views.Public.class)
		private String projectIdProjectName;
		 @JsonView(Views.Public.class)
		private Long resourceIdResourceId;
		 @JsonView(Views.Public.class)
		private String resourceIdResourceName;
		 @JsonView(Views.Public.class)
		private String resourceIdResourceType;
		 @JsonView(Views.Public.class)
		private String resourceIdResourceTypeDescription;
		 @JsonView(Views.Public.class)
		private Date auctionBeginDate;
		 @JsonView(Views.Public.class)
		private Date auctionEndDate;
		 @JsonView(Views.Public.class)
		private Long auctionId;
		 @JsonView(Views.Public.class)
		private Long auctionPeriodId;
		 @JsonView(Views.Public.class)
		private String auctionType;
		 @JsonView(Views.Public.class)
		private Long commitmentPeriodId;
		 @JsonView(Views.Public.class)
		private String auctionTypeDescription;
		 @JsonView(Views.Public.class)
		private Date commitmentPeriodBeginDate;
		 @JsonView(Views.Public.class)
		private String commitmentPeriodDescription;
		 @JsonView(Views.Public.class)
		private Date commitmentPeriodEndDate;
		 @JsonView(Views.Public.class)
		private Long commitmentPeriodParentPeriodId;
		 @JsonView(Views.Public.class)
		private Long commitmentPeriodPeriodId;
		 @JsonView(Views.Public.class)
		private String commitmentPeriodPeriodType;
		 @JsonView(Views.Public.class)
		private Date periodBeginDate;
		 @JsonView(Views.Public.class)
		private String periodDescription;
		 @JsonView(Views.Public.class)
		private Date periodEndDate;
		 @JsonView(Views.Public.class)
		private Long periodParentPeriodId;
		 @JsonView(Views.Public.class)
		private Long periodPeriodId;
		 @JsonView(Views.Public.class)
		private String periodPeriodType;
		 @JsonView(Views.Public.class)
		private Long customerIdCustomerId;
		 @JsonView(Views.Public.class)
		private String customerIdCustomerName;
		 @JsonView(Views.Public.class)
		private String attachmentTypeApplicationCategoryType;
		 @JsonView(Views.Public.class)
		private String attachmentTypeAttachmentType;
		 @JsonView(Views.Public.class)
		private String attachmentTypeDescription;
		 @JsonView(Views.Public.class)
		private Long attachmentAttachmentId;
		 @JsonView(Views.Public.class)
		private Date attachmentCreateDate;
		 @JsonView(Views.Public.class)
		private String attachmentDescription;
		 @JsonView(Views.Public.class)
		private String attachmentFileName;
		 @JsonView(Views.Public.class)
		private String attachmentExtension;
		 @JsonView(Views.Public.class)
		private String attachmentIdFilePath;

		public Long getAttachmentId() {
			return attachmentId;
		}

		public void setAttachmentId(Long attachmentId) {
			this.attachmentId = attachmentId;
		}

		    public String getAttachmentType() {
			return attachmentType;
		    }

		    public void setAttachmentType(String attachmentType) {
			this.attachmentType = attachmentType;
		    }

		    public Long getProposalId() {
			return proposalId;
		    }

		    public void setProposalId(Long proposalId) {
			this.proposalId = proposalId;
		    }

		    public Long getCustomerId() {
			return customerId;
		    }

		    public void setCustomerId(Long customerId) {
			this.customerId = customerId;
		    }

		    public Long getPeriodId() {
			return periodId;
		    }

		    public void setPeriodId(Long periodId) {
			this.periodId = periodId;
		    }

		    public Long getProjectId() {
			return projectId;
		    }

		    public void setProjectId(Long projectId) {
			this.projectId = projectId;
		    }

		    public String getProjectType() {
			return projectType;
		    }

		    public void setProjectType(String projectType) {
			this.projectType = projectType;
		    }

		    public Long getResourceId() {
			return resourceId;
		    }

		    public void setResourceId(Long resourceId) {
			this.resourceId = resourceId;
		    }

		    public String getProjectTypeDescription() {
			return projectTypeDescription;
		    }

		    public void setProjectTypeDescription(String projectTypeDescription) {
			this.projectTypeDescription = projectTypeDescription;
		    }

		    public String getProjectIdProjectName() {
			return projectIdProjectName;
		    }

		    public void setProjectIdProjectName(String projectIdProjectName) {
			this.projectIdProjectName = projectIdProjectName;
		    }

		    public Long getResourceIdResourceId() {
			return resourceIdResourceId;
		    }

		    public void setResourceIdResourceId(Long resourceIdResourceId) {
			this.resourceIdResourceId = resourceIdResourceId;
		    }

		    public String getResourceIdResourceName() {
			return resourceIdResourceName;
		    }

		    public void setResourceIdResourceName(String resourceIdResourceName) {
			this.resourceIdResourceName = resourceIdResourceName;
		    }

		    public String getResourceIdResourceType() {
			return resourceIdResourceType;
		    }

		    public void setResourceIdResourceType(String resourceIdResourceType) {
			this.resourceIdResourceType = resourceIdResourceType;
		    }

		    public String getResourceIdResourceTypeDescription() {
			return resourceIdResourceTypeDescription;
		    }

		    public void setResourceIdResourceTypeDescription(String resourceIdResourceTypeDescription) {
			this.resourceIdResourceTypeDescription = resourceIdResourceTypeDescription;
		    }

		    public Date getAuctionBeginDate() {
			return auctionBeginDate;
		    }

		    public void setAuctionBeginDate(Date auctionBeginDate) {
			this.auctionBeginDate = auctionBeginDate;
		    }

		    public Date getAuctionEndDate() {
			return auctionEndDate;
		    }

		    public void setAuctionEndDate(Date auctionEndDate) {
			this.auctionEndDate = auctionEndDate;
		    }

		    public Long getAuctionId() {
			return auctionId;
		    }

		    public void setAuctionId(Long auctionId) {
			this.auctionId = auctionId;
		    }

		    public Long getAuctionPeriodId() {
			return auctionPeriodId;
		    }

		    public void setAuctionPeriodId(Long auctionPeriodId) {
			this.auctionPeriodId = auctionPeriodId;
		    }

		    public String getAuctionType() {
			return auctionType;
		    }

		    public void setAuctionType(String auctionType) {
			this.auctionType = auctionType;
		    }

		    public Long getCommitmentPeriodId() {
			return commitmentPeriodId;
		    }

		    public void setCommitmentPeriodId(Long commitmentPeriodId) {
			this.commitmentPeriodId = commitmentPeriodId;
		    }

		    public String getAuctionTypeDescription() {
			return auctionTypeDescription;
		    }

		    public void setAuctionTypeDescription(String auctionTypeDescription) {
			this.auctionTypeDescription = auctionTypeDescription;
		    }

		    public Date getCommitmentPeriodBeginDate() {
			return commitmentPeriodBeginDate;
		    }

		    public void setCommitmentPeriodBeginDate(Date commitmentPeriodBeginDate) {
			this.commitmentPeriodBeginDate = commitmentPeriodBeginDate;
		    }

		    public String getCommitmentPeriodDescription() {
			return commitmentPeriodDescription;
		    }

		    public void setCommitmentPeriodDescription(String commitmentPeriodDescription) {
			this.commitmentPeriodDescription = commitmentPeriodDescription;
		    }

		    public Date getCommitmentPeriodEndDate() {
			return commitmentPeriodEndDate;
		    }

		    public void setCommitmentPeriodEndDate(Date commitmentPeriodEndDate) {
			this.commitmentPeriodEndDate = commitmentPeriodEndDate;
		    }

		    public Long getCommitmentPeriodParentPeriodId() {
			return commitmentPeriodParentPeriodId;
		    }

		    public void setCommitmentPeriodParentPeriodId(Long commitmentPeriodParentPeriodId) {
			this.commitmentPeriodParentPeriodId = commitmentPeriodParentPeriodId;
		    }

		    public Long getCommitmentPeriodPeriodId() {
			return commitmentPeriodPeriodId;
		    }

		    public void setCommitmentPeriodPeriodId(Long commitmentPeriodPeriodId) {
			this.commitmentPeriodPeriodId = commitmentPeriodPeriodId;
		    }

		    public String getCommitmentPeriodPeriodType() {
			return commitmentPeriodPeriodType;
		    }

		    public void setCommitmentPeriodPeriodType(String commitmentPeriodPeriodType) {
			this.commitmentPeriodPeriodType = commitmentPeriodPeriodType;
		    }

		    public Date getPeriodBeginDate() {
			return periodBeginDate;
		    }

		    public void setPeriodBeginDate(Date periodBeginDate) {
			this.periodBeginDate = periodBeginDate;
		    }

		    public String getPeriodDescription() {
			return periodDescription;
		    }

		    public void setPeriodDescription(String periodDescription) {
			this.periodDescription = periodDescription;
		    }

		    public Date getPeriodEndDate() {
			return periodEndDate;
		    }

		    public void setPeriodEndDate(Date periodEndDate) {
			this.periodEndDate = periodEndDate;
		    }

		    public Long getPeriodParentPeriodId() {
			return periodParentPeriodId;
		    }

		    public void setPeriodParentPeriodId(Long periodParentPeriodId) {
			this.periodParentPeriodId = periodParentPeriodId;
		    }

		    public Long getPeriodPeriodId() {
			return periodPeriodId;
		    }

		    public void setPeriodPeriodId(Long periodPeriodId) {
			this.periodPeriodId = periodPeriodId;
		    }

		    public String getPeriodPeriodType() {
			return periodPeriodType;
		    }

		    public void setPeriodPeriodType(String periodPeriodType) {
			this.periodPeriodType = periodPeriodType;
		    }

		    public Long getCustomerIdCustomerId() {
			return customerIdCustomerId;
		    }

		    public void setCustomerIdCustomerId(Long customerIdCustomerId) {
			this.customerIdCustomerId = customerIdCustomerId;
		    }

		    public String getCustomerIdCustomerName() {
			return customerIdCustomerName;
		    }

		    public void setCustomerIdCustomerName(String customerIdCustomerName) {
			this.customerIdCustomerName = customerIdCustomerName;
		    }

		    public String getAttachmentTypeApplicationCategoryType() {
			return attachmentTypeApplicationCategoryType;
		    }

		    public void setAttachmentTypeApplicationCategoryType(String attachmentTypeApplicationCategoryType) {
			this.attachmentTypeApplicationCategoryType = attachmentTypeApplicationCategoryType;
		    }

		    public String getAttachmentTypeAttachmentType() {
			return attachmentTypeAttachmentType;
		    }

		    public void setAttachmentTypeAttachmentType(String attachmentTypeAttachmentType) {
			this.attachmentTypeAttachmentType = attachmentTypeAttachmentType;
		    }

		    public String getAttachmentTypeDescription() {
			return attachmentTypeDescription;
		    }

		    public void setAttachmentTypeDescription(String attachmentTypeDescription) {
			this.attachmentTypeDescription = attachmentTypeDescription;
		    }

		    public Long getAttachmentAttachmentId() {
			return attachmentAttachmentId;
		    }

		    public void setAttachmentAttachmentId(Long attachmentAttachmentId) {
			this.attachmentAttachmentId = attachmentAttachmentId;
		    }

		    public Date getAttachmentCreateDate() {
			return attachmentCreateDate;
		    }

		    public void setAttachmentCreateDate(Date attachmentCreateDate) {
			this.attachmentCreateDate = attachmentCreateDate;
		    }

		    public String getAttachmentDescription() {
			return attachmentDescription;
		    }

		    public void setAttachmentDescription(String attachmentDescription) {
			this.attachmentDescription = attachmentDescription;
		    }

		    public String getAttachmentFileName() {
			return attachmentFileName;
		    }

		    public void setAttachmentFileName(String attachmentFileName) {
			this.attachmentFileName = attachmentFileName;
		    }

		    public String getAttachmentExtension() {
			return attachmentExtension;
		    }

		    public void setAttachmentExtension(String attachmentExtension) {
			this.attachmentExtension = attachmentExtension;
		    }

		    public String getAttachmentIdFilePath() {
			return attachmentIdFilePath;
		    }

		    public void setAttachmentIdFilePath(String attachmentIdFilePath) {
			this.attachmentIdFilePath = attachmentIdFilePath;
		    }
	}

	@Autowired
    	private JdbcTemplate jdbcTemplate;
    	
    	private ObjectMapper objectMapper = new ObjectMapper();
    	
    	private String sqlQuery = "SELECT x.attach_proposal_attachment_id AS AttachmentId, x.attach_proposal_attachment_type AS AttachmentType, x.attach_proposal_proposal_id AS ProposalId, x.attach_proposal_proposal_id_customer_id AS CustomerId, x.attach_proposal_proposal_id_period_id AS PeriodId, x.attach_proposal_proposal_id_project_id AS ProjectId, x.attach_proposal_proposal_id_project_type AS ProjectType, x.attach_proposal_proposal_id_resource_id AS ResourceId, x.attach_proposal_proposal_id_project_type_project_type_desc AS ProjectTypeDescription, x.attach_proposal_proposal_id_project_id_project_name AS ProjectIdProjectName, x.attach_proposal_proposal_id_resource_id_resource_id AS ResourceIdResourceId, x.attach_proposal_proposal_id_resource_id_resource_name AS ResourceIdResourceName, x.attach_proposal_proposal_id_resource_id_resource_type AS ResourceIdResourceType, x.attach_proposal_proposal_id_resource_id_resource_type_resource_type_desc AS ResourceIdResourceTypeDescription, x.attach_proposal_proposal_id_auction_id_auction_begin_date AS AuctionBeginDate, x.attach_proposal_proposal_id_auction_id_auction_end_date AS AuctionEndDate, x.attach_proposal_proposal_id_auction_id_auction_id AS AuctionId, x.attach_proposal_proposal_id_auction_id_auction_period_id AS AuctionPeriodId, x.attach_proposal_proposal_id_auction_id_auction_type AS AuctionType, x.attach_proposal_proposal_id_auction_id_commitment_period_id AS CommitmentPeriodId, x.attach_proposal_proposal_id_auction_id_auction_type_auction_type AS AuctionTypeDescription, x.attach_proposal_proposal_id_auction_id_commitment_period_id_begin_date AS CommitmentPeriodBeginDate, x.attach_proposal_proposal_id_auction_id_commitment_period_id_description AS CommitmentPeriodDescription, x.attach_proposal_proposal_id_auction_id_commitment_period_id_end_date AS CommitmentPeriodEndDate, x.attach_proposal_proposal_id_auction_id_commitment_period_id_parent_period_id AS CommitmentPeriodParentPeriodId, x.attach_proposal_proposal_id_auction_id_commitment_period_id_period_id AS CommitmentPeriodPeriodId, x.attach_proposal_proposal_id_auction_id_commitment_period_id_period_type AS CommitmentPeriodPeriodType, x.attach_proposal_proposal_id_period_id_begin_date AS PeriodBeginDate, x.attach_proposal_proposal_id_period_id_description AS PeriodDescription, x.attach_proposal_proposal_id_period_id_end_date AS PeriodEndDate, x.attach_proposal_proposal_id_period_id_parent_period_id AS PeriodParentPeriodId, x.attach_proposal_proposal_id_period_id_period_id AS PeriodPeriodId, x.attach_proposal_proposal_id_period_id_period_type AS PeriodPeriodType, x.attach_proposal_proposal_id_customer_id_customer_id AS CustomerIdCustomerId, x.attach_proposal_proposal_id_customer_id_customer_name AS CustomerIdCustomerName, x.attach_proposal_attachment_type_application_category_type AS AttachmentTypeApplicationCategoryType, x.attach_proposal_attachment_type_attachment_type AS AttachmentTypeAttachmentType, x.attach_proposal_attachment_type_description AS AttachmentTypeDescription, x.attach_proposal_attachment_id_attachment_id AS AttachmentIdAttachmentId, x.attach_proposal_attachment_id_create_date AS AttachmentIdCreateDate, x.attach_proposal_attachment_id_description AS AttachmentIdDescription, x.attach_proposal_attachment_id_file_name AS AttachmentIdFileName, x.attach_proposal_attachment_id_file_path AS AttachmentIdFilePath";

	@GetMapping("")
	public ResponseEntity<List<ResultDTO>> getDocs(@RequestParam Map<String,String> allRequestParams) throws JsonProcessingException{
		String innerJoin = Hardcoded.dataBaseTree.getTreeInnerJoin();
		String filters = Hardcoded.dataBaseTree.generateFilterQuery(allRequestParams);
		String query = sqlQuery + " FROM (" + innerJoin + " WHERE " + filters + ") AS x;";
		try{
			List<ResultDTO> data = jdbcTemplate.query(query, new RowMapper<ResultDTO>() {
				
				@Override
				public ResultDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
				    ResultDTO out = new ResultDTO();
					out.setAttachmentId(rs.getLong("AttachmentId"));
					out.setAttachmentType(rs.getString("AttachmentType"));
					out.setProposalId(rs.getLong("ProposalId"));
					out.setCustomerId(rs.getLong("CustomerId"));
					out.setPeriodId(rs.getLong("PeriodId"));
					out.setProjectId(rs.getLong("ProjectId"));
					out.setProjectType(rs.getString("ProjectType"));
					out.setResourceId(rs.getLong("ResourceId"));
					out.setProjectTypeDescription(rs.getString("ProjectTypeDescription"));
					out.setProjectIdProjectName(rs.getString("ProjectIdProjectName"));
					out.setResourceIdResourceId(rs.getLong("ResourceIdResourceId"));
					out.setResourceIdResourceName(rs.getString("ResourceIdResourceName"));
					out.setResourceIdResourceType(rs.getString("ResourceIdResourceType"));
					out.setResourceIdResourceTypeDescription(rs.getString("ResourceIdResourceTypeDescription"));
					out.setAuctionBeginDate(rs.getDate("AuctionBeginDate"));
					out.setAuctionEndDate(rs.getDate("AuctionEndDate"));
					out.setAuctionId(rs.getLong("AuctionId"));
					out.setAuctionPeriodId(rs.getLong("AuctionPeriodId"));
					out.setAuctionType(rs.getString("AuctionType"));
					out.setCommitmentPeriodId(rs.getLong("CommitmentPeriodId"));
					out.setAuctionTypeDescription(rs.getString("AuctionTypeDescription"));
					out.setCommitmentPeriodBeginDate(rs.getDate("CommitmentPeriodBeginDate"));
					out.setCommitmentPeriodDescription(rs.getString("CommitmentPeriodDescription"));
					out.setCommitmentPeriodEndDate(rs.getDate("CommitmentPeriodEndDate"));
					out.setCommitmentPeriodParentPeriodId(rs.getLong("CommitmentPeriodParentPeriodId"));
					out.setCommitmentPeriodPeriodId(rs.getLong("CommitmentPeriodPeriodId"));
					out.setCommitmentPeriodPeriodType(rs.getString("CommitmentPeriodPeriodType"));
					out.setPeriodBeginDate(rs.getDate("PeriodBeginDate"));
					out.setPeriodDescription(rs.getString("PeriodDescription"));
					out.setPeriodEndDate(rs.getDate("PeriodEndDate"));
					out.setPeriodParentPeriodId(rs.getLong("PeriodParentPeriodId"));
					out.setPeriodPeriodId(rs.getLong("PeriodPeriodId"));
					out.setPeriodPeriodType(rs.getString("PeriodPeriodType"));
					out.setCustomerIdCustomerId(rs.getLong("CustomerIdCustomerId"));
					out.setCustomerIdCustomerName(rs.getString("CustomerIdCustomerName"));
					out.setAttachmentTypeApplicationCategoryType(rs.getString("AttachmentTypeApplicationCategoryType"));
					out.setAttachmentTypeAttachmentType(rs.getString("AttachmentTypeAttachmentType"));
					out.setAttachmentTypeDescription(rs.getString("AttachmentTypeDescription"));
					out.setAttachmentAttachmentId(rs.getLong("AttachmentAttachmentId"));
					out.setAttachmentCreateDate(rs.getDate("AttachmentCreateDate"));
					out.setAttachmentDescription(rs.getString("AttachmentDescription"));
					out.setAttachmentFileName(rs.getString("AttachmentFileName"));
					out.setAttachmentExtension(rs.getString("AttachmentExtension"));
					out.setAttachmentIdFilePath(rs.getString("AttachmentIdFilePath"));
					return out;
				}
			    }); //.queryForList(query);
			return new ResponseEntity<>(data, HttpStatus.OK);
		}
		catch (Exception e){
			System.out.println(e);
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.NO_CONTENT);
		}
	}
	
	@GetMapping("/help")
	public ResponseEntity<String> getHelp(@RequestParam Map<String,String> allRequestParams) throws JsonProcessingException{
		return new ResponseEntity<>(Hardcoded.dataBaseTree.documentation(), HttpStatus.OK);
	}
	
};
