package org.eclipse.milo.examples.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.toList;
import static com.google.common.collect.Lists.newArrayList;

import java.awt.event.ItemEvent;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.api.subscriptions.UaSubscription;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedDataItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.ManagedSubscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryData;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadResult;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadRawModifiedDetails;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






public class RetrieveAndInsertValues implements ClientExample {

	private final Logger logger = LoggerFactory.getLogger(RetrieveAndInsertValues.class);
	HashMap<String, OpcDataNode> insertList = new HashMap<String, OpcDataNode>();

	public static void main(String[] args) throws Exception {

		RetrieveAndInsertValues example = new RetrieveAndInsertValues();

		new ClientExampleRunner(example).run();

	}

	public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {

		//client.connect().get();
		// opc.tcp://:21381/MatrikonOpcUaWrapper
		// opc.tcp://:21381/MatrikonOpcUaWrapper
		/*
		 * OpcUaClient abc =
		 * OpcUaClient.create("opc.tcp://WIN-I0R1BA5ALO3:21381/MatrikonOpcUaWrapper",
		 * endpoints -> endpoints.stream().filter(e -> true).findFirst(), configBuilder
		 * -> configBuilder.setApplicationName(LocalizedText.
		 * english("eclipse milo opc-ua client"))
		 * .setApplicationUri("urn:eclipse:milo:examples:client")
		 * 
		 * .setRequestTimeout(UInteger.valueOf(10000)).build());
		 * 
		 * abc.connect().get();
		 */

	
		
		 //readValue();
		//subscribe();
		// historical();
		//managedSubscription();
		readValues();
		future.complete(client);
	}

	

	private void readValues() throws Exception {


		OpcUaClient abc = OpcUaClient.create("opc.tcp://:21381/MatrikonOpcUaWrapper",
				endpoints -> endpoints.stream().filter(e -> true).findFirst(),
				configBuilder -> configBuilder.setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
						.setApplicationUri("urn:eclipse:milo:examples:client")

						.setRequestTimeout(UInteger.valueOf(10000)).build());

		abc.connect().get();
		List<NodeId> nodeIds = new ArrayList<NodeId>();
		
		
		
		
		//Variant value = new Variant(valueIfAbsent);
		
		
		NodeId nodeId = new NodeId(2, "0:Square Waves.Gemicio");
		
		
	
		
		
		NodeId node = new NodeId(2, "0:Bucket Brigade.Gemicio.Gemici.NASILYA");
		nodeIds.add(new NodeId(2, "0:Random.String"));
        nodeIds.add(new NodeId(2, "0:Bucket Brigade.Gemicio.Gemici.NASILYA"));
        nodeIds.add(new NodeId(2, "0:Bucket Brigade.Gemicio.Gemici"));
        nodeIds.add(new NodeId(2, "0:Square Waves.Gemicio"));
        nodeIds.add(node);
		
		List<DataValue> dataValues = abc.readValues(0.0, TimestampsToReturn.Both, nodeIds).get();
		for (DataValue dataValue : dataValues) {
			System.out.println(dataValue.toString());
		}
		
		
		
		
		System.out.println(dataValues.get(0).toString());
		abc.disconnect();
	}

	private void managedSubscription() throws Exception {
		
		HashMap<String, String> dataTable = new HashMap<String, String>();
		
		OpcUaClient abc = OpcUaClient.create("opc.tcp://:21381/MatrikonOpcUaWrapper",
				endpoints -> endpoints.stream().filter(e -> true).findFirst(),
				configBuilder -> configBuilder.setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
						.setApplicationUri("urn:eclipse:milo:examples:client")

						.setRequestTimeout(UInteger.valueOf(10000)).build());

		abc.connect().get();
		
		ManagedSubscription subscription = ManagedSubscription.create(abc);

        subscription.addDataChangeListener((items, values) -> {
            for (int i = 0; i < items.size(); i++) {
                
            //	logger.info( "subscription value received: item={}, value={}",  items.get(i).getNodeId(), values.get(i).getValue());
                dataTable.put(items.get(i).getNodeId().toString(), values.get(i).getValue().toString());
                
            }
        });
        List<NodeId> nodeIds = new ArrayList<NodeId>();
       
        nodeIds.add(new NodeId(2, "0:Random.String"));
        nodeIds.add(new NodeId(2, "0:Bucket Brigade.Gemicio.Gemici.NASILYA"));
        nodeIds.add(new NodeId(2, "0:Bucket Brigade.Gemicio.Gemici"));
        nodeIds.add(new NodeId(2, "0:Square Waves.Gemicio"));
        
        nodeIds.add(new NodeId(2, "0:WHS_DK094PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK100PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK129PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK242PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK263PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK266PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK292PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK326PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK404PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK430PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK517PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK526PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK555PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK580PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK720PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK728PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK729PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK731PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK733PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK734PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK743PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK747PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK750PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK751PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK753PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK754PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK756PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK758PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK761PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK766PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK770PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK771PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK775PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK777PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK781PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK787PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK788PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK795PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK797PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK799PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK805PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK817PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK818PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK819PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK822PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK830PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK835PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK836PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK847PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK849PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK853PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK855PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK867PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK883PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK884PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK887PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK895PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK917PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK920PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK922PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK928PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK931PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK935PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK936PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK939PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK958PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK978PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK979PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK983PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK094TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK100TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK129TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK242TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK263TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK266TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK292TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK326TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK404TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK430TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK517TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK526TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK555TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK580TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK720TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK728TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK729TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK731TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK733TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK734TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK743TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK747TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK750TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK751TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK753TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK754TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK756TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK758TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK761TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK766TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK770TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK771TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK775TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK777TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK781TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK787TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK788TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK795TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK797TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK799TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK805TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK817TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK818TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK819TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK822TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK830TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK835TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK836TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK847TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK849TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK853TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK855TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK867TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK883TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK884TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK887TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK895TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK917TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK920TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK922TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK928TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK931TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK935TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK936TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK939TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK958TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK978TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK979TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK983TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK725PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK735PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK738PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK740PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK741PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK765PI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK768PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK725TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK735PI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK738TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK740TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK741TI001"));
        nodeIds.add(new NodeId(2, "0:WHS_DK765TI102"));
        nodeIds.add(new NodeId(2, "0:WHS_DK768TI001"));
        
        
        List<ManagedDataItem> dataItems = subscription.createDataItems(nodeIds);
        dataTable.toString();
        
        logger.info("Ended" + dataTable.toString());
        
        
        Thread.sleep(10000L);
        insertDatasToDB(dataTable);
	
	}

