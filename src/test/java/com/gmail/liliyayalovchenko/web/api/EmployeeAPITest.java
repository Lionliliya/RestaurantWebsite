package com.gmail.liliyayalovchenko.web.api;

import com.gmail.liliyayalovchenko.web.configuration.WebConfig;
import com.gmail.liliyayalovchenko.web.configuration.WebInitializer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.Charset;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {WebConfig.class, WebInitializer.class}, loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration
public class EmployeeAPITest {


    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(
            MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

    private MockMvc mockMvc;

    @Autowired
    private EmployeeAPI employeeAPI;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(employeeAPI).build();
    }

    @Test
    public void shouldFindById() throws Exception {
        mockMvc.perform(get("/employee/{id}", 2))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.firstName", is("James")))
                .andExpect(jsonPath("$.secondName", is("Tomson")))
                .andExpect(jsonPath("$.position", is("WAITER")));
    }

    @Test
    public void shouldFindAllEmployee() throws Exception {
        mockMvc.perform(get("/employee"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0]", hasEntry("firstName", "Suise")))
                .andExpect(jsonPath("$[0]", not(hasEntry("id", 1))))
                .andExpect(jsonPath("$[0].secondName", is("Donnavan")))
                .andExpect(jsonPath("$[1].firstName", is("James")))
                .andExpect(jsonPath("$[1].secondName", is("Tomson")))
                .andExpect(jsonPath("$[2].firstName", is("Alex")))
                .andExpect(jsonPath("$[2].secondName", is("Marteen")));

    }

    @Test
    public void shouldFindEmployeeByFirstName() throws Exception {
        mockMvc.perform(get("/employee/firstName/James"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].firstName", is("James")))
                .andExpect(jsonPath("$[0].secondName", is("Tomson")))
                .andExpect(jsonPath("$[0].emplDate", is("15-01-2015")))
                .andExpect(jsonPath("$[0].position", is("WAITER")))
                .andExpect(jsonPath("$[0].orderList", notNullValue()));
    }

    @Test
    public void shouldFindEmployeeByFullName() throws Exception {
        mockMvc.perform(get("/employee/Suise/Donnavan"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.firstName", is("Suise")))
                .andExpect(jsonPath("$.secondName", is("Donnavan")))
                .andExpect(jsonPath("$.emplDate", is("10-01-2016")))
                .andExpect(jsonPath("$.position", is("WAITRESS")))
                .andExpect(jsonPath("$.orderList", hasSize(0)));
    }


    @Test
    public void employeeNotFound() throws Exception {
        mockMvc.perform(get("/employee/{id}", 23))
                .andExpect(status().isNotFound());
    }
}