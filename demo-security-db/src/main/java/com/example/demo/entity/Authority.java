package com.example.demo.entity;


import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Authority {
    /** ユーザー名 */
    @NotNull
    private String username;

    /** 権限 */
    @NotNull
    private String authority;
}
