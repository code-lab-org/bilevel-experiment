package edu.stevens.code.eager.io;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZThread;
import org.zeromq.ZThread.IAttachedRunnable;
import org.zeromq.ZThread.IDetachedRunnable;

import com.google.gson.Gson;

import edu.stevens.code.eager.App;
import edu.stevens.code.eager.Designer;
import edu.stevens.code.eager.DesignerApp;
import edu.stevens.code.eager.Manager;
import edu.stevens.code.eager.ManagerApp;
import edu.stevens.code.eager.Task;

public class ZmqAmbassador implements Ambassador {
	private static Logger logger = LogManager.getLogger(ZmqAmbassador.class);
	private static final int PROXY_IN_PORT = 5563;
	private static final int PROXY_OUT_PORT = 5564;
	private static final String UPDATE_TOPIC = "update";
	private static final String MANAGER_TOPIC = "Manager";
	private static final List<String> DESIGNER_TOPICS = Arrays.asList(
		"Designer 0", "Designer 1", "Designer 2", "Designer 3"
	);
	
	private App app;
	private ZContext context;
	private Socket publisher;

	private Gson gson = new Gson();
	
	private final Map<Object, String> registeredInstances = 
			Collections.synchronizedMap(new HashMap<Object, String>());
	private final Map<String, Object> discoveredInstances = 
			Collections.synchronizedMap(new HashMap<String, Object>());
	
	public ZmqAmbassador() { }

	@Override
	public void connectManager(ManagerApp managerApp, String federationName) {
		this.app = managerApp;

		logger.debug("Setting up proxy.");
		ZThread.start(new IDetachedRunnable() {
			@Override
			public void run(Object[] args) {
				try (ZContext ctx = new ZContext()) {
					Socket backend = ctx.createSocket(SocketType.XPUB);
					backend.bind(String.format("tcp://%s:%d", federationName, PROXY_OUT_PORT));
					
					Socket frontend = ctx.createSocket(SocketType.XSUB);
					frontend.bind(String.format("tcp://%s:%d", federationName, PROXY_IN_PORT));
			        
			        ZMQ.proxy(frontend, backend, null);
				}
			}
		});

		context = new ZContext();

		logger.debug("Setting up manager subscriber.");
		ZThread.fork(context, new IAttachedRunnable() {
			@Override
			public void run(Object[] args, ZContext ctx, Socket pipe) {		
				Socket subscriber = ctx.createSocket(SocketType.SUB);
				subscriber.connect(String.format("tcp://%s:%d", federationName, PROXY_OUT_PORT));
				subscriber.subscribe(UPDATE_TOPIC.getBytes(ZMQ.CHARSET));
				for(String topic : DESIGNER_TOPICS) {
					logger.debug(String.format("%s subscribing to %s", managerApp.getController().toString(), topic));
					subscriber.subscribe(topic.getBytes(ZMQ.CHARSET));
				}
				
	            while (!Thread.currentThread().isInterrupted()) {
	                String address = subscriber.recvStr();
	                String contents = subscriber.recvStr();
	                //logger.debug(String.format("%s RECV: %s : %s", managerApp.getController().toString(), address, contents));
	                if(UPDATE_TOPIC.contentEquals(address) && managerApp.getController().toString().contentEquals(contents)) {
	                	updateManager(managerApp.getController());
	                } else if(DESIGNER_TOPICS.contains(address)) {
	                	if(!discoveredInstances.containsKey(address)) {
	                		updateManager(managerApp.getController());
	                	}
		                reflectDesignerUpdates(address, gson.fromJson(contents, DesignerModel.class));
	                }
	            }
	            ctx.destroySocket(subscriber);
			}
		});

		logger.debug("Setting up manager publisher.");
		publisher = context.createSocket(SocketType.PUB);
		publisher.connect(String.format("tcp://%s:%d", federationName, PROXY_IN_PORT));
		
		registeredInstances.put(
			managerApp.getController(), 
			managerApp.getController().toString()
		);
		updateManager(managerApp.getController());
	}

