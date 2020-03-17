package gol.view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import gol.model.CellType;

public class NeighboursWindow extends JDialog {

	private CellType cellType;
	
	public NeighboursWindow(Frame owner, String title, boolean modal, CellType cellType)
	{
		super(owner, title, modal);
		this.cellType = cellType;
		initialize();
	}

	private void initialize()
	{
		Container contentPane = this.getContentPane();
		
		this.setBounds(100, 100, 800, 600);
		this.setResizable(false);
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.LINE_AXIS));
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.LIGHT_GRAY);
		JPanel scrollPaneContents = new JPanel();
		
		scrollPane.setViewportView(scrollPaneContents);
		
		setVisible(true);
	}
}
