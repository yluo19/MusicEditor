package FileStuff;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import Bean.Music;
import List.MusicList;


public class FileList {
	
//写文�?(防止中文乱码)//Forbid 
    public static void writeFile(String filePathAndName, String fileContent) {
      try {
       File f = new File(filePathAndName);
       if (!f.exists()) {
        f.createNewFile();
       }
       OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f,true),"UTF-8");
       BufferedWriter writer=new BufferedWriter(write);   
       writer.write(fileContent);
       writer.close();
      } catch (Exception e) {
       System.out.println("Write wrong file");
       e.printStackTrace();
      }
    }
    //清空
    public static void  clear(String path){
    	try {
    	  	new FileOutputStream(path).write(new String("").getBytes());
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("clear file wrong"+e.getMessage());
		}
  
    	
    }
    
    
    //�?(防止中文乱码)

	 public static void readFileByLines(String fileName) {
		 


	        File file = new File(fileName);
	        BufferedReader reader = null;
	        try {
	        	 reader=new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"UTF-8"));
	        	
	            String tempString = null;
	            ArrayList<Music> list=MusicList.getList();
	            while ((tempString = reader.readLine()) != null) {
	        		 String[] s= tempString.split(",");
	        		 Music music= new Music();
	        	     	music.setId(s[0]);//id
			        	music.setName(s[1]);//filename
			        	music.setPath(s[2]);//filepath
			        	list.add(music);
	            }
	            reader.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (reader != null) {
	                try {
	                    reader.close();
	                } catch (IOException e1) {
	                }
	            }
	        }
	    }
	 
}
