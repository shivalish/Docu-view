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
    api: {auction_type: []}, //default value unselected
  },
  {
    name: "Auction Period",
    dropdown: Array.from({length: 151}, (_, i) => i + 1900),
    api: {auction_period: 1900}, //default value starts at 1900
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
    api: {attachment_type: ""}, //default value is unselected
  },
  {
    name: "Customer Name",
    textbox: true,
    api: {customer_name: ""},
  },
  {
    name: "File Name",
    textbox: true,
    api: {file_name: ""},
  }
];
