package edu.stevens.code.ptg.hla;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.Manager;
import edu.stevens.code.ptg.Task;
import hla.rti1516e.AttributeHandle;
import hla.rti1516e.AttributeHandleSet;
import hla.rti1516e.AttributeHandleValueMap;
import hla.rti1516e.CallbackModel;
import hla.rti1516e.FederateAmbassador;
import hla.rti1516e.FederateHandle;
import hla.rti1516e.FederateHandleSaveStatusPair;
import hla.rti1516e.FederateHandleSet;
import hla.rti1516e.FederateRestoreStatus;
import hla.rti1516e.FederationExecutionInformationSet;
import hla.rti1516e.InteractionClassHandle;
import hla.rti1516e.LogicalTime;
import hla.rti1516e.MessageRetractionHandle;
import hla.rti1516e.ObjectClassHandle;
import hla.rti1516e.ObjectInstanceHandle;
import hla.rti1516e.OrderType;
import hla.rti1516e.ParameterHandleValueMap;
import hla.rti1516e.RTIambassador;
import hla.rti1516e.ResignAction;
import hla.rti1516e.RestoreFailureReason;
import hla.rti1516e.RtiFactory;
import hla.rti1516e.RtiFactoryFactory;
import hla.rti1516e.SaveFailureReason;
import hla.rti1516e.SynchronizationPointFailureReason;
import hla.rti1516e.TransportationTypeHandle;
import hla.rti1516e.encoding.DataElementFactory;
import hla.rti1516e.encoding.EncoderFactory;
import hla.rti1516e.encoding.HLAfixedRecord;
import hla.rti1516e.encoding.HLAinteger32BE;
import hla.rti1516e.exceptions.AlreadyConnected;
import hla.rti1516e.exceptions.FederateAlreadyExecutionMember;
import hla.rti1516e.exceptions.FederateInternalError;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederatesCurrentlyJoined;
import hla.rti1516e.exceptions.FederationExecutionAlreadyExists;
import hla.rti1516e.exceptions.FederationExecutionDoesNotExist;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIexception;

public class Ambassador implements FederateAmbassador {
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
	
	private static Logger logger = LogManager.getLogger(Ambassador.class);
	
	protected final RTIambassador rtiAmbassador;
	private final EncoderFactory encoderFactory;
	private String federationName = null;
	private final Map<Object, ObjectInstanceHandle> instanceHandles = new HashMap<Object, ObjectInstanceHandle>();
	
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
	
	public void connectManager(Manager manager, String federationName) throws RTIexception {
		this.connect(federationName, manager.toString(), FEDERATE_TYPE_MANAGER);
		publishManagerAttributes();
		subscribeManagerAttributes();
		subscribeDesignerAttributes();

		logger.debug("Registering this manager object.");
		ObjectInstanceHandle instance = rtiAmbassador.registerObjectInstance(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER));
		instanceHandles.put(manager,  instance);
		logger.debug("Registered this manager object.");
		
