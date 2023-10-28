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
    name: "Auction Type",
    textboxes: 2,
    api: "/api/v1/bruh3",
  },
  {
    name: "Resource Type",
    dropdown: 2,
    api: "/api/v1/bruh4",
  },

  {
    name: "Customer Name",
    //TODO: come up with way to filter by multiple customer names
    textboxes: 1,
    api: "/api/v1/bruh6",
  },
  {
    name: "Periods",
    calendar: true,
    api: "/api/v1/bruh7",
  },
  {
    name: "Proposal Type",
    calendar: true,
    api: "/api/v1/bruh8",
  },
];
