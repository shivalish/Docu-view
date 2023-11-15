package FWD_Development.DocuView.controllers.api.v1;


/* CUSTOM ADDED LIBS */
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.MimeType;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.io.FileInputStream;
import java.io.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import java.util.Iterator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

//update so database --> google drive

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/fileshare")
public class FileShareV1 {

    private final GoogleDriveService googleDriveService;
    @Autowired
    	private JdbcTemplate jdbcTemplate;
    @Autowired
    public FileShareV1(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }

    // for testing
    @GetMapping("/list")
    public List<File> listFiles() throws IOException {
        FileList fileList = googleDriveService.drive.files().list().execute();
        return fileList.getFiles();
    }


    // iframe: pdf and html
    //{".txt", ".xlsm", ".xlsx"}
    @GetMapping("/preview/{fileId}")
    public ResponseEntity<Resource> previewFile(@PathVariable String fileId) throws Exception {
        fileId = getGoogleId(getFilePath(fileId));
        File fileData = googleDriveService.drive.files().get(fileId).execute();
        String fileName = fileData.getName();
        String ext = fileName.substring( fileName.lastIndexOf('.') + 1);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + fileName);
	InputStream inputStream;
        if (ext.equalsIgnoreCase("zip") || ext.equalsIgnoreCase("zipx") 
        || ext.equalsIgnoreCase("avi") || ext.equalsIgnoreCase("mov") 
        || ext.equalsIgnoreCase("mp3") || ext.equalsIgnoreCase("mpeg")
        || ext.equalsIgnoreCase("msg") || ext.equalsIgnoreCase("tiff")
        ){
            inputStream = new java.io.FileInputStream(new java.io.File("src/main/resources/icons/"+ ext + ".png"));
            ext = "png";
        }
        else{
            OutputStream outputStream = new ByteArrayOutputStream();
            googleDriveService.drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
            byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
            inputStream = new ByteArrayInputStream(bytes);
            if (ext.equalsIgnoreCase("doc") || ext.equalsIgnoreCase("docx")){
                com.aspose.words.Document document = new com.aspose.words.Document(inputStream);
                outputStream = new ByteArrayOutputStream();
                document.save(outputStream, com.aspose.words.SaveFormat.PDF);
                bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
                inputStream = new ByteArrayInputStream(bytes);
                ext = "pdf";
            }
            // modified from https://stackoverflow.com/questions/26056485/java-apache-poi-excel-save-as-pdf
            if (ext.equalsIgnoreCase("xlsm") || ext.equalsIgnoreCase("xlsx") || ext.equalsIgnoreCase("xls")){
		    // Read workbook into HSSFWorkbook
		    HSSFWorkbook my_xls_workbook = new HSSFWorkbook(inputStream); 
		    // Read worksheet into HSSFSheet
		    HSSFSheet my_worksheet = my_xls_workbook.getSheetAt(0); 
		    // To iterate over the rows
		    Iterator<Row> rowIterator = my_worksheet.iterator();
		    //We will create output PDF document objects at this point
		    Document iText_xls_2_pdf = new Document();
		    outputStream = new ByteArrayOutputStream();
		    PdfWriter.getInstance(iText_xls_2_pdf, outputStream);
		    iText_xls_2_pdf.open();
		    PdfPTable my_table = new PdfPTable(my_worksheet.getRow(0).getPhysicalNumberOfCells());
		    //We will use the object below to dynamically add new data to the table
		    PdfPCell table_cell;
		    //Loop through rows.
		    while(rowIterator.hasNext()) {
			    Row row = rowIterator.next(); 
			    Iterator<Cell> cellIterator = row.cellIterator();
			            while(cellIterator.hasNext()) {
			                    Cell cell = cellIterator.next(); //Fetch CELL
			                    switch(cell.getCellType()) { //Identify CELL type
			                            //you need to add more code here based on
			                            //your requirement / transformations
			                    case Cell.CELL_TYPE_STRING:
			                            //Push the data from Excel to PDF Cell
			                             table_cell=new PdfPCell(new Phrase(cell.getStringCellValue()));
			                             //feel free to move the code below to suit to your needs
			                             my_table.addCell(table_cell);
			                            break;
			                    }
			                    //next line
			            }

		    }
		    //Finally add the table to PDF document
		    iText_xls_2_pdf.add(my_table);                       
		    iText_xls_2_pdf.close();
                bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
                inputStream = new ByteArrayInputStream(bytes);
                ext = "pdf";
            }
            if (ext.equalsIgnoreCase("pdf")){
            	outputStream = new ByteArrayOutputStream();
                try (PDDocument document = PDDocument.load(inputStream)) {
		    PDFRenderer pdfRenderer = new PDFRenderer(document);
		    int numberOfPages = document.getNumberOfPages();
		    BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300);
		    ImageIO.write(bim, "png", outputStream);
            	    bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
            	    inputStream = new ByteArrayInputStream(bytes);
        	}
        	ext = "png";
            }
            
        }
        InputStreamResource resource = new InputStreamResource(inputStream);
        
        // TODO make look pretty
        if (ext.equalsIgnoreCase("png"))
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);
        else if (ext.equalsIgnoreCase("jpg"))
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_JPEG_VALUE);
        else if (ext.equalsIgnoreCase("gif"))
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_GIF_VALUE);
        else if (ext.equalsIgnoreCase("bmp"))
            headers.add(HttpHeaders.CONTENT_TYPE, "image/bmp");
        else if (ext.equalsIgnoreCase("htm") || ext.equalsIgnoreCase("html"))
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE);
        else if (ext.equalsIgnoreCase("txt"))
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        else if (ext.equalsIgnoreCase("pdf"))
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE);
            
        return ResponseEntity.ok()
                .headers(headers)
                .body(resource);
    }
    

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws IOException {
        // Use Google Drive API to get the file
        fileId = getGoogleId(getFilePath(fileId));
        OutputStream outputStream = new ByteArrayOutputStream();
        File fileData = googleDriveService.drive.files().get(fileId).execute();
        googleDriveService.drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);

        // Convert OutputStream to InputStream
        byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        // Return the file as a resource
        InputStreamResource resource = new InputStreamResource(inputStream);

        // Set content type and headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getName() + "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    //zip files
    @GetMapping("/download/zipFiles")
    public ResponseEntity<Resource> zipFiles(@RequestParam List<String> fileIds) throws IOException {  
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(outputStream);

        for (String fileId : fileIds) {
            fileId = getGoogleId(getFilePath(fileId));
            File fileData = googleDriveService.drive.files().get(fileId).execute();

            InputStream inputStream = googleDriveService.drive.files().get(fileId).executeMediaAsInputStream();

            ZipEntry zipEntry = new ZipEntry(fileData.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = inputStream.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            inputStream.close();
            zipOut.closeEntry();
        }
        zipOut.close();

        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(outputStream.toByteArray()));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
                .body(resource);
    }

    public String getFilePath(String file_id) {
        String query = "SELECT CONCAT(file_path, '/', file_name) AS full_path FROM ATTACHMENT_FILE WHERE attachment_id = ?;";
        return jdbcTemplate.queryForList(query, String.class, file_id).get(0);
    }

    public String getGoogleId(String path) throws IOException {
        path = path.startsWith("/") ? path.substring(1) : path;
        path = path.startsWith("\\") ? path.substring(1) : path;
        String rootFolderId = googleDriveService.drive.files().get("root").execute().getId();
        String[] pathArray;
        if (path.contains("\\"))
            pathArray = path.split("\\\\");
        else
            pathArray = path.split("/");
        String currentFolderId = rootFolderId;
        String fileId = null;
        for (String file : pathArray) {
            if (!file.isEmpty()) {
                FileList result = googleDriveService.drive.files().list()
                        .setQ("'" + currentFolderId + "' in parents and name = '" + file + "'")
                        .setFields("files(id)")
                        .execute();
                if (result.getFiles() != null && !result.getFiles().isEmpty()) {
                    fileId = result.getFiles().get(0).getId();
                } else {
                    return null;
                }
            }
            if (fileId == null) {
                return null; // File not found
            }
            currentFolderId = fileId;
        }
        return currentFolderId;
    }
};
