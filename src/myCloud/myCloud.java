package myCloud;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class myCloud {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
      //  if (args.length < 3 || args[0] != "-a") { // ficheiros?
      //     System.err.println("Usage: java myCloud -a <server address> -c|-s|-e|-g <filename(s)>");
      //     System.exit(1);
      //  }
    	
        String[] host = null;
        String operation;
        List<String> filelist;
        filelist = new ArrayList<>();
        filelist.add("pdf.pdf");
        //filelist.add("livro.pdf");
        int i = 0;
        while (i < args.length) {
            if (args[i].equals("-a")) {
                host = args[i + 1].split(":");

            } else if (args[i].equals("-c")) {
                operation = args[i];
                filelist = new ArrayList<>();
                for (int j = 1; i + j < args.length; j++) {
                    String f = args[i + j];
                    filelist.add(f);
                }
                ;
            } else if (args[i].equals("-s")) {
                operation = args[i];
                filelist = new ArrayList<>();
                for (int j = 1; i + j < args.length; j++) {
                    String f = args[i + j];
                    filelist.add(f);
                }
                ;
            } else if (args[i].equals("-e")) {
                operation = args[i];
                filelist = new ArrayList<>();
                for (int j = 1; i + j < args.length; j++) {
                    String f = args[i + j];
                    filelist.add(f);
                }
                ;
            } else if (args[i].equals("-g")) {
                operation = args[i];
                filelist = new ArrayList<>();
                for (int j = 1; i + j < args.length; j++) {
                    String f = args[i + j];
                    filelist.add(f);
                }
                ;
            }
            i++;
        }
        try {
			sendFiles(Cifra.cipherFile(filelist),"127.0.0.1", 23457);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        //sendFiles(filelist, "127.0.0.1", 23457);
        }
        //enviar ficheiros
        public static void sendFiles(List<String> filelist, String address, int socket) throws IOException{
            Socket echoSocket = new Socket(address, socket);
	        ObjectOutputStream outStream = new ObjectOutputStream(echoSocket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(echoSocket.getInputStream());
			BufferedInputStream bis = null;
			InputStream in = null;
			OutputStream out = null;
			
	        outStream.writeObject((int) filelist.size());
			for (String fname: filelist) {
					outStream.writeObject(fname);
					
					File file = new File(fname);
					
			        // Get the size of the file
			        long length = file.length();
			        outStream.writeObject((int) length);
			        byte[] bytes = new byte[(int) length];
			        in = new FileInputStream(file);
			        
			        bis = new BufferedInputStream(in);
			        
			        bis.read(bytes, 0, bytes.length);
			        
			        
			        out = echoSocket.getOutputStream();
			        	        	     
			        out.write(bytes, 0, bytes.length);
			        out.flush();
			        
			        System.out.println(fname + " sent!");
			}
			out.close();
	        in.close();
	        echoSocket.close();

        }


        
        
        
}
