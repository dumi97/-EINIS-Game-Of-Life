package gol.model;;

public class Settings {
	
	private static int nextCellId = 1;
	public static int cellsInRow = 100;
	public static int cellsInColumn = 50;
	
	public static int maxInRow = 1000;
	public static int maxInColumn = 1000;

	private static int rollbackId = 1;
	
	public static int getNextCellId()
	{
		return nextCellId++;
	}
	
	public static int getTotalCellTypes()
	{
		return nextCellId-1;
	}
	
	public static void setIdRollback()
	{
		Settings.rollbackId = Settings.nextCellId;
	}
	
	public static void RollbackId()
	{
		Settings.nextCellId = Settings.rollbackId;
	}
}
