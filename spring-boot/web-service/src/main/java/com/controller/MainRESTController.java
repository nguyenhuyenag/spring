package com.controller;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.dao.EmployeeDAO;
import com.model.Employee;

@RestController

public class MainRESTController {

	@Autowired
	private EmployeeDAO employeeDAO;

	@RequestMapping("/")
	@ResponseBody
	public String welcome() {
		return "Welcome to RestTemplate Example.";
	}

	// URL:
	// http://localhost:8080/SomeContextPath/employees
	// http://localhost:8080/SomeContextPath/employees.xml
	// http://localhost:8080/SomeContextPath/employees.json
	@RequestMapping(value = "/employees", //
			method = RequestMethod.GET, //
			produces = { MediaType.APPLICATION_JSON, //
					MediaType.APPLICATION_XML })
	@ResponseBody
	public List<Employee> getEmployees() {
		List<Employee> list = employeeDAO.getAllEmployees();
		return list;
	}

	// URL:
	// http://localhost:8080/SomeContextPath/employee/{empNo}
	// http://localhost:8080/SomeContextPath/employee/{empNo}.xml
	// http://localhost:8080/SomeContextPath/employee/{empNo}.json
	@RequestMapping(value = "/employee/{empNo}", //
			method = RequestMethod.GET, //
			produces = { MediaType.APPLICATION_JSON, //
					MediaType.APPLICATION_XML })
	@ResponseBody
	public Employee getEmployee(@PathVariable("empNo") String empNo) {
		return employeeDAO.getEmployee(empNo);
	}

	// URL:
	// http://localhost:8080/SomeContextPath/employee
	// http://localhost:8080/SomeContextPath/employee.xml
	// http://localhost:8080/SomeContextPath/employee.json

	@RequestMapping(value = "/employee", //
			method = RequestMethod.POST, //
			produces = { MediaType.APPLICATION_JSON, //
					MediaType.APPLICATION_XML })
	@ResponseBody
	public Employee addEmployee(@RequestBody Employee emp) {

		System.out.println("(Service Side) Creating employee: " + emp.getEmpNo());

		return employeeDAO.addEmployee(emp);
	}

	// URL:
	// http://localhost:8080/SomeContextPath/employee
	// http://localhost:8080/SomeContextPath/employee.xml
	// http://localhost:8080/SomeContextPath/employee.json
	@RequestMapping(value = "/employee", //
			method = RequestMethod.PUT, //
			produces = { MediaType.APPLICATION_JSON, //
					MediaType.APPLICATION_XML })
	@ResponseBody
	public Employee updateEmployee(@RequestBody Employee emp) {

		System.out.println("(Service Side) Editing employee: " + emp.getEmpNo());

		return employeeDAO.updateEmployee(emp);
	}

	// URL:
	// http://localhost:8080/SomeContextPath/employee/{empNo}
	@RequestMapping(value = "/employee/{empNo}", //
            method = RequestMethod.DELETE, //
            produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    @ResponseBody
    public void deleteEmployee(@PathVariable("empNo") String empNo) {
 
        System.out.println("(Service Side) Deleting employee: " + empNo);
 
        employeeDAO.deleteEmployee(empNo);
    }
}
