import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.swing.JOptionPane;


public class DownLoad {
	public static File mp3file=null;
    public static String mp3URL=null;
    public static File f=null;
    public static String s=null;
	/**
	 * @param args
	 */
    public  void beforeDownLoad()
    {
    	this.mp3URL=JOptionPane.showInputDialog("请输入正确的URL！");
    	this.s=JOptionPane.showInputDialog("输入歌曲名(不用加.mp3");
    	this.mp3file=new File("E:\\MP3PlayerDownLoad");
    	mp3file.mkdir();
    	this.f=new File(mp3file,s+".mp3");
    }
    
	public  void startDownLoad() {
		JOptionPane.showMessageDialog(null,"正在下载..");
        byte[] buffer = new byte[8 * 1024];
        URL u;
        URLConnection connection = null;
        try {
                u = new URL(mp3URL);
                connection = u.openConnection();
        } catch (Exception e) {
        	JOptionPane.showMessageDialog(null,"URL错误，下载失败!");
                return;
        }
        connection.setReadTimeout(100000);
        InputStream is = null;
        FileOutputStream fos = null;
        try {
                f.createNewFile();
                is = connection.getInputStream();
                fos = new FileOutputStream(f);
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                }

        } catch (Exception e) {
                f.delete();
        } finally {
                if (fos != null) {
                        try {
                                fos.close();
                        } catch (IOException e) {
                        }
                }
                if (is != null) {
                        try {
                                is.close();
                        } catch (IOException e) {
                        }
                }
        }
        buffer = null;
        JOptionPane.showMessageDialog(null,"下载完毕。");
      
	}
}
