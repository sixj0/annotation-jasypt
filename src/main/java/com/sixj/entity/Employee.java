package com.sixj.entity;

import com.sixj.annotation.NeedEncrypt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author sixiaojie
 * @date 2020-08-04-15:25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    private String id;

    private String name;

    @NeedEncrypt(secretKey = "bd154!*74e-9fba0")
    private String password;

}