	@Override
	public void connectDesigner(DesignerApp designerApp, String federationName) {
		this.app = designerApp;
		
		context = new ZContext();
		
		logger.debug("Setting up designer subscriber.");
		ZThread.fork(context, new IAttachedRunnable() {
			@Override
			public void run(Object[] args, ZContext ctx, Socket pipe) {
				Socket subscriber = ctx.createSocket(SocketType.SUB);
				subscriber.connect(String.format("tcp://%s:%d", federationName, PROXY_OUT_PORT));
				
				logger.debug(String.format("%s subscribing to Manager", designerApp.getController().toString()));
				subscriber.subscribe(UPDATE_TOPIC.getBytes(ZMQ.CHARSET));
				for(String topic : DESIGNER_TOPICS) {
					if(!topic.contentEquals(designerApp.getController().toString())) {
						subscriber.subscribe(topic.getBytes(ZMQ.CHARSET));
					}
				}
				subscriber.subscribe(MANAGER_TOPIC.getBytes(ZMQ.CHARSET));
	            while (!Thread.currentThread().isInterrupted()) {
	                String address = subscriber.recvStr();
	                String contents = subscriber.recvStr();
	                //logger.debug(String.format("%s RECV: %s : %s", designerApp.getController().toString(), address, contents));
	                if(UPDATE_TOPIC.contentEquals(address) && designerApp.getController().toString().contentEquals(contents)) {
	                	updateDesigner(designerApp.getController());
	                } else if(DESIGNER_TOPICS.contains(address)) {
		                reflectDesignerUpdates(address, gson.fromJson(contents, DesignerModel.class));
	                } else if(MANAGER_TOPIC.contentEquals(address)) {
		                reflectManagerUpdates(address, gson.fromJson(contents, ManagerModel.class));
	                }
	            }
	            ctx.destroySocket(subscriber);
			}
		});

		logger.debug("Setting up designer publisher.");
		publisher = context.createSocket(SocketType.PUB);
		publisher.connect(String.format("tcp://%s:%d", federationName, PROXY_IN_PORT));
		
		registeredInstances.put(
			designerApp.getController(), 
			designerApp.getController().toString()
		);
		updateDesigner(designerApp.getController());
	}

	@Override
	public void updateManager(Manager manager, Object... properties) {
		if(registeredInstances.containsKey(manager)) {
			logger.info("Updating manager attribute values.");
			ManagerModel model = new ManagerModel();
			if(properties.length == 0 || Arrays.asList(properties).contains(Manager.PROPERTY_ROUND)) {
				model.setRoundName(manager.getRoundName());
			}
			if(properties.length == 0 || Arrays.asList(properties).contains(Manager.PROPERTY_TIME)) {
				model.setTimeRemaining(manager.getTimeRemaining());
			}
			if(properties.length == 0 || Arrays.asList(properties).contains(Manager.PROPERTY_TASKS)) {
				List<TaskModel> tasks = new ArrayList<TaskModel>();
				for(Task task : manager.getTasks()) {
					TaskModel modelTask = new TaskModel();
					modelTask.setName(task.getName());
					List<Integer> ids = new ArrayList<Integer>();
					for(int id : task.getDesignerIds()) {
						ids.add(id);
					}
					modelTask.setDesignerIds(ids);
					tasks.add(modelTask);
				}
				model.setTasks(tasks);
			}
			if(! (model.getRoundName() == null 
					&& model.getTimeRemaining() == null 
					&& model.getTasks() == null)) {
				logger.debug(String.format("Publishing to %s : %s", manager.toString(), gson.toJson(model)));
                publisher.sendMore(manager.toString());
                publisher.send(gson.toJson(model));
				logger.debug("Updated manager attribute values.");
			} else {
				logger.debug("No new manager attribute values to update.");
			}
		} else {
			logger.warn("Manager not registered as object instance.");
		}
	}
	
	private void requestUpdates(String name) {
		logger.debug(String.format("Requesting %s attribute value updates.", name));
		publisher.sendMore(UPDATE_TOPIC);
		publisher.send(name);
	}
	
	private void reflectManagerUpdates(String name, ManagerModel model) {
		logger.info("Reflecting manager attibute values.");
		if(!discoveredInstances.containsKey(name)) {
			discoveredInstances.put(name, app.getManager());
			if(model.getRoundName() == null 
					|| model.getTimeRemaining() == null 
					|| model.getTasks() == null) {
				requestUpdates(name);
			}
		}
		if(discoveredInstances.containsKey(name)) {
			Manager manager = (Manager) discoveredInstances.get(name);
			if(model.getRoundName() != null) {
				manager.setRoundName(model.getRoundName());
			}
			if(model.getTimeRemaining() != null) {
				manager.setTimeRemaining(model.getTimeRemaining());
			}
			if(model.getTasks() != null) {
				Task[] tasks = new Task[model.getTasks().size()];
				for(int i = 0; i < model.getTasks().size(); i++) {
					int[] ids = new int[model.getTasks().get(i).getDesignerIds().size()];
					for(int j = 0; j < model.getTasks().get(i).getDesignerIds().size(); j++) {
						ids[j] = model.getTasks().get(i).getDesignerIds().get(j);
					}
					tasks[i] = new Task(model.getTasks().get(i).getName(), ids);
				}
				manager.setTasks(tasks);
			}
			logger.debug("Reflected manager attibute values.");
		} else {
			logger.warn("Object instance not discovered.");
		}
	}
	