		updateManager(manager);
	}
	
	public void connectDesigner(Designer designer, String federationName) throws RTIexception {
		this.connect(federationName, designer.toString(), FEDERATE_TYPE_DESIGNER);
		publishDesignerAttributes();
		subscribeDesignerAttributes();
		subscribeManagerAttributes();

		logger.debug("Registering this designer object.");
		ObjectInstanceHandle instance = rtiAmbassador.registerObjectInstance(
				rtiAmbassador.getObjectClassHandle(CLASS_NAME_DESIGNER));
		instanceHandles.put(designer,  instance);
		logger.debug("Registered this designer object.");
		
		updateDesigner(designer);
	}
	
	public void updateManager(Manager manager) throws RTIexception {
		if(instanceHandles.containsKey(manager)) {
			logger.info("Updating manager attribute values.");
			AttributeHandleValueMap attributes = 
					rtiAmbassador.getAttributeHandleValueMapFactory().create(0);
			
			byte[] id = encoderFactory.createHLAunicodeString(manager.getRoundName()).toByteArray();
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
					ATTRIBUTE_NAME_MANAGER_ROUND), id);
			byte[] designs = encoderFactory.createHLAinteger32BE(manager.getTimeRemaining()).toByteArray();
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
					ATTRIBUTE_NAME_MANAGER_TIME), designs);
			byte[] tasks = encoderFactory.createHLAfixedArray(new DataElementFactory<HLAfixedRecord>(){
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
			}, Task.NUM_DESIGNERS).toByteArray();
			attributes.put(rtiAmbassador.getAttributeHandle(
					rtiAmbassador.getObjectClassHandle(CLASS_NAME_MANAGER), 
					ATTRIBUTE_NAME_MANAGER_TASKS), tasks);
			rtiAmbassador.updateAttributeValues(instanceHandles.get(manager), attributes, new byte[0]);
			logger.debug("Updated manager attribute values.");
		} else {
			logger.warn("Manager not registered as object instance.");
		}
	}
	
	public void updateDesigner(Designer designer) throws RTIexception {
		if(instanceHandles.containsKey(designer)) {
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
			rtiAmbassador.updateAttributeValues(instanceHandles.get(designer), attributes, new byte[0]);
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
	public void connectionLost(String faultDescription) throws FederateInternalError {
		// TODO Auto-generated method stub
	}

	@Override
	public void reportFederationExecutions(FederationExecutionInformationSet theFederationExecutionInformationSet)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void synchronizationPointRegistrationSucceeded(String synchronizationPointLabel)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void synchronizationPointRegistrationFailed(String synchronizationPointLabel,
			SynchronizationPointFailureReason reason) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void announceSynchronizationPoint(String synchronizationPointLabel, byte[] userSuppliedTag)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void federationSynchronized(String synchronizationPointLabel, FederateHandleSet failedToSyncSet)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initiateFederateSave(String label) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initiateFederateSave(String label, LogicalTime time) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void federationSaved() throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void federationNotSaved(SaveFailureReason reason) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void federationSaveStatusResponse(FederateHandleSaveStatusPair[] response) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestFederationRestoreSucceeded(String label) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestFederationRestoreFailed(String label) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void federationRestoreBegun() throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initiateFederateRestore(String label, String federateName, FederateHandle federateHandle)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void federationRestored() throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void federationNotRestored(RestoreFailureReason reason) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void federationRestoreStatusResponse(FederateRestoreStatus[] response) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startRegistrationForObjectClass(ObjectClassHandle theClass) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopRegistrationForObjectClass(ObjectClassHandle theClass) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnInteractionsOn(InteractionClassHandle theHandle) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnInteractionsOff(InteractionClassHandle theHandle) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectInstanceNameReservationSucceeded(String objectName) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void objectInstanceNameReservationFailed(String objectName) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void multipleObjectInstanceNameReservationSucceeded(Set<String> objectNames) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void multipleObjectInstanceNameReservationFailed(Set<String> objectNames) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass,
			String objectName) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverObjectInstance(ObjectInstanceHandle theObject, ObjectClassHandle theObjectClass,
			String objectName, FederateHandle producingFederate) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
			SupplementalReflectInfo reflectInfo) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime,
			OrderType receivedOrdering, SupplementalReflectInfo reflectInfo) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reflectAttributeValues(ObjectInstanceHandle theObject, AttributeHandleValueMap theAttributes,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime,
			OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReflectInfo reflectInfo)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport,
			SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime,
			OrderType receivedOrdering, SupplementalReceiveInfo receiveInfo) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveInteraction(InteractionClassHandle interactionClass, ParameterHandleValueMap theParameters,
			byte[] userSuppliedTag, OrderType sentOrdering, TransportationTypeHandle theTransport, LogicalTime theTime,
			OrderType receivedOrdering, MessageRetractionHandle retractionHandle, SupplementalReceiveInfo receiveInfo)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering,
			SupplementalRemoveInfo removeInfo) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering,
			LogicalTime theTime, OrderType receivedOrdering, SupplementalRemoveInfo removeInfo)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeObjectInstance(ObjectInstanceHandle theObject, byte[] userSuppliedTag, OrderType sentOrdering,
			LogicalTime theTime, OrderType receivedOrdering, MessageRetractionHandle retractionHandle,
			SupplementalRemoveInfo removeInfo) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributesInScope(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributesOutOfScope(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void provideAttributeValueUpdate(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes,
			byte[] userSuppliedTag) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnUpdatesOnForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnUpdatesOnForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes,
			String updateRateDesignator) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void turnUpdatesOffForObjectInstance(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmAttributeTransportationTypeChange(ObjectInstanceHandle theObject,
			AttributeHandleSet theAttributes, TransportationTypeHandle theTransportation) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportAttributeTransportationType(ObjectInstanceHandle theObject, AttributeHandle theAttribute,
			TransportationTypeHandle theTransportation) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmInteractionTransportationTypeChange(InteractionClassHandle theInteraction,
			TransportationTypeHandle theTransportation) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reportInteractionTransportationType(FederateHandle theFederate, InteractionClassHandle theInteraction,
			TransportationTypeHandle theTransportation) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestAttributeOwnershipAssumption(ObjectInstanceHandle theObject,
			AttributeHandleSet offeredAttributes, byte[] userSuppliedTag) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestDivestitureConfirmation(ObjectInstanceHandle theObject, AttributeHandleSet offeredAttributes)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeOwnershipAcquisitionNotification(ObjectInstanceHandle theObject,
			AttributeHandleSet securedAttributes, byte[] userSuppliedTag) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeOwnershipUnavailable(ObjectInstanceHandle theObject, AttributeHandleSet theAttributes)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestAttributeOwnershipRelease(ObjectInstanceHandle theObject, AttributeHandleSet candidateAttributes,
			byte[] userSuppliedTag) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmAttributeOwnershipAcquisitionCancellation(ObjectInstanceHandle theObject,
			AttributeHandleSet theAttributes) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void informAttributeOwnership(ObjectInstanceHandle theObject, AttributeHandle theAttribute,
			FederateHandle theOwner) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeIsNotOwned(ObjectInstanceHandle theObject, AttributeHandle theAttribute)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void attributeIsOwnedByRTI(ObjectInstanceHandle theObject, AttributeHandle theAttribute)
			throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeRegulationEnabled(LogicalTime time) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeConstrainedEnabled(LogicalTime time) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeAdvanceGrant(LogicalTime theTime) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestRetraction(MessageRetractionHandle theHandle) throws FederateInternalError {
		// TODO Auto-generated method stub
		
	}

}