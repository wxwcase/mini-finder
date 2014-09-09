package com.wxwcase;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class DragHereIcon implements Icon {
    
	private int size = 80;
	private float a = 4f;
	private float b = 8f;
	private int r = 16;
	private int f = size/4;
	private Font font = new Font("Monospace", Font.PLAIN, size);
	private FontRenderContext frc = new FontRenderContext(null, true, true);
	private Shape s = new TextLayout("\u21E9", font, frc).getOutline(null);
	private Color linec = Color.GRAY;
    // Main frame and subframe components:
	private static final JFrame frame = new JFrame();
    private static JFrame subframe = null;
    private static Container subframeContentPane = null;
    private static JPanel contentPanel = null;
    private static String tags = "";
    private static ButtonGroup bg = null;
    private static JButton viewButton = null;
    private static JButton addButton = null;
    private static JScrollPane scrollPane = null;
    private static JTextArea textArea = null;
    private static JPanel viewPane = null;
    private static Object[] possibilities = {
    "Bioinfomatics", "Science", "Graph", "Algorithm"
    };
    
	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2 = (Graphics2D)g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
		g2.translate(x, y);
        
		g2.setStroke(new BasicStroke(a));
		g2.setPaint(linec);
		g2.draw(new RoundRectangle2D.Float(a,a,size-2*a-1,size-2*a-1,r,r));
        
		g2.setStroke(new BasicStroke(b));
		g2.setColor(UIManager.getColor("Panel.background"));
		g2.drawLine(1*f,0*f,1*f,4*f);
		g2.drawLine(2*f,0*f,2*f,4*f);
		g2.drawLine(3*f,0*f,3*f,4*f);
		g2.drawLine(0*f,1*f,4*f,1*f);
		g2.drawLine(0*f,2*f,4*f,2*f);
		g2.drawLine(0*f,3*f,4*f,3*f);
        
		g2.setPaint(linec);
		Rectangle2D b = s.getBounds();
		Point2D.Double p = new Point2D.Double(b.getX() + b.getWidth()/2d, b.getY() + b.getHeight()/2d);
		AffineTransform toCenterAT = AffineTransform.getTranslateInstance(size/2d - p.getX(), size/2d - p.getY());
		g2.fill(toCenterAT.createTransformedShape(s));
		g2.translate(-x,-y);
		g2.dispose();
	}
    
    @Override public int getIconWidth()  { return size; }
	@Override public int getIconHeight() { return size; }
	public static JComponent makeUI() {
        JLabel label = new JLabel(new DragHereIcon());
        label.setText("<html>Drag <strong>Files</strong> Here");
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setForeground(Color.GRAY);
        label.setFont(new Font("Monospace", Font.PLAIN, 22));
        JPanel p = new JPanel();
        p.add(label);
        p.setBorder(BorderFactory.createEmptyBorder(8,8,8,8));
	  	return p;
	}
	
	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
        System.out.println("Action done! ");
        //  MySQLDatabase mydb = new MySQLDatabase();
        DerbyDB mydb = new DerbyDB();
        //  delete some test data tables
        mydb.deleteTable("testtable");
        mydb.deleteTable("testtable2");
	}
	
	public static void createAndShowGUI() {
        // final JFrame f = new JFrame();
		JMenuBar mb = new JMenuBar();
		JMenu viewMenu = new JMenu("View");
		JMenu editMenu = new JMenu("Edit");
        JMenuItem undo = new JMenuItem("Undo");
        JMenuItem lastTag = new JMenuItem("Last tag added");
        editMenu.add(undo);
        viewMenu.add(lastTag);
		mb.add(viewMenu);
		mb.add(editMenu);
		frame.setJMenuBar(mb);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(DragHereIcon.makeUI());
		MyDragDropListener myDragDropListener = new MyDragDropListener();
	  	new DropTarget(frame, myDragDropListener);
		
		frame.setSize(280, 200);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	// show pop-up frame:
	public static void showPopup() {
        // Add a new Frame:
        createSubframe();
	}
    
    private static JFrame createSubframe() {
        subframe = new JFrame();
        subframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        subframe.setSize(400, 300);
        subframe.setLocationRelativeTo(frame);
        contentPanel = new JPanel();
        subframeContentPane = subframe.getContentPane();
        subframeContentPane.add(contentPanel);
        subframe.setVisible(true);
        viewButton = new JButton("View tag");
        addButton = new JButton("Add tag");
        contentPanel.add(viewButton);
        contentPanel.add(addButton);
        textArea = new JTextArea(10, 20);
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        textArea.append(tags);
        subframeContentPane.add(BorderLayout.SOUTH, contentPanel);
        subframeContentPane.add(BorderLayout.CENTER, scrollPane);
        viewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //                textArea.setText("");
                String str = JOptionPane.showInputDialog(subframe, "Tag name:", "Adding tag", JOptionPane.PLAIN_MESSAGE);
                if(str == null)
                	return;
                tags += str + "\n";
                textArea.setText(tags);
            }
        });
        
        return subframe;
    }
	
	// Need to implements another DnD listener to pop up a MessageDialog for tagging.
	public static class MyDragDropListener implements DropTargetListener {
	    @Override
	    public void drop(DropTargetDropEvent event) {
	        // Accept copy drops
	        event.acceptDrop(DnDConstants.ACTION_COPY);
	        // Get the transfer which can provide the dropped item data
	        Transferable transferable = event.getTransferable();
	        // Get the data formats of the dropped item
	        DataFlavor[] flavors = transferable.getTransferDataFlavors();
	        // Loop through the flavors
	        for (DataFlavor flavor : flavors) {
	            try {
	                // If the drop items are files
	                if (flavor.isFlavorJavaFileListType()) {
	                    // Get all of the dropped files
	                    List<File> files = (List) transferable.getTransferData(flavor);
	                    // Loop them through
	                    for (File file : files) {
	                        // Print out the file path, or get them into database:
	                    	System.out.println("Drag done!");
	                    	String resultStatement;
	                    	if(file.isDirectory()) {
	                    		resultStatement = "INSERT INTO testtable3 (isDir, fileName, filePath) VALUES('Y','"+ file.getName() + "','" + file.getPath() + "')";
                            } else {
	                    		resultStatement = "INSERT INTO testtable3 (isDir, fileName, filePath) VALUES('N','"+ file.getName() + "','" + file.getPath() + "')";
                            }
	                    	tags = "Avaliable tags:\n" + "File Name: " + file.getName() + "\nFile Path: " + file.getPath() + "\n";
	                    	System.out.println("result statement: " + resultStatement);
	                    	
	                    	// transfer data into MySQL database: 'test', table: 'testtable'
	                    	MySQLDatabase mSQLdb = new MySQLDatabase();
	                    	boolean isDataImported = mSQLdb.importData(resultStatement);
                            //	                    	boolean isDataImported = MySQLDatabase.importData(resultStatement);
	                    	System.out.println("Data imported: " + isDataImported);
	                    	if(file.isDirectory() && isDataImported)
	                    		System.out.println("    >>> Directory name: '" + file.getName() + "'.\n" +
                                                   "    >>> Directory path: '" + file.getPath() + "'.");
	                    	else if(!file.isDirectory() && isDataImported){
	                    		System.out.println("    >>> File name is: '" + file.getName() + "'.");
		                        System.out.println("    >>> File path is '" + file.getPath() + "'.");
	                    	} else {
	                    		System.out.println("Error importing data");
	                    	}
	                    }
	                }
	                // If the file already has a tag, show tag of that file
	                // else show message dialog to enter the tag of the file
	                showPopup();
	            } catch (Exception e) {
	                // Print out the error stack
	                e.printStackTrace();
	            }
	        }
	        // Inform that the drop is complete
	        event.dropComplete(true);
	    }
	    @Override
	    public void dragEnter(DropTargetDragEvent event) {}
	    @Override
	    public void dragExit(DropTargetEvent event) {
	    	// pop up a menu to get 'Tag' into a database table, better show the current tag attributes and
	    	// insert new entries
            //	    	showPopup();
	    }
	    @Override
	    public void dragOver(DropTargetDragEvent event) {}
	    @Override
	    public void dropActionChanged(DropTargetDragEvent event) {}
	}
}