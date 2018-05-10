package com.khylo.timetracker.model;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khylo.timetracker.mongorepo.TimesheetRepo;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TimeTrackerRepoIntegrationTests {
    @Autowired private WebApplicationContext wac;

    @Autowired private ObjectMapper mapper;
    
    @Autowired private TimesheetRepo timesheetRepo;

    private MockMvc mvc;
    
    private static final int sizeOfObjects=8;
    // Saving url in create test for use in delete test
    private static String newObjUrl;
    
    private BigDecimal bd(String b) {
    	return new BigDecimal(b);
    }
    
    private BigDecimal bd(int b) {
    	return new BigDecimal(b);
    }


    @BeforeClass
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();

		timesheetRepo.deleteAll();
		Timesheet li = Timesheet.builder().name("Keith").manager("Padraig").agent("eFrontiers").year(2018).month(4).days(bd(20)).onCall(bd(4)).build();
		timesheetRepo.save(li);
		timesheetRepo.save(Timesheet.builder().name("Keith").manager("Padraig").client("Fidelity").agent("eFrontiers").year(2018).month(4).days(bd(20)).onCall(bd(4)).build());
		timesheetRepo.save(Timesheet.builder().name("Keith").manager("Padraig").client("Fidelity").agent("eFrontiers").year(2018).month(3).days(bd("20.5")).onCall(bd(8)).build());
		timesheetRepo.save(Timesheet.builder().name("Keith").manager("Padraig").client("Fidelity").agent("eFrontiers").year(2018).month(2).days(bd(20)).onCall(bd(4)).build());
		timesheetRepo.save(Timesheet.builder().name("Keith").manager("Padraig").client("Fidelity").agent("eFrontiers").year(2018).month(1).days(bd(19)).onCall(bd(4)).build());
		timesheetRepo.save(Timesheet.builder().name("Keith").manager("Padraig").client("Fidelity").agent("eFrontiers").year(2017).month(12).days(bd(19)).onCall(bd(8)).build());
		timesheetRepo.save(Timesheet.builder().name("Test").manager("Mgr").client("client").agent("Agent").year(2016).month(4).days(bd(20)).overtime(bd("4.5")).onCall(bd(4)).build());
		timesheetRepo.save(Timesheet.builder().name("Test2").manager("Mgr").client("client").agent("Agent").year(2016).month(4).days(bd(5)).overtime(bd(".5")).onCall(bd(4)).build());
    }
    
    @AfterClass
    public void teardown() {
    	timesheetRepo.deleteAll();
    }
    
    @Test
    public void testClean() throws Exception {
    	MvcResult result = mvc.perform(get("/terms").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200)).andReturn();
    	Map<String, Object> newObj = mapper.readValue(result.getResponse().getContentAsString(), Map.class);
    	List<Map> terms = ((List<Map>)((Map)newObj.get("_embedded")).get("terms"));
    	for(Map term: terms) {
    		if(term.get("name").equals("TestTerm")) {
    			doDelete(((Map)((Map)term.get("_links")).get("self")).get("href").toString());
    		}
    	}
    }
    
    private void doDelete(String url) throws Exception {
    	mvc.perform(delete(url).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().is(204));
    }


    @Test
    public void testCreateJson() throws Exception {
    	// confirm size before
    	ResultActions res = mvc.perform(get("/timesheets").accept(MediaType.APPLICATION_JSON));
        res.andExpect(status().isOk())
           .andExpect(jsonPath("$._embedded.terms", hasSize(sizeOfObjects)));
        //create
    	Timesheet newTerm = Timesheet.builder().name("testy").build();
    	MvcResult result = mvc.perform(post("/timesheets").content(mapper.writeValueAsString(newTerm)).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201)).andReturn();
    	//Confirm size after
    	res = mvc.perform(get("/timesheets").accept(MediaType.APPLICATION_JSON));
        res.andExpect(status().isOk())
           .andExpect(jsonPath("$._embedded.terms", hasSize(sizeOfObjects+1)));
        // Save url of new object for delete
        Map<String, Object> jsonRespAsMap = mapper.readValue(result.getResponse().getContentAsString(), Map.class);
    	newObjUrl = ((Map)((Map)jsonRespAsMap.get("_links")).get("self")).get("href").toString();
    	
    }
    
    @Test
    public void testDelete() throws Exception {
    	//Delete blocked. .expect 405
        mvc.perform(delete(newObjUrl).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(405));
    }
    

    @Test
    public void testGetAll() throws Exception {
    	ResultActions res = mvc.perform(get("/timesheets").accept(MediaType.APPLICATION_JSON));
        res.andExpect(status().isOk())
           .andExpect(jsonPath("$._embedded.timesheets[*].name", hasItem("EBS")))
           .andExpect(jsonPath("$._embedded.timesheets[*].name", hasItem("EFS")))
           .andExpect(jsonPath("$._embedded.timesheets[*].name", hasItem("Glacier")))
           .andExpect(jsonPath("$._embedded.timesheets[*].name", hasItem("S3")))
           .andExpect(jsonPath("$._embedded.timesheets[*].name", hasItem("CosmosDB")))
           .andExpect(jsonPath("$._embedded.timesheets[*].name", hasItems("EBS", "EFS", "S3", "Glacier", "Citus", "CosmosDB")));
        
        // Sorted Order by param?
        res = mvc.perform(get("/terms").param("sort","y,dsc").param("sort","m,dsc").accept(MediaType.APPLICATION_JSON));
        res.andExpect(status().isOk())
        	.andExpect(jsonPath("$._embedded.timesheets[0].days", is(bd("20"))))
        	.andExpect(jsonPath("$._embedded.timesheets[1].days", is(bd("20"))))	
        	.andExpect(jsonPath("$._embedded.timesheets[2].days", is(bd("20.5"))))
		   .andExpect(jsonPath("$._embedded.timesheets[3].days", is(bd("20"))))
		   .andExpect(jsonPath("$._embedded.timesheets[4].days", is(bd("19"))))
		   .andExpect(jsonPath("$._embedded.timesheets[5].days", is(bd("19"))))
		   .andExpect(jsonPath("$._embedded.timesheets[6].name", is("Test")))
		   .andExpect(jsonPath("$._embedded.timesheets[7].name", is("Test2")));

    }
    
    @Test
    public void testSearchByLabels() throws Exception {
    	List<Map<String, String>> testDataTable = List.of(Map.of("name", "Test", "ans","1"),
    														Map.of("name", "Keith", "ans","6"));
    	for(Map<String, String> testData: testDataTable) {
    		String name = testData.get("name");
    		Integer ans = Integer.parseInt(testData.get("ans"));
    		String[] vals = testData.get("vals").split(",");
        	ResultActions res = mvc.perform(get("/terms/search/findByName").param("name", name).accept(MediaType.APPLICATION_JSON));
        	res.andExpect(status().isOk())
        	.andExpect(jsonPath("$._embedded.terms", hasSize(ans)))
        	.andExpect(jsonPath("$._embedded.terms[*].name", hasItems(vals)));
    	}
        

    }
}
