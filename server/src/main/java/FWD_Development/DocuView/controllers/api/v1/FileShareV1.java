package FWD_Development.DocuView.controllers.api.v1;

/* CUSTOM ADDED LIBS */
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.attribute.FileTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.groupdocs.viewer.Viewer;
import com.groupdocs.viewer.options.PngViewOptions;
import com.groupdocs.viewer.options.ViewOptions;

//update so database --> google drive

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/fileshare")
public class FileShareV1 {
    
    private static final String VIEWER_LOC_VALUE = "viewerCache";
    public static final java.nio.file.Path VIEWER_LOC = java.nio.file.Paths.get(VIEWER_LOC_VALUE);

    private final GoogleDriveService googleDriveService;
    @Autowired
    	private JdbcTemplate jdbcTemplate;

    @Autowired
    public FileShareV1(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
        if (!java.nio.file.Files.exists(VIEWER_LOC)) {
            try {
                java.nio.file.Files.createDirectory(VIEWER_LOC);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    // for testing
    @GetMapping("/list")
    public List<File> listFiles() throws IOException {
        // googleDriveService.drive is of type Drive, docu: https://developers.google.com/resources/api-libraries/documentation/drive/v3/java/latest/com/google/api/services/drive/Drive.html 
        FileList fileList = googleDriveService.drive.files().list().execute();
        return fileList.getFiles();
    }

    // iframe: pdf and html
    // {".txt", ".xlsm", ".xlsx"}

    public static void previewCache(GoogleDriveService googleDriveService, JdbcTemplate jdbcTemplate, String fileId) throws Exception{
        fileId = getGoogleId(googleDriveService, getFilePath(jdbcTemplate, fileId));
        if (fileId == null || fileId.equals("")) return;
        java.nio.file.Path filePath = VIEWER_LOC.resolve(fileId + ".png");
        if (java.nio.file.Files.exists(filePath)){
            FileTime lastModifiedTime = java.nio.file.Files.getLastModifiedTime(filePath);
            long hours = ChronoUnit.HOURS.between(lastModifiedTime.toInstant(), Instant.now());
            if (hours > 24) {
                java.nio.file.Files.delete(filePath);
            } else {
                return;
            }
        }

        File fileData = googleDriveService.drive.files().get(fileId).execute();
        String fileName = fileData.getName();
        String ext = fileName.substring( fileName.lastIndexOf('.') + 1);
        InputStream inputStream;
        if (ext.equalsIgnoreCase("avi") || ext.equalsIgnoreCase("mov") 
        || ext.equalsIgnoreCase("mp3") || ext.equalsIgnoreCase("mpeg")
        || ext.equalsIgnoreCase("msg") || ext.equalsIgnoreCase("zip")
        ){
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            inputStream = classLoader.getResourceAsStream("icons/"+ ext + ".png");    
            ext = "png";
        }
        else {
            inputStream = googleDriveService.drive.files().get(fileId).executeMediaAsInputStream();
        }
        try (Viewer viewer = new Viewer(inputStream)) {
            ViewOptions viewOptions = new PngViewOptions(filePath.toString());
            viewer.view(viewOptions, 1);
        }
    }

    @GetMapping("/preview/{fileId}")
    public ResponseEntity<Resource> previewFile(@PathVariable String fileId) throws Exception {
        fileId = getGoogleId(googleDriveService, getFilePath(jdbcTemplate, fileId));
        if (fileId == null) return ResponseEntity.notFound().build();
        if (fileId.equals("")) return ResponseEntity.notFound().build();
        
        java.nio.file.Path filePath = VIEWER_LOC.resolve(fileId + ".png");
        if (java.nio.file.Files.exists(filePath)) {
            
            FileTime lastModifiedTime = java.nio.file.Files.getLastModifiedTime(filePath);
            long hours = ChronoUnit.HOURS.between(lastModifiedTime.toInstant(), Instant.now());

            if (hours > 24) {
                java.nio.file.Files.delete(filePath);
            } else {
                FileSystemResource resource = new FileSystemResource(filePath.toFile());
                return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
            }
        }
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
        try (Viewer viewer = new Viewer(inputStream)) {
            String outputName = filePath.toString();
            ViewOptions viewOptions = new PngViewOptions(outputName);
            viewer.view(viewOptions, 1);
            java.io.File file = new java.io.File(outputName);
            FileSystemResource resource = new FileSystemResource(file);
            return ResponseEntity.ok()
                    .contentLength(resource.contentLength())
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
        }
        
        
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws IOException {
        fileId = getGoogleId(googleDriveService, getFilePath(jdbcTemplate, fileId));
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
    @GetMapping({"/download/zipFiles", "/download/zipfiles"})
    public ResponseEntity<Resource> zipFiles(@RequestParam List<String> fileIds) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(outputStream);

        for (String fileId : fileIds) {
            fileId = getGoogleId(googleDriveService, getFilePath(jdbcTemplate, fileId));
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
        // fix to be be byte resource
        ByteArrayResource resource = new ByteArrayResource(outputStream.toByteArray());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
                .body(resource);
    }
    public static String getFilePath(JdbcTemplate jdbcTemplate, String file_id) {
        String query = "SELECT CONCAT(file_path, '/', file_name) AS full_path FROM ATTACHMENT_FILE WHERE attachment_id = ?;";
        return jdbcTemplate.queryForList(query, String.class, file_id).get(0);
    }

    public static String getGoogleId(GoogleDriveService googleDriveService, String path) throws IOException {
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
