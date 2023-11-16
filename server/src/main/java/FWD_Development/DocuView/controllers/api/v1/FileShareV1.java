package FWD_Development.DocuView.controllers.api.v1;

import java.util.ArrayList;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.groupdocs.viewer.FileType;
import com.groupdocs.viewer.Viewer;
import com.groupdocs.viewer.interfaces.PageStreamFactory;
import com.groupdocs.viewer.interfaces.ResourceStreamFactory;
import com.groupdocs.viewer.options.HtmlViewOptions;
import com.groupdocs.viewer.options.ViewOptions;
import com.groupdocs.viewer.options.LoadOptions;

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
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        InputStream inputStream;
        OutputStream outputStream = new ByteArrayOutputStream();
        googleDriveService.drive.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        byte[] bytes = ((ByteArrayOutputStream) outputStream).toByteArray();
        inputStream = new ByteArrayInputStream(bytes);
        final List<ByteArrayOutputStream> pages = new ArrayList<>();
        System.out.println(fileName);
        // add for Praesent.doc
        if (ext.equalsIgnoreCase("avi") || ext.equalsIgnoreCase("mov") 
        || ext.equalsIgnoreCase("mp3") || ext.equalsIgnoreCase("mpeg") || ext.equalsIgnoreCase("msg")
        ){
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            inputStream = classLoader.getResourceAsStream("icons/"+ ext + ".png");    
            ext = "png";
        }
        try (Viewer viewer = viewerChecker(inputStream, ext)) {
            // https://docs.groupdocs.com/viewer/java/save-output-to-stream/
            PageStreamFactory pageStreamFactory = new PageStreamFactory() {

                @Override
                public OutputStream createPageStream(int pageNumber) {
                    ByteArrayOutputStream pageStream = new ByteArrayOutputStream();
                    pages.add(pageStream);
                    return pageStream;
                }

                @Override
                public void closePageStream(int pageNumber, OutputStream outputStream) {
                    // Do not release page stream as we'll need to keep the stream open
                }
            };

            ViewOptions viewOptions = HtmlViewOptions.forEmbeddedResources(pageStreamFactory);
            viewer.view(viewOptions);
            inputStream = new ByteArrayInputStream(pages.get(0).toByteArray());
        }
        InputStreamResource resource = new InputStreamResource(inputStream);
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

    // zip files
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
