package org.eclipse.milo.examples.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RetriveAndInsertValues {

	private final Logger logger = LoggerFactory.getLogger(RetriveAndInsertValues.class);

	public static void main(String[] args) throws Exception {

		BrowseExample example = new BrowseExample();

		new ClientExampleRunner(example).run();

	}

	public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {

		client.connect().get();

		browseNode("", client, Identifiers.RootFolder);

		future.complete(client);
	}

	private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {
		BrowseDescription browse = new BrowseDescription(browseRoot, BrowseDirection.Forward, Identifiers.References,
				true, uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
				uint(BrowseResultMask.All.getValue()));

		try {
			BrowseResult browseResult = client.browse(browse).get();

			List<ReferenceDescription> references = toList(browseResult.getReferences());

			for (ReferenceDescription rd : references) {

				if (rd.getBrowseName().getName().contains("Gemicio")
						&& rd.getBrowseName().getNamespaceIndex().toString().equals("2")) {

					logger.info("rd to string" + rd.toString());

					DataValue dataValue4 = client.readValue(0.0, TimestampsToReturn.Both,

							NodeId.parse("ns=" + rd.getBrowseName().getNamespaceIndex() + ";s="
									+ rd.getNodeId().getIdentifier()))
							.get();
					logger.info("dataValue4 : " + dataValue4);
				}
				rd.getNodeId().toNodeId(client.getNamespaceTable())
						.ifPresent(nodeId -> browseNode(indent + "  ", client, nodeId));

			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
		}
	}

}
