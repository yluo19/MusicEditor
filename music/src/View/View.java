package View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellRenderer;

import Bean.Model;
import Bean.Music;
import FileStuff.DirInput;
import FileStuff.FileInput;
import FileStuff.FileList;
import FileStuff.List_File;
import List.MusicList;
import List.ThreadList;
import List.ViewList;
import Service.Play;
import Service.Setting;


public class View extends JFrame implements MouseListener,ActionListener,WindowListener {
	
	private JButton stop, open,del,next,pre;
	private  Play p; //
	private JPanel[] jPanels;
	private MusicList list;
	private JScrollPane jsp;
	private JTable jt;
	private JRootPane j;
	private Model model;
	private JSlider jSliderVolume;
	private JSlider jSliderPlayProgress;
	private FileInput fileinput;
	private DirInput dirInput;
    private JMenuBar jb;
    private JMenu jm;
    private JMenuItem fm,dm;
    private JComboBox jBox;
    
	public View(){
		System.out.println(ViewList.getList().size());
		if (ViewList.getList().size()==0) {
			Open();
		}
	}

	
	
	private void Open() {
		
		jb=new JMenuBar();
		jm=new JMenu("Open");
		
		fm=new JMenuItem("File");
		
		dm=new JMenuItem("Folder");
		
		fm.addActionListener(this);
		dm.addActionListener(this);
		jb.add(jm);
		
		jm.add(fm);
		jm.add(dm);
		
		
		
		//this.setJMenuBar(jb);
		JPanel p1=new JPanel();
		JPanel p2=new JPanel();
		JPanel p3=new JPanel();
		j=new JRootPane();
		
		j.setJMenuBar(jb);
		
		
		p2.add(j);
		
		
		 open=new JButton("Play");
		 stop=new JButton("Stop");
		 open.addMouseListener(this);
		 stop.addMouseListener(this);
		 pre=new JButton("Next");
		 next=new JButton("Pre");
		 pre.addMouseListener(this);
		 next.addMouseListener(this);
		p1.setLayout(new GridLayout(2,1));
		
		JPanel jPanel2=new JPanel();
		
		jPanel2.add(open);
		jPanel2.add(stop);
		jPanel2.add(pre);
		jPanel2.add(next);
		
		p1.add(jPanel2);
		/*p1.add(open);
		p1.add(stop);
		p1.add(pre);
		p1.add(next);*/
		
		
		
		
		
		del=new JButton("Delete");
		del.addMouseListener(this);
		
		 jSliderPlayProgress = new JSlider();   
	        jSliderPlayProgress.setValue(0);
	        jSliderPlayProgress.setEnabled(false);
	        jSliderPlayProgress.setPreferredSize(new Dimension(200, 20));
	        
	        
	        p1.add(jSliderPlayProgress);
	        
		
		jSliderVolume = new JSlider();   
		jSliderVolume.setValue(0);
	     //jSliderPlayProgress.setEnabled(false);
		jSliderVolume.setPreferredSize(new Dimension(100, 20));//Set length of Slider
	     
	     
	     
		jSliderVolume.addChangeListener(new ChangeListener()
	        {
	            public void stateChanged(ChangeEvent evt) 
	            {
	            	System.out.println(jSliderVolume.getValue());
	            	if (ThreadList.getList().size()!=0) {
	            		ThreadList.getList().get(0).getVolume().setValue((float)jSliderVolume.getValue());
					}
	            	
	            	
	            }
	        });
		String[] v={"Order play","Shuffle","repeat once","all repeat","once"};
		
		jBox=new JComboBox(v);
		
		jBox.addActionListener(this);
	        
        p2.add(jBox);		
		p2.add(del);
		
		
		 p2.add(jSliderVolume);
		
		jPanels=new JPanel[list.getList().size()];
		
		for (int i = 0; i < list.getList().size(); i++) {
			
			Music music=list.getList().get(i);
			
			JPanel jPanel=new MyJPanel(music);
			
			
			JLabel jLabel=new JLabel(music.getName(),SwingConstants.CENTER);
			jLabel.setSize(300, 10);
			jLabel.setHorizontalTextPosition(JLabel.CENTER);
			
			jPanel.setBackground(Color.WHITE);
			
			jPanels[i]=jPanel;
			
			jPanel.addMouseListener(this);
			
			jPanel.add(jLabel);
			
			
			
			p3.add(jPanel);
			
			
		}
		
		
		p3.setBackground(Color.WHITE);
		
	    p3.setLayout(new GridLayout(10, 1));
		
		p3.setSize(320, 500);
		
		
		this.add(p1,BorderLayout.NORTH);
		this.add(p2,BorderLayout.SOUTH);
		
		
	        
	        
		
		
		model=new Model();		
		
		jt=new JTable(model){ // Set jtable to Transparent
			   public Component prepareRenderer(TableCellRenderer renderer,
					     int row, int column) {
					    Component c = super.prepareRenderer(renderer, row, column);
					    if (c instanceof JComponent) {
					     ((JComponent) c).setOpaque(false);
					    }
					    return c;
					   }
					  };;
					  
		jt.setOpaque(false);
		
		jt.setRowHeight(30);
		jt.setSelectionMode(ListSelectionModel.SINGLE_SELECTION );
		jt.setShowHorizontalLines(false);
		jt.setSelectionBackground(new Color(189,215,238));
		jt.addMouseListener(this);
		
		jsp = new JScrollPane(jt);
		
		jsp.setOpaque(false);
		jsp.getViewport().setOpaque(false);
		
		
		//addmusic();
		
		//this.add(p3,BorderLayout.CENTER);
		
		this.add(jsp,BorderLayout.CENTER);
		
		
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		this.addWindowListener(this);
		
	    Image image=this.getToolkit().getImage("img/icon.png");
	    
	    this.setIconImage(image);
		
		this.setTitle("Music Editor");
		
		  ImageIcon icon = new ImageIcon("img/bg.jpg"); 
		  JLabel lab = new JLabel(icon); 
		  lab.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight()); 
		  
