package com.jiangxue.waxberry.manager.user.entity;

import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "auth_users")
public class ManagerUser {
    @Id
    private String id;
    
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String loginname;

} 
