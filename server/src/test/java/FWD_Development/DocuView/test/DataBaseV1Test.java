package FWD_Development.DocuView.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

import FWD_Development.DocuView.controllers.api.v1.DataBaseV1;
import FWD_Development.DocuView.controllers.api.v1.GoogleDriveService;
import FWD_Development.DocuView.controllers.api.v1.JWTHandler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isEmptyString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.is;
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
class DataBaseV1Test {

    @Autowired
    private DataBaseV1 controller;

    private String token = "Bearer " + JWTHandler.createJWT("test", "test", 1000000);

    @Autowired
	private MockMvc mockMvc;

    @Test
	void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

    @Test
    void apiLoadInfinite() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/database").header("Authorization", token))
            .andExpect(status().isOk())
			.andExpect(content().string(not(isEmptyString())));
    }

    @Test
    void apiLoadPages() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/database/pages").header("Authorization", token))
            .andExpect(status().isOk())
			.andExpect(content().string(not(isEmptyString())));
    }

    @Test
    void apiLoadHelp() throws Exception {
        this.mockMvc
            .perform(get("/api/v1/database/help").header("Authorization", token))
            .andExpect(status().isOk())
			.andExpect(content().string(not(isEmptyString())));
    }

}