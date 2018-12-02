import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class IMGui implements ActionListener {
	JTextField entryField;
	JList mainList;
	JTextArea consoleWindow;
	DefaultListModel mainListModel;
	JScrollPane consoleWindowScroll;
	JFileChooser fileChooser;
	JButton importBtn;
	XLSParser parser; //ApachePOI
	
	
	public void go() {
		parser=new XLSParser();
		
		JFrame frame=new JFrame("Inventarisation matcher");
		
		entryField=new JTextField();
		entryField.addActionListener(this);
		entryField.setActionCommand("entryField");
		entryField.requestFocusInWindow();
		
		mainListModel=new DefaultListModel();
		
		mainList=new JList(mainListModel);
		mainList.setLayoutOrientation(JList.VERTICAL);
		JScrollPane mainListScroll=new JScrollPane(mainList);
		consoleWindow=new JTextArea();
		consoleWindowScroll=new JScrollPane(consoleWindow);
		consoleWindowScroll.setPreferredSize(new Dimension(0,100));
		fileChooser=new JFileChooser();
		importBtn=new JButton("Import");
		importBtn.addActionListener(this);
		
		frame.getContentPane().add(BorderLayout.NORTH, entryField);
		frame.getContentPane().add(BorderLayout.CENTER, mainListScroll);
		frame.getContentPane().add(BorderLayout.SOUTH, consoleWindowScroll);
		frame.getContentPane().add(BorderLayout.EAST, importBtn);
		frame.setSize(400, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		consoleWindow.append("Application loading successfull.\n");
	}
	
	public void actionPerformed(ActionEvent evt) {
		
		if(evt.getActionCommand().contains("entryField")) {
			String entryData=entryField.getText();
			boolean isInList=false;
			isInList=mainListModel.removeElement(entryData);
			if(!isInList) {
				consoleWindow.append(entryData+" not found.\n");
				consoleWindow.setCaretPosition(consoleWindow.getDocument().getLength());
			}
			else {
				consoleWindow.append(entryData+" found and removed.\n");
				consoleWindow.setCaretPosition(consoleWindow.getDocument().getLength());
			}
			entryField.setText("");
		}
		
		else if(evt.getSource()==importBtn) {
			int openResult=fileChooser.showOpenDialog(entryField);
			if(openResult==JFileChooser.APPROVE_OPTION) {
				
				File file=fileChooser.getSelectedFile();
				String ext=parser.getExtension(file);
				
				if(ext.contains("xls")) {
					ArrayList<String> data=new ArrayList<String>();
					
					consoleWindow.append("Opening file "+file.getName()+"...\n");
					mainListModel.clear();
					
					try {
						data=parser.parseSim(file);
					}
					catch(Exception e) {
						e.printStackTrace();
					}
					
					for(String item : data) {
						mainListModel.addElement(item);
					}
					
					consoleWindow.append(file.getName()+" loaded.\n");
					entryField.requestFocusInWindow();
				}
				else {
					consoleWindow.append("Wrong file extension!\n");
				}
			}
			else {
				consoleWindow.append("File selection aborted.\n");
			}
		}
	}
	
	public static void main(String[] args) {
		IMGui im=new IMGui();
		im.go();
	}
	
}
