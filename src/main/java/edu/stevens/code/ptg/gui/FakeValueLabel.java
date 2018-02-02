package edu.stevens.code.ptg.gui;

import java.util.Observable;
import java.util.Observer;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.FakeValueMap;
import edu.stevens.code.ptg.Task;

public class FakeValueLabel extends ValueLabel {
	private static final long serialVersionUID = -125874855243548180L;
	
	private int myOtherDesign, partnerOtherDesign;
	
	public FakeValueLabel() {
		super();
	}
	
	public void bindTo(DesignerApp app, int myStrategy, int partnerStrategy) {
		super.bindTo(app, myStrategy, partnerStrategy);
		for(Designer designer : app.getDesigners()) {
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(designer == app.getDesignPartner() && designer.isReadyToShare()) {
						if(partnerOtherDesign != designer.getDesign(myStrategy)) {
							partnerOtherDesign = designer.getDesign(myStrategy);
							updateLabel();
						}
					} else if(designer == app.getController()) {
						if(myOtherDesign != app.getController().getDesign(partnerStrategy)) {
							myOtherDesign = app.getController().getDesign(partnerStrategy);
							updateLabel();
						}
					}
				}
			});
		}
	}
	
	protected int getValue() {
		int value = super.getValue();
		if(myStrategy != partnerStrategy) {
			int i = app.getController().getId();
			Task task = app.getManager().getTaskByDesignerId(i);
			value = new FakeValueMap(task.getValueMap()).getFakeValues(
					task.getDesignerId(0)==i ? myStrategy : partnerStrategy, 
					task.getDesignerId(0)==i ? partnerStrategy : myStrategy, 
					task.getDesignerId(0)==i ? myDesign : partnerDesign, 
					task.getDesignerId(0)==i ? partnerDesign : myDesign,
					task.getDesignerId(0)==i ? myOtherDesign : partnerOtherDesign, 
					task.getDesignerId(0)==i ? partnerOtherDesign : myOtherDesign
			)[task.getDesignerId(0)==i ? 0 : 1];
		}
		return value;
	}
}
