package gol.controller;
import gol.view.View;

import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import gol.model.CellType;
import gol.model.Model;
import gol.model.Settings;

public class Controller {
	
	private View view;
	private Model model;
	private Controller controller = this;
	private ClickListener clickListener = new ClickListener();
	
	// mouse holding properties
	private Timer mouseHoldTimer = new Timer();
	private MouseHeldTask mouseHeldTask;
	private MouseEvent lastClickEvent;
	private int cellWidth, cellHeight;
	private int xDif, yDif;
	
	// auto iteration properties
	private Timer autoTimer = new Timer();
	private int autoBase = 1501;
	private int autoSpeed;
	private AutoIterator autoIterator;
	
	private class AutoIterator extends TimerTask {
		@Override
		public void run()
		{
			model.makeIteration();
		}
	}
	
	private class MouseHeldTask extends TimerTask {
		@Override
	    public void run() {			

			int x = (MouseInfo.getPointerInfo().getLocation().x + xDif) / cellWidth;
			int y = (MouseInfo.getPointerInfo().getLocation().y + yDif) / cellHeight;
			
			if(view.setIdOnClick())
			{
				model.cellClickedSet(x, y, view.getLMBSetId());
			}
			else
			{
				if(SwingUtilities.isLeftMouseButton(lastClickEvent))
					model.cellClicked(x, y, 0);
				else if(SwingUtilities.isRightMouseButton(lastClickEvent))
					model.cellClicked(x, y, 1);
				else if(SwingUtilities.isMiddleMouseButton(lastClickEvent))
					model.cellClicked(x, y, 2);
			}
				
			updateGame();
	    }
	}
	
	private class ClickListener extends MouseAdapter
	{
		@Override
		public void mousePressed(MouseEvent e)
		{
			// auto task is running - ignore mouse clicks
			if(autoIterator != null)
				return;
			
			// mouse task already running
			if(mouseHeldTask != null)
				return;
			
			// calculate the x and y indexes for the clicked cell
			cellWidth = (int)Math.floor((double)view.getCellPanel().getWidth() / Settings.cellsInRow);
			cellHeight = (int)Math.floor((double)view.getCellPanel().getHeight() / Settings.cellsInColumn);
			
			if(cellWidth == 0 || cellHeight == 0)
				return;
			
			xDif = e.getX() - MouseInfo.getPointerInfo().getLocation().x;
			yDif = e.getY() - MouseInfo.getPointerInfo().getLocation().y;
			
			mouseHeldTask = new MouseHeldTask();
			lastClickEvent = e;
			model.clearClickedCells();
			mouseHoldTimer.scheduleAtFixedRate(mouseHeldTask, 0, 1);
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			// auto task is running - ignore mouse clicks
			if(autoIterator != null)
				return;
			
			// mouse task already terminated
			if(mouseHeldTask == null)
				return;
			
			mouseHeldTask.cancel();
			mouseHeldTask = null;
		}
	}
	
	public Controller()
	{
		this.model = new Model(controller);
		model.generateCells();
		
		EventQueue.invokeLater(new Runnable() {
			public void run()
			{
				try
				{
					view = new View(controller, clickListener, model.getCellGrid());
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	public void updateGame()
	{
		view.painterUpdateGame(model.getCellGrid(), model.getCurrentIteration());
		repaint();
	}

	private void repaint()
	{
		view.painterDraw();
	}
	
	public void clear()
	{
		model.generateCells();
		updateGame();
	}
	
	public void auto()
	{
		if(autoIterator != null)
			return;
		
		// if mouse task is active - kill it
		if(mouseHeldTask != null)
		{
			mouseHeldTask.cancel();
			mouseHeldTask = null;
		}
		
		
		autoIterator = new AutoIterator();
		autoTimer.scheduleAtFixedRate(autoIterator, 0, autoBase - autoSpeed);
	}
	
	public void stop()
	{
		if(autoIterator == null)
			return;
		
		autoIterator.cancel();
		autoIterator = null;
	}
	
	public void makeStep()
	{
		model.makeIteration();
	}
	
	public void updateSpeed(int newSpeed)
	{
		int oldSpeed = this.autoSpeed;
		this.autoSpeed = newSpeed;
		
		if(autoIterator != null)
		{
			autoIterator.cancel();
			autoIterator = new AutoIterator();
			autoTimer.scheduleAtFixedRate(autoIterator, autoBase - oldSpeed, autoBase - this.autoSpeed);
		}
	}
	
	public void applySettingChange(HashMap<Integer,CellType> newCellTypes, boolean mustReloadGrid)
	{
		if(newCellTypes == null)
			return;
		
		model.applySettingsChange(newCellTypes);
		if(mustReloadGrid)
			clear();
	}
	
	public void saveFile()
	{
		if(model.getSaveFile() == null)
			saveAsFile();
		else
		{
			model.writeToFile();
		}
	}
	
	public void saveAsFile()
	{
		if(model.getFileChooser() == null)
			model.setFileChooser();
		
		model.getFileChooser().setDialogTitle("Save File as...");
		
		int result = model.getFileChooser().showSaveDialog(view.getFrmGameOfLife());

		if(result == JFileChooser.APPROVE_OPTION)
		{
			File f = model.getFileChooser().getSelectedFile();
			String filePath = f.getAbsolutePath();
			if(!filePath.matches(".+\\.[a-zA-Z]*"))
			{
			    f = new File(filePath + ".gol");
			}
			model.writeToFile(f);
			view.updateFileName(f.getAbsolutePath());
		}
	}
	
	public void loadFile()
	{
		if(model.getFileChooser() == null)
			model.setFileChooser();
		
		model.getFileChooser().setDialogTitle("Load File");
		
		int result = model.getFileChooser().showOpenDialog(view.getFrmGameOfLife());

		if(result == JFileChooser.APPROVE_OPTION)
		{
			File f = model.getFileChooser().getSelectedFile();
			result = model.loadFromFile(f);
			if(result == 0)
			{
				view.updateFileName(f.getAbsolutePath());
				updateGame();
			}
			else if(result == 1)
			{
				JOptionPane.showMessageDialog(view.getFrmGameOfLife(), "Error while loading file", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else if(result == 2)
			{
				JOptionPane.showMessageDialog(view.getFrmGameOfLife(), "Wrong or corrupted file", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
