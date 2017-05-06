package main;

import javax.swing.UIManager;

import List.ViewList;
import View.View;

public class start {

	public static void main(String[] args) {
		if (ViewList.getList().size()==0) {
			
		 View v=new View();
		 
		 ViewList.add(v);
		 
			try
	    	{
	    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        }catch(Exception exception){
	        	exception.printStackTrace();
	        }
		 
		}
		
		System.out.println(" Start !");
	}
}
