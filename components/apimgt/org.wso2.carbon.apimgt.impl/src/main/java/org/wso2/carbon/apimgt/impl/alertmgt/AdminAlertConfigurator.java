/*
 *  Copyright (c) 2019, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied. See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.wso2.carbon.apimgt.impl.alertmgt;

import org.wso2.carbon.apimgt.impl.dto.AlertTypeDTO;

import java.util.List;
import java.util.Map;
import java.util.Properties;

public class AdminAlertConfigurator implements AlertConfigurator {

    @Override
    public List<AlertTypeDTO> getSupportedAlertTypes() {
        return null;
    }

    @Override
    public List<AlertTypeDTO> getSubscribedAlerts(String userName) {
        return null;
    }

    @Override
    public void subscribe(String userName, List<String> emailsList, List<AlertTypeDTO> alertTypeDTOList) {

    }

    @Override
    public void unsubscribe(String userName) {

    }

    @Override
    public void addAlertConfiguration(String userName, String alertName, List<Properties> configProperties) {

    }

    @Override
    public List<Properties> getAlertConfiguration(String userName, String alertName) {
        return null;
    }

    @Override
    public void removeAlertConfiguration(String userName, String alertName,
            List<Properties> configProperties) {

    }
}
