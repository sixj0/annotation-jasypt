package com.sixj.service;

import com.sixj.entity.Employee;

import java.util.List;

/**
 * @author sixiaojie
 * @date 2020-08-04-15:27
 */
public interface EmployeeService {

    /**
     * 查询员工列表
     * @return
     */
    List<Employee> list();
}
