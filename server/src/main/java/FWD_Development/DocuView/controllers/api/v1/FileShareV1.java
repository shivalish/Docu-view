package FWD_Development.DocuView.controllers.api.v1;

import java.util.ArrayList;
/* CUSTOM ADDED LIBS */
import java.util.List;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;
/* CUSTOM ADDED LIBS */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.groupdocs.viewer.FileType;
import com.groupdocs.viewer.Viewer;
import com.groupdocs.viewer.interfaces.PageStreamFactory;
import com.groupdocs.viewer.options.HtmlViewOptions;
import com.groupdocs.viewer.options.PngViewOptions;
import com.groupdocs.viewer.options.ViewOptions;
import com.groupdocs.viewer.options.LoadOptions;
import javax.activation.MimetypesFileTypeMap;

import java.time.Instant;

@CrossOrigin(origins = "http://localhost:3000") // Default React port
@RestController
@RequestMapping("/api/v1/fileshare")
public class FileShareV1 {

    private final GoogleDriveService googleDriveService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    final MimetypesFileTypeMap fileTypeMap = new MimetypesFileTypeMap();
    

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
        final List<ByteArrayOutputStream> pages = new ArrayList<>();
        System.out.println(fileName);
        try (Viewer viewer = viewerChecker(inputStream, ext)) {
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
            String outputName = fileId + "_" + Instant.now().getEpochSecond();
            ViewOptions viewOptions = new PngViewOptions(outputName);
            Instant.now().getEpochSecond();
            viewer.view(viewOptions);
            //inputStream = new ByteArrayInputStream(pages.get(0).toByteArray());
            java.io.File file = new java.io.File(outputName);
            inputStream = new java.io.FileInputStream(file);
            byte[] bytes = inputStream.readAllBytes();
	        ByteArrayResource resource = new ByteArrayResource(bytes);	
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
        
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws IOException {
        // Use Google Drive API to get the file
        fileId = getGoogleId(getFilePath(fileId));
        File fileData = googleDriveService.drive.files().get(fileId).execute();
        InputStream inputStream = googleDriveService.drive.files().get(fileId).executeMediaAsInputStream();
	    byte[] bytes = inputStream.readAllBytes();
	    ByteArrayResource resource = new ByteArrayResource(bytes);	
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getName() + "\"")
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // zip files
    @GetMapping("/download/zipFiles")
    public ResponseEntity<Resource> zipFiles(@RequestParam List<String> fileIds) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOut = new ZipOutputStream(outputStream);

        for (String fileId : fileIds) {
            fileId = getGoogleId(getFilePath(fileId));
            File fileData = googleDriveService.drive.files().get(fileId).execute();
            String fileName = fileData.getName();
            String name = fileName.substring(0, fileName.lastIndexOf('.'));
            String ext = fileName.substring( fileName.lastIndexOf('.') + 1);

            InputStream inputStream = googleDriveService.drive.files().get(fileId).executeMediaAsInputStream();

            ZipEntry zipEntry = new ZipEntry(name + Instant.now().getEpochSecond() + ext);
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
        byte[] bytes = outputStream.toByteArray();
	    ByteArrayResource resource = new ByteArrayResource(bytes);
        return ResponseEntity.ok()
        	.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"files.zip\"")
        	.contentLength(resource.contentLength())
            .contentType(MediaType.MULTIPART_FORM_DATA)
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
