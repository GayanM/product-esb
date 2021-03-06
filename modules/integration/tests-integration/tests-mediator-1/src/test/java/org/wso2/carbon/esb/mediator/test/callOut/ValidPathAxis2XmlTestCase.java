/*
*Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*WSO2 Inc. licenses this file to you under the Apache License,
*Version 2.0 (the "License"); you may not use this file except
*in compliance with the License.
*You may obtain a copy of the License at
*
*http://www.apache.org/licenses/LICENSE-2.0
*
*Unless required by applicable law or agreed to in writing,
*software distributed under the License is distributed on an
*"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*KIND, either express or implied.  See the License for the
*specific language governing permissions and limitations
*under the License.
*/
package org.wso2.carbon.esb.mediator.test.callOut;


import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.AXIOMUtil;
import org.apache.axis2.AxisFault;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.wso2.carbon.automation.engine.frameworkutils.FrameworkPathUtil;
import org.wso2.carbon.automation.engine.annotations.ExecutionEnvironment;
import org.wso2.carbon.automation.engine.annotations.SetEnvironment;
import org.wso2.esb.integration.common.utils.ESBIntegrationTest;
import org.wso2.esb.integration.common.utils.common.FileManager;

import javax.xml.namespace.QName;
import java.util.Iterator;

import static org.testng.Assert.assertTrue;

public class ValidPathAxis2XmlTestCase extends ESBIntegrationTest {


    @BeforeClass(alwaysRun = true)
    public void setEnvironment() throws Exception {

        super.init();
        String axis2XmlPath = FrameworkPathUtil.getSystemResourceLocation() + "/artifacts/ESB/mediatorconfig/callout/client_repo/conf/axis2.xml";
        String uriSynapse = FrameworkPathUtil.getSystemResourceLocation() + "/artifacts/ESB/mediatorconfig/callout/ValidPath_Axis2Xml.xml";

        OMElement lineItem = AXIOMUtil.stringToOM(FileManager.readFile(uriSynapse));

        Iterator sequenceElements = lineItem.getChildElements();      // get all sequence elements

        while (sequenceElements.hasNext()) {

            OMElement sequenceElement = (OMElement) sequenceElements.next();

            Iterator callOutElements = sequenceElement.getChildrenWithLocalName("callout");        // get all callout elements


            while (callOutElements.hasNext()) {

                OMElement callOutElement = (OMElement) callOutElements.next();

                Iterator configElments = callOutElement.getChildrenWithLocalName("configuration");        // get configuration elements


                while (configElments.hasNext()) {

                    OMElement configElment = (OMElement) configElments.next();           //this is the configuration element

                    configElment.getAttribute(new QName("axis2xml")).setAttributeValue(axis2XmlPath);    // gets the attribute of axis2xml and changes it to a different path

                }
            }
        }

        updateESBConfiguration(lineItem);

    }

    @SetEnvironment(executionEnvironments = {ExecutionEnvironment.STANDALONE})
    @Test(groups = "wso2.esb")
    public void TestPath() throws AxisFault {

        OMElement response = axis2Client.sendSimpleStockQuoteRequest(getMainSequenceURL(), "", "IBM");    // send the simplestockquote request. service url is set at the synapse

        boolean ResponseContainsIBM = response.getFirstElement().toString().contains("IBM");      //checks whether the  response contains IBM

        assertTrue(ResponseContainsIBM, "Symbol Mismatched");

    }


    @AfterClass(alwaysRun = true)
    public void cleanup() throws Exception {

        super.cleanup();

    }


}
