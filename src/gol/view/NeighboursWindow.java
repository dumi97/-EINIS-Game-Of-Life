package gol.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import gol.model.CellType;


public class NeighboursWindow extends JDialog {

	@SuppressWarnings("unused")
	private CellType cellTypeRef;
	private CellType cellType;
	private JPanel scrollPaneContents;
	
	public NeighboursWindow(Frame owner, String title, boolean modal, CellType cellType)
	{
		super(owner, title, modal);
		this.cellTypeRef = cellType;
		this.cellType = new CellType(cellType);
		initialize();
	}

	private void initialize()
	{
		Container contentPane = this.getContentPane();
		
		// main frame
		this.setBounds(100, 100, 430, 600);
		this.setResizable(false);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(new BorderLayout());
		((JComponent) contentPane).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// scroll pane
		JScrollPane scrollPane = new JScrollPane();
		scrollPaneContents = new JPanel();
		scrollPaneContents.setBackground(Color.LIGHT_GRAY);
		scrollPaneContents.setLayout(new BoxLayout(scrollPaneContents, BoxLayout.Y_AXIS));
		
		buildNeighboursData();
		
		scrollPane.setViewportView(scrollPaneContents);
		
		
		// new/apply/cancel buttons
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setLayout(new BorderLayout());
		JButton btnNew = new JButton("New");
		JPanel exitButtonsPanel = new JPanel();
		exitButtonsPanel.setLayout(new FlowLayout());
		exitButtonsPanel.setBackground(Color.DARK_GRAY);
		JButton btnApply = new JButton("Apply");
		JButton btnCancel = new JButton("Cancel");
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
			JPanel mainPanel = new JPanel();
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
			JFormattedTextField textFieldMinNeigh = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
			textFieldMinNeigh.setValue(new Integer(cellType.getMinNeighboursToSurvive().get(i)));
			textFieldMinNeigh.setHorizontalAlignment(SwingConstants.CENTER);
			textFieldMinNeigh.setPreferredSize(new Dimension(40, textFieldMinNeigh.getMinimumSize().height+5));
			minNeighPanel.add(lblMinNeigh);
			minNeighPanel.add(Box.createRigidArea(new Dimension(5, 0)));
			minNeighPanel.add(textFieldMinNeigh);
			// maxNeighboursToSurvive
			JPanel maxNeighPanel = new JPanel();
			maxNeighPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel lblMaxnNeigh = new JLabel("Maximum neighbours to survive");
			JFormattedTextField textFieldMaxNeigh = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
			textFieldMaxNeigh.setValue(new Integer(cellType.getMaxNeighboursToSurvive().get(i)));
			textFieldMaxNeigh.setHorizontalAlignment(SwingConstants.CENTER);
			textFieldMaxNeigh.setPreferredSize(new Dimension(40, textFieldMaxNeigh.getMinimumSize().height+5));
			maxNeighPanel.add(lblMaxnNeigh);
			maxNeighPanel.add(Box.createRigidArea(new Dimension(5, 0)));
			maxNeighPanel.add(textFieldMaxNeigh);
			// neighboursToStartLife
			JPanel neighToStartPanel = new JPanel();
			neighToStartPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
			JLabel lblNeighToStart = new JLabel("Neighbours to start life");
			JFormattedTextField textFieldNeighToStart = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
			textFieldNeighToStart.setValue(new Integer(cellType.getNeighboursToStartLife().get(i)));
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
			JLabel lblId = new JLabel("ID");
			JFormattedTextField textFieldId = new JFormattedTextField(NumberFormat.INTEGER_FIELD);
			textFieldId.setValue(new Integer(i));
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
			
			scrollPaneContents.add(mainPanel);
			scrollPaneContents.repaint();
			scrollPaneContents.revalidate();
		}
	}
}
