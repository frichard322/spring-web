package edu.bbte.idde.frim1910.backend.config;

import lombok.Data;

import java.io.Serializable;

@Data
public class ConfigBean implements Serializable {
    private String daoFactoryImplementation;
    private String jdbcClassName;
    private String jdbcURL;
    private String username;
    private String password;
    private Integer poolSize;
}
