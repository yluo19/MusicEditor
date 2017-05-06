package List;

import java.util.ArrayList;

import Service.Play;


final public class ThreadList {

	private static ArrayList<Play> list;
	
	

	static{
		if (list==null) {
			list=new ArrayList<Play>();
		}
	}
	
	public static void add(Play player){
		
		list.add(player);
	}
	
	public static ArrayList<Play> getList() {
		return list;
	}
	
}
