package com.sixj.service.impl;

import com.sixj.annotation.EnableEncrypt;
import com.sixj.entity.Employee;
import com.sixj.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sixiaojie
 * @date 2020-08-04-15:39
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Override
    @EnableEncrypt
    public List<Employee> list(){
        List<Employee> employees = initEmpList();
        return employees;
    }


    private List<Employee> initEmpList(){
        ArrayList<Employee> empList = new ArrayList<>();
        empList.add(new Employee("1","曹操","111111"));
        empList.add(new Employee("2","孙权","222222"));
        empList.add(new Employee("3","刘备","333333"));
        empList.add(new Employee("4","关羽","444444"));
        empList.add(new Employee("5","张飞","555555"));
        return empList;
    }

}
