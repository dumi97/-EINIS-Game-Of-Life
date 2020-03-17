package gol.model;;

public class Settings {
	
	private static int nextCellId = 1;
	public static int cellsInRow = 100;
	public static int cellsInColumn = 50;

	public static int getNextCellId()
	{
		return nextCellId++;
	}
	
	public static int getTotalCellTypes()
	{
		return nextCellId-1;
	}
}
