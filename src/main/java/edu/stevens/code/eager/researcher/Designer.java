package edu.stevens.code.eager.researcher;

public class Designer {
	
	/* Variable xA */
	private int xA;
	public int getXA() { return xA; }
	public void setXA(int x_A) { this.xA = x_A;	}
	
	/* Variable xB */
	private int xB;
	public int getXB() { return xB; }
	public void setXB(int x_B) { this.xB = x_B;	}
	
	/* Designer's ID */
	private int id;
	public int getID() { return id; }
	public void setID(int ID) { this.id = ID;	}
	
	/* Designer's name */
	private String name;
	public String getDesignerName() {	return name; }
	public void setDesignerName(String designer_name) { this.name = designer_name; }

	
	/* Default constructor */
	public Designer(int ID){
		this.setID(ID);	
	}
	
	/* Constructor with designer's name */
	public Designer(int ID, String designer_name){
		this.setID(ID);	
		this.setDesignerName(designer_name);
	}
	
	
	
}
