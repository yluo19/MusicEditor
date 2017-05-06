package List;

import java.util.ArrayList;

import Bean.Music;


final public class MusicList {

	private static ArrayList<Music> list;
	
	static{
		
		
		if (list==null) {
			System.out.println("Create the list");
			list=new ArrayList<Music>();
		}
	}
	
	public static void add(Music music){
		
		list.add(music);
	}
	
	public static ArrayList<Music> getList() {
		return list;
	}

	public static Music get(int id){
		return list.get(id);
	}
	
}
