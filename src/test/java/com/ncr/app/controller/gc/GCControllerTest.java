package com.ncr.app.controller.gc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.ncr.app.exception.GeoCodingException;
import com.ncr.app.service.gc.GeoCodingService;

@RunWith(SpringRunner.class)
@WebMvcTest(GCController.class)
public class GCControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	GeoCodingService geoCodingService;

	@Test
	public void testGetFormattedAddress() throws Exception {

		// Test valid case :
		// http://localhost:8080/address?lat=33.969601&lng=-84.100033
		given(geoCodingService.getFormattedAddress(33.969601d, -84.100033d))
				.willReturn("2651 Satellite Blvd, Duluth, GA 30096, USA");
		mockMvc.perform(get("/address").contentType(MediaType.APPLICATION_JSON).param("lat", "33.969601d").param("lng",
				"-84.100033d")).andExpect(status().is2xxSuccessful())
				.andExpect(jsonPath("$.formattedAddress").value("2651 Satellite Blvd, Duluth, GA 30096, USA"));
		// verify(geoCodingService, times (1)).getFormattedAddress(33.969601d,
		// -84.100033d);

		// Test 0.0, 0.0d
		given(geoCodingService.getFormattedAddress(0.0d, 0.0d)).willThrow(new GeoCodingException("Invalid Params"));
		mockMvc.perform(
				get("/address").contentType(MediaType.APPLICATION_JSON).param("lat", "0.0d").param("lng", "0.0d"))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage").value("ProcessingError"));

		// Test 0.0, -0.0d
		given(geoCodingService.getFormattedAddress(0.0d, -0.0d)).willThrow(new GeoCodingException("Invalid Params"));
		mockMvc.perform(
				get("/address").contentType(MediaType.APPLICATION_JSON).param("lat", "0.0d").param("lng", "-0.0d"))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage").value("ProcessingError"));

		// Test missing params.
		given(geoCodingService.getFormattedAddress(isNull(Double.class), any(Double.class)))
				.willThrow(new GeoCodingException("Invalid Params"));
		mockMvc.perform(get("/address").contentType(MediaType.APPLICATION_JSON).param("lng", "-0.0d"))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage").value("MissingParams"));

		given(geoCodingService.getFormattedAddress(any(Double.class), isNull(Double.class)))
				.willThrow(new GeoCodingException("Invalid Params"));
		mockMvc.perform(get("/address").contentType(MediaType.APPLICATION_JSON).param("lat", "-0.0d"))
				.andExpect(status().is5xxServerError()).andExpect(jsonPath("$.errorMessage").value("MissingParams"));
		
		reset(geoCodingService);

	}

}
