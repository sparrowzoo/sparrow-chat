/*
Licensed to the Apache Software Foundation (ASF) under one or more
contributor license agreements.  See the NOTICE file distributed with
this work for additional information regarding copyright ownership.
The ASF licenses this file to You under the Apache License, Version 2.0
(the "License"); you may not use this file except in compliance with
the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.sparrow.chat.boot.controller;

import com.sparrow.datasource.DataSourceValidChecker;
import com.sparrow.messaging.domain.service.ClientConnection;
import com.sparrow.messaging.domain.service.ConnectionRegistry;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.enums.DeviceType;
import com.sparrow.spring.config.SparrowConfig;
import com.sparrow.spring.filter.monitor.Monitor;
import com.sparrow.spring.filter.monitor.MonitorResult;
import com.sparrow.support.checker.ConnectionValidCheckerAdapter;
import com.sparrow.utility.StringUtility;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class ContactHealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Monitor monitor;

    @Autowired
    private UserProfileAppService userProfileAppService;
    @Autowired
    private SparrowConfig config;

    @Autowired
    private ConnectionRegistry connectionRegistry;

    @GetMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        String im = this.config.getMvc().getRootPath() + "/im";
        log.info("redirect to im " + im);
        response.sendRedirect(im);
    }

    @GetMapping("ds")
    public String dataSourceCheck() {
        DataSourceValidChecker connectionValidChecker = new ConnectionValidCheckerAdapter();
        try {
            connectionValidChecker.isValid(dataSource);
            return "OK";
        } catch (Exception e) {
            return this.dataSource.toString();
        }
    }

    @GetMapping("env/{env}")
    public Boolean env(@PathVariable("env") String env, ServletRequest servletRequest) {
        return !StringUtility.isNullOrEmpty(System.getenv(env));
    }


    @GetMapping("monitor")
    public MonitorResult monitor() throws BusinessException {
        MonitorResult monitorResult = this.monitor.result();
        monitorResult.setOnlineUserMap(this.getOnlineUserChannel());
        return monitorResult;
    }

    private Map<String, Map<DeviceType, String>> getOnlineUserChannel() throws BusinessException {
        Map<String, Map<DeviceType, ClientConnection>> userChannel = this.connectionRegistry.getAll();
        Map<String, Map<DeviceType, String>> status = new HashMap<>();

        for (String userId : userChannel.keySet()) {
            Map<DeviceType, ClientConnection> clientConnectionMap = userChannel.get(userId);
            status.computeIfAbsent(userId, ignored -> new HashMap<>());
            for (DeviceType deviceType : clientConnectionMap.keySet()) {
                ClientConnection connection = userChannel.get(userId).get(deviceType);
                status.get(userId).put(deviceType, connection.loginUser().getHost());
            }
        }
        return status;
    }
}
