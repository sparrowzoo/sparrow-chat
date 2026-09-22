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

import com.sparrow.chat.domain.netty.UserContainer;
import com.sparrow.datasource.DataSourceValidChecker;
import com.sparrow.passport.api.UserProfileAppService;
import com.sparrow.passport.protocol.dto.UserProfileDTO;
import com.sparrow.protocol.BusinessException;
import com.sparrow.spring.filter.monitor.Monitor;
import com.sparrow.spring.filter.monitor.MonitorResult;
import com.sparrow.support.checker.ConnectionValidCheckerAdapter;
import com.sparrow.utility.StringUtility;
import io.netty.channel.Channel;
import jakarta.servlet.ServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ContactHealthController {

//    @Autowired
    private DataSource dataSource;

    @Autowired
    private Monitor monitor;

    @Autowired
    private UserProfileAppService userProfileAppService;

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

    private Map<String, String> getOnlineUserChannel() throws BusinessException {
        UserContainer userContainer = UserContainer.getContainer();
        Map<String, Channel> userChannel = userContainer.getChannelMap();
        Set<Long> userIds = userChannel.keySet().stream().map(Long::parseLong).collect(Collectors.toSet());

        Map<String, String> onlineUserChannel = new HashMap<>();
        Map<Long, UserProfileDTO> userProfileDTOMap = this.userProfileAppService.getUserMap(userIds);

        for (String userId : userContainer.getChannelMap().keySet()) {
            UserProfileDTO userProfileDTO = userProfileDTOMap.get(Long.parseLong(userId));
            onlineUserChannel.put(userProfileDTO.getUserName(), userChannel.get(userId).toString());
        }
        return onlineUserChannel;
    }
}
