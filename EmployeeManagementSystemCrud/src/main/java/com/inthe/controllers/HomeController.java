package com.inthe.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.inthe.model.User;
import com.inthe.repository.UserRepository;


@Controller
@ComponentScan(value = {"com.inthe.repository"})
public class HomeController {
	

	@Autowired
	private UserRepository repository;
	
//	@RequestMapping("/")
//	public String HomePage() {
//		return "homePage";
//	}
	
	
	@GetMapping("/")
	public String index(Model model,@AuthenticationPrincipal UserDetails currentUser) {
		List<User> users=  (List<User>) repository.findAll();
		model.addAttribute("userList", users);
		
		if(currentUser!=null) {
			model.addAttribute("current_user",currentUser);
		}
		return "index";
	}
	
	@GetMapping("/registration")
	public String registration(Model model) {
		model.addAttribute("user", new User());
		return "registration";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	


	@RequestMapping(value="/register", method = RequestMethod.POST)
	public String registerUser(@ModelAttribute(name = "user") @Valid User user, BindingResult errors, @RequestParam("confirm_pass") String pass,Model model) {
		user.setRole("employee");
		if(pass.equals(user.getPass())) {
			user.setPass((new BCryptPasswordEncoder()).encode(pass));
		}else {
			errors.rejectValue ("pass","error.user","passwords dont match");
		}
		if(errors.hasErrors()) {
			model.addAttribute("errors", errors);
			errors.getAllErrors();
			return "/registration";
		}
		repository.save(user);
		return "redirect:/registration?success";
	}
	
	
	@RequestMapping("/add_employee")
	public String showNewForm(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		
		return "add_employee";
	}
	
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(@ModelAttribute("user") User user, @RequestParam("current_id")  @Nullable Long current) {
		if(current==user.getId()) {
			user.setPass((new BCryptPasswordEncoder()).encode(user.getPass()));
			System.out.println("here");
		}
		repository.save(user);
		return "redirect:/";
	}
	@RequestMapping(value = "/edit_employee", method = RequestMethod.POST)
	public ModelAndView edit(@RequestParam("id") Long id,@AuthenticationPrincipal UserDetails currentUser, Model model) {
		ModelAndView modelAndView = new ModelAndView("edit_employee");
		User user = repository.findById(id).get();
		
		//Role role = roleRepo.findById(user.getId()).get();
		
		//System.out.println(role.getRole());
		
		modelAndView.addObject("user", user);
		if(currentUser!=null) {
			model.addAttribute("current_user",currentUser);
			model.addAttribute("id",id);
			System.out.println(currentUser);
		}
		
		return modelAndView;
	}
	
	@RequestMapping(value = "/delete_employee", method = RequestMethod.POST)
	public String deleteUser(@RequestParam("id") Long id) {
		repository.deleteById(id);
		
		return "redirect:/";
	}

}
