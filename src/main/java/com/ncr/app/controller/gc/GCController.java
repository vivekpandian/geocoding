package com.ncr.app.controller.gc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncr.app.exception.GeoCodingException;
import com.ncr.app.models.ApiErrorResponse;
import com.ncr.app.models.RevGeoCodingResponse;
import com.ncr.app.service.gc.GeoCodingService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@Controller
@RequestMapping("/geocoding")
public class GCController {

	@Autowired
	private GeoCodingService geoCodingService;

	@RequestMapping(value = "/address", method = RequestMethod.GET)
	@ApiOperation(value = "getFormattedAddress", nickname = "getFormattedAddress")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "lat", value = "Latitude", required = true, dataType = "numeric", paramType = "query"),
			@ApiImplicitParam(name = "lng", value = "Longitude", required = true, dataType = "numeric", paramType = "query") })
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success", response = RevGeoCodingResponse.class),
			@ApiResponse(code = 500, message = "Failure", response = ApiErrorResponse.class) })
	public @ResponseBody RevGeoCodingResponse getFormattedAddress(
			@RequestParam(name = "lat", required = true) double latitude,
			@RequestParam(name = "lng", required = true) double longitutude) throws GeoCodingException {
		
		String address = geoCodingService.getFormattedAddress(latitude, longitutude);
		RevGeoCodingResponse revGeoCodingResponse = new RevGeoCodingResponse();
		revGeoCodingResponse.setFormattedAddress(address);
		return revGeoCodingResponse;
	}

}
