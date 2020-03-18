package gol.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gol.model.CellType;


public class NeighboursWindow extends JDialog {

	private CellType cellType;
	private JPanel scrollPaneContents;
	private ArrayList<CellNeighbourData> cellNeighbourDataList;
	
	public NeighboursWindow(Frame owner, String title, boolean modal, CellType cellType)
	{
		super(owner, title, modal);
		this.cellType = cellType;
		initialize();
	}

	private void initialize()
	{
		Container contentPane = this.getContentPane();
		cellNeighbourDataList = new ArrayList<CellNeighbourData>();
		
		// main frame
		this.setBounds(100, 100, 430, 600);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(new BorderLayout());
		((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// scroll pane
		JScrollPane scrollPane = new JScrollPane();
		scrollPaneContents = new JPanel();
		scrollPaneContents.setBackground(Color.LIGHT_GRAY);
		scrollPaneContents.setLayout(new BoxLayout(scrollPaneContents, BoxLayout.Y_AXIS));
		scrollPane.setViewportView(scrollPaneContents);
		
		buildNeighboursData();
		
		// new/apply/cancel buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		JButton btnNew = new JButton("New");
		btnNew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				addNeighbourData();
			}
		});
		JPanel exitButtonsPanel = new JPanel();
		exitButtonsPanel.setLayout(new FlowLayout());
		exitButtonsPanel.setBackground(Color.DARK_GRAY);
		JButton btnApply = new JButton("Apply");
		btnApply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				closeNeighboursWindow(true);
			}
		});
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				closeNeighboursWindow(false);
			}
		});
		exitButtonsPanel.add(btnApply);
		exitButtonsPanel.add(Box.createRigidArea(new Dimension(20, 0)));
		exitButtonsPanel.add(btnCancel);
		buttonsPanel.add(btnNew, BorderLayout.NORTH);
		buttonsPanel.add(exitButtonsPanel, BorderLayout.SOUTH);

		
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(buttonsPanel, BorderLayout.SOUTH);
		setVisible(true);
	}
	
	private void buildNeighboursData()
	{
		// scroll pane contents elements
		for(Integer i : cellType.getMaxNeighboursToSurvive().keySet())
		{
			int minNeigh = cellType.getMinNeighboursToSurvive().get(i);
			int MinNeigh = cellType.getMaxNeighboursToSurvive().get(i);
			int neighToLive = cellType.getNeighboursToStartLife().get(i);

			cellNeighbourDataList.add(new CellNeighbourData(i, minNeigh, MinNeigh, neighToLive, scrollPaneContents, this));
		}
		scrollPaneContents.repaint();
		scrollPaneContents.revalidate();
	}
	
	private void addNeighbourData()
	{
		cellNeighbourDataList.add(new CellNeighbourData(-1, 2, 3, 3, scrollPaneContents, this));
		rebuildNeighboursData();
	}
	
	public void deleteNeighbourData(CellNeighbourData cnd)
	{
		cellNeighbourDataList.remove(cnd);
		rebuildNeighboursData();
	}
	
	private void rebuildNeighboursData()
	{
		scrollPaneContents.removeAll();
		for(CellNeighbourData cnd : cellNeighbourDataList)
			cnd.addSelfToParent();
		scrollPaneContents.repaint();
		scrollPaneContents.revalidate();
	}
	
	private void closeNeighboursWindow(boolean applyChanges)
	{
		if(applyChanges)
		{
			applyChanges();
		}
		
		this.dispose();
	}
	
	private void applyChanges()
	{
		// check for duplicates
		HashSet<Integer> duplicateCheck = new HashSet<Integer>();
		for(CellNeighbourData cnd : cellNeighbourDataList)
		{
			if(cnd.getId() < 0)
				continue;
			
			if(duplicateCheck.contains(cnd.getId()))
			{
				String message = "The neighbour data contains duplicate IDs."
						+ "\nAll subsequent occurances of duplicate IDs will be ignored."
						+ "\nContinue anyway?";
				
				int dialogResult = JOptionPane.showConfirmDialog (null, message,"Warning - Duplicate IDs", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if(dialogResult == JOptionPane.YES_OPTION)
				{
					break;
				}
				else
				{
					return;
				}
			}
			else
			{
				duplicateCheck.add(cnd.getId());
			}
		}
		
		duplicateCheck.clear();
		cellType.clearNeighbourData();
		for(CellNeighbourData cnd : cellNeighbourDataList)
		{
			if(cnd.getId() < 0 || duplicateCheck.contains(cnd.getId()))
				continue;
			duplicateCheck.add(cnd.getId());
			
			int minNeigh = cnd.getMinNeigh() >= 0 ? cnd.getMinNeigh() : 0;
			int maxNeigh = cnd.getMaxNeigh() >= 0 ? cnd.getMaxNeigh() : 0;
			int neighToStart = cnd.getNeighToStart() >= 0 ? cnd.getNeighToStart() : 0;
			
			cellType.setCellTypeNeighbourData(cnd.getId(), minNeigh, maxNeigh, neighToStart);
		}
	}
}
