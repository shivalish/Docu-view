# REST-API

## `GET /api/v1/fileshare/preview/{fileId}`

**Description:** Retrieves a preview of the file with the specified ID from the Google Drive. If the preview is not available or the file is not found, a 404 Not Found status is returned.

**Path Parameters:**

- `fileId`: The ID of the file to preview.

**Response Type:** `image/png`

**Response:** A PNG image of the file preview.

**Side Effects:** If the preview of the file is older than 24 hours, it is deleted and a new preview is generated.

## `GET /api/v1/fileshare/download/{fileId}`

**Description:** Downloads the file with the specified ID from the Google Drive.

**Path Parameters:**

- `fileId`: The ID of the file to download.

**Response Type:** `application/octet-stream`

**Response:** The file as a downloadable resource. The `Content-Disposition` header is set to `attachment; filename="{fileId}"`, which prompts the browser to download the file instead of displaying it.

## `GET /api/v1/fileshare/download/zipFiles`

**Description:** Downloads multiple files from the Google Drive as a single ZIP file.

**Query Parameters:**

- `fileIds`: A list of IDs of the files to download.

**Response Type:** `application/octet-stream`

**Response:** A ZIP file containing the requested files as a downloadable resource. The `Content-Disposition` header is set to `attachment; filename="files.zip"`, which prompts the browser to download the file instead of displaying it.

## `GET /api/v1/filters`

**Description:** Retrieves a list of filters.

**Response Type:** `application/json`

**Response:** A JSON array of filters. The filters are hardcoded in the `Hardcoded.outerArray` variable.

## `GET /api/v1/database` or `GET /api/v1/database/infinite`

**Description:** Retrieves a list of documents based on the provided query parameters. The documents are retrieved from a database and cached asynchronously.

**Query Parameters (optional):**

- `fileCreation` (ISO 8601 date): The creation date of the file.
- `fileExtension` (string): The extension of the file. Possible values are `.bmp`, `.doc`, `.docx`, `.htm`, `.html`, `.jpg`, `.msg`, `.pdf`, `.txt`, `.xlsm`, `.xlsx`, `.zip`, `.zipx`.
- `fileName` (string): The name of the file.
- `attachmentType` (string): The type of the attachment.
- `customerName` (string): The name of the customer.
- `commitmentDateStart` (ISO 8601 date): The start date of the commitment.
- `commitmentDateEnd` (ISO 8601 date): The end date of the commitment.
- `auctionType` (string): The type of the auction.
- `resourceType` (string): The type of the resource.
- `auctionDateStart` (ISO 8601 date): The start date of the auction.
- `auctionDateEnd` (ISO 8601 date): The end date of the auction.
- `proposalDateStart` (ISO 8601 date): The start date of the proposal.
- `proposalsDateEnd` (ISO 8601 date): The end date of the proposal.

**Response Type:** `application/json`

**Response:** A JSON array of items that contain database information.

## `GET /api/v1/database/pages`

**Description:** Retrieves a paginated list of documents based on the provided query parameters. The documents are retrieved from a database and cached asynchronously.

**Query Parameters (optional):**

- `perPage` (default = 50): The number of documents to return per page. The value must be between 1 and 100.
- `page` (default = 1): The page number to return.
- `fileCreation` (ISO 8601 date): The creation date of the file.
- `fileExtension` (string): The extension of the file. Possible values are `.bmp`, `.doc`, `.docx`, `.htm`, `.html`, `.jpg`, `.msg`, `.pdf`, `.txt`, `.xlsm`, `.xlsx`, `.zip`, `.zipx`.
- `fileName` (string): The name of the file.
- `attachmentType` (string): The type of the attachment.
- `customerName` (string): The name of the customer.
- `commitmentDateStart` (ISO 8601 date): The start date of the commitment.
- `commitmentDateEnd` (ISO 8601 date): The end date of the commitment.
- `auctionType` (string): The type of the auction.
- `resourceType` (string): The type of the resource.
- `auctionDateStart` (ISO 8601 date): The start date of the auction.
- `auctionDateEnd` (ISO 8601 date): The end date of the auction.
- `proposalDateStart` (ISO 8601 date): The start date of the proposal.
- `proposalsDateEnd` (ISO 8601 date): The end date of the proposal.

**Response Type:** `application/json`

**Response:** A JSON array of items that contain database information.

## `GET /api/v1/database/pages/content`

**Description:** Retrieves the content of a specific page of documents based on the provided query parameters. The documents are retrieved from a database.

**Query Parameters: Same as ``GET /api/v1/database/pages``**

**Response Type:** `application/json` 

**Response:** A JSON object containing the following properties:

- `count`: The total number of documents.
- `page`: The current page number.
- `per_page`: The number of documents per page.
- `total_pages`: The total number of pages.

If no documents are found, a 204 No Content status is returned.

## `GET /api/v1/database/help`

**Description:** Retrieves help information for the `/api/v1/database` endpoint based on the provided query parameters.

**Response Type:** `application/json`

**Response:** A JSON string containing the URI query for the `/api/v1/database` endpoint.
