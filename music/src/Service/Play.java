package Service;
 
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
 
import javax.sound.sampled.*;
import javax.swing.JSlider;
import javax.swing.JTable;

import Bean.Model;
import Bean.Music;
import List.MusicList;
import List.ThreadList;
import List.ViewList;
import View.View;
import javazoom.jl.*;
import javazoom.jl.player.Player;

/*"duration"	
"author"	
"title"	
"copyright"	

"date"	
"comment"*/
public class Play extends Thread{
	
	public Play(JSlider jSliderVolume,JSlider jSliderPlayProgress) {
		super();
		this.jSliderVolume = jSliderVolume;
		this.jSliderPlayProgress=jSliderPlayProgress;
	}

	private Play p;
	private Player player;
	private long time = 0;
	// some lock somewhere...
	Object lock = new Object();
	// some paused variable   
	private boolean paused = false;
	
	
	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	 private JSlider jSliderPlayProgress;


	// some paused variable  
	private boolean over = false;
	
	//Automatically play next song or not
	private boolean isNext=true;
	
	
	AudioInputStream din = null;
	SourceDataLine line=null;
	
	private FloatControl volume = null;

	private JSlider jSliderVolume; 
	
	public JSlider getjSliderVolume() {
		return jSliderVolume;
	}

	public void setjSliderVolume(JSlider jSliderVolume) {
		this.jSliderVolume = jSliderVolume;
	
		
	}


	private Music music;//java bean no constructor
	
	public Music getMusic() {
		return music;
	}

	public void setMusic(Music music) {
		this.music = music;
	}
	
	   public FloatControl getVolume(){
	        return volume;
	    }

