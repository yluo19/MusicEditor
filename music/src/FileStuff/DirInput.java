package FileStuff;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

/**
 *class FileInput
 */
public class DirInput
{
   private JFileChooser fdialog=null;
   private File f;
   private JFrame jf;
   public DirInput(JFrame jf)
   {    
       this.jf=jf;
       fdialog=new JFileChooser("D:");
       fdialog.setFileFilter(new FileFilter() {
		
		@Override
		public String getDescription() {
			// TODO Auto-generated method stub
			return "Content";
		}
		
		@Override
		public boolean accept(File f) {
			// TODO Auto-generated method stub
			if(f.isDirectory()) return true;
			return false;
		}
	});
       fdialog.setAcceptAllFileFilterUsed(false);
       fdialog.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
       fdialog.setMultiSelectionEnabled(false);
      
   }
   public void open()
   {
      f=null;
      int result = fdialog.showOpenDialog(jf);
      if(result == fdialog.APPROVE_OPTION){
    	  
    	  f=fdialog.getSelectedFile();
    	  
      }
   }
   public File getFile()
   {
   		return f;
   }
   
}
