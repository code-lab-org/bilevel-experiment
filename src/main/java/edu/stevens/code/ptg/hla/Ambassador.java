package edu.stevens.code.ptg.hla;

import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.ptg.App;
import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;
import edu.stevens.code.ptg.ManagerApp;
import edu.stevens.code.ptg.Task;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.NullFederateAmbassador;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.encoding.DataElementFactory;
import hla.rti1516e.encoding.DecoderException;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAboolean;
import hla.rti1516e.encoding.HLAfixedArray;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.encoding.HLAunicodeString;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIexception;

public class Ambassador extends NullFederateAmbassador {
	private static Logger logger = LogManager.getLogger(Ambassador.class);
	
	private static final String FEDERATE_TYPE_DESIGNER = "designer";
	private static final String FEDERATE_TYPE_MANAGER = "manager";
	private static final String FOM_PATH = "code.xml";
	private static final String CLASS_NAME_DESIGNER = "Designer";
	private static final String ATTRIBUTE_NAME_DESIGNER_ID = "id";
	private static final String ATTRIBUTE_NAME_DESIGNER_DESIGN = "design";
	private static final String ATTRIBUTE_NAME_DESIGNER_STRATEGY = "strategy";
	private static final String ATTRIBUTE_NAME_DESIGNER_SHARE = "share";
	private static final String[] ATTRIBUTE_NAMES_DESIGNER = new String[]{
			ATTRIBUTE_NAME_DESIGNER_ID, 
			ATTRIBUTE_NAME_DESIGNER_DESIGN,
			ATTRIBUTE_NAME_DESIGNER_STRATEGY,
			ATTRIBUTE_NAME_DESIGNER_SHARE
		};
	private static final String CLASS_NAME_MANAGER = "Manager";
	private static final String ATTRIBUTE_NAME_MANAGER_ROUND = "round";
	private static final String ATTRIBUTE_NAME_MANAGER_TIME = "time";
	private static final String ATTRIBUTE_NAME_MANAGER_TASKS = "tasks";
	private static final String[] ATTRIBUTE_NAMES_MANAGER = new String[]{
			ATTRIBUTE_NAME_MANAGER_ROUND, 
			ATTRIBUTE_NAME_MANAGER_TIME,
			ATTRIBUTE_NAME_MANAGER_TASKS
		};
	
	protected final RTIambassador rtiAmbassador;
	private final EncoderFactory encoderFactory;
	private String federationName = null;
	private App app = null;
	private final Map<Object, ObjectInstanceHandle> registeredInstances = 
			Collections.synchronizedMap(new HashMap<Object, ObjectInstanceHandle>());
	private final Map<ObjectInstanceHandle, Object> discoveredInstances = 
			Collections.synchronizedMap(new HashMap<ObjectInstanceHandle, Object>());
	
	public Ambassador() throws RTIexception {
		RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
		rtiAmbassador = rtiFactory.getRtiAmbassador();
		encoderFactory = rtiFactory.getEncoderFactory();
	}
	
	private void connect(String federationName, String federateName, String federateType) throws RTIexception {
		logger.debug("Connecting to the RTI.");
		try {
			rtiAmbassador.connect(this, CallbackModel.HLA_IMMEDIATE);
			logger.info("Connected to the RTI.");
		} catch(AlreadyConnected ignored) {}
		
		logger.debug("Creating the federation execution.");
		try {
			rtiAmbassador.createFederationExecution(federationName, 
					new URL[]{getClass().getClassLoader().getResource(FOM_PATH)});
			logger.info("Created the federation execution.");
		} catch(FederationExecutionAlreadyExists ignored) {
			logger.trace("Federation execution already exists.");
		}

		logger.debug("Joining the federation execution.");
		try {
			rtiAmbassador.joinFederationExecution(federateName, 
					federateType, federationName);
			this.federationName = federationName;
			logger.info("Joined the federation execution.");
		} catch(FederateAlreadyExecutionMember ignored) { 
			logger.trace("Already joined to the federation execution.");
		}
	}
	
