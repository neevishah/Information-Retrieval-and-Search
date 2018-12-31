package test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.text.*;

import index.DocScore;
import index.Index;
import io.StaticDocSource;
import soln.index.InvertedIndex;
import soln.io.FileDocSource;
import soln.score.TFIDFScoringFun;
import soln.tokenizer.IndexingTokenizer;

/** You are *not* required to understand this code other than how to modify the main() method
 *  with different configurations of the search engine.  This class can help you debug since
 *  it visualizes the search results.
 * 
 * @author ssanner@mie.utoronto.ca
 *
 */
public class TestSearchGUI {

	public static final boolean DEBUG = true;
	
	public static final float LINE_SPACING = 1.0f;
	public static final int   FONT_SIZE = 14;
	public static final int   DEFAULT_HITS_PER_DOC = 20;
	public static final int   DEFAULT_CONTEXT_SIZE = 100;
	
	protected Index index = null;
	
	protected JFrame f = null;
	protected JTextField query = null;
	protected JTextField hits_per_doc = null;
	protected JTextField context_size = null;
	protected int hits_per_doc_val = 0;
	protected int context_size_val = 0;
	protected JTable leftNav = null;
	protected RankedResultsModel results_model = null;
	
	protected JTextPane display = null;
	protected JScrollPane scrollLeftNav = null;
	protected JScrollPane scrollDisplay = null;
	
	protected StyledDocument sdoc = null;
	protected Style norm_style[] = new Style[4];
	protected Style highlight_style[] = new Style[2];
	
	protected String query_entered = null; 
	protected String last_query_identifier = null;
	
