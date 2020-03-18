package gol.view;

import java.awt.Graphics;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.util.HashMap;
import java.awt.Graphics2D;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;

import gol.controller.Controller;
import gol.model.Cell;
import gol.model.CellType;
import gol.model.Model;
import gol.model.Settings;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.JLayeredPane;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import java.awt.SystemColor;

public class View {

	private Controller controller;
	
	private JFrame frmGameOfLife;
	private JPanel cellPanel;
	@SuppressWarnings("unused")
	private MouseListener mouseListener;
	
	private Painter painter;
	
	private JLayeredPane layeredPane;
	private JPanel settingsPanel;
	private JPanel mainPanel;
	
	private JLabel lblCurrentIteration;
	
	private JFormattedTextField textFieldWidth;
	private JFormattedTextField textFieldHeight;
	private JPanel scrollPaneContents;
	
	private HashMap<Integer, CellType> tempCellTypes;
	private int tempWidth, tempHeight;
	

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
		frmGameOfLife.getContentPane().setBackground(Color.DARK_GRAY);
		frmGameOfLife.setTitle("Game of Life");
		frmGameOfLife.setBounds(100, 100, 800, 600);
		frmGameOfLife.setMinimumSize(new Dimension(800, 600));
		frmGameOfLife.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		layeredPane = new JLayeredPane();
		layeredPane.setBackground(Color.DARK_GRAY);
		GroupLayout groupLayout = new GroupLayout(frmGameOfLife.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 785, Short.MAX_VALUE)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addComponent(layeredPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 562, Short.MAX_VALUE)
		);
		
		mainPanel = new JPanel();
		mainPanel.setBackground(Color.DARK_GRAY);
		layeredPane.setLayout(new CardLayout(0, 0));
		
		//cellPanel = new JPanel();
		cellPanel = painter;
		cellPanel.setBackground(Color.LIGHT_GRAY);
		cellPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		
		JButton btnClear = new JButton("Clear");
		
		JLabel lblSimulationSpeed = new JLabel("Simulation Speed");
		lblSimulationSpeed.setForeground(Color.WHITE);
		
		JSlider sldSpeed = new JSlider();
		sldSpeed.setBackground(Color.DARK_GRAY);
		sldSpeed.setValue(500);
		sldSpeed.setMaximum(1500);
		
		JButton btnStop = new JButton("Stop");
		btnStop.setEnabled(false);
		
		JButton btnAuto = new JButton("Auto");
		
		JButton btnStep = new JButton("Step");
		
		JLabel lblCurrentIterationName = new JLabel("Current iteration: ");
		lblCurrentIterationName.setForeground(Color.WHITE);
		
		lblCurrentIteration = new JLabel("0");
		lblCurrentIteration.setForeground(Color.WHITE);
		lblCurrentIteration.setAlignmentY(Component.TOP_ALIGNMENT);
		lblCurrentIteration.setFont(new Font("Tahoma", Font.BOLD, 12));
		
		JButton btnSettings = new JButton("Settings");
		
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(cellPanel, GroupLayout.DEFAULT_SIZE, 764, Short.MAX_VALUE)
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(btnClear)
							.addPreferredGap(ComponentPlacement.RELATED, 241, Short.MAX_VALUE)
							.addComponent(lblSimulationSpeed)
							.addGap(5)
							.addComponent(sldSpeed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(5)
							.addComponent(btnStop)
							.addGap(5)
							.addComponent(btnAuto)
							.addGap(5)
							.addComponent(btnStep))
						.addGroup(gl_mainPanel.createSequentialGroup()
							.addComponent(lblCurrentIterationName)
							.addGap(5)
							.addComponent(lblCurrentIteration, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnSettings))
					.addContainerGap())
		);
		gl_mainPanel.setVerticalGroup(
			gl_mainPanel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_mainPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnSettings)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnClear)
						.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
							.addComponent(sldSpeed, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnStop)
							.addComponent(btnAuto)
							.addComponent(btnStep)
							.addGroup(gl_mainPanel.createSequentialGroup()
								.addGap(4)
								.addComponent(lblSimulationSpeed))))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(lblCurrentIterationName)
						.addComponent(lblCurrentIteration))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cellPanel, GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE)
					.addContainerGap())
		);
		mainPanel.setLayout(gl_mainPanel);
		layeredPane.add(mainPanel, "name_6094827274475");
		
		settingsPanel = new JPanel();
		settingsPanel.setBackground(Color.DARK_GRAY);
		layeredPane.add(settingsPanel, "name_6099088074100");
		
		JButton btnCancel = new JButton("Cancel");
		
		JButton btnApply = new JButton("Apply");
		
		JLabel lblSettings = new JLabel("Settings");
		lblSettings.setHorizontalAlignment(SwingConstants.CENTER);
		lblSettings.setForeground(Color.WHITE);
		lblSettings.setFont(new Font("Tahoma", Font.PLAIN, 32));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.LIGHT_GRAY);
		
		JLabel lblCellTypes = new JLabel("Cell Types");
		lblCellTypes.setHorizontalAlignment(SwingConstants.CENTER);
		lblCellTypes.setForeground(Color.WHITE);
		lblCellTypes.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		JPanel resolutionPane = new JPanel();
		resolutionPane.setBackground(Color.DARK_GRAY);
		resolutionPane.setBorder(null);
		
		JButton btnNew = new JButton("New");
		GroupLayout gl_settingsPanel = new GroupLayout(settingsPanel);
		gl_settingsPanel.setHorizontalGroup(
			gl_settingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNew, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(679, Short.MAX_VALUE))
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addGap(345)
					.addComponent(lblSettings, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(346))
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addGap(118)
					.addComponent(resolutionPane, GroupLayout.DEFAULT_SIZE, 569, Short.MAX_VALUE)
					.addGap(118))
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addGap(351)
					.addComponent(lblCellTypes, GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
					.addGap(352))
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addGroup(gl_settingsPanel.createParallelGroup(Alignment.TRAILING)
						.addGroup(Alignment.LEADING, gl_settingsPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
						.addGroup(gl_settingsPanel.createSequentialGroup()
							.addContainerGap(566, Short.MAX_VALUE)
							.addComponent(btnApply, GroupLayout.PREFERRED_SIZE, 96, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)))
					.addGap(31))
		);
		gl_settingsPanel.setVerticalGroup(
			gl_settingsPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_settingsPanel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblSettings)
					.addGap(22)
					.addComponent(resolutionPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(11)
					.addComponent(lblCellTypes, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_settingsPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_settingsPanel.createSequentialGroup()
							.addGap(32)
							.addGroup(gl_settingsPanel.createParallelGroup(Alignment.BASELINE)
								.addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnApply, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_settingsPanel.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNew)))
					.addContainerGap())
		);
		
		scrollPaneContents = new JPanel();
		scrollPaneContents.setBackground(Color.LIGHT_GRAY);
		scrollPane.setViewportView(scrollPaneContents);
		scrollPaneContents.setLayout(new BoxLayout(scrollPaneContents, BoxLayout.Y_AXIS));
		resolutionPane.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel lblWidth = new JLabel("Grid Width");
		lblWidth.setForeground(Color.WHITE);
		resolutionPane.add(lblWidth);
		lblWidth.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		textFieldWidth = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		textFieldWidth.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldWidth.setBackground(SystemColor.menu);
		resolutionPane.add(textFieldWidth);
		textFieldWidth.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textFieldWidth.setColumns(10);
		
		Component rigidArea = Box.createRigidArea(new Dimension(20, 20));
		resolutionPane.add(rigidArea);
		
		JLabel lblHeight = new JLabel("Grid Height");
		lblHeight.setForeground(Color.WHITE);
		resolutionPane.add(lblHeight);
		lblHeight.setFont(new Font("Tahoma", Font.PLAIN, 18));
		
		textFieldHeight = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		textFieldHeight.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldHeight.setBackground(SystemColor.menu);
		resolutionPane.add(textFieldHeight);
		textFieldHeight.setFont(new Font("Tahoma", Font.PLAIN, 18));
		textFieldHeight.setColumns(10);
		settingsPanel.setLayout(gl_settingsPanel);
		
		// step button action listener
		btnStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.makeStep();
			}});
		// auto button action listener
		btnAuto.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				btnAuto.setEnabled(false);
				btnStop.setEnabled(true);
				
				btnStep.setEnabled(false);
				btnClear.setEnabled(false);
				btnSettings.setEnabled(false);
				controller.auto();
			}});
		// stop button action listener
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				btnAuto.setEnabled(true);
				btnStop.setEnabled(false);
				
				btnStep.setEnabled(true);
				btnClear.setEnabled(true);
				btnSettings.setEnabled(true);
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
		// clear button action listener
		btnClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				controller.clear();
			}});
		// settings button action listener
		btnSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Settings.setIdRollback();
				buildSettings();
				switchLayerdPane(1);
			}
		});
		// new cell type settings button action listener
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(tempCellTypes == null)
					return;
				
				addTempCellType();
				buildCellTypesWindow();
			}
		});
		// apply settings button action listener
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeSettings(true);
			}
		});
		// cancel settings button action listener
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeSettings(false);
			}
		});
	
		frmGameOfLife.getContentPane().setLayout(groupLayout);
	}
	
	private void closeSettings(boolean applyChanges)
	{
		if(applyChanges)
		{
			int newWidth = (int)textFieldWidth.getValue();
			int newHeight = (int)textFieldHeight.getValue();
			if(newWidth <= 0)
				newWidth = 1;
			if(newHeight <= 0)
				newHeight = 1;
			Settings.cellsInRow = newWidth > Settings.maxInRow ? Settings.maxInRow : newWidth;
			Settings.cellsInColumn = newHeight > Settings.maxInColumn ? Settings.maxInColumn : newHeight;
			controller.applySettingChange(tempCellTypes);
		}
		else
		{
			Settings.RollbackId();
		}
		
		switchLayerdPane(0);
		tempCellTypes = null;
	}
	
	public void switchLayerdPane(int panelNumber) //0-main, 1-settings
	{
		layeredPane.removeAll();
		
		switch(panelNumber)
		{
		case 0:
			layeredPane.add(mainPanel);
			break;
		case 1:
			layeredPane.add(settingsPanel);
			break;
		default:
			layeredPane.add(mainPanel);
			break;
		}
		
		layeredPane.repaint();
		layeredPane.revalidate();
	}
	
	public void buildSettings()
	{
		tempCellTypes = new HashMap<Integer, CellType>();
		tempWidth = new Integer(Settings.cellsInRow);
		tempHeight = new Integer(Settings.cellsInColumn);
		
		// set width and height fields
		textFieldWidth.setValue(new Integer(tempWidth));
		textFieldHeight.setValue(new Integer(tempHeight));
		
		// deep copy cell types and build panels
		for(Integer i : Model.cellTypes.keySet())
		{
			CellType currentCellType = Model.cellTypes.get(i);
			tempCellTypes.put(new Integer(i), new CellType(currentCellType));
		}
		
		buildCellTypesWindow();
	}
	
	public void buildCellTypesWindow()
	{
		if(tempCellTypes == null)
			return;
		
		scrollPaneContents.removeAll();
		
		// build cell types
		for(Integer i : tempCellTypes.keySet())
		{
			CellType currentCellType = tempCellTypes.get(i);

			JPanel cellPanel = new JPanel();
			cellPanel.setBackground(Color.LIGHT_GRAY);
			cellPanel.setLayout(new BoxLayout(cellPanel, BoxLayout.LINE_AXIS));
			cellPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			cellPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
			
			// panel innards
			// id
			JLabel lblId = new JLabel();
			lblId.setText("ID");
			cellPanel.add(lblId);
			cellPanel.add(Box.createRigidArea(new Dimension(10,0)));
			JTextField textFieldId = new JTextField();
			textFieldId.setEnabled(false);
			textFieldId.setDisabledTextColor(Color.BLACK);
			textFieldId.setText(Integer.toString(currentCellType.id));
			textFieldId.setHorizontalAlignment(SwingConstants.CENTER);
			textFieldId.setMaximumSize(new Dimension(18000, textFieldId.getMinimumSize().height+5));
			cellPanel.add(textFieldId);
			
			cellPanel.add(Box.createRigidArea(new Dimension(20,0)));
			cellPanel.add(Box.createHorizontalGlue());
			
			// max neighbours
			JLabel lblMaxNeigh = new JLabel();
			lblMaxNeigh.setText("Max Total Neighbours");
			cellPanel.add(lblMaxNeigh);
			cellPanel.add(Box.createRigidArea(new Dimension(10,0)));
			JFormattedTextField textFieldMaxNeigh = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
			textFieldMaxNeigh.setValue(new Integer(currentCellType.maxNeighboursTotal));
			textFieldMaxNeigh.setHorizontalAlignment(SwingConstants.CENTER);
			textFieldMaxNeigh.setMaximumSize(new Dimension(18000, textFieldId.getMinimumSize().height+5));
			textFieldMaxNeigh.addPropertyChangeListener("value", new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt)
				{
					System.out.println((int)textFieldMaxNeigh.getValue());
					tempCellTypes.get(i).maxNeighboursTotal = (int)textFieldMaxNeigh.getValue();
				}
			});
			cellPanel.add(textFieldMaxNeigh);
			
			cellPanel.add(Box.createRigidArea(new Dimension(20,0)));
			cellPanel.add(Box.createHorizontalGlue());
			
			// edit neighbours
			JButton btnEditNeighbours = new JButton();
			btnEditNeighbours.setText("Edit Neighbour Interactions");
			cellPanel.add(btnEditNeighbours);
			btnEditNeighbours.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e)
				{
					@SuppressWarnings("unused")
					NeighboursWindow nw = new NeighboursWindow(frmGameOfLife, "Neighbour properties", true, currentCellType);
				}
			});
			
			cellPanel.add(Box.createRigidArea(new Dimension(20,0)));
			cellPanel.add(Box.createHorizontalGlue());
			
			// color
			JLabel lblColor = new JLabel();
			lblColor.setText("Color");
			cellPanel.add(lblColor);
			cellPanel.add(Box.createRigidArea(new Dimension(10,0)));
			JPanel colorBox = new JPanel();
			colorBox.setMinimumSize(new Dimension(60, 60));
			colorBox.setMaximumSize(new Dimension(30000, 50));
			colorBox.setBackground(new Color(currentCellType.color.getRGB()));
			cellPanel.add(colorBox);
			colorBox.addMouseListener(new MouseAdapter() {
				public void mouseClicked (MouseEvent e) 
				{
					Color c = JColorChooser.showDialog(null, "Select Cell Color", colorBox.getBackground());
					if(c != null)
					{
						colorBox.setBackground(c);
						tempCellTypes.get(i).color = c;
					}
				}
			});
			colorBox.setLayout(new BorderLayout());
			JLabel lblEdit = new JLabel();
			lblEdit.setHorizontalAlignment(SwingConstants.CENTER);
			lblEdit.setVerticalAlignment(SwingConstants.CENTER);
			lblEdit.setText("Edit");
			colorBox.add(lblEdit, BorderLayout.CENTER);
			
			cellPanel.add(Box.createRigidArea(new Dimension(20,0)));
			cellPanel.add(Box.createHorizontalGlue());
			
			// delete this cell type
			JButton btnDelete = new JButton();
			btnDelete.setText("X");
			cellPanel.add(btnDelete);
			btnDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					tempCellTypes.remove(i);
					buildCellTypesWindow();
				}
			});
			
			scrollPaneContents.add(cellPanel);
			
		}
		
		scrollPaneContents.repaint();
		scrollPaneContents.revalidate();
	}
	
	public JPanel getCellPanel()
	{
		return this.cellPanel;
	}
	
	public void addTempCellType()
	{
		int id = Settings.getNextCellId();
		tempCellTypes.put(id, new CellType(id, 6, 2, 3, 3, new Color((int)(Math.random() * 0x1000000))));
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
