package com.ncr.app;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.ncr.app.models.RevGeoCodingResponse;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeocodingApplicationIntegrationTests {
   
	@Autowired
    private TestRestTemplate restTemplate;
	
    @Test
    public void createClientTestGetAddress() {
        ResponseEntity<RevGeoCodingResponse> responseEntity =
            restTemplate.getForEntity("/geocoding/address?lat=33.969601&lng=-84.100033", RevGeoCodingResponse.class);
        RevGeoCodingResponse revGeoCodingResponse = responseEntity.getBody();

        Assert.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assert.assertEquals("2651 Satellite Blvd, Duluth, GA 30096, USA", revGeoCodingResponse.getFormattedAddress());
    }

}
