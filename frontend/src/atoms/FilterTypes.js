export default [
  {
    name: "Project Type",
    api: "/api/v1/bruh",
  },
  {
    name: "File Creation Date",
    calendar: true,
    api: "/api/v1/bruh9",
  },
  {
    name: "File Name",
    textboxes: 1,
    api: "/api/v1/bruh5",
  },
  {
    name: "Document Type",
    checkbox: [".pdf", ".docx", ".txt"],
    api: "/api/v1/bruh2",
  },
  {
    name: "Customer Name",
    textbox: true,
    placeholder: "customer name",
    api: { customer_name: [] },
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
    api: { project_name: [] }
  },

  {
    name: "Customer Name",
    //TODO: come up with way to filter by multiple customer names
    textboxes: 1,
    api: "/api/v1/bruh6",
  },
  {
    name: "File Name",
    textbox: true,
    placeholder: "file name",
    api: { file_name: [] },
  },
];
