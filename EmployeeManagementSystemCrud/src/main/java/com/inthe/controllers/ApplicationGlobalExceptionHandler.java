package com.inthe.controllers;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;




@ControllerAdvice
public class ApplicationGlobalExceptionHandler extends RuntimeException{

	

	private static final long serialVersionUID = 1L;

	@ExceptionHandler(Exception.class)
	public ModelAndView handleException(HttpServletRequest request ,Exception ex) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exception",ex);
		modelAndView.addObject("url", request.getRequestURL());
		modelAndView.setViewName("/error");
		return modelAndView;
	}
	
//	@ExceptionHandler(value = UsernameNotFoundException.class)
//	public String userNotFound(UsernameNotFoundException ex) {
//		
//		return "error/404";
//	}
}