	private void insertDatasToDB(HashMap<String, String> dataTable) {
		
		for ( HashMap.Entry<String, String> entry : dataTable.entrySet()) {
		    System.out.println(entry.getKey() + " value is " + entry.getValue());
		}
		
	}

	private void readValue() throws Exception {

		OpcUaClient abc = OpcUaClient.create("opc.tcp://:21381/MatrikonOpcUaWrapper",
				endpoints -> endpoints.stream().filter(e -> true).findFirst(),
				configBuilder -> configBuilder.setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
						.setApplicationUri("urn:eclipse:milo:examples:client")

						.setRequestTimeout(UInteger.valueOf(10000)).build());

		abc.connect().get();

		List<NodeId> filterList = new ArrayList<>();

		NodeId node = new NodeId(2, "0:Bucket Brigade.Gemicio.Gemici.NASILYA");

		filterList.add(node);
		abc.registerNodes(filterList);

		DataValue dataValue55 = abc.readValue(0.0, TimestampsToReturn.Both, node).get();

		browseNode("", abc, Identifiers.RootFolder);
		logger.info("XXXXXXXXXXXXXXXX data val" + dataValue55.toString());
		

	}

	private void historical() throws Exception {

		System.out.println("history started");
		OpcUaClient abc = OpcUaClient.create("opc.tcp://:21381/MatrikonOpcUaWrapper",
				endpoints -> endpoints.stream().filter(e -> true).findFirst(),
				configBuilder -> configBuilder.setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
						.setApplicationUri("urn:eclipse:milo:examples:client")

						.setRequestTimeout(UInteger.valueOf(10000)).build());

		abc.connect().get();

		DateTime beforeTime = new DateTime(132633021736490000L);

		logger.info("beforeTime :" + beforeTime);

		HistoryReadDetails historyReadDetails = new ReadRawModifiedDetails(false, beforeTime, DateTime.now(), uint(0),
				true);

		HistoryReadValueId historyReadValueId = new HistoryReadValueId(
				new NodeId(2, "0:Bucket Brigade.Gemicio.Gemici.NASILYA"), null, QualifiedName.NULL_VALUE,
				ByteString.NULL_VALUE);
		DataValue dataValue55 = abc
				.readValue(0.0, TimestampsToReturn.Both, new NodeId(2, "0:Bucket Brigade.Gemicio.Gemici.NASILYA"))
				.get();
		List<HistoryReadValueId> nodesToRead = new ArrayList<>();
		nodesToRead.add(historyReadValueId);

		HistoryReadResponse historyReadResponse = abc
				.historyRead(historyReadDetails, TimestampsToReturn.Both, false, nodesToRead).get();

		HistoryReadResult[] historyReadResults = historyReadResponse.getResults();
		if (historyReadResults != null) {
			HistoryReadResult historyReadResult = historyReadResults[0];
			StatusCode statusCode = historyReadResult.getStatusCode();

			if (statusCode.isGood()) {
				HistoryData historyData = (HistoryData) historyReadResult.getHistoryData()
						.decode(abc.getStaticSerializationContext());

				List<DataValue> dataValues = l(historyData.getDataValues());

				dataValues.forEach(v -> System.out.println("value=" + v));
			} else {
				System.out.println("History read failed: " + statusCode);
			}
		}
		System.out.println("history ended");
	}

