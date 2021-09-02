package com.web;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.service.ChangeService;

@RestController

//@RequestMapping("/") This is global level mapping 
public class WebController {
	
	@Autowired
	private ChangeService changeService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/change/{bill}/{order}")
	ResponseEntity<String> getChangeForBill(@PathVariable("bill") Integer bill, @PathVariable("order") String order) throws Exception{
		if( order.isBlank() || ! (order.toUpperCase().equals("MIN") || order.toUpperCase().equals("MAX"))) {
			throw new Exception("Invalid Order, Please provide MIN or MAX");
		}
		//this.changeService.getChangeForBill(bill);
		return ResponseEntity.ok().body(this.changeService.getChangeForBill(bill, order.toUpperCase()));
	}
	
	
	//With @Validated it will throw ConstraintViolationException and that has needs to be handled
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  ResponseEntity<String> handleConstraintViolationException(MethodArgumentTypeMismatchException e) {
	    return new ResponseEntity<>("not valid bill : " + e.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	
	@ExceptionHandler(Exception.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  ResponseEntity<String> handleException(Exception e) {
	    return new ResponseEntity<>( e.getMessage(), HttpStatus.BAD_REQUEST);
	  }

}
