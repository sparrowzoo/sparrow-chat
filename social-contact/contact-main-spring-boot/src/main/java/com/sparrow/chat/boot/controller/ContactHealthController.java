package com.sparrow.chat.boot.controller;

import com.sparrow.datasource.DataSourceValidChecker;
import com.sparrow.datasource.checker.ConnectionValidCheckerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class ContactHealthController {

    @Autowired
    private DataSource dataSource;

    private Logger logger = LoggerFactory.getLogger(ContactHealthController.class);

    @RequestMapping("ds")
    public String dataSourceCheck() {
        DataSourceValidChecker connectionValidChecker = new ConnectionValidCheckerAdapter();
        try {
            connectionValidChecker.isValid(dataSource);
            return "OK";
        } catch (Exception e) {
            return this.dataSource.toString();
        }
    }
}
