package gol.model;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import gol.controller.Controller;

public class Model {

	private Controller controller;
	private int currentIteration;
	private Cell cellGrid[][];
	private Cell oldCellGrid[][];
	public static HashMap<Integer, CellType> cellTypes = new HashMap<Integer, CellType>();
	private HashSet<String> cellsClicked = new HashSet<String>();
	
	private JFileChooser fileChooser;
	private File saveFile;

	public Model(Controller controller)
	{
		this.controller = controller;
		addCellType(6, 2, 3, 3, new Color(255, 255, 255)); // 1
		
		addCellType(6, 2, 5, 2, new Color(30, 255, 30)); // 2
		setCellTypeNeighbourData(2, 1, 0, 0, 9);
	}
	
	public JFileChooser getFileChooser()
	{
		return fileChooser;
	}
	
	public File getSaveFile()
	{
		return saveFile;
	}

	public void setSaveFile(File saveFile)
	{
		this.saveFile = saveFile;
	}
	
	public Cell[][] getCellGrid()
	{
		return cellGrid;
	}
	
	public int getCurrentIteration()
	{
		return currentIteration;
	}
	
	public void addCellType(int maxNeighboursTotal, int minNeighboursToSurvive, int maxNeighboursToSurvive, int neighboursToStartLife, Color color)
	{
		int id = Settings.getNextCellId();
		cellTypes.put(id, new CellType(id, maxNeighboursTotal, minNeighboursToSurvive, maxNeighboursToSurvive, neighboursToStartLife, color));
	}
	
	public void setCellTypeNeighbourData(int cellId, int neighbourId, int minNeighboursToSurvive, int maxNeighboursToSurvive, int neighboursToStartLife)
	{
		CellType ct = cellTypes.get(cellId);
		
		if(ct == null)
			return;
		
		ct.setCellTypeNeighbourData(neighbourId, minNeighboursToSurvive, maxNeighboursToSurvive, neighboursToStartLife);
	}
	
	public void generateCells()
	{
		currentIteration = 0;
		this.cellGrid = new Cell[Settings.cellsInColumn][Settings.cellsInRow];
		
		for(int i = 0; i < cellGrid.length; ++i)
		{
			for(int j = 0; j < cellGrid[i].length; ++j)
			{
				cellGrid[i][j] = new Cell(j, i, 0);
			}
		}
	}
	
	public void randomize()
	{
		Random random = new Random();
		List<Integer> keys = new ArrayList<Integer>(cellTypes.keySet());
		keys.add(0);
		
		for(int i = 0; i < cellGrid.length; ++i)
		{
			for(int j = 0; j < cellGrid[i].length; ++j)
			{
				int randomId = keys.get(random.nextInt(keys.size()));
				cellGrid[i][j].setId(randomId);;
			}
		}
	}
	
	public void cellClicked(int x, int y, int buttonNum)
	{	
		// clicked outside of the game
		if(y >= cellGrid.length || x >= cellGrid[0].length || y < 0 || x < 0)
			return;
		
		// cell already clicked in this mouse sweep
		if(cellsClicked.contains(x+","+y))
			return;
		
		cellsClicked.add(x+","+y);
		
		// handle different mouse buttons
		if(buttonNum == 0) // left
			cellGrid[y][x].incrementId();
		else if(buttonNum == 1) // right
			cellGrid[y][x].decrementId();
		else if(buttonNum == 2) // middle
			cellGrid[y][x].setId(0);
	}
	
	public void cellClickedSet(int x, int y, int setIdNum)
	{	
		if(!cellTypes.containsKey(setIdNum))
			setIdNum = 0;
		
		// clicked outside of the game
		if(y >= cellGrid.length || x >= cellGrid[0].length || y < 0 || x < 0)
			return;
		
		// cell already clicked in this mouse sweep
		if(cellsClicked.contains(x+","+y))
			return;
		
		cellsClicked.add(x+","+y);

		cellGrid[y][x].setId(setIdNum);
	}
	
	public void clearClickedCells()
	{
		cellsClicked.clear();
	}
	
	public void makeIteration()
	{
		++currentIteration;
		oldCellGrid = new Cell[Settings.cellsInColumn][Settings.cellsInRow];
		
		// deep copy current gird
		for(int i = 0; i < cellGrid.length; ++i)
		{
			for(int j = 0; j < cellGrid[i].length; ++j)
			{
				oldCellGrid[i][j] = new Cell(cellGrid[i][j]);
			}
		}
		
		
		for(int i = 0; i < oldCellGrid.length; ++i)
		{
			for(int j = 0; j < oldCellGrid[0].length; ++j)
			{
				handleCell(cellGrid[i][j]);
			}
		}
		
		controller.updateGame();
	}
	
