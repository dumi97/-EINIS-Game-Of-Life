package gol.model;

import java.awt.Color;
import java.util.HashMap;

public class CellType {
	public int id;
	public int maxNeighboursTotal;
	private HashMap<Integer, Integer> minNeighboursToSurvive = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> maxNeighboursToSurvive = new HashMap<Integer, Integer>();
	private HashMap<Integer, Integer> neighboursToStartLife = new HashMap<Integer, Integer>();
	public Color color;
	
	public CellType(int id, int maxNeighboursTotal, int minNeighboursToSurvive, int maxNeighboursToSurvive, int neighboursToStartLife, Color color)
	{
		this.id = id;
		this.color = color;
		this.maxNeighboursTotal = maxNeighboursTotal;
		setCellTypeNeighbourData(id, minNeighboursToSurvive, maxNeighboursToSurvive, neighboursToStartLife);
	}
	
	public CellType(CellType other)
	{
		this.id = other.id;
		this.maxNeighboursTotal = other.maxNeighboursTotal;
		this.color = other.color;
		
		HashMap<Integer, Integer> tempMinNeigh = other.getMinNeighboursToSurvive();
		HashMap<Integer, Integer> tempMaxNeigh = other.getMaxNeighboursToSurvive();
		HashMap<Integer, Integer> tempNeighToStart = other.getNeighboursToStartLife();
		
		// deep copy all hash maps
		for(Integer i : tempMinNeigh.keySet())
		{
			this.minNeighboursToSurvive.put(new Integer(i), new Integer(tempMinNeigh.get(i)));
			this.maxNeighboursToSurvive.put(new Integer(i), new Integer(tempMaxNeigh.get(i)));
			this.neighboursToStartLife.put(new Integer(i), new Integer(tempNeighToStart.get(i)));
		}
	}
	
	public HashMap<Integer, Integer> getMinNeighboursToSurvive()
	{
		return minNeighboursToSurvive;
	}

	public HashMap<Integer, Integer> getMaxNeighboursToSurvive()
	{
		return maxNeighboursToSurvive;
	}

	public HashMap<Integer, Integer> getNeighboursToStartLife()
	{
		return neighboursToStartLife;
	}
	
	public boolean setCellTypeNeighbourData(int neighbourId, int minNeighboursToSurvive, int maxNeighboursToSurvive, int neighboursToStartLife)
	{		
		System.out.println("Setting data for cell " + id + ": nId=" + neighbourId + ", minN=" + minNeighboursToSurvive + ", maxN=" + maxNeighboursToSurvive + ", nToStart=" + neighboursToStartLife);
		
		if(minNeighboursToSurvive > maxNeighboursToSurvive)
			minNeighboursToSurvive = maxNeighboursToSurvive;
		
		this.minNeighboursToSurvive.put(neighbourId, minNeighboursToSurvive);
		this.maxNeighboursToSurvive.put(neighbourId, maxNeighboursToSurvive);
		this.neighboursToStartLife.put(neighbourId, neighboursToStartLife);
		
		return true;	
	}
	
	// returns true if cell can be born
	public boolean checkBirthCondition(int givenId, int count, int totalNeighbours)
	{
		// the given id is not present in neighboursToStartLife
		if(!neighboursToStartLife.containsKey(givenId))
			return false;
		
		// overpopulation
		if(totalNeighbours > maxNeighboursTotal)
			return false;
		
		// neighbour requirement not met
		if(count != neighboursToStartLife.get(givenId))
			return false;
		
		return true;
	}
	
	public boolean diesFromTotalOverpopulation(int totalNeighbours)
	{
		if(totalNeighbours > maxNeighboursTotal)
			return true;
		return false;
	}
	
	// returns true if condition met and cell dies
	public boolean checkDeathCondition(HashMap<Integer, Integer> neighbourTypes)
	{
		boolean dies = false;
		
		for(int i : minNeighboursToSurvive.keySet())
		{
			int count = neighbourTypes.containsKey(i) ? neighbourTypes.get(i) : 0;
			
			if(count < minNeighboursToSurvive.get(i) || count > maxNeighboursToSurvive.get(i))
			{
				dies = true;
				break;
			}	
		}
		
		return dies;
	}
}
