package List;

import java.util.ArrayList;

import View.View;


final public class ViewList {

	private static ArrayList<View> list;
	
	

	static{
		if (list==null) {
			System.out.println("Sucessfully create the list");
			list=new ArrayList<View>();
		}
	}
	
	public static void add(View v){
		
		list.add(v);
	}
	
	public static ArrayList<View> getList() {
		return list;
	}
	
}