	private void reflectDesignerUpdates(String name, DesignerModel model) {
		logger.info("Reflecting designer attibute values.");
		if(!discoveredInstances.containsKey(name)) {
			if(model.getId() != null) {
				discoveredInstances.put(name, app.getDesigner(model.getId()));
			}
			if(model.getId() == null 
					|| model.getDesigns() == null 
					|| model.getStrategy() == null 
					|| model.isReadyToShare() == null) {
				requestUpdates(name);
			}
		}
		if(discoveredInstances.containsKey(name)) {
			Designer designer = (Designer) discoveredInstances.get(name);
			if(model.getDesigns() != null) {
				int[] designs = new int[model.getDesigns().size()];
				for(int i = 0; i < model.getDesigns().size(); i++) {
					designs[i] = model.getDesigns().get(i);
				}
				designer.setDesigns(designs);
			}
			if(model.getStrategy() != null) {
				designer.setStrategy(model.getStrategy());
			}
			if(model.isReadyToShare() != null) {
				designer.setReadyToShare(model.isReadyToShare());
			}
			logger.debug("Reflected designer attibute values.");
		} else {
			logger.warn("Object instance not discovered.");
		}
	}

	@Override
	public void updateDesigner(Designer designer, Object... properties) {
		if(registeredInstances.containsKey(designer)) {
			logger.info("Updating designer attribute values.");
			DesignerModel model = new DesignerModel();
			if(properties.length == 0 || Arrays.asList(properties).contains(Designer.PROPERTY_ID)) {
				model.setId(designer.getId());
			}
			if(properties.length == 0 || Arrays.asList(properties).contains(Designer.PROPERTY_DESIGNS)) {
				List<Integer> designs = new ArrayList<Integer>();
				for(int design : designer.getDesigns()) {
					designs.add(new Integer(design));
				}
				model.setDesigns(designs);
			}
			if(properties.length == 0 || Arrays.asList(properties).contains(Designer.PROPERTY_STRATEGY)) {
				model.setStrategy(designer.getStrategy());
			}
			if(properties.length == 0 || Arrays.asList(properties).contains(Designer.PROPERTY_SHARE)) {
				model.setReadyToShare(designer.isReadyToShare());
			}
			if(!(model.getId() == null 
					&& model.getDesigns() == null 
					&& model.getStrategy() == null 
					&& model.isReadyToShare() == null)) {
				logger.debug(String.format("Publishing to %s : %s", designer.toString(), gson.toJson(model)));
                publisher.sendMore(designer.toString());
                publisher.send(gson.toJson(model));
				logger.debug("Updated designer attribute values.");
			} else {
				logger.debug("No new designer attribute values to update.");
			}
		} else {
			logger.warn("Designer not registered as object instance.");
		}
	}

	@Override
	public void disconnect() {
		context.destroySocket(publisher);
	}
	
	public static class DesignerModel {
		Integer id;
		List<Integer> designs;
		Integer strategy;
		Boolean readyToShare;
		
		public DesignerModel() { }
		
		public void setId(Integer id) {
			this.id = id;
		}
		
		public Integer getId() {
			return id;
		}

		public List<Integer> getDesigns() {
			return designs;
		}

		public void setDesigns(List<Integer> designs) {
			this.designs = designs;
		}

		public Integer getStrategy() {
			return strategy;
		}

		public void setStrategy(Integer strategy) {
			this.strategy = strategy;
		}

		public Boolean isReadyToShare() {
			return readyToShare;
		}

		public void setReadyToShare(Boolean readyToShare) {
			this.readyToShare = readyToShare;
		}
	}
	
	public static class ManagerModel {
		String roundName;
		Integer timeRemaining;
		List<TaskModel> tasks;
		
		public ManagerModel() { }

		public String getRoundName() {
			return roundName;
		}

		public void setRoundName(String roundName) {
			this.roundName = roundName;
		}

		public Integer getTimeRemaining() {
			return timeRemaining;
		}

		public void setTimeRemaining(Integer timeRemaining) {
			this.timeRemaining = timeRemaining;
		}

		public List<TaskModel> getTasks() {
			return tasks;
		}

		public void setTasks(List<TaskModel> tasks) {
			this.tasks = tasks;
		}
	}
	
	public static class TaskModel {		
		String name;
		List<Integer> designerIds;
		
		public TaskModel() { }

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<Integer> getDesignerIds() {
			return designerIds;
		}

		public void setDesignerIds(List<Integer> designerIds) {
			this.designerIds = designerIds;
		}
	}
}