	private void handleCell(Cell cell)
	{
		ArrayList<Cell> neighbours = getCellNeighbours(cell.getX(), cell.getY());
		HashMap<Integer, Integer> neighbourTypes = new HashMap<Integer, Integer>(); // <neighbour_id, count>
		neighbourTypes.put(0, 0);
		int totalNeighbours;
		
		for(Cell c : neighbours)
		{
			if(neighbourTypes.containsKey(c.getId()))
			{
				neighbourTypes.put(c.getId(), neighbourTypes.get(c.getId())+1);
			}
			else
			{
				neighbourTypes.put(c.getId(), 1);
			}
		}
		
		totalNeighbours = neighbours.size() - neighbourTypes.get(0);
		
		// handle alive cell
		if(cell.isAlive())
		{
			CellType thisCellType = cellTypes.get(cell.getId());
			
			// check for total overpopulation
			if(thisCellType.diesFromTotalOverpopulation(totalNeighbours))
			{
				cell.setId(0);
				return;
			}
				
			// check death conditions
			if(thisCellType.checkDeathCondition(neighbourTypes))
			{
				cell.setId(0);
				return;
			}
		}
		// handle dead cell
		else
		{
			ArrayList<Integer> possibleTypes = new ArrayList<Integer>();
			for(int type : cellTypes.keySet())
			{
				for(int i : neighbourTypes.keySet())
				{
					// omit dead cells as they can't start life
					if(i == 0)
						continue;
					
					if(cellTypes.get(type).checkBirthCondition(i, neighbourTypes.get(i), totalNeighbours))
						possibleTypes.add(type);
				}
			}
			
			if(possibleTypes.size() > 0)
			{
				int rnd = new Random().nextInt(possibleTypes.size());
			    cell.setId(possibleTypes.get(rnd));
			}
		}

	}
	
	public ArrayList<Cell> getCellNeighbours(int x, int y)
	{
		ArrayList<Cell> result = new ArrayList<Cell>();
		
		for(int i = y-1; i < y+2; i++)
		{
			for(int j = x-1; j < x+2; j++)
			{
				if(i >= 0 && i < oldCellGrid.length && j >= 0 && j < oldCellGrid[0].length && (j != x || i != y))
					result.add(oldCellGrid[i][j]);
			}
		}
		
		return result;
	}
	
	public void applySettingsChange(HashMap<Integer,CellType> newCellTypes)
	{
		cellTypes.clear();
		
		for(Integer i : newCellTypes.keySet())
		{
			cellTypes.put(new Integer(i), new CellType(newCellTypes.get(i)));
		}
	}
	
	public void setFileChooser()
	{
		fileChooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Game of Life Files", "gol");
		fileChooser.setFileFilter(filter);
		
		String currentPath = Paths.get(".").toAbsolutePath().normalize().toString();
		fileChooser.setCurrentDirectory(new File(currentPath));
	}
	
	public void writeToFile(File file)
	{
		this.saveFile = file;
		writeToFile();
	}
	
	public void writeToFile()
	{
		try
		{
			FileOutputStream fos = new FileOutputStream(saveFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeInt(Settings.controlSequence); // control sequence
			oos.writeInt(Settings.cellsInColumn);
			oos.writeInt(Settings.cellsInRow);
			oos.writeInt(currentIteration);
			
			// cell types
			oos.writeInt(cellTypes.size());
			for(Integer ct : cellTypes.keySet()) // id, maxNeighTotal, color, array data
			{
				CellType currentType = cellTypes.get(ct);
				
				oos.writeInt(ct);
				oos.writeInt(currentType.maxNeighboursTotal);
				oos.writeObject(currentType.color);

				HashMap<Integer, Integer> minNeigh = currentType.getMinNeighboursToSurvive();
				HashMap<Integer, Integer> maxNeigh = currentType.getMaxNeighboursToSurvive();
				HashMap<Integer, Integer> neighToStart = currentType.getNeighboursToStartLife();
				
				oos.writeInt(minNeigh.size());
				for(Integer i : minNeigh.keySet())
				{
					oos.writeInt(i);
					oos.writeInt(minNeigh.get(i));
					oos.writeInt(maxNeigh.get(i));
					oos.writeInt(neighToStart.get(i));
				}
			}
			
			// cell grid
			for(int i = 0; i < cellGrid.length; ++i) // id
			{
				for(int j = 0; j < cellGrid[i].length; ++j)
				{
					oos.writeInt(cellGrid[i][j].getId());
				}
			}
			
			oos.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/*
	 * @returns 0 - success, 1 - error, 2 - wrong file
	 */
	public int loadFromFile(File file)
	{
		int result = 0;
		this.saveFile = file;

		try
		{
			FileInputStream fis = new FileInputStream(saveFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			// check control sequence
			if(ois.readInt() != Settings.controlSequence)
			{
				ois.close();
				return 2;
			}
			Settings.cellsInColumn = ois.readInt();
			Settings.cellsInRow = ois.readInt();
			currentIteration = ois.readInt();
			
			// cell types
			int cellTypeLength = ois.readInt();
			cellTypes.clear();
			for(int i = 0; i < cellTypeLength; ++i) // id, length, byte data
			{
				CellType cellType;
				int id = ois.readInt();
				int maxNeighTotal = ois.readInt();
				Color color = (Color)ois.readObject();
				
				cellType = new CellType(id, maxNeighTotal, color);
				
				int neighLen = ois.readInt();
				for(int neigh = 0; neigh < neighLen; ++neigh)
				{
					int neighId = ois.readInt();
					int minNeigh = ois.readInt();
					int maxNeigh = ois.readInt();
					int neighToStart = ois.readInt();
					cellType.setCellTypeNeighbourData(neighId, minNeigh, maxNeigh, neighToStart);
				}
				
				cellTypes.put(id, cellType);
			}
			
			// cell grid
			this.cellGrid = new Cell[Settings.cellsInColumn][Settings.cellsInRow];
			for(int i = 0; i < cellGrid.length; ++i)
			{
				for(int j = 0; j < cellGrid[i].length; ++j)
				{
					cellGrid[i][j] = new Cell(j, i, ois.readInt());
				}
			}
			
			ois.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return result;
	}
}