	private void publishDesignerAttributes() throws RTIexception {
		logger.debug("Publishing designer class attributes.");
		AttributeHandleSet designerHandles = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attribute : ATTRIBUTE_NAMES_DESIGNER) {
			designerHandles.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
					attribute));
		}
		rtiAmbassador.publishObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), designerHandles);
		logger.info("Published designer class attributes.");
	}
	
	private void subscribeDesignerAttributes() throws RTIexception {
		logger.debug("Subscribing designer class attributes.");
		AttributeHandleSet designerHandles = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attribute : ATTRIBUTE_NAMES_DESIGNER) {
			designerHandles.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
					attribute));
		}
		rtiAmbassador.subscribeObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), designerHandles);
		logger.info("Subscribed designer class attributes.");
	}
	
	private void publishManagerAttributes() throws RTIexception {
		logger.debug("Publishing manager class attributes.");
		AttributeHandleSet managerHandles = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attribute : ATTRIBUTE_NAMES_MANAGER) {
			managerHandles.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
					attribute));
		}
		rtiAmbassador.publishObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), managerHandles);
		logger.info("Published manager class attributes.");
	}
	
	private void subscribeManagerAttributes() throws RTIexception {
		logger.debug("Subscribing manager class attributes.");
		AttributeHandleSet managerHandles = 
				rtiAmbassador.getAttributeHandleSetFactory().create();
		for(String attribute : ATTRIBUTE_NAMES_MANAGER) {
			managerHandles.add(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
					attribute));
		}
		rtiAmbassador.subscribeObjectClassAttributes(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), managerHandles);
		logger.info("Subscribed manager class attributes.");
	}
	
	public void connectManager(ManagerApp managerApp, String federationName) throws RTIexception {
		this.app = managerApp;
		
		this.connect(federationName, managerApp.getSelf().toString(), FEDERATE_TYPE_MANAGER);
		publishManagerAttributes();
		subscribeDesignerAttributes();

		logger.debug("Registering this manager object.");
		ObjectInstanceHandle instance = rtiAmbassador.registerObjectInstance(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER));
		registeredInstances.put(managerApp.getSelf(), instance);
		logger.debug("Registered this manager object.");
		
		updateManager(managerApp.getSelf());
	}
	
	public void connectDesigner(DesignerApp designerApp, String federationName) throws RTIexception {
		this.app = designerApp;
		
		this.connect(federationName, designerApp.getSelf().toString(), FEDERATE_TYPE_DESIGNER);
		publishDesignerAttributes();
		subscribeDesignerAttributes();
		subscribeManagerAttributes();

		logger.debug("Registering this designer object.");
		ObjectInstanceHandle instance = rtiAmbassador.registerObjectInstance(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER));
		registeredInstances.put(designerApp.getSelf(),  instance);
		logger.debug("Registered this designer object.");

		updateDesigner(designerApp.getSelf());
	}
	
	public void updateManager(Manager manager) throws RTIexception {
		if(registeredInstances.containsKey(manager)) {
			logger.info("Updating manager attribute values.");
			AttributeHandleValueMap attributes = 
					rtiAmbassador.getAttributeHandleValueMapFactory().create(0);
			HLAunicodeString round = encoderFactory.createHLAunicodeString(manager.getRoundName());
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
					ATTRIBUTE_NAME_MANAGER_ROUND), round.toByteArray());
			HLAinteger32BE time = encoderFactory.createHLAinteger32BE(manager.getTimeRemaining());
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
					ATTRIBUTE_NAME_MANAGER_TIME), time.toByteArray());
			HLAfixedArray<HLAfixedRecord> tasks = encoderFactory.createHLAfixedArray(new DataElementFactory<HLAfixedRecord>(){
				@Override
				public HLAfixedRecord createElement(int taskId) {
					HLAfixedRecord task = encoderFactory.createHLAfixedRecord();
					task.add(encoderFactory.createHLAunicodeString(manager.getTask(taskId).getName()));
					task.add(encoderFactory.createHLAfixedArray(new DataElementFactory<HLAinteger32BE>(){
						@Override
						public HLAinteger32BE createElement(int designerId) {
							return encoderFactory.createHLAinteger32BE(manager.getTask(taskId).getDesignerId(designerId));
						}
					}, Task.NUM_DESIGNERS));
					return task;
				}
			}, Task.NUM_DESIGNERS);
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
					ATTRIBUTE_NAME_MANAGER_TASKS), tasks.toByteArray());
			rtiAmbassador.updateAttributeValues(registeredInstances.get(manager), attributes, new byte[0]);
			logger.debug("Updated manager attribute values.");
		} else {
			logger.warn("Manager not registered as object instance.");
		}
	}
	
	public void updateDesigner(Designer designer) throws RTIexception {
		if(registeredInstances.containsKey(designer)) {
			logger.info("Updating designer attribute values.");
			AttributeHandleValueMap attributes = 
					rtiAmbassador.getAttributeHandleValueMapFactory().create(0);
			byte[] id = encoderFactory.createHLAinteger32BE(designer.getId()).toByteArray();
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
					ATTRIBUTE_NAME_DESIGNER_ID), id);
			byte[] designs = encoderFactory.createHLAfixedArray(
					encoderFactory.createHLAinteger32BE(designer.getDesign(0)),
					encoderFactory.createHLAinteger32BE(designer.getDesign(1))).toByteArray();
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
					ATTRIBUTE_NAME_DESIGNER_DESIGN), designs);
			byte[] strategy = encoderFactory.createHLAinteger32BE(designer.getStrategy()).toByteArray();
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
					ATTRIBUTE_NAME_DESIGNER_STRATEGY), strategy);
			byte[] share = encoderFactory.createHLAboolean(designer.isReadyToShare()).toByteArray();
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
					ATTRIBUTE_NAME_DESIGNER_SHARE), share);
			rtiAmbassador.updateAttributeValues(registeredInstances.get(designer), attributes, new byte[0]);
			logger.debug("Updated designer attribute values.");
		} else {
			logger.warn("Designer not registered as object instance.");
		}
	}
	
	public void disconnect() throws RTIexception {
		logger.debug("Resigning from the federation execution.");
		try {
			rtiAmbassador.resignFederationExecution(ResignAction.DELETE_OBJECTS_THEN_DIVEST);
			logger.info("Resigned from the federation execution.");
		} catch (FederateNotExecutionMember ignored) {
			logger.trace("Federate is not an execution member.");
		} catch (NotConnected ignored) { 
			logger.trace("Federate is not connected.");
		}

		if(this.federationName != null) {
			logger.debug("Destroying the federation execution.");
			try {
				rtiAmbassador.destroyFederationExecution(this.federationName);
				logger.info("Destroyed the federation execution.");
			} catch (FederatesCurrentlyJoined ignored) {
				logger.trace("Other federates are currently joined.");
			} catch (FederationExecutionDoesNotExist ignored) {
				logger.trace("Federation execution does not exist.");
			} catch (NotConnected ignored) {
				logger.trace("Federate is not connected.");
			}
		}

		logger.debug("Disconnecting from the RTI.");
		rtiAmbassador.disconnect();
		logger.info("Disconnected from the RTI.");
	}

	@Override
	public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass,
			String objectName) throws FederateInternalError {
		logger.info("Discovering object instance " + objectName);
		try {
			if(rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER).equals(theObjectClass)) {
				// don't record the discovered designer yet... don't know the id
				logger.debug("Discovered designer instance.");
			} else if(rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER).equals(theObjectClass)) {
				// record the discovered manager
				discoveredInstances.put(theObject, app.getManager());
				logger.debug("Discovered manager instance.");
			} else {
				logger.warn("Could not determine object class.");
			}
		} catch (RTIexception e) {
			logger.error(e);
		}
	}

	@Override
	public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
			SupplementalReflectInfo reflectInfo) throws FederateInternalError {
		logger.info("Reflecting attibute values.");
		
		try {
			byte[] idData = theAttributes.get(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
					ATTRIBUTE_NAME_DESIGNER_ID));
			if(idData != null) {
				HLAinteger32BE id = encoderFactory.createHLAinteger32BE();
				id.decode(idData);
				discoveredInstances.put(theObject, app.getDesigner(id.getValue()));
			}
			if(discoveredInstances.containsKey(theObject)) {
				if(discoveredInstances.get(theObject) instanceof Designer) {
					Designer designer = (Designer) discoveredInstances.get(theObject);
					byte[] designsData = theAttributes.get(rtiAmbassador.getAttributeHandle(
							rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
							ATTRIBUTE_NAME_DESIGNER_DESIGN));
					if(designsData != null) {
						HLAfixedArray<HLAinteger32BE> designs = encoderFactory.createHLAfixedArray(
								encoderFactory.createHLAinteger32BE(),
								encoderFactory.createHLAinteger32BE());
						designs.decode(designsData);
						for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
							designer.setDesign(i, designs.get(i).getValue());
						}
					}
					byte[] strategyData = theAttributes.get(rtiAmbassador.getAttributeHandle(
							rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
							ATTRIBUTE_NAME_DESIGNER_STRATEGY));
					if(strategyData != null) {
						HLAinteger32BE strategy = encoderFactory.createHLAinteger32BE();
						strategy.decode(strategyData);
						designer.setStrategy(strategy.getValue());
					}
					byte[] shareData = theAttributes.get(rtiAmbassador.getAttributeHandle(
							rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER), 
							ATTRIBUTE_NAME_DESIGNER_SHARE));
					if(shareData != null) {
						HLAboolean share = encoderFactory.createHLAboolean();
						share.decode(shareData);
						designer.setReadyToShare(share.getValue());
					}
					logger.debug("Reflected designer attibute values.");
				} else if(discoveredInstances.get(theObject) instanceof Manager) {
					Manager manager = (Manager) discoveredInstances.get(theObject);
					byte[] roundData = theAttributes.get(rtiAmbassador.getAttributeHandle(
							rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
							ATTRIBUTE_NAME_MANAGER_ROUND));
					if(roundData != null) {
						HLAunicodeString round = encoderFactory.createHLAunicodeString();
						round.decode(roundData);
						manager.setRoundName(round.getValue());
					}
					byte[] timeData = theAttributes.get(rtiAmbassador.getAttributeHandle(
							rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
							ATTRIBUTE_NAME_MANAGER_TIME));
					if(timeData != null) {
						HLAinteger32BE time = encoderFactory.createHLAinteger32BE();
						time.decode(timeData);
						manager.setTimeRemaining(time.getValue());
					}
					byte[] taskData = theAttributes.get(rtiAmbassador.getAttributeHandle(
							rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
							ATTRIBUTE_NAME_MANAGER_TASKS));
					if(taskData != null) {
						HLAfixedArray<HLAfixedRecord> tasks = encoderFactory.createHLAfixedArray(new DataElementFactory<HLAfixedRecord>(){
							@Override
							public HLAfixedRecord createElement(int taskId) {
								HLAfixedRecord task = encoderFactory.createHLAfixedRecord();
								task.add(encoderFactory.createHLAunicodeString());
								task.add(encoderFactory.createHLAfixedArray(new DataElementFactory<HLAinteger32BE>(){
									@Override
									public HLAinteger32BE createElement(int designerId) {
										return encoderFactory.createHLAinteger32BE();
									}
								}, Task.NUM_DESIGNERS));
								return task;
							}
						}, Task.NUM_DESIGNERS);
						tasks.decode(taskData);
						for(int i = 0; i < Manager.NUM_TASKS; i++) {
							if(tasks.get(i).get(0) instanceof HLAunicodeString) {
								manager.getTask(i).setName(((HLAunicodeString)tasks.get(i).get(0)).getValue());
							}
							for(int j = 0; j < Task.NUM_DESIGNERS; j++) {
								if(tasks.get(i).get(1) instanceof HLAfixedArray<?> 
									&& ((HLAfixedArray<?>)tasks.get(i).get(1)).get(j) instanceof HLAinteger32BE) {
									manager.getTask(i).setDesignerId(j, 
											((HLAinteger32BE)((HLAfixedArray<?>)tasks.get(i).get(1)).get(j)).getValue());
								}
							}
						}
					}
					logger.debug("Reflected manager attibute values.");
				}
			} else {
				logger.warn("Object instance not discovered.");
			}
		} catch(RTIexception | DecoderException e) {
			logger.error(e);
		}
	}

	@Override
	public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering,
			SupplementalRemoveInfo removeInfo) throws FederateInternalError {
		logger.info("Removing object instance.");
		if(discoveredInstances.containsKey(theObject)) {
			discoveredInstances.remove(theObject);
			logger.info("Removed object instance.");
		} else {
			logger.warn("Object instance not discovered.");
		}
	}
}