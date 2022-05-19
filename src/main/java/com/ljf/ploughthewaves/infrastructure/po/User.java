package com.ljf.ploughthewaves.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public Integer id;
    public String openId;
    public String name;
    public String school;
    public Boolean isPublic;
    public Boolean isAdmin;
    public String role;
    public String password;
}
