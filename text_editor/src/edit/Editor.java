package edit;

	import java.io.*;
	import java.awt.*;
	import java.util.Date;
	import java.text.DateFormat;
	import java.text.SimpleDateFormat;
	import javax.swing.*;
	import javax.swing.filechooser.FileFilter;
	import javax.swing.filechooser.FileNameExtensionFilter;
	import javax.swing.border.*;
	import java.awt.event.*;
	import javax.swing.Action;
	import java.io.FileWriter;
	import java.io.FileReader;
	import java.sql.Connection;
	import java.sql.DriverManager;
	import java.sql.PreparedStatement; 
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.*;
	import java.util.logging.Level;
	import java.util.logging.Logger;
	import static javax.swing.JFrame.EXIT_ON_CLOSE;



	public class Editor extends JFrame{
		private static final long serialVersionUID = 1L;
		Connection con;
		PreparedStatement pst;
		ResultSet rs;
		int id=0;
	    final JTextArea textarea=new JTextArea(20,60);
		
			
		private JFileChooser fc=new JFileChooser();
		public void connect()
		{
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
				con=DriverManager.getConnection("jdbc:mysql://localhost:3306/text_editor_db","root","yash");
			} catch(ClassNotFoundException ex) {
				Logger.getLogger(Editor.class.getName()).log(Level.SEVERE,null,ex);
			} catch(SQLException ex) {
				Logger.getLogger(Editor.class.getName()).log(Level.SEVERE,null,ex);
			}
		}
	public Editor() {
	connect();
	JScrollPane scrollPane=new JScrollPane(textarea);
	FileFilter txtFilter=new FileNameExtensionFilter("Plain text","txt");
	fc.setFileFilter(txtFilter);

	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

	JMenuBar menuBar=new JMenuBar();
	
	Font mainfont=new Font("Comic Sans MS",Font.PLAIN,30);
	textarea.setFont(mainfont);

	JMenu file =new JMenu("File");

	JButton clear=new JButton("Clear All");
	clear.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			textarea.setText(null);
		}
	});
	JButton Size= new JButton("Change size");
	JSlider jl=new JSlider(JSlider.HORIZONTAL,100);
	jl.setValue(30);
	Size.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			Font font =new Font("Comic Sans MS",Font.PLAIN,jl.getValue());
			textarea.setFont(font);
		}
	});

	JButton BOLD =new JButton("BOLD");
	BOLD.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Font bold=new Font("Comic Sans MS",Font.BOLD,jl.getValue());
			textarea.setFont(bold);
		}
	});

	JButton Slant =new JButton("Slant");
	Slant.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			Font slant=new Font("Comic Sans MS",Font.ITALIC,jl.getValue());
			textarea.setFont(slant);
		}
	});

	Date date=new Date();
	DateFormat df =new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
	String strDate= df.format(date);
	JLabel dt =new JLabel(" Session Start Time :-"+strDate+ " ");

	menuBar.add(file);
	menuBar.add(clear);

	menuBar.add(Size);
	menuBar.add(jl);
	menuBar.add(BOLD);
	menuBar.add(Slant);
	menuBar.add(dt);


	file.add(Open);
	file.add(Save);
	file.add(Exit);


	setJMenuBar(menuBar);

	this.add(scrollPane);


	setDefaultCloseOperation(EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setVisible(true);
	setTitle("TEXT EDITOR");
	
	}


	Action Open=new AbstractAction("Open File") {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if(fc.showOpenDialog(null)==JFileChooser.APPROVE_OPTION) {
				openFile(fc.getSelectedFile().getAbsolutePath());
			}
			System.out.println(fc.getSelectedFile().getAbsolutePath());
		}
	};

	Action Save= new AbstractAction("Save File") {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			saveFile();
		}
	};

	Action Exit= new AbstractAction("Exit") {
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	public void openFile(String fileName) {
		try
		{
			Date date=new Date();
			DateFormat df =new SimpleDateFormat("HH:mm:ss dd-mm-yyyy");
			String strDate=df.format(date);
			try {
				pst= con.prepareStatement("insert into record(name,time)values(?,?)");
				
				pst.setString(1, fileName);
				pst.setString(2,strDate);
				pst.executeUpdate();
				JOptionPane.showMessageDialog(this,"Time noted");
			}catch (SQLException ex) {
				Logger.getLogger(Editor.class.getName()).log(Level.SEVERE,null,ex);
			}
			
			FileReader reader=new FileReader(fileName);
			BufferedReader br=new BufferedReader(reader);
			textarea.read(br, null);
			setTitle(fileName);
			br.close();
			textarea.requestFocus();
		}
		catch(IOException e) {
			System.out.println("File not found");
		}
	}
	public void saveFile()
	{
		if(fc.showSaveDialog(null)==JFileChooser.APPROVE_OPTION) {
			FileWriter fw=null;
			try {
				String path=fc.getSelectedFile().getAbsolutePath();
				String filename=fc.getSelectedFile().getName();
				int len =filename.length();
				
				if(filename.endsWith(".txt")) {
					fw= new FileWriter(fc.getSelectedFile().getAbsolutePath());
				}else {
					fw= new FileWriter(fc.getSelectedFile().getAbsolutePath()+".txt");
				}
				textarea.write(fw);
				fw.close();
				textarea.setText(null);
				}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
public static void main(String[] args) {
		JFrame frame =new Editor();
		frame.setSize(1028,720);
	}
	}


