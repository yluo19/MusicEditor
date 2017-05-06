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
	//菜单栏设置
	 m=new JMenuBar(); 
	JMenu file=new JMenu("文件",true);
	JMenuItem open=new JMenuItem("打开");
	open.addActionListener(this);
	
	JMenuItem addmusic=new JMenuItem("添加音乐到当前列表");
	addmusic.addActionListener(this);	
	
	JMenuItem DownLoad=new JMenuItem("下载歌曲");
	DownLoad.addActionListener(this);
	
	//JMenuItem OnlineMusic = new JMenuItem("在线播放");
	//OnlineMusic.addActionListener(this);
	
	file.add(open);
	file.add(addmusic);
	file.add(DownLoad);
	//file.add(OnlineMusic);
	
	JMenu playmode = new JMenu("播放模式"); 
	JMenuItem listloop = new JMenuItem("列表循环"); 
	listloop.addActionListener(this);
	JMenuItem singleplay = new JMenuItem("单曲循环");
	singleplay.addActionListener(this);
	JMenuItem randomplay = new JMenuItem("随机播放");
	randomplay.addActionListener(this);
	
	playmode.add(listloop);
	playmode.add(singleplay);
	playmode.add(randomplay);	
	
	JMenu ChangeColor = new JMenu("更换皮肤"); 
	JMenuItem toPINK = new JMenuItem("粉色可爱");
	toPINK.addActionListener(this);
	JMenuItem toBULE = new JMenuItem("蓝色静谧");
	toBULE.addActionListener(this);
	JMenuItem toGREEN = new JMenuItem("绿色春意");
	toGREEN.addActionListener(this);
	JMenuItem todefault = new JMenuItem("默认皮肤");
	todefault.addActionListener(this);
	ChangeColor.add(toPINK);
	ChangeColor.add(toBULE);
	ChangeColor.add(toGREEN);
	ChangeColor.add(todefault);
	m.add(file);
	m.add(playmode);
	m.add(ChangeColor);
	setJMenuBar(m);
	//播放按钮设置
	JPanel p=new JPanel();
	JButton last=new JButton("上一曲");
	last.addActionListener(this);
	JButton suspend=new JButton("播放/暂停");
	suspend.addActionListener(this);
	JButton next=new JButton("下一曲");
	next.addActionListener(this);
	
	p.setLayout(new GridLayout(1,0));
	p.add(last);
	p.add(suspend);
	p.add(next);
	add("South",p);
	//播放列表设置
	playlist=new JList<String>();
	playlist.setVisibleRowCount(10);
	JScrollPane scrollPane=new JScrollPane(playlist);
    pl=new JPanel();
	pl.setLayout(new BorderLayout());
	JLabel L=new JLabel("~~~~~软件三班  朱伟鹤 3111006210~~~~");
	pl.add("North",L);
	pl.add("Center",scrollPane);
	
	songplaying.setEditable(false);
	pl.add("South",songplaying);
	add("Center",pl);
	pack();
	setVisible(true);
	//列表右键菜单设置
    jPopupMenu=new JPopupMenu();
	final JMenuItem Delete=new JMenuItem("删除");
	Delete.addActionListener(this);
	
	jPopupMenu.add(Delete);
	
	playlist.add(jPopupMenu);
	//关闭操作
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
	//实现双击播放音乐
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
				
				 songplaying.setText("当前正在播放："+songname.get(songNOplaying));
			}	
			
		}
	});
 }
 
 
	 
 
 @Override
  public void actionPerformed(ActionEvent e) {
	 if (e.getActionCommand().equals("打开")) { 
		 FileDialog fd = new FileDialog(new JFrame(), "打开媒体文件", FileDialog.LOAD); 
		 fd.setVisible(true); 
		 // 如果用户放弃选择文件，则返回 
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
			 isplaying=true;//获取播放状态
			 songNOplaying=songname.indexOf(name);//记录歌曲位置
			 songplaying.setText("当前正在播放："+name);
			 playlist.setListData(songname);
		    
		 
		 } 
     }
	 //打开动作到此处理结束

	 if(e.getActionCommand().equals("添加音乐到当前列表"))
	 {
		 FileDialog fd = new FileDialog(new JFrame(), "打开媒体文件", FileDialog.LOAD); 
		 fd.setVisible(true); 
		 // 如果用户放弃选择文件，则返回 
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
	 //添加音乐到播放列表处理到此结束	
	 if(e.getActionCommand().equals("下载歌曲"))
	 {
	
		DownLoad d=new DownLoad();
		d.beforeDownLoad();
		d.startDownLoad();
	 }
	 //下载处理到此结束

	 if(e.getActionCommand().equals("上一曲"))
	  {
		if(songNOplaying==0) 
			JOptionPane.showMessageDialog(null,"当前为第一首歌，无上一曲");
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
			 songplaying.setText("当前正在播放："+songname.get(songNOplaying));
			
		}
		 
	  }
	 //上一曲处理到此结束
	 if(e.getActionCommand().equals("下一曲"))
	  {
		if(songNOplaying==songpath.size()-1) 
			JOptionPane.showMessageDialog(null,"当前为最后一首歌，无下一曲");
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
			 songplaying.setText("当前正在播放："+songname.get(songNOplaying));
			
		}
		 
	  }
	 //下一曲处理到此结束
	 if (e.getActionCommand().equals("播放/暂停")) { 
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
    //播放暂停处理到此结束
	 if (e.getActionCommand().equals("列表循环"))
	 {
		JOptionPane.showMessageDialog(null,"已切换到列表循环。");
		playmode=1; 
	 }
	 if (e.getActionCommand().equals("单曲循环"))
	 {
		 JOptionPane.showMessageDialog(null,"已切换到单曲循环。");
		playmode=2; 
	 }
	 if (e.getActionCommand().equals("随机播放"))
	 {
		 JOptionPane.showMessageDialog(null,"已切换到随机播放。");
		playmode=3; 
	 }
	 //播放模式处理
	 if (e.getActionCommand().equals("粉色可爱"))
	 {
		 pl.setBackground(Color.pink);
		 m.setBackground(Color.pink);
		 playlist.setBackground(Color.pink);
	 }
	 if (e.getActionCommand().equals("蓝色静谧"))
	 {
		 pl.setBackground(Color.blue);
		 m.setBackground(Color.blue);
		 playlist.setBackground(Color.blue);
	 }
	 if (e.getActionCommand().equals("绿色春意"))
	 {
		 pl.setBackground(Color.green);
		 m.setBackground(Color.green);
		 playlist.setBackground(Color.green);
	 }
	 if (e.getActionCommand().equals("默认皮肤"))
	 {
		 pl.setBackground(Color.white);
		 m.setBackground(Color.white);
		 playlist.setBackground(Color.white);
	 }
	 //更换皮肤处理结束
	 if (e.getActionCommand().equals("删除")) { 
	  removeNO=playlist.getSelectedIndex();
	  songname.remove(removeNO);
	  songpath.remove(removeNO);
	  playlist.setListData(songname);
	  if(songNOplaying==removeNO)
		  player.close();
	  
	 }
	 //删除到此处理结束
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
				 songplaying.setText("当前正在播放："+songname.get(songNOplaying));
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
			 songplaying.setText("当前正在播放："+songname.get(songNOplaying));
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
				 songplaying.setText("当前正在播放："+songname.get(songNOplaying));
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