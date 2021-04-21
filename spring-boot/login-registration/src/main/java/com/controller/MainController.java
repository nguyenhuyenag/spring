package com.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.util.WebUtils;

@Controller
public class MainController {

	@GetMapping({ "/", "welcome" })
	public String welcomePage(Model model) {
		model.addAttribute("title", "Welcome");
		model.addAttribute("message", "This is welcome page!");
		return "welcomePage";
	}

	@GetMapping("login")
	public String loginPage() {
		return "loginPage";
	}

//	@RequestMapping(value = "/admin", method = RequestMethod.GET)
//	public String adminPage(Model model, Principal principal) {
//		User loginedUser = (User) ((Authentication) principal).getPrincipal();
//		String userInfo = WebUtils.toString(loginedUser);
//		model.addAttribute("userInfo", userInfo);
//		return "adminPage";
//	}
//
//
//	@RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
//	public String logoutSuccessfulPage(Model model) {
//		model.addAttribute("title", "Logout");
//		return "logoutSuccessfulPage";
//	}

	@GetMapping(value = "/userInfo")
	public String userInfo(Model model, Principal principal) {
		// Sau khi user login thanh cong se co principal
		String userName = principal.getName();
		System.out.println("User Name: " + userName);
		User loginedUser = (User) ((Authentication) principal).getPrincipal();
		String userInfo = WebUtils.toString(loginedUser);
		model.addAttribute("userInfo", userInfo);
		return "userInfoPage";
	}

//	@RequestMapping(value = "/403", method = RequestMethod.GET)
//	public String accessDenied(Model model, Principal principal) {
//		if (principal != null) {
//			User loginedUser = (User) ((Authentication) principal).getPrincipal();
//			String userInfo = WebUtils.toString(loginedUser);
//			model.addAttribute("userInfo", userInfo);
//			String message = "Hi " + principal.getName() //
//					+ "<br> You do not have permission to access this page!";
//			model.addAttribute("message", message);
//		}
//		return "403Page";
//	}

}
