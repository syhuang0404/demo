package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemoResponse {
    private String message;
    private List<Object> rows;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setRows(List<Object> rows) {
        this.rows = rows;
    }

    public List<Object> getRows() {
        return rows;
    }
}