	public TestSearchGUI(Index s) {

		index = s;
		index.buildIndex();
		
		// Build structure of Window
		f = new JFrame();

		Container contentPane = f.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JPanel top_row = new JPanel();
		top_row.setLayout(new BorderLayout());
		JPanel icons = new JPanel();
		icons.setLayout(new FlowLayout());
		top_row.add(icons, BorderLayout.WEST);
		JPanel top_box = new JPanel();
		FlowLayout top_box_layout = new FlowLayout();
		top_box_layout.setAlignOnBaseline(false);
		top_box.setLayout(top_box_layout);
		
		top_box.add(new JLabel("Enter your query: "));
		query = new JTextField(60);
		top_box.add(query);

		top_box.add(new JLabel("  # matches per document: "));
		hits_per_doc = new JTextField(4);
		hits_per_doc.setText("" + DEFAULT_HITS_PER_DOC);
		top_box.add(hits_per_doc);
		
		top_box.add(new JLabel("  context size: "));
		context_size = new JTextField(4);
		context_size.setText("" + DEFAULT_CONTEXT_SIZE);
		top_box.add(context_size);

		Dimension top_box_dim = new Dimension(300, 46);
		top_box.setPreferredSize(top_box_dim);
		top_box.setMinimumSize(top_box_dim);
		top_box.setMaximumSize(top_box_dim);
		top_row.add(top_box, BorderLayout.CENTER);
		
		display = new JTextPane();
		//50, 1000
		
		results_model = new RankedResultsModel();
		leftNav = new JTable(results_model);
		leftNav.setPreferredScrollableViewportSize(new Dimension(500, 70));
		leftNav.setFillsViewportHeight(true);
		 		
		leftNav.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = leftNav.getSelectionModel();
		rowSM.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				// Ignore extra messages.
				if (e.getValueIsAdjusting())
					return;

				ListSelectionModel lsm = (ListSelectionModel) e.getSource();
				if (lsm.isSelectionEmpty()) {
					System.out.println("No rows are selected.");
				} else {
					int selectedRow = lsm.getMinSelectionIndex();
					displayRow(selectedRow);
					//System.out.println("Row " + selectedRow + " is now selected with content:\n" + 
					//		index.getDocSource().getDoc((Integer)results_model.data[selectedRow][2]));
				}
			}
		});
		
		Dimension d = new Dimension(300, 100);
		leftNav.setMinimumSize(d);
		scrollLeftNav = new JScrollPane(leftNav);
		scrollDisplay = new JScrollPane(display);

		Dimension d2 = new Dimension(800, 600);
		scrollDisplay.setPreferredSize(d2);

		DefaultActionListener def_act = new DefaultActionListener();
		query.addActionListener(def_act);
		hits_per_doc.addActionListener(def_act);
		context_size.addActionListener(def_act);

		contentPane.add(top_row, BorderLayout.NORTH);
		contentPane.add(scrollLeftNav, BorderLayout.WEST);
		contentPane.add(scrollDisplay, BorderLayout.CENTER);

		f.setTitle("MIE250 Search Engine");
		f.pack();
		f.setVisible(true);

		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				//System.out.println(query.getText());
				System.exit(0);
			}
		});
		
		// Set styles for use when display documents
		sdoc = (StyledDocument)display.getDocument();
        norm_style[0] = sdoc.addStyle("StyleName1", null);
        StyleConstants.setItalic(norm_style[0], false);
        StyleConstants.setBold(norm_style[0], false);
        StyleConstants.setFontFamily(norm_style[0], "SansSerif");
        StyleConstants.setFontSize(norm_style[0], FONT_SIZE);
        StyleConstants.setBackground(norm_style[0], Color.white);
        StyleConstants.setForeground(norm_style[0], Color.black);
        StyleConstants.setLineSpacing(norm_style[0], LINE_SPACING);
        StyleConstants.setAlignment(norm_style[0], StyleConstants.ALIGN_LEFT);
        
        highlight_style[0] = sdoc.addStyle("StyleName2", null);
        StyleConstants.setItalic(highlight_style[0], true);
        StyleConstants.setBold(highlight_style[0], false);
        StyleConstants.setFontFamily(highlight_style[0], "SansSerif");
        StyleConstants.setFontSize(highlight_style[0], FONT_SIZE);
        StyleConstants.setBackground(highlight_style[0], color("B4D671"));
        StyleConstants.setForeground(highlight_style[0], Color.black);
        StyleConstants.setLineSpacing(highlight_style[0], LINE_SPACING);
        StyleConstants.setAlignment(highlight_style[0], StyleConstants.ALIGN_LEFT);

        norm_style[1] = sdoc.addStyle("StyleName3", null);
        StyleConstants.setItalic(norm_style[1], false);
        StyleConstants.setBold(norm_style[1], false);
        StyleConstants.setFontFamily(norm_style[1], "SansSerif");
        StyleConstants.setFontSize(norm_style[1], FONT_SIZE);
        StyleConstants.setBackground(norm_style[1], color("E1E1E1"));
        StyleConstants.setForeground(norm_style[1], Color.black);
        StyleConstants.setLineSpacing(norm_style[1], LINE_SPACING);
        StyleConstants.setAlignment(norm_style[1], StyleConstants.ALIGN_LEFT);
       
        highlight_style[1] = sdoc.addStyle("StyleName4", null);
        StyleConstants.setItalic(highlight_style[1], true);
        StyleConstants.setBold(highlight_style[1], false);
        StyleConstants.setFontFamily(highlight_style[1], "SansSerif");
        StyleConstants.setFontSize(highlight_style[1], FONT_SIZE);
        StyleConstants.setBackground(highlight_style[1], color("C0ADE0"));
        StyleConstants.setForeground(highlight_style[1], Color.black);
        StyleConstants.setLineSpacing(highlight_style[1], LINE_SPACING);
        StyleConstants.setAlignment(highlight_style[1], StyleConstants.ALIGN_LEFT);

        norm_style[2] = sdoc.addStyle("StyleName5", null);
        StyleConstants.setItalic(norm_style[2], false);
        StyleConstants.setBold(norm_style[2], true);
        StyleConstants.setFontFamily(norm_style[2], "SansSerif");
        StyleConstants.setFontSize(norm_style[2], FONT_SIZE + 2);
        StyleConstants.setBackground(norm_style[2], Color.white);
        StyleConstants.setForeground(norm_style[2], Color.black);
        StyleConstants.setLineSpacing(norm_style[2], LINE_SPACING);
        StyleConstants.setAlignment(norm_style[2], StyleConstants.ALIGN_CENTER);

        norm_style[3] = sdoc.addStyle("StyleName5", null);
        StyleConstants.setItalic(norm_style[3], false);
        StyleConstants.setBold(norm_style[3], true);
        StyleConstants.setFontFamily(norm_style[3], "SansSerif");
        StyleConstants.setFontSize(norm_style[3], FONT_SIZE);
        StyleConstants.setBackground(norm_style[3], Color.white);
        StyleConstants.setForeground(norm_style[3], Color.black);
        StyleConstants.setLineSpacing(norm_style[3], LINE_SPACING);
        StyleConstants.setAlignment(norm_style[3], StyleConstants.ALIGN_CENTER);
	}
	
	public class DefaultActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			showDisplay();
		}
	}
	
	// Invoked to display highlighted document content
	protected void displayRow(int selectedRow) {
				
		// Build a regular expression from the query
		if (query_entered == null)
			return;
		
		ArrayList<String> tokens = index.getTokenizer().tokenize(query_entered);
		String content = index.getDocSource().getDoc((Integer)results_model.data[selectedRow][2]);

		StringBuilder regex = new StringBuilder();
		for (int i = 0; i < tokens.size(); i++)
			regex.append((i == 0 ? "(" : "|(\\W") + tokens.get(i) + "\\W)");
			//regex.append((i == 0 ? "(" : "|(") + tokens.get(i) + ")");
		
		display.setText("");
		Pattern p = Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE); // use * since need to ensure single letters still valid 
		Matcher m = p.matcher(content);
		int hit = 1;
		int style = 0;
		while (m.find()) {
			int start_context = m.start() - context_size_val;
			if (start_context < 0)
				start_context = 0;
			int end_context = m.end() + context_size_val;
			if (end_context > content.length())
				end_context = content.length();
			//System.out.println(content.substring(start_context, end_context));
			
			// Equivalently we could also say: ret.add(s.substring(m.start(), m.end()));
			try {
				style = style == 0 ? 1 : 0; // Alternate highlight styles
				sdoc.insertString(sdoc.getLength(), "hit=" + hit++ + " ", norm_style[3]);
				sdoc.insertString(sdoc.getLength(), content.substring(start_context, m.start()), norm_style[style]);
				sdoc.insertString(sdoc.getLength(), content.substring(m.start(), m.end()), highlight_style[style]);
				sdoc.insertString(sdoc.getLength(), content.substring(m.end(), end_context) + "\n", norm_style[style]);
				sdoc.insertString(sdoc.getLength(), "\n", norm_style[style]);
			} catch (BadLocationException e) {
				System.err.println(e);
				e.printStackTrace(System.err);
			}
			
			if (hit > hits_per_doc_val)
				break;
        }
	
	}

	public void showDisplay() {
		
		display.setText("");
		query_entered = query.getText();
		
		hits_per_doc_val = DEFAULT_HITS_PER_DOC;
		try {
			hits_per_doc_val = Integer.parseInt(hits_per_doc.getText());
		} catch (NumberFormatException e) {
			hits_per_doc.setText("" + DEFAULT_HITS_PER_DOC);
		}
		
		context_size_val = DEFAULT_CONTEXT_SIZE;
		try {
			context_size_val = Integer.parseInt(context_size.getText());
		} catch (NumberFormatException e) {
			context_size.setText("" + DEFAULT_HITS_PER_DOC);
		}
		context_size_val = Integer.parseInt(context_size.getText());
		
		ArrayList<DocScore> doc_scores = index.getSortedSearchResults(query_entered);
		
		results_model.setRows(doc_scores);
	}
		
	public Color color(String color) {
		int r = Integer.parseInt(color.substring(0,2), 16);
		int g = Integer.parseInt(color.substring(2,4), 16);
		int b = Integer.parseInt(color.substring(4,6), 16);
		return new Color(r,g,b);
	}

	// Specific implementation of the table to display ranked results
	protected class RankedResultsModel extends AbstractTableModel {
	    private String[] columnNames = {"Rank",
	                                    "Score",
	                                    "Doc ID",
	                                    "Content"};
	    private Object[][] data = { };
	
	    public int getColumnCount() {
	        return columnNames.length;
	    }
	
	    public int getRowCount() {
	        return data.length;
	    }
	
	    public String getColumnName(int col) {
	        return columnNames[col];
	    }
	
	    public Object getValueAt(int row, int col) {
	        return data[row][col];
	    }
	
	    /*
	     * Don't need to implement this method unless your table's
	     * editable.
	     */
	    public boolean isCellEditable(int row, int col) {
	    	return false;
	    }
	
	    public void setRows(ArrayList<DocScore> results) {
	    	
	    	data = new Object[results.size()][4];
	    	
	    	// rank, score, doc ID, content
	    	for (int rank = 0; rank < results.size(); rank++) {
	    		DocScore ds = results.get(rank);
	        	data[rank][0] = rank + 1;
	        	data[rank][1] = ds.getScore();
	        	data[rank][2] = ds.getDocID();
	        	String content = ds.getContent();
	        	if (content.length() > 50)
	        		content = content.substring(0, 50);
	        	data[rank][3] = content;
	        	fireTableCellUpdated(rank, 0);
	        	fireTableCellUpdated(rank, 1);
	        	fireTableCellUpdated(rank, 2);
	        	fireTableCellUpdated(rank, 3);
	    	}
	    }
	
	    private void printDebugData() {
	        int numRows = getRowCount();
	        int numCols = getColumnCount();
	
	        for (int i=0; i < numRows; i++) {
	            System.out.print("    row " + i + ":");
	            for (int j=0; j < numCols; j++) {
	                System.out.print("  " + data[i][j]);
	            }
	            System.out.println();
	        }
	        System.out.println("--------------------------");
	    }
	}

	public static void main(String args[]) {
		Index s = new InvertedIndex(new StaticDocSource(), new IndexingTokenizer(), new TFIDFScoringFun());
		TestSearchGUI t = new TestSearchGUI(s);
	}
}