	   public void setVolume()
	    {
	    	if(line!=null)
	    	{
	    		if(line.isControlSupported(FloatControl.Type.MASTER_GAIN))
	         	{
	        		jSliderVolume.setEnabled(true);
	        		volume= (FloatControl)line.getControl( FloatControl.Type.MASTER_GAIN );
	        		jSliderVolume.setMinimum((int)volume.getMinimum());
	            	jSliderVolume.setMaximum((int)volume.getMaximum());
	            	//jSliderVolume.setValue((int)(volume.getMinimum()+(4*(volume.getMaximum()-volume.getMinimum()))/5));
	            	volume.setValue((float)(volume.getMinimum()+(4*(volume.getMaximum()-volume.getMinimum()))/5));
	         	}
	    		System.out.println("1");
	        }
	        else
	        {
	         	volume=null;
	         	jSliderVolume.setEnabled(false);
	        System.out.println("2");
	        }	
	    }
	   
	   
	public  void run(){
		
		AudioInputStream in=null;
		
		try {
			
			File file = new File(music.getPath());// input music path
		
			// the song which can not play, then go next song and delete
			try {
				 BufferedInputStream buffer = new BufferedInputStream(

		                    new FileInputStream(file));

		            player = new Player(buffer);

		            player.play(); 
			
			
			} catch (Exception e) {
				//there is a problem to delete the song
				MusicList.getList().remove(music.getId());
				
				System.out.println(music.getId()+"Music ID"+music.getPath());
				
				ViewList.getList().get(0).getJt().setModel(new Model());
				
				
				nextmusic();
			}
			
			AudioFormat baseFormat = in.getFormat();
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
					baseFormat.getChannels()*2, baseFormat.getSampleRate(), //baseFormat.getChanne
					false);
			if(baseFormat.getEncoding()==AudioFormat.Encoding.PCM_UNSIGNED || baseFormat.getEncoding()==AudioFormat.Encoding.ULAW ||
					baseFormat.getEncoding()==AudioFormat.Encoding.ALAW || baseFormat.getEncoding()==AudioFormat.Encoding.PCM_SIGNED){
	             		time=(file.length()*8000000)/((int)(decodedFormat.getSampleRate()*baseFormat.getSampleSizeInBits()));
	             		System.out.println("Time :"+time);//52989353
	            }else{
	                 int bitrate=0;
	                 if(baseFormat.properties().get("bitrate")!=null){
	                    
	                     bitrate=(int)((Integer)(baseFormat.properties().get("bitrate")));
	                     if(bitrate!=0)
	                     time=(file.length()*8000000)/bitrate;
	                     System.out.println("Time2"+time);
	                 }
	                 
	            }
			
			
			din = AudioSystem.getAudioInputStream(decodedFormat, in);//getAudioInputStream(target format , sourceStream)
			DataLine.Info info = new DataLine.Info(SourceDataLine.class, decodedFormat);
			line = (SourceDataLine) AudioSystem.getLine(info);
			line.open();
			setVolume();
			jSliderPlayProgress.setMaximum((int)time);
	    	jSliderPlayProgress.setValue(0);
			if(line!=null){
				line.open(decodedFormat);
				byte[] data = new byte[4096];
				int nBytesRead;
				
			synchronized (lock) {
				while ((nBytesRead = din.read(data, 0, data.length)) != -1) {
					//System.out.println(line.getMicrosecondPosition());
					while (paused) {
						if(line.isRunning()) {
							line.stop();
							System.out.println("pause");
						}
						try {
							lock.wait();
							System.out.println("wait");
						}
						catch(InterruptedException e) {
						}
					}
					if(!line.isRunning()&&!over) {
						System.out.println("Start playing");
						line.start();
						
					}
					
					if (over&&line.isRunning()) {
						System.out.println("Stop playing");
						jSliderPlayProgress.setValue(0);
						isNext=false;
						line.drain();
						line.stop();
						line.close();
					}
					
					jSliderPlayProgress.setValue((int)line.getMicrosecondPosition());
					line.write(data, 0, nBytesRead);
				}
				
				System.out.println("play over");
		
				nextmusic();
			}
			
		}
		/*	if(line != null) {
				line.open(decodedFormat);
				byte[] data = new byte[4096];
				// Start
				line.start();
				int nBytesRead;
				while ((nBytesRead = din.read(data, 0, data.length)) != -1) {	
					line.write(data, 0, nBytesRead);
				}
				// Stop
				line.drain();
				line.stop();
				line.close();
				din.close();
			}*/
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			if(din != null) {
				try { din.close(); } catch(IOException e) { }
			}
		}
	}

	private void nextmusic() {
		String mode=Setting.getMode();
		if (isNext&&!mode.equals("one")) {
			int nextid=0;
			int currentid=Integer.parseInt(this.music.getId());
			System.out.println(mode);
			if (mode.equals("default")&&(currentid==MusicList.getList().size()-1)){
				return;
			}
			
			if (mode.equals("rand")) {
				 Random random = new Random();
				 nextid=Math.abs(random.nextInt())%MusicList.getList().size();
			}else if (mode.equals("onecircle")) {
				nextid=currentid;
			}else if (mode.equals("default")&&!(currentid==MusicList.getList().size()-1)) {
				 nextid=currentid+1;
			}else if (mode.equals("morecircle")) {
				
				nextid=(currentid==MusicList.getList().size()-1)?0:currentid+1;
			}
			System.out.println(nextid+"----"+MusicList.getList().size());
			JTable jTable=ViewList.getList().get(0).getJt();
			if(nextid==0){
				
				jTable.setRowSelectionInterval(0,0);
				
			}else {
					jTable.setRowSelectionInterval(nextid-1,nextid); 
			}
				  this.stopplay();
				  ThreadList.getList().clear();
				  p=new Play(jSliderVolume,jSliderPlayProgress);
				  p.setMusic(MusicList.getList().get(nextid));
				  ThreadList.getList().add(p);
				  p.start();
		}
	}
	


	
	public void startplay(){
		over=false;
	}
	
	
	
	
	public void stopplay(){
		over=true;
	}
	
	
	
	public void userPressedPause() {
	 paused = true;
	}
	 
	
	public void userPressedPlay() {
			 synchronized(lock) {
				  paused = false;
				  lock.notifyAll();
		}

	}
	
	public void Pause(){
		if (paused) {
			 synchronized(lock) {
				  paused = false;
				  lock.notifyAll();
				 }
		}else{
			 paused = true;
		}
		
	}
	

	
 
}