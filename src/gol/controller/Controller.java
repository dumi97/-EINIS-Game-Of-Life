package gol.controller;
import gol.view.View;

import java.awt.EventQueue;
import java.awt.MouseInfo;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.SwingUtilities;

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
			
			if(SwingUtilities.isLeftMouseButton(lastClickEvent))
				model.cellClicked(x, y, true);
			else if(SwingUtilities.isRightMouseButton(lastClickEvent))
				model.cellClicked(x, y, false);
				
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
}
