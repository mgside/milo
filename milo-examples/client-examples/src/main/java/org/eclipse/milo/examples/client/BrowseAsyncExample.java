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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowseAsyncExample implements ClientExample {

    public static void main(String[] args) throws Exception {
        BrowseAsyncExample example = new BrowseAsyncExample();

        new ClientExampleRunner(example).run();
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
        // synchronous connect
        client.connect().get();

        // start browsing at root folder
        UaNode rootNode = client.getAddressSpace().getNode(Identifiers.RootFolder);
        
        Tree<UaNode> tree = new Tree<>(rootNode);
        List<NodeId> nodeIds = new ArrayList<NodeId>();
        /*
        NodeId nodeID = new NodeId(2,"0:Bucket Brigade.Gemicio");
        NodeId nodeID2 = new NodeId(2,"0:SessionDiagnostics/0:QueryNextCount");
        nodeIds.add(nodeID);
        nodeIds.add(nodeID2);
        client.readValues(0.0, TimestampsToReturn.Both, nodeIds);
        
        
        NodeId nodeID1 = new NodeId(2,"0:Random.Int1");
        client.readValue(
                0.0,
                TimestampsToReturn.Neither,
                nodeID
            );
        
        DataValue dataValue = client.readValue(
                0.0,
                TimestampsToReturn.Server,
                nodeID
            ).get();
               
        logger.info("dataValue  : " + dataValue.getValue().getValue());      
        
        DataValue dataValue1 = client.readValue(
                0.0,
                TimestampsToReturn.Neither,
                NodeId.parse("ns=2;s=HelloWorld/Dynamic/Int32")
            ).get();
        
        DataValue dataValue2 = client.readValue(
                0.0,
                TimestampsToReturn.Neither,
                nodeID2
            ).get();
        
        
        QualifiedName qfName = new QualifiedName(2, "Gemicio");
        
        ReadValueId rvID = new ReadValueId(nodeID2, null, null, qfName);
        
       */
        
        NodeId nodeID = new NodeId(2,"0:@ClientCount");
        
        
        DataValue dataValue1 = client.readValue(
                0.0,
                TimestampsToReturn.Neither,
                NodeId.parse("ns=2;s=0:Read Error.Int1")
            ).get();
        
        DataValue dataValue2 = client.readValue(
                0.0,
                TimestampsToReturn.Neither,
                nodeID
            ).get();
               
        DataValue dataValue3 = client.readValue(
                0.0,
                TimestampsToReturn.Neither,
                NodeId.parse("ns=2;s=0:Saw-toothed Waves.Int1")
            ).get();
        
        DataValue dataValue4 = client.readValue(
                0.0,
                TimestampsToReturn.Both,
                NodeId.parse("ns=2;s=0:Bucket Brigade.Gemicio.Gemici")
            ).get();
        
        /*logger.info("dataValue1 : " + dataValue1);
        logger.info("dataValue2 : " + dataValue2);
        logger.info("dataValue3 : " + dataValue3);*/
        logger.info("dataValue4 : " + dataValue4);
        
        long startTime = System.nanoTime();
        
        browseRecursive(client, tree).get();
        long endTime = System.nanoTime();

        traverse(tree, 0, (depth, n) -> logger.info(indent(depth) + n.getBrowseName().getName()));

        logger.info(
            "Browse took {}ms",
            TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS));

        future.complete(client);
    }

    

	private CompletableFuture<Void> browseRecursive(OpcUaClient client, Tree<UaNode> tree) {
        
    	return client.getAddressSpace().browseNodesAsync(tree.node).thenCompose(nodes -> {
            // Add each child node to the tree
            nodes.forEach(tree::addChild);

            // For each child node browse for its children
            Stream<CompletableFuture<Void>> futures =
                tree.children.stream().map(child -> browseRecursive(client, child));

            // Return a CompletableFuture that completes when the child browses complete
            return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
        });
    }

    private static String indent(int depth) {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            s.append("  ");
        }
        return s.toString();
    }

    private static <T> void traverse(Tree<T> tree, int depth, BiConsumer<Integer, T> consumer) {
        consumer.accept(depth, tree.node);

        tree.children.forEach(child -> traverse(child, depth + 1, consumer));
    }

    private static class Tree<T> {

        final List<Tree<T>> children = Lists.newCopyOnWriteArrayList();

        final T node;

        Tree(T node) {
            this.node = node;
        }

        void addChild(T child) {
            children.add(new Tree<>(child));
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                .add("node", node)
                .add("children", children)
                .toString();
        }

    }

}