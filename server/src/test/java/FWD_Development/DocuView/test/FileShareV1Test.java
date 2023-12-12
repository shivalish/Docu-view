package FWD_Development.DocuView.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import FWD_Development.DocuView.controllers.api.v1.FileShareV1;
import FWD_Development.DocuView.controllers.api.v1.GoogleDriveService;
import FWD_Development.DocuView.controllers.api.v1.JWTHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@SpringBootTest
@AutoConfigureMockMvc
public class FileShareV1Test {

    @Autowired
    private FileShareV1 controller;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
	private MockMvc mockMvc;

    private String token = "Bearer " + JWTHandler.createJWT("test", "test", 1000000);

    @Test
	void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
        
	}

    @Test
    void apiLoadDownload() throws Exception {
        // ONLY WORKS IF THERE IS A FILE WITH ID 773 IN THE DATABASE
        String fileId = "773";

        java.io.File file = new java.io.File("src/test/resources", "test.xls");
        byte[] expectedContent = java.nio.file.Files.readAllBytes(file.toPath());
        
        this.mockMvc
            .perform(get("/api/v1/fileshare/download/" + fileId).header("Authorization", token))
            .andExpect(status().isOk())
			.andExpect(content().bytes(expectedContent));
    }

    @Test
    void apiLoadPreview() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/fileshare/preview/773").header("Authorization", token))
            .andExpect(status().isOk())
			.andExpect(content().string(not(isEmptyString())));
    }

    @Test
    void apiLoadZipFiles() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/fileshare/download/zipFiles").header("Authorization", token).param("fileIds", "773").param("fileIds", "773"))
            .andExpect(status().isOk())
			.andExpect(content().string(not(isEmptyString())));
    }

    

    
}
