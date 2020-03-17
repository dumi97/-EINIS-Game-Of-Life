package gol.view;

import java.awt.Graphics;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.Graphics2D;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import gol.controller.Controller;
import gol.model.Cell;
import gol.model.Settings;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSlider;

public class View {

	private Controller controller;
	
	private JFrame frmGameOfLife;
	private JPanel cellPanel;
	@SuppressWarnings("unused")
	private MouseListener mouseListener;
	
	private Painter painter;
	private JLabel lblCurrentIteration;
	private JButton btnAuto;
	private JButton btnStop;
	private JLabel lblSimulationSpeed;

	/**
	 * Create the application.
	 */
	public View(Controller controler, MouseListener mouseListener, Cell[][] cells)
	{
		this.controller = controler;
		this.mouseListener = mouseListener;
		painter = new Painter(mouseListener);
		painter.updateCellGrid(cells, 0);
		
		initialize();
		frmGameOfLife.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frmGameOfLife = new JFrame();
		frmGameOfLife.setTitle("Game of Life");
		frmGameOfLife.setBounds(100, 100, 800, 600);
		frmGameOfLife.setMinimumSize(new Dimension(800, 600));
		frmGameOfLife.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//cellPanel = new JPanel();
		cellPanel = painter;
		cellPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JButton btnStep = new JButton("Step");
		
		JButton btnClear = new JButton("Clear");
		
		JLabel lblCurrentIterationName = new JLabel("Current iteration: ");
		
		lblCurrentIteration = new JLabel("0");
		lblCurrentIteration.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		btnAuto = new JButton("Auto");
		
		btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		
		JSlider sldSpeed = new JSlider();
		sldSpeed.setValue(500);
		sldSpeed.setMaximum(1500);
		
		lblSimulationSpeed = new JLabel("Simulation Speed");
		GroupLayout groupLayout = new GroupLayout(frmGameOfLife.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(cellPanel, GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, 108, Short.MAX_VALUE)
									.addComponent(lblSimulationSpeed)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(sldSpeed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(7)
									.addComponent(btnStop, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnAuto, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnStep, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)))
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblCurrentIterationName)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblCurrentIteration)
							.addContainerGap(673, Short.MAX_VALUE))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(30)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnStep)
								.addComponent(btnClear)
								.addComponent(btnAuto)
								.addComponent(btnStop)
								.addComponent(lblSimulationSpeed))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblCurrentIterationName)
								.addComponent(lblCurrentIteration)))
						.addComponent(sldSpeed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(8)
					.addComponent(cellPanel, GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
					.addContainerGap())
		);
		frmGameOfLife.getContentPane().setLayout(groupLayout);
		
		// step button action listener
		btnStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.makeStep();
			}});
		// clear button action listener
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.clear();
			}});
		// auto button action listener
		btnAuto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				btnStop.setEnabled(true);
				btnAuto.setEnabled(false);
				btnStep.setEnabled(false);
				btnClear.setEnabled(false);
				controller.auto();
			}});
		// stop button action listener
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				btnStep.setEnabled(true);
				btnClear.setEnabled(true);
				btnAuto.setEnabled(true);
				btnStop.setEnabled(false);
				controller.stop();
			}});
		// speed slider action listener
		sldSpeed.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e)
			{
				controller.updateSpeed(sldSpeed.getValue());
			}
		});
	}
	
	public JPanel getCellPanel()
	{
		return this.cellPanel;
	}
	
	public void painterUpdateGame(Cell[][] cells, int currentIteration)
	{
		if(painter != null)
			painter.updateCellGrid(cells, currentIteration);
	}
	
	public void painterDraw()
	{
		if(painter != null)
			painter.repaint();
	}
	
	private void render(Graphics g, Cell[][] cells, int currentIteration)
	{
		updateIteration(currentIteration);
		drawGrid(g, cells);
	}
	
	private void updateIteration(int currentIteration)
	{
		lblCurrentIteration.setText(Integer.toString(currentIteration));
	}
	
	private void drawGrid(Graphics g, Cell[][] cells)
	{
		Graphics2D g2d = (Graphics2D)g;
		
		double cellWidth = cellPanel.getWidth() / Settings.cellsInRow;
		double cellHeight = cellPanel.getHeight() / Settings.cellsInColumn;

        int locationX, locationY;
        for (int i = 0; i < Settings.cellsInColumn; ++i)
        {
            locationY = (int)Math.round((double)i * cellHeight);
            for (int j = 0; j < Settings.cellsInRow; ++j)
            {
                locationX = (int)Math.round((double)j * cellWidth);
                g2d.setColor(cells[i][j].getColor());
                g2d.fillRect(locationX, locationY, (int)Math.round(cellWidth), (int)Math.round(cellHeight));
                g2d.setColor(Color.BLACK);
                g2d.drawRect(locationX, locationY, (int)Math.round(cellWidth), (int)Math.round(cellHeight));
            }
        }
	}
	
	private class Painter extends JPanel
	{
		private Cell[][] cells;
		private int currentIteration;
		
		public Painter(MouseListener listener)
		{
			setFocusable(true);
			requestFocus();
			setBackground(new Color(230, 230, 230));
			addMouseListener(listener);
		}
		
		public void updateCellGrid(Cell[][] cells, int currentIteration)
		{
			this.cells = cells;
			this.currentIteration = currentIteration;
		}
		
		@Override
		public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			render(g, cells, currentIteration);
		}
	}
}