		  this.getContentPane().add(lab, -1);  
		  this.getContentPane().add(jsp, 0); 
		  Container con = this.getContentPane();
		  ((JPanel)con).setOpaque(false); 
		  p2.setOpaque(false);
         jSliderVolume.setOpaque(false);
         jPanel2.setOpaque(false);
         p1.setOpaque(false);
         p3.setOpaque(false);
         jSliderPlayProgress.setOpaque(false);
		this.setLocation(400, 200);
		this.setSize(800, 800);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("Start playing");
		
		if (e.getSource()==open) {
			
		
			
			if (p==null) {
			  p=new Play(jSliderVolume,jSliderPlayProgress);
			  p.setMusic(MusicList.getList().get(0));
			  jt.setRowSelectionInterval(0,0);
			  ThreadList.add(p);
			  open.setText("Pause");
			  p.start();
			}else{
				if (ThreadList.getList().size()!=0) {
					p=ThreadList.getList().get(0);
				}
				
				String s=p.isPaused()?"Pause":"Play";
				open.setText(s);
				p.Pause();
			}
			
		}else if (e.getSource()==stop) {
			if (ThreadList.getList().size()!=0) {
				p=ThreadList.getList().get(0);
			}
			if (p!=null) {
				p.stopplay();
				p=null;
				open.setText("Play");
			}
		
			
		}else if (e.getSource()==pre) {//pre
			premusic();

		}else if (e.getSource()==next) {//next
			nextmusic();
		}else if (e.getSource()==del) {
			
			delmusic();
	  			
		}else if (e.getSource()==jt&&e.getClickCount()==2) {//Double click 
			
			clickmusic();
			
			
			
		}

		
	}

	private void clickmusic() {
		//双击Jtable
		System.out.println("Click");
		
		int rowNum = this.jt.getSelectedRow();
		System.out.println(rowNum);
		if(rowNum == -1) {
			JOptionPane.showMessageDialog(this, "You didn't choose anyone");
			return;
		}
		ArrayList<Play> list=ThreadList.getList();
		
		
		System.out.println(list.size()+"Music files number");
		
		 if (list.size()==0) {
			 
			 p=new Play(jSliderVolume,jSliderPlayProgress);
			  p.setMusic(MusicList.getList().get(rowNum));
			  ThreadList.add(p);
			  open.setText("Pause");
			  p.start();
		}else{
			System.out.println("Stop");
		list.get(0).stopplay();
		list.clear();
		p=new Play(jSliderVolume,jSliderPlayProgress);
		    p.setMusic(MusicList.getList().get(rowNum));
		    open.setText("Pause");
		    list.add(p);
		    p.start();
		}
	}

	private void delmusic() {
		int rowNum = this.jt.getSelectedRow();
		
		
		MusicList.getList().remove(rowNum);
		
		System.out.println(MusicList.getList().size());
		
		jt.setModel(new Model());

		
		   ArrayList<Play> list=ThreadList.getList();
		   p=new Play(jSliderVolume,jSliderPlayProgress);
		   System.out.println(list.size()+"Size");
		   if (list.size()!=0) {
		    	list.get(0).stopplay();
				list.clear();
				open.setText("Pause");
				if(rowNum==0){
					System.out.println("the first one");
			  			    jt.setRowSelectionInterval(0,0);
						    p.setMusic(MusicList.getList().get(rowNum));
						    
						   
				}else if(rowNum==MusicList.getList().size()){
						System.out.println("the last one");
			  			
			  			jt.setRowSelectionInterval(rowNum-2,rowNum-1);
						    p.setMusic(MusicList.getList().get(rowNum-1));
						    
				}else {
					System.out.println("middle");
					
			  			jt.setRowSelectionInterval(rowNum-1,rowNum);
						p.setMusic(MusicList.getList().get(rowNum));
						    
				}
				 list.add(p);
				 p.start();
		   }
	}

	public JTable getJt() {
		return jt;
	}

	private void premusic() {
		System.out.println("previous song");
		
		ArrayList<Play> list=ThreadList.getList();
		
		int id=Integer.parseInt(list.get(0).getMusic().getId());
		
		if(id!=0){
		if (id==1) {
			jt.setRowSelectionInterval(0,0);
		}else{
			jt.setRowSelectionInterval(id-2,id-1);
		}
		System.out.println(id);
		
		list.get(0).stopplay();
		list.clear();
		
		  p=new Play(jSliderVolume,jSliderPlayProgress);

		  p.setMusic(MusicList.getList().get(id-1));
		  System.out.println(id-1);
		  
		  open.setText("Pause");
		  list.add(p);
		  p.start();
		}
	}

	private void nextmusic() {
		System.out.println("Next song");
		ArrayList<Play> list=ThreadList.getList();
		int id=Integer.parseInt(list.get(0).getMusic().getId());
		
		System.out.println(id);
		if(id!=MusicList.getList().size()-1){ //122
			
		jt.setRowSelectionInterval(id,id+1);  //123
		
		list.get(0).stopplay();
		list.clear();
		
		p=new Play(jSliderVolume,jSliderPlayProgress);
		
		  p.setMusic(MusicList.getList().get(id+1));
		  System.out.println(id+1);
		  
		  open.setText("Pause");
		  list.add(p);
		  p.start();
}
	}
   

	
	private void addmusic(String path) {
		
		System.out.println("add mp3 folder");
		ArrayList<Music> musiclist=MusicList.getList();
			
		    List_File fm = new List_File();
	        ArrayList<String[]> FileList = fm.serachFiles(path);
	        
	        for (int i = 0; i < FileList.size(); i++) {
	        	Music music= new Music();
		        	music.setId(musiclist.size()+"");
		        	String[] s=(String[]) FileList.get(i);
		        	
		        	music.setName(s[0]);
		        	music.setPath(s[1]);
	        	musiclist.add(music);
			}
	        
				jt.setModel(new Model());
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==fm) {
		if(fileinput==null)	fileinput = new FileInput(this);
        	fileinput.open();
        	File[] s=fileinput.getFiles();
        	ArrayList<Music> musiclist=MusicList.getList();
        	
        	
        	if(s!=null){
        	    for(int i=0;i<s.length;i++){
        	    Music music= new Music();
	        	music.setId(musiclist.size()+"");
	        	music.setName(s[i].getName());
	        	music.setPath(s[i].getAbsolutePath());
        	    musiclist.add(music);
			jt.setModel(new Model());
        	    }
        	}
		}else if (e.getSource()==dm) {
			
			
			if(dirInput==null) dirInput = new DirInput(this);
			
			dirInput.open();
        	
        	File s=dirInput.getFile();
        	
        	
        	if(s!=null){
        	    	
        	    	addmusic(s.getAbsolutePath());
        	    }
        	
			
			
		}else if (e.getSource()==jBox) { 
			
        
      if (ThreadList.getList().size()!=0) {
		p=ThreadList.getList().get(0);
	  }else {
		  p=new Play(jSliderVolume,jSliderPlayProgress);
		  ThreadList.getList().add(p);
	  }
      
      String[] s={"default","rand","onecircle","morecircle","one"};
      
	  Setting.setMode(s[jBox.getSelectedIndex()]);
		
		
		}
	}



	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("Close");
	}



	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("close");
		
		if (MusicList.getList().size()!=0) {
			System.out.println("Write file");
			
			FileList.clear("file/musiclist.txt");
			ArrayList<Music> list=MusicList.getList();
			for (int i = 0; i < list.size(); i++) {
				FileList.writeFile("file/musiclist.txt",list.get(i).getId()+","+list.get(i).getName()+","
						+list.get(i).getPath()+"\n");
			}
			
		}
		
		
		
		
		
	}



	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		System.out.println("open");
		
		File file=new File("file/musiclist.txt");
		
		if (file.exists()==false) {
			try {
				file.createNewFile();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}else {


			FileList.readFileByLines("file/musiclist.txt");
			jt.setModel(new Model());
		}
		
		
    	
	}

}
