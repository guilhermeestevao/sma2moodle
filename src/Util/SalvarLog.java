package Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalvarLog {

	private static final String caminho = "/var/moodledata/logs/";
	private static SimpleDateFormat formatador = new SimpleDateFormat("dd-MM-yyyy");  
	private static File f;
	
	public static synchronized void salvarArquivo(String valor){
		String data = formatador.format(new Date());
		data = data+".txt";
		f =  new File(caminho);	
		
		if(!f.exists()){
			f.mkdir();
			f = new File(caminho+data);
			try {
				f.createNewFile();
				BufferedWriter buffer = new BufferedWriter(new FileWriter(f,true));
				buffer.write(valor+"\n");
				buffer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}else{
			f = new File(caminho+data);
			if(!f.exists()){

				try {
					f.createNewFile();
					BufferedWriter buffer = new BufferedWriter(new FileWriter(f,true));
					buffer.write(valor+"\n");
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				
				try {
					BufferedWriter buffer;
					buffer = new BufferedWriter(new FileWriter(f,true));
					buffer.write(valor+"\n");
					buffer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}	
	}

	
	
}
