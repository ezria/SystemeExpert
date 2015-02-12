package ToolBox;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;

public abstract class DialogBox {
	public static File openDialog(File file, String info){
		JFileChooser dialogue=null;
		try {
			if(file!= null && file.exists())
				dialogue =new JFileChooser(file.getCanonicalPath());
			else 
				dialogue =new JFileChooser();
        } catch(IOException e) {
        	System.err.println("impossible de lire le fichier, ouverture de la boite de dialogue sur le répertoire HOME");
        	dialogue =new JFileChooser();
        }
		dialogue.setDialogTitle(info);
		dialogue.setApproveButtonText("Ouvrir");
		dialogue.showOpenDialog(null);
		return dialogue.getSelectedFile();
	}
}