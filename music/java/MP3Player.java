import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.EndOfMediaEvent;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.Player;
import javax.media.Time;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import com.sun.media.protocol.http.DataSource;




public class MP3Player extends JFrame implements ActionListener ,ControllerListener{
 private boolean isplaying=false;
 private String name,dir,path;
 private Player player=null;
 private JList<String> playlist;
 private Vector songname=new Vector(),songname1=null;
 private ArrayList<String> songpath=new ArrayList<String>(),songpath1=null;
 private JTextField songplaying=new JTextField();
 private int songNOplaying=0,removeNO,playmode=1;
 private JPopupMenu jPopupMenu=null;
 private JMenuBar m=null;
 private JPanel pl=null;
 
 public  MP3Player(String title){
	 super(title);
	//�˵�������
	 m=new JMenuBar(); 
	JMenu file=new JMenu("�ļ�",true);
	JMenuItem open=new JMenuItem("��");
	open.addActionListener(this);
	
	JMenuItem addmusic=new JMenuItem("������ֵ���ǰ�б�");
	addmusic.addActionListener(this);	
	
	JMenuItem DownLoad=new JMenuItem("���ظ���");
	DownLoad.addActionListener(this);
	
	//JMenuItem OnlineMusic = new JMenuItem("���߲���");
	//OnlineMusic.addActionListener(this);
	
	file.add(open);
	file.add(addmusic);
	file.add(DownLoad);
	//file.add(OnlineMusic);
	
	JMenu playmode = new JMenu("����ģʽ"); 
	JMenuItem listloop = new JMenuItem("�б�ѭ��"); 
	listloop.addActionListener(this);
	JMenuItem singleplay = new JMenuItem("����ѭ��");
	singleplay.addActionListener(this);
	JMenuItem randomplay = new JMenuItem("�������");
	randomplay.addActionListener(this);
	
	playmode.add(listloop);
	playmode.add(singleplay);
	playmode.add(randomplay);	
	
	JMenu ChangeColor = new JMenu("����Ƥ��"); 
	JMenuItem toPINK = new JMenuItem("��ɫ�ɰ�");
	toPINK.addActionListener(this);
	JMenuItem toBULE = new JMenuItem("��ɫ����");
	toBULE.addActionListener(this);
	JMenuItem toGREEN = new JMenuItem("��ɫ����");
	toGREEN.addActionListener(this);
	JMenuItem todefault = new JMenuItem("Ĭ��Ƥ��");
	todefault.addActionListener(this);
	ChangeColor.add(toPINK);
	ChangeColor.add(toBULE);
	ChangeColor.add(toGREEN);
	ChangeColor.add(todefault);
	m.add(file);
	m.add(playmode);
	m.add(ChangeColor);
	setJMenuBar(m);
	//���Ű�ť����
	JPanel p=new JPanel();
	JButton last=new JButton("��һ��");
	last.addActionListener(this);
	JButton suspend=new JButton("����/��ͣ");
	suspend.addActionListener(this);
	JButton next=new JButton("��һ��");
	next.addActionListener(this);
	
	p.setLayout(new GridLayout(1,0));
	p.add(last);
	p.add(suspend);
	p.add(next);
	add("South",p);
	//�����б�����
	playlist=new JList<String>();
	playlist.setVisibleRowCount(10);
	JScrollPane scrollPane=new JScrollPane(playlist);
    pl=new JPanel();
	pl.setLayout(new BorderLayout());
	JLabel L=new JLabel("~~~~~�������  ��ΰ�� 3111006210~~~~");
	pl.add("North",L);
	pl.add("Center",scrollPane);
	
	songplaying.setEditable(false);
	pl.add("South",songplaying);
	add("Center",pl);
	pack();
	setVisible(true);
	//�б��Ҽ��˵�����
    jPopupMenu=new JPopupMenu();
	final JMenuItem Delete=new JMenuItem("ɾ��");
	Delete.addActionListener(this);
	
	jPopupMenu.add(Delete);
	
	playlist.add(jPopupMenu);
	//�رղ���
	addWindowListener(new WindowAdapter() { 
		public void windowClosing(WindowEvent e) { 
		dispose(); 
		} 

		public void windowClosed(WindowEvent e) { 
		if (player != null) 
		player.close(); 
		System.exit(0); 
		} 
		});
	//ʵ��˫����������
	playlist.addMouseListener(new MouseAdapter()
	{
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton()==MouseEvent.BUTTON3) {
				jPopupMenu.show(playlist, e.getX(), e.getY());
			    }	
			if(e.getButton()==MouseEvent.BUTTON1&&e.getClickCount()==2)
			{
				if(player!=null)
				{	player.close();}
				songNOplaying=playlist.getSelectedIndex();
				try { 
					 player = Manager.createPlayer(new MediaLocator("file:" + songpath.get(songNOplaying)));
					 player.start();
					 
					 }
				 catch(Exception e1)
				    {
				 	 e1.printStackTrace();
				 	 return;
				    }	
				
				 songplaying.setText("��ǰ���ڲ��ţ�"+songname.get(songNOplaying));
			}	
			
		}
	});
 }
 
 
	 
 
 @Override
  public void actionPerformed(ActionEvent e) {
	 if (e.getActionCommand().equals("��")) { 
		 FileDialog fd = new FileDialog(new JFrame(), "��ý���ļ�", FileDialog.LOAD); 
		 fd.setVisible(true); 
		 // ����û�����ѡ���ļ����򷵻� 
		 if (fd.getFile() == null) { 
		 return; 
		 }
		 
		 else{
		 name = fd.getFile(); 
		 dir = fd.getDirectory(); 
		 path =dir+name; 
		 if(player!=null)
		  {
			 player.close();
		  }
	         songname.add(name);
	         songpath.add(path);
			 try { 
				 player = Manager.createPlayer(new MediaLocator("file:" + path));
				 player.start();
				 }
			 catch(Exception e1)
			    {
			 	 e1.printStackTrace();
			 	 return;
			    }
			 player.addControllerListener(this);
			 player.prefetch();
			 isplaying=true;//��ȡ����״̬
			 songNOplaying=songname.indexOf(name);//��¼����λ��
			 songplaying.setText("��ǰ���ڲ��ţ�"+name);
			 playlist.setListData(songname);
		    
		 
		 } 
     }
	 //�򿪶������˴������

	 if(e.getActionCommand().equals("������ֵ���ǰ�б�"))
	 {
		 FileDialog fd = new FileDialog(new JFrame(), "��ý���ļ�", FileDialog.LOAD); 
		 fd.setVisible(true); 
		 // ����û�����ѡ���ļ����򷵻� 
		 if (fd.getFile() == null) { 
		 return; 
		 }
		 
		 else{
		 name = fd.getFile(); 
		 dir = fd.getDirectory(); 
		 path =dir+name; 
		 songname.add(name);
		 songpath.add(path);
		 playlist.setListData(songname);
		 }		 		
		 }
	 //������ֵ������б����˽���	
	 if(e.getActionCommand().equals("���ظ���"))
	 {
	
		DownLoad d=new DownLoad();
		d.beforeDownLoad();
		d.startDownLoad();
	 }
	 //���ش����˽���

	 if(e.getActionCommand().equals("��һ��"))
	  {
		if(songNOplaying==0) 
			JOptionPane.showMessageDialog(null,"��ǰΪ��һ�׸裬����һ��");
		else
		{
			songNOplaying=songNOplaying-1;
			if(player!=null)
				player.close();
			 try { 
				 player = Manager.createPlayer(new MediaLocator("file:" + songpath.get(songNOplaying)));
				 player.start();
				 }
			 catch(Exception e1)
			    {
			 	 e1.printStackTrace();
			 	 return;
			    }
			 player.addControllerListener(this);
			 player.prefetch();
			 songplaying.setText("��ǰ���ڲ��ţ�"+songname.get(songNOplaying));
			
		}
		 
	  }
	 //��һ�������˽���
	 if(e.getActionCommand().equals("��һ��"))
	  {
		if(songNOplaying==songpath.size()-1) 
			JOptionPane.showMessageDialog(null,"��ǰΪ���һ�׸裬����һ��");
		else
		{
			songNOplaying=songNOplaying+1;
			if(player!=null)
				player.close();
			 try { 
				 player = Manager.createPlayer(new MediaLocator("file:" + songpath.get(songNOplaying)));
				 player.start();
				 }
			 catch(Exception e1)
			    {
			 	 e1.printStackTrace();
			 	 return;
			    }
			 player.addControllerListener(this);
			 player.prefetch();
			 songplaying.setText("��ǰ���ڲ��ţ�"+songname.get(songNOplaying));
			
		}
		 
	  }
	 //��һ�������˽���
	 if (e.getActionCommand().equals("����/��ͣ")) { 
		    if(isplaying)
		    {
		    	player.stop();
		    	isplaying=false;
		    }
		    else
		    {
		    	player.start();
		    	isplaying=true;
		    }
		 return; 
		 } 
    //������ͣ�����˽���
	 if (e.getActionCommand().equals("�б�ѭ��"))
	 {
		JOptionPane.showMessageDialog(null,"���л����б�ѭ����");
		playmode=1; 
	 }
	 if (e.getActionCommand().equals("����ѭ��"))
	 {
		 JOptionPane.showMessageDialog(null,"���л�������ѭ����");
		playmode=2; 
	 }
	 if (e.getActionCommand().equals("�������"))
	 {
		 JOptionPane.showMessageDialog(null,"���л���������š�");
		playmode=3; 
	 }
	 //����ģʽ����
	 if (e.getActionCommand().equals("��ɫ�ɰ�"))
	 {
		 pl.setBackground(Color.pink);
		 m.setBackground(Color.pink);
		 playlist.setBackground(Color.pink);
	 }
	 if (e.getActionCommand().equals("��ɫ����"))
	 {
		 pl.setBackground(Color.blue);
		 m.setBackground(Color.blue);
		 playlist.setBackground(Color.blue);
	 }
	 if (e.getActionCommand().equals("��ɫ����"))
	 {
		 pl.setBackground(Color.green);
		 m.setBackground(Color.green);
		 playlist.setBackground(Color.green);
	 }
	 if (e.getActionCommand().equals("Ĭ��Ƥ��"))
	 {
		 pl.setBackground(Color.white);
		 m.setBackground(Color.white);
		 playlist.setBackground(Color.white);
	 }
	 //����Ƥ���������
	 if (e.getActionCommand().equals("ɾ��")) { 
	  removeNO=playlist.getSelectedIndex();
	  songname.remove(removeNO);
	  songpath.remove(removeNO);
	  playlist.setListData(songname);
	  if(songNOplaying==removeNO)
		  player.close();
	  
	 }
	 //ɾ�����˴������
 }
 
 @Override
 public void controllerUpdate(ControllerEvent e) {
 	// TODO Auto-generated method stub
	 if(e instanceof EndOfMediaEvent)  
	 {   
		 if(playmode==1)
		 {
		 if(songNOplaying==songname.size()-1)
		 {
			 songNOplaying=0;
			 if(player!=null)
					player.close();
				 try { 
					 player = Manager.createPlayer(new MediaLocator("file:" + songpath.get(songNOplaying)));
					 player.start();
					 }
				 catch(Exception e1)
				    {
				 	 e1.printStackTrace();
				 	 return;
				    }
				 player.addControllerListener(this);
				 player.prefetch();
				 songplaying.setText("��ǰ���ڲ��ţ�"+songname.get(songNOplaying));
		 }
		 
		 else{
		 songNOplaying++;
		 if(player!=null)
				player.close();
			 try { 
				 player = Manager.createPlayer(new MediaLocator("file:" + songpath.get(songNOplaying)));
				 player.start();
				 }
			 catch(Exception e1)
			    {
			 	 e1.printStackTrace();
			 	 return;
			    }
			 player.addControllerListener(this);
			 player.prefetch();
			 songplaying.setText("��ǰ���ڲ��ţ�"+songname.get(songNOplaying));
	        }
		 }
		 else if(playmode==2)
		 {
			 player.setMediaTime(new Time(0));
			 player.start();
		 }
		 else if(playmode==3)
		 {
			 songNOplaying=(int)(Math.random()*songname.size());
			 if(player!=null)
					player.close();
				 try { 
					 player = Manager.createPlayer(new MediaLocator("file:" + songpath.get(songNOplaying)));
					 player.start();
					 }
				 catch(Exception e1)
				    {
				 	 e1.printStackTrace();
				 	 return;
				    }
				 player.addControllerListener(this);
				 player.prefetch();
				 songplaying.setText("��ǰ���ڲ��ţ�"+songname.get(songNOplaying));
		 }
	 }
 }
 public static void main(String[] arg0)
 {
	 MP3Player i=new MP3Player("MP3Player");
	 i.setSize(300, 400);
	 i.setResizable(false);
 }
}