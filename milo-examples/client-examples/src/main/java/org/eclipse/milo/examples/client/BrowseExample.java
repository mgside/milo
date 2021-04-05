/*
 * Copyright (c) 2019 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;

public class BrowseExample implements ClientExample {

    public static void main(String[] args) throws Exception {
        BrowseExample example = new BrowseExample();

        new ClientExampleRunner(example).run();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
        // synchronous connect
        client.connect().get();
      
        browseNode("", client, Identifiers.RootFolder);
        
        
        //UaVariableNode node1 = client.getAddressSpace().getVariableNode(new NodeId(2, "HelloWorld/CustomStructTypeVariable"));
        
        
        
        
        future.complete(client);
    }

    private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {
        BrowseDescription browse = new BrowseDescription(
            browseRoot,
            BrowseDirection.Forward,
            Identifiers.References,
            true,
            uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
            uint(BrowseResultMask.All.getValue())
        );

        try {
            BrowseResult browseResult = client.browse(browse).get();
            
            List<ReferenceDescription> references = toList(browseResult.getReferences());

            for (ReferenceDescription rd : references) {
            	//logger.info("{} Node={}", indent, rd.getBrowseName().getName());            	
            	//logger.info("identifier : " + rd.getNodeId().getIdentifier());
            	//logger.info("ns : " + rd.getBrowseName().getNamespaceIndex());
               if(rd.getBrowseName().getName().contains("Gemicio") && rd.getBrowseName().getNamespaceIndex().toString().equals("2") ) {
                /*	logger.info("{} TEST - rd.getBrowseName().getNamespaceIndex() ={}", indent, rd.getBrowseName().getNamespaceIndex());
                	logger.info("{} TEST - rd.getNodeId().getIdentifier() ={}", indent, rd.getNodeId().getIdentifier());               	
                	logger.info("{} TEST - rd.getNodeId().getType() ={}", indent, rd.getNodeId().getType()); */
                	logger.info("rd to string" + rd.toString());
                	
                	
                	DataValue dataValue4 = client.readValue(
                            0.0,
                            TimestampsToReturn.Both,
                            
                            NodeId.parse("ns="+rd.getBrowseName().getNamespaceIndex()+";s="+rd.getNodeId().getIdentifier())
                        ).get();
                    logger.info("dataValue4 : " + dataValue4);
                }
                // recursively browse to children
                rd.getNodeId().toNodeId(client.getNamespaceTable())
                    .ifPresent(nodeId -> browseNode(indent + "  ", client, nodeId));
                
                
                
                
                
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
        }
    }

}

