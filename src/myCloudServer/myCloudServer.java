//Seguranca-Informatica
//Projetos SI - grupo 16
//
//Raphael Marques - 55135
//Ruben Silva - 56911
//Matilde Silva - 56895

package myCloudServer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class myCloudServer {

	public static void main(String[] args) {
		System.out.println("servidor: main");
		int portNumber = 23456; //TODO trocar p o que vem em args
		String[] user = new String[] {"maria", "maria2", "mariapass"};
		// Integer.parseInt(args[0]);
		myCloudServer server = new myCloudServer();
		//server.genKeyStore(user);
		server.startServer(portNumber);
	}

	public void startServer(int port) {

		ServerSocket sSoc = null;

		try {
			sSoc = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		while (true) {
			try {
				Socket inSoc = sSoc.accept();
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
			public void genKeyStore(String[] user) {
			String command = "keytool -genkeypair -noprompt -alias " + user[0]
					+ " -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore." + user[0] + "Cloud -dname \" CN=" + user[1]
					+ ", OU=FC, O=UL, L=Lisboa, ST=LS, C=PT \" -storepass " + user[2] + " -keypass " + user[2];
		String[] cmd = command.split(" ");
			System.out.println("*************************************");
			try {
				Runtime.getRuntime().exec(cmd);
				System.out.println("-------------------------------------");

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			System.out.println("conexao de novo cliente");
		}

		public void run() {

			try {
				// cria as duas strings
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
				InputStream in = null;
	        	OutputStream out = null;
	        	BufferedInputStream bis = null;

				String operation = (String) inStream.readObject();
				if (operation.equals("-g")){
					// send file
					
					List<String> filelist = (List<String>) inStream.readObject();
					List<String> filetosendlist = new ArrayList<>();
					for (String fname: filelist){
						File file0 = new File("ServerFiles/" + "received_" + fname+ ".assinado");
						File file1 = new File("ServerFiles/" + "received_" + fname+ ".assinatura");
						File file2 = new File("ServerFiles/" + "received_" + fname+ ".cifrado");
						File file3 = new File("ServerFiles/" + "received_" + fname+ ".chave_secreta");
						if (file0.exists()) {
							filetosendlist.add(fname + ".assinado");} 
						if (file1.exists()) {
							filetosendlist.add(fname + ".assinatura");} 
						if (file2.exists()) {
							filetosendlist.add(fname + ".cifrado");} 
						if (file3.exists()) {
							filetosendlist.add(fname + ".chave_secreta");
						}
						if (filetosendlist.size() == 0){
							System.out.println("O ficheiro " + fname + " não foi encontrado");
						}
					}
												
			        outStream.writeObject((int) filetosendlist.size());
					for (String fname: filetosendlist) {
		                try {
		                    
		                	File file = new File("ServerFiles/" + "received_" + fname);
		                    in = new FileInputStream(file);
					        
					        bis = new BufferedInputStream(in);
							outStream.writeObject(fname);
					        // Get the size of the file
					        long length = file.length();
					        outStream.writeObject((int) length);
					        byte[] bytes = new byte[(int) length];

					        bis.read(bytes, 0, bytes.length);
					        
					        
					        out = socket.getOutputStream();
					        	        	     
					        out.write(bytes, 0, bytes.length);
					        out.flush();

		                    System.out.println(fname + " sent!");
		                    bis.close();
		                    in.close();
		                    
					        
					     } catch (FileNotFoundException e) {
					    	 System.out.println("O ficheiro " + fname + " não foi encontrado");
					     }
					}

				} else {
				int filenumber = (int) inStream.readObject();
					
					// RECEIVE FILE
					for (int j = 0; j < filenumber; j++) {
						String filename = (String) inStream.readObject();
						int filesize = (int) inStream.readObject();
						boolean exists = false;
						if (operation.equals("-e")){
							File newDir = new File("ServerFiles/" + "received_" + filename + ".seguro");
							filename += ".seguro";
							exists = newDir.exists();
							newDir.createNewFile();}
						else if (operation.equals("-s")){
							if (!filename.endsWith(".assinatura")){
								filename += ".assinado";
							}
							File newDir = new File("ServerFiles/" + "received_" + filename);
							exists = newDir.exists();
							newDir.createNewFile();
							
						}
						else {
							File newDir = new File("ServerFiles/" + "received_" + filename);
							exists = newDir.exists();
							newDir.createNewFile();}
						
						 try {
					            in = socket.getInputStream();
					        } catch (IOException ex) {
					            System.out.println("Can't get socket input stream. ");
					        }
	
					        try {
					            out = new FileOutputStream("ServerFiles/" + "received_" + filename);
					        } catch (FileNotFoundException ex) {
					            System.out.println("File not found. " + ex);
					        }
	
					        byte[] bytes = new byte[filesize]; //used to be filesize
					        int count;
					        while (filesize > 0 && (count = in.read(bytes, 0, (int) Math.min(bytes.length, filesize))) != -1) {
					        	out.write(bytes, 0, count);
					        	filesize -= count;
					        }
					        out.close();
					        outStream.writeObject(!exists);

					        System.out.println(filename + " has been saved.");
					      
					}
					in.close();
					  inStream.close();
					  outStream.close();
					}
			} catch (IOException e) {
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			}
		
		}
	}
}
				


