package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.model.Product;
import com.service.ProductService;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService service;
	
	@RequestMapping({"/", "product"})
	public String viewHomePage(Model model) {
		return viewPage(model, 1, "name", "asc");
	}
	
	@RequestMapping("/page/{pageNum}")
	public String viewPage(Model model, //
			@PathVariable(name = "pageNum") int pageNum, // 
			@Param("sortField") String sortField, //
			@Param("sortDir") String sortDir) {
		
		Page<Product> page = service.listAll(pageNum, sortField, sortDir);
		
		List<Product> listProducts = page.getContent();
		
		model.addAttribute("currentPage", pageNum);		
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", "asc".equals(sortDir) ? "desc" : "asc");
		
		model.addAttribute("listProducts", listProducts);
		
		return "index";
	}	
	
	@RequestMapping("/new")
	public String showNewProductForm(Model model) {
		Product tProduct = new Product();
		model.addAttribute("product", tProduct);
		return "new_product";
	}
	
//	@RequestMapping(value = "/save", method = RequestMethod.POST)
//	public String saveProduct(@ModelAttribute("product") Product tProduct) {
//		service.save(tProduct);
//		return "redirect:/";
//	}
//	
//	@RequestMapping("/edit/{id}")
//	public ModelAndView showEditProductForm(@PathVariable(name = "id") Long id) {
//		ModelAndView mav = new ModelAndView("edit_product");
//		Product tProduct = service.get(id);
//		mav.addObject("product", tProduct);
//		return mav;
//	}	
//	
//	@RequestMapping("/delete/{id}")
//	public String deleteProduct(@PathVariable(name = "id") Long id) {
//		service.delete(id);
//		return "redirect:/";
//	}
}
