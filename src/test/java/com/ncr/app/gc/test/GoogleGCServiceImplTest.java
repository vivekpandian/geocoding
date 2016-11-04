package com.ncr.app.gc.test;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.maps.model.LatLng;
import com.ncr.app.exception.GeoCodingException;
import com.ncr.app.service.gc.GeoCodingService;
import com.ncr.app.util.gc.GoogleMapsGeoCodingClient;;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoogleGCServiceImplTest {

	@MockBean
	private GoogleMapsGeoCodingClient googleMapsGeoCodingClientMock;

	@Autowired
	private GeoCodingService geoCodingService;

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
		reset(googleMapsGeoCodingClientMock);
	}

	@Test
	public void testGetFormattedAddressWhenGMGCThrowsException() throws GeoCodingException {
		given(this.googleMapsGeoCodingClientMock.getFormattedAddressForCoords(any(LatLng.class)))
				.willThrow(new GeoCodingException("Exception from Google Maps for reverse Geocode"));
		try {
			geoCodingService.getFormattedAddress(33.969601d, -84.100033d);
			Assert.fail("Didn't throw exception");
		} catch (GeoCodingException geoCodingException) {
			// pass
		}
	}

	@Test
	public void testGetFormattedAddressWhenNoError() throws Exception {
		Double validLat = 30.969601d;
		Double validLng = -80.100033d;
		LatLng validLatLng = new LatLng(validLat, validLng);
		String addressForValidLatLng = "some street, some city, some state, some country";

		// Valid case
		given(this.googleMapsGeoCodingClientMock.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng))))
				.willReturn(addressForValidLatLng);

		String address = geoCodingService.getFormattedAddress(validLat, validLng);
		Assert.assertEquals(addressForValidLatLng, address);
	}

	@Test
	public void testCachingWhenGetFormattedAddress() throws Exception {
		Double validLat1 = 33.969601d;
		Double validLng1 = -84.100033d;
		LatLng validLatLng1 = new LatLng(validLat1, validLng1);
		String addressForValidLatLng1 = "2651 Satellite Blvd, Duluth, GA 30096, USA";
		// setup Valid case for address 1
		given(this.googleMapsGeoCodingClientMock.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng1))))
				.willReturn(addressForValidLatLng1);

		String address = this.geoCodingService.getFormattedAddress(validLat1, validLng1);
		Assert.assertEquals(addressForValidLatLng1, address);
		verify(googleMapsGeoCodingClientMock, times(1))
				.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng1)));

		// call again for the same address. it should get from cache this time
		address = this.geoCodingService.getFormattedAddress(validLat1, validLng1);
		Assert.assertEquals(addressForValidLatLng1, address);
		// verify googlemaps api is called only once
		verify(googleMapsGeoCodingClientMock, times(1))
				.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng1)));

		Double validLat2 = 30.969601d;
		Double validLng2 = -80.100033d;
		LatLng validLatLng2 = new LatLng(validLat2, validLng2);
		String addressForValidLatLng2 = "some street2, some city2, some state2, some country2";
		// setup Valid case for address 2
		given(this.googleMapsGeoCodingClientMock.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng2))))
				.willReturn(addressForValidLatLng2);

		String address2 = this.geoCodingService.getFormattedAddress(validLat2, validLng2);
		Assert.assertEquals(addressForValidLatLng2, address2);
		verify(googleMapsGeoCodingClientMock, times(1))
				.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng2)));

		Double validLat3 = 20.969601d;
		Double validLng3 = -70.100033d;
		LatLng validLatLng3 = new LatLng(validLat3, validLng3);
		String addressForValidLatLng3 = "some street3, some city3, some state3, some country3";
		// setup Valid case for address 3
		given(this.googleMapsGeoCodingClientMock.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng3))))
				.willReturn(addressForValidLatLng3);

		String address3 = this.geoCodingService.getFormattedAddress(validLat3, validLng3);
		Assert.assertEquals(addressForValidLatLng3, address3);
		verify(googleMapsGeoCodingClientMock, times(1))
				.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng3)));

		// Cache has address2, address3 now.

		// call again for the address1. This should evict address2 and address1
		// to cache
		address = this.geoCodingService.getFormattedAddress(validLat1, validLng1);
		Assert.assertEquals(addressForValidLatLng1, address);
		// verify count is 2 for address1
		verify(googleMapsGeoCodingClientMock, times(2))
				.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng1)));

		address3 = this.geoCodingService.getFormattedAddress(validLat3, validLng3);
		Assert.assertEquals(addressForValidLatLng3, address3);
		// verify address3 is still in cache
		verify(googleMapsGeoCodingClientMock, times(1))
				.getFormattedAddressForCoords(argThat(new LatLngMatcher(validLatLng3)));

	}

	@TestConfiguration
	static class Config {

		@Bean
		public CacheManager cacheManager() {
			return new EhCacheCacheManager(ehCacheCacheManager().getObject());
		}

		@Bean
		public EhCacheManagerFactoryBean ehCacheCacheManager() {
			EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
			cmfb.setConfigLocation(new ClassPathResource("ehcache-test.xml"));
			cmfb.setShared(true);
			return cmfb;
		}

	}

}
