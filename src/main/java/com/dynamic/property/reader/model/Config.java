package com.dynamic.property.reader.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "config")
public class Config {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String key;
    private String value;
    private String component;

   
}
