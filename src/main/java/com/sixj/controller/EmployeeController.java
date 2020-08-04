package com.sixj.controller;

import com.sixj.entity.Employee;
import com.sixj.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author sixiaojie
 * @date 2020-08-04-15:28
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public Object getEmpList(){
        List<Employee> list = employeeService.list();
        return list;
    }
}
