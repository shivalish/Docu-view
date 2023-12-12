package FWD_Development.DocuView.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import FWD_Development.DocuView.controllers.api.v1.Login;
import FWD_Development.DocuView.controllers.api.v1.GoogleDriveService;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


@SpringBootTest
@AutoConfigureMockMvc

public class LoginFlowTest {
    // this test is to test the login flow of the application
    // it add users so be careful when running it

    @Autowired
    private Login controller;

    @Autowired
	private MockMvc mockMvc;

    @Test
	void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}

    @Test
    void apiLoginPath() throws Exception {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc
        .perform(post("/api/v1/auth/signup")
            .param("username", username)
            .param("password", password))
        .andExpect(status().isOk())
        .andExpect(content().string(not(isEmptyString())))
        .andExpect(content().json("{\"message\": \"Signup successful\"}"));

        System.out.println("Singuup successful");

        MvcResult result = this.mockMvc
        .perform(post("/api/v1/auth/login")
            .param("username", username)
            .param("password", password))
        .andExpect(status().isOk())
        .andExpect(content().string(not(isEmptyString())))
        .andReturn();

        System.out.println("Login successful");

        String content = result.getResponse().getContentAsString();

        Map<String, String> responseMap = objectMapper.readValue(content, new TypeReference<Map<String, String>>() {});

        String authToken = responseMap.get("authToken");

        result = this.mockMvc
            .perform(post("/api/v1/auth/refreshToken")
                .header("Authorization", "Bearer " + authToken))
            .andExpect(status().isOk())
            .andReturn();
        System.out.println("refreshToken successful");
        
        content = result.getResponse().getContentAsString();
        
    }
    
}
