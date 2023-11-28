export default [
  {
    name: "Auction Type",
    checkbox: [
      "Annual Bilateral Period",
      "First Annual Reconfiguration Auction",
      "Second Annual Reconfiguration Auction",
      "Third Annual Reconfiguration Auction",
      "FCA",
      "Monthly Bilateral Period",
      "Monthly Reconfiguration Auction",
      "RCA2",
    ],
<<<<<<< HEAD
    api: { auctionType: [] }, //default value unselected
=======
    api: { auction_type: [] }, //default value unselected
>>>>>>> parent of d7e2204 (Backend stage (#69))
  },
  {
    name: "Auction Period",
    dropdown: Array.from({ length: 151 }, (_, i) => i + 1900),
<<<<<<< HEAD
    api: { auctionPeriod: 1900 }, //default value starts at 1900; TODO: we need to change this to match backend requirements
=======
    api: { auction_period: 1900 }, //default value starts at 1900
>>>>>>> parent of d7e2204 (Backend stage (#69))
  },
  {
    name: "Resource Type",
    checkbox: [
      "Demand Resource",
      "Generator",
      "Import"
    ],
<<<<<<< HEAD
    api: { resourceType: [] }
=======
    api: { resource_type: [] }
>>>>>>> parent of d7e2204 (Backend stage (#69))
  },
  {
    name: "Attachment Type",
    combo: [
      "FLOOROFFERPRICE.REVIEWREQ.COSTJUST",
      "PROPOSAL.OTHERS",
      "PROPOSAL.QDN",
      "PROPOSAL.SA.SPR.CERT.DOC",
      "QP.DR.CUST_ACQ_PLAN",
      "QP.DR.FUNDINGSOURCE",
      "QP.DR.MV_PLAN",
      "QP.DR.MV_PLAN_SUPPORT",
      "QP.DR.PROJECT_DESCRIPTION",
      "QP.IMPORT.EXPORT_CONTRACT",
      "QP.IMPORT.EXT_RESOURCE",
      "QP.IMPORT.OWNERSHIP",
      "QP.IMPORT.SYS_LOAD_PROJECTIONS"
    ],
    placeholder: "attachment",
<<<<<<< HEAD
    api: { attachmentType: "" }, //default value is unselected
=======
    api: { attachment_type: "" }, //default value is unselected
>>>>>>> parent of d7e2204 (Backend stage (#69))
  },
  {
    name: "Customer Name",
    textbox: true,
    placeholder: "customer name",
    api: { customerName: [] },
  },
  {
    name: "Project Name",
    combo: [
      "Gravity Works",
      "Nice Load Response",
      "Solar Yes",
      "New Contract Year",
      "Additional LR"
    ],
    placeholder: "project",
    api: { projectName: [] }
  },
  {
    name: "Project Type",
    combo: [
      "Reestablishment",
      "Environmental Upgrade",
      "Incremental Increase of Existing Demand Resource",
      "Increase above Threshold",
      "Incremental Capacity",
      "New Demand Resource",
      "New Generation >= 20 MW",
      "New Generation < 20MW",
      "New Import",
      "Repowering",
      "Significant Increase",
    ],
    placeholder: "project type",
<<<<<<< HEAD
    api: { projectType: [] }
=======
    api: { project_type: [] }
>>>>>>> parent of d7e2204 (Backend stage (#69))
  },
  {
    name: "File Name",
    textbox: true,
    placeholder: "file name",
    api: { fileName: [] },
  },
];