	private void subscribe() throws Exception {

		OpcUaClient abc = OpcUaClient.create("opc.tcp://:21381/MatrikonOpcUaWrapper",
				endpoints -> endpoints.stream().filter(e -> true).findFirst(),
				configBuilder -> configBuilder.setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
						.setApplicationUri("urn:eclipse:milo:examples:client")

						.setRequestTimeout(UInteger.valueOf(10000)).build());

		abc.connect().get();

		UaSubscription subscription = abc.getSubscriptionManager().createSubscription(1000.0).get();

		ArrayList<String> itemNames = new ArrayList<String>();
		ArrayList<ReadValueId> readValueIds = new ArrayList<ReadValueId>();
		ArrayList<MonitoredItemCreateRequest> monitoredItemCreateRequests = new ArrayList<MonitoredItemCreateRequest>();

	
		itemNames.add("Bucket Brigade.Gemicio.Gemici.NASILYA");
		itemNames.add("Bucket Brigade.Gemicio.Gemici");
		itemNames.add("Random.String");
		itemNames.add("Square Waves.Gemicio");
		for (int i = 0; i < itemNames.size(); i++) {
			NodeId node = new NodeId(2, "0:" + itemNames.get(i));
			ReadValueId readValueId = new ReadValueId(node, AttributeId.Value.uid(), null, QualifiedName.NULL_VALUE);
			readValueIds.add(readValueId);

		}
		UInteger clientHandle = subscription.nextClientHandle();
		MonitoringParameters parameters = new MonitoringParameters(clientHandle, 1000.0, // sampling interval
				null, // filter, null means use default
				uint(10), // queue size
				true // discard oldest //true
		);

		for (int i = 0; i < readValueIds.size(); i++) {

			MonitoredItemCreateRequest request = new MonitoredItemCreateRequest(readValueIds.get(i),
					MonitoringMode.Reporting, parameters);
			
			monitoredItemCreateRequests.add(request);
		}
		
		UaSubscription.ItemCreationCallback onItemCreated = (item, id) -> item
				.setValueConsumer(this::onSubscriptionValue);

		List<UaMonitoredItem> items = subscription
				.createMonitoredItems(TimestampsToReturn.Both, monitoredItemCreateRequests, onItemCreated).get();
		
		for (UaMonitoredItem item : items) {
			if (item.getStatusCode().isGood()) {
				logger.info("item created for nodeId={}", item.getReadValueId().getNodeId());
			} else {
				logger.warn("failed to create item for nodeId={} (status={})", item.getReadValueId().getNodeId(),
						item.getStatusCode());
			}
		}
		
		
		
		Thread.sleep(50000);
	}

	private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {

		BrowseDescription browse = new BrowseDescription(browseRoot, BrowseDirection.Forward, Identifiers.References,
				true, uint(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
				uint(BrowseResultMask.All.getValue()));

		try {
			BrowseResult browseResult = client.browse(browse).get();

			List<ReferenceDescription> references = toList(browseResult.getReferences());

			for (ReferenceDescription rd : references) {

				if (rd.getBrowseName().getNamespaceIndex().toString().equals("2")
						&& rd.getNodeClass().equals(NodeClass.Variable)) {

					DataValue dataValue = client.readValue(0.0, TimestampsToReturn.Both,

							NodeId.parse("ns=" + rd.getBrowseName().getNamespaceIndex() + ";s="
									+ rd.getNodeId().getIdentifier()))
							.get();
					logger.info(rd.getNodeId().getIdentifier() + " : " + dataValue);

					OpcDataNode datas = new OpcDataNode();

					datas.setPath(rd.getNodeId().getIdentifier().toString());
					datas.setNamespace(rd.getBrowseName().getNamespaceIndex().toString());
					if (dataValue.getValue().isNotNull()) {
						datas.setValue(dataValue.getValue().getValue().toString());
					}
					datas.setType(rd.getTypeId().getType().toString());

					datas.setDate(dataValue.getServerTime().getJavaDate().toString());

					insertList.put(rd.getNodeId().getIdentifier().toString(), datas);
				}
				rd.getNodeId().toNodeId(client.getNamespaceTable())
						.ifPresent(nodeId -> browseNode(indent + "  ", client, nodeId));
			}
		} catch (InterruptedException | ExecutionException e) {
			logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
		}
	}

	public final class OpcDataNode {

		private String path = "";
		private String namespace = "";
		private String value = "";
		private String type = "";
		private String unit = "";
		private String date = "";

		public void setPath(String path) {
			this.path = path;
		}

		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public void setType(String type) {
			this.type = type;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public String getPath() {
			return path;
		}

		public String getNamespace() {
			return namespace;
		}

		public String getValue() {
			return value;
		}

		public String getType() {
			return type;
		}

		public String getUnit() {
			return unit;
		}

		public String getDate() {
			return date;
		}

	}

	private void onSubscriptionValue(UaMonitoredItem item, DataValue value) {
		logger.info("subscription value received: item={}, value={}", item.getReadValueId().getNodeId(),
				value.getValue());
		item.getReadValueId();
	}

}
