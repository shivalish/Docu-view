import axios from "axios";

const x = async () => {
  const res = await axios.get('http://localhost:8080/api/v1/database');
  return res.data;
}

async function getComboData(){
  return await x()
}

const bigData = await getComboData();
let projTypes = new Set();
let projName = [];
bigData.forEach(file => {
  projTypes.add(`${file.projectName} (${file.attachmentId})`)
  projName.push(`${file.projectName} (${file.attachmentId})`)
})

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
    api: { auctionType: [] }, //default value unselected
  },
  {
    name: "Auction Period",
    dropdown: Array.from({ length: 151 }, (_, i) => i + 1900),
    api: { auctionPeriod: 1900 }, //default value starts at 1900; TODO: we need to change this to match backend requirements
  },
  {
    name: "Resource Type",
    checkbox: [
      "Demand Resource",
      "Generator",
      "Import"
    ],
    api: { resourceType: [] }
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
    api: { attachmentType: "" }, //default value is unselected
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
      ...projName
    ],
    placeholder: "project",
    api: { projectName: [] }
  },
  {
    name: "Project Type",
    combo: [
      ...projTypes
    ],
    placeholder: "project type",
    api: { projectType: [] }
  },
  {
    name: "File Name",
    textbox: true,
    placeholder: "file name",
    api: { fileName: [] },
  },
];
