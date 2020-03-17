package gol.model;

import java.awt.Color;
import java.io.Serializable;

public class Cell implements Serializable{
	private int x, y;
	private int id;
	
	public Cell()
	{
		setValues(-1, -1, -1);
	}
	
	public Cell(int x, int y, int id)
	{
		setValues(x, y, id);
	}
	
	public Cell(Cell c)
	{
		this(c.getX(), c.getY(), c.getId());
	}
	
	public void setValues(int x, int y, int id)
	{
		this.x = x;
		this.y = y;
		this.id = id;
	}
	
	public int getX()
	{
		return this.x;
	}
	
	public int getY()
	{
		return this.y;
	}
	
	public int getId()
	{
		return this.id;
	}
	
	public void setId(int id)
	{
		this.id = id;
	}
	
	public void incrementId()
	{
		if(Settings.getTotalCellTypes() < 1)
			return;
		
		id = (id+1) % (Settings.getTotalCellTypes()+1);
		//System.out.println("New cell " + x + "," + y + " id: " + id);
	}
	
	public void decrementId()
	{
		if(Settings.getTotalCellTypes() < 1)
			return;
		
		id = id - 1;
		if(id < 0)
			id = Settings.getTotalCellTypes(); 
		//System.out.println("New cell " + x + "," + y + " id: " + id);
	}
	
	public Color getColor()
	{
		if(!Model.cellTypes.containsKey(id))
			return new Color(50, 50, 50);
	
		return Model.cellTypes.get(id).color;
	}
	
	public boolean isAlive()
	{
		if(id != 0)
			return true;
		return false;
	}
	
	@Override
    public String toString() {
        return "Cell [x=" + x + ", ye=" + y + ", id=" + id + "]";
    }
}
