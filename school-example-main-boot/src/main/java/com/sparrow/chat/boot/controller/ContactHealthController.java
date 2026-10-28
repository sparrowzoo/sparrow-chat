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
import com.sparrow.spring.filter.monitor.Monitor;
import com.sparrow.spring.filter.monitor.MonitorResult;
import com.sparrow.support.checker.ConnectionValidCheckerAdapter;
import com.sparrow.utility.StringUtility;
import jakarta.inject.Inject;
import jakarta.servlet.ServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ContactHealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private Monitor monitor;

    @Autowired
    private UserProfileAppService userProfileAppService;

    @Inject
    private ConnectionRegistry connectionRegistry;

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

    @RequestMapping("env/{env}")
    public Boolean env(@PathVariable("env") String env, ServletRequest servletRequest) {
        return !StringUtility.isNullOrEmpty(System.getenv(env));
    }


    @RequestMapping("monitor")
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
            status.computeIfAbsent(userId, _ -> new HashMap<>());
            for (DeviceType deviceType : clientConnectionMap.keySet()) {
                ClientConnection connection = userChannel.get(userId).get(deviceType);
                status.get(userId).put(deviceType, connection.loginUser().getHost());
            }
        }
        return status;
    }

}
