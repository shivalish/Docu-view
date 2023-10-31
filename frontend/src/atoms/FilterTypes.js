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
    api: "/api/v1/bruh11",
  },
  {
    name: "Auction Period",
    dropdown: true, //may have to change to array later
    api: "/api/v1/bruh",
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
    api: "/api/v1/bruh2",
  },
  {
    name: "Customer Name",
    textboxes: 1,
    api: "/api/v1/bruh3",
  },
  {
    name: "File Name",
    textboxes: 1,
    api: "/api/v1/bruh5",
  }
];
