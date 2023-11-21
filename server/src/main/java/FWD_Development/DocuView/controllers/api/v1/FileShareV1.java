package FWD_Development.DocuView.controllers.api.v1;

<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
/* CUSTOM ADDED LIBS */
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
import java.util.Set;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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
//update so database --> google drive

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/fileshare")
public class FileShareV1 {
    
    private final String VIEWER_LOC_VALUE = "viewer/.cache/";
    private final java.nio.file.Path VIEWER_LOC = java.nio.file.Paths.get(VIEWER_LOC_VALUE);

    private final GoogleDriveService googleDriveService;
    @Autowired
<<<<<<< Updated upstream
    	private JdbcTemplate jdbcTemplate;
=======
    private JdbcTemplate jdbcTemplate;
   
    final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap(); 

>>>>>>> Stashed changes
    @Autowired
    public FileShareV1(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }


    // for testing
    @GetMapping("/list")
    public List<File> listFiles() throws IOException {
        // googleDriveService.drive is of type Drive, docu: https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/com/google/api/services/drive/Drive.html 
        FileList fileList = googleDriveService.drive.files().list().execute();
        return fileList.getFiles();
    }
<<<<<<< Updated upstream
    
     @GetMapping("/preview/{fileId}")
    public ResponseEntity<Resource> previewFile(@PathVariable String fileId) throws IOException {
        return null;
=======

    private Viewer viewerChecker(InputStream inputStream, String ext){
        if (ext.equalsIgnoreCase("msg")) return new Viewer(inputStream, new LoadOptions(FileType.MSG));
        FileType filetype = FileType.fromExtension("." + ext);
        if (filetype == null) return new Viewer(inputStream);
        return new Viewer(inputStream, new LoadOptions(filetype));
    }

    // iframe: pdf and html
    // {".txt", ".xlsm", ".xlsx"}
    @GetMapping("/preview/{fileId}")
    public ResponseEntity<Resource> previewFile(@PathVariable String fileId) throws Exception {
        fileId = getGoogleId(getFilePath(fileId));
        if (fileId == null) return ResponseEntity.notFound().build();
        if (fileId.equals("")) return ResponseEntity.notFound().build();

        try {
            java.nio.file.Path element = VIEWER_LOC.resolve(fileId + ".png");
            java.nio.file.attribute.FileTime lastModifiedTime = java.nio.file.Files.getLastModifiedTime(element);
            long hours = java.time.temporal.ChronoUnit.HOURS.between(lastModifiedTime.toInstant(), Instant.now());
            if (hours > 24) {
                java.nio.file.Files.delete(element);
            }
            else {
                FileSystemResource resource = new FileSystemResource(VIEWER_LOC.toFile());
                return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
            }
        } catch(IOException e) {}
              
        File fileData = googleDriveService.drive.files().get(fileId).execute();
        String fileName = fileData.getName();
        String ext = fileName.substring( fileName.lastIndexOf('.') + 1);
        InputStream inputStream;
        if (ext.equalsIgnoreCase("avi") || ext.equalsIgnoreCase("mov") 
        || ext.equalsIgnoreCase("mp3") || ext.equalsIgnoreCase("mpeg") || ext.equalsIgnoreCase("msg")
        ){
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            inputStream = classLoader.getResourceAsStream("icons/"+ ext + ".png");    
            ext = "png";
        }
        else {
            inputStream = googleDriveService.drive.files().get(fileId).executeMediaAsInputStream();
        }
        // System.out.println(fileName);
        try (Viewer viewer = viewerChecker(inputStream, ext)) {
            //final List<ByteArrayOutputStream> pages = new ArrayList<>();
            // https://docs.groupdocs.com/viewer/java/save-output-to-stream/
            //PageStreamFactory pageStreamFactory = new PageStreamFactory() {
            //    @Override
            //    public OutputStream createPageStream(int pageNumber) {
            //        ByteArrayOutputStream pageStream = new ByteArrayOutputStream();
            //        pages.add(pageStream);
            //        return pageStream;
            //    }
            //
            //    @Override
            //    public void closePageStream(int pageNumber, OutputStream outputStream) {
            //        // Do not release page stream as we'll need to keep the stream open
            //    }
            //};
            //ViewOptions viewOptions = HtmlViewOptions.forEmbeddedResources(pageStreamFactory);
            String outputName = VIEWER_LOC.resolve(fileId + ".png").toString();
            ViewOptions viewOptions = new PngViewOptions(outputName);
            viewer.view(viewOptions, 1);
            //inputStream = new ByteArrayInputStream(pages.get(0).toByteArray());
            java.io.File file = new java.io.File(outputName);
            FileSystemResource resource = new FileSystemResource(file);
            //Merger merger = new Merger(inputStream);
            //for (int i = 1; i < pages.size(); i++) {
            //	InputStream page = new ByteArrayInputStream(pages.get(i).toByteArray());
            //	merger.join(page);
            //}
            //outputStream = new ByteArrayOutputStream();
            //merger.save(outputStream);
            //byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
            //inputStream = new ByteArrayInputStream(bytes);
            // Set content type and headers
	        return ResponseEntity.ok()
		        .contentLength(resource.contentLength())
		        .contentType(MediaType.IMAGE_PNG)
		        .body(resource);
        }
        
>>>>>>> Stashed changes
    }
    

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws IOException {
        // Use Google Drive API to get the file
        OutputStream outputStream = new ByteArrayOutputStream();
        googleDriveService.drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);

        // Convert OutputStream to InputStream
        byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);

        // Return the file as a resource
        InputStreamResource resource = new InputStreamResource(inputStream);

        // Set content type and headers
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileId + "\"");

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

};
