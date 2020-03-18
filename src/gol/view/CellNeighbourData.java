package gol.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CellNeighbourData {

	private CellNeighbourData self = this;
	private JComponent parent;
	@SuppressWarnings("unused")
	private NeighboursWindow parentClass;
	
	private JFormattedTextField textFieldId;
	private JFormattedTextField textFieldMinNeigh;
	private JFormattedTextField textFieldMaxNeigh;
	private JFormattedTextField textFieldNeighToStart;
	
	private JComponent mainPanel;
	
	public CellNeighbourData(int neighId, int minNeigh, int maxNeigh, int neighToStart, JComponent parent, NeighboursWindow parentClass)
	{
		this.parent = parent;
		this.parentClass = parentClass;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		mainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 5, 0, 5);
		gbc.anchor = GridBagConstraints.CENTER;
		
		// minNeighboursToSurvive 
		JPanel minNeighPanel = new JPanel();
		minNeighPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMinNeigh = new JLabel("Minimum neighbours to survive");
		textFieldMinNeigh = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		textFieldMinNeigh.setValue(new Integer(minNeigh));
		textFieldMinNeigh.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldMinNeigh.setPreferredSize(new Dimension(40, textFieldMinNeigh.getMinimumSize().height+5));
		minNeighPanel.add(lblMinNeigh);
		minNeighPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		minNeighPanel.add(textFieldMinNeigh);
		// maxNeighboursToSurvive
		JPanel maxNeighPanel = new JPanel();
		maxNeighPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblMaxnNeigh = new JLabel("Maximum neighbours to survive");
		textFieldMaxNeigh = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		textFieldMaxNeigh.setValue(new Integer(maxNeigh));
		textFieldMaxNeigh.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldMaxNeigh.setPreferredSize(new Dimension(40, textFieldMaxNeigh.getMinimumSize().height+5));
		maxNeighPanel.add(lblMaxnNeigh);
		maxNeighPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		maxNeighPanel.add(textFieldMaxNeigh);
		// neighboursToStartLife
		JPanel neighToStartPanel = new JPanel();
		neighToStartPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		JLabel lblNeighToStart = new JLabel("Neighbours to start life");
		textFieldNeighToStart = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		textFieldNeighToStart.setValue(new Integer(neighToStart));
		textFieldNeighToStart.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldNeighToStart.setPreferredSize(new Dimension(40, textFieldNeighToStart.getMinimumSize().height+5));
		neighToStartPanel.add(lblNeighToStart);
		neighToStartPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		neighToStartPanel.add(textFieldNeighToStart);
		// all neighbour values together
		JPanel valuesPanel = new JPanel();
		//valuesPanel.setLayout(new BoxLayout(valuesPanel, BoxLayout.Y_AXIS));
		valuesPanel.setLayout(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 0;
		valuesPanel.add(minNeighPanel, gbc);
		gbc.gridy = 1;
		valuesPanel.add(maxNeighPanel, gbc);
		gbc.gridy = 2;
		valuesPanel.add(neighToStartPanel, gbc);
		
		// id and delete
		JPanel idPanel = new JPanel();
		idPanel.setLayout(new GridBagLayout());
		JButton btnDelete = new JButton("X");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{
				parentClass.deleteNeighbourData(self);
			}
		});
		JLabel lblId = new JLabel("ID");
		textFieldId = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
		textFieldId.setValue(new Integer(neighId));
		textFieldId.setPreferredSize(new Dimension(40, textFieldId.getMinimumSize().height+5));
		textFieldId.setHorizontalAlignment(SwingConstants.CENTER);
		gbc.gridx = 0;
		gbc.gridy = 0;
		idPanel.add(btnDelete, gbc);
		//idPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		gbc.gridx = 1;
		idPanel.add(lblId, gbc);
		//idPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		gbc.gridx = 2;
		idPanel.add(textFieldId, gbc);
		
		mainPanel.add(idPanel, BorderLayout.WEST);
		mainPanel.add(valuesPanel, BorderLayout.CENTER);
		
		addSelfToParent();
	}
	
	public void addSelfToParent()
	{
		parent.add(mainPanel);
	}
	
	public int getId()
	{
		return (int)textFieldId.getValue();
	}
	
	public int getMinNeigh()
	{
		return (int)textFieldMinNeigh.getValue();
	}
	
	public int getMaxNeigh()
	{
		return (int)textFieldMaxNeigh.getValue();
	}
	
	public int getNeighToStart()
	{
		return (int)textFieldNeighToStart.getValue();
	}
}
