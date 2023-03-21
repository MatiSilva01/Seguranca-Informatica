package myCloudServer;

import java.io.BufferedInputStream;
import java.io.File;
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

public class myCloudServer {

	public static void main(String[] args) {
		/**
		 * if (args.length != 1) {
		 * System.err.println("Usage: java myCloudServer port");
		 * System.exit(1);
		 * }
		 */
		System.out.println("servidor: main");
		int portNumber = 23457;
		String[] user = new String[] {"maria", "maria2", "mariapass"};
		// Integer.parseInt(args[0]);
		myCloudServer server = new myCloudServer();
		server.genKeyStore(user);
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
					+ " -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore." + user[0] + " -dname \" CN=" + user[1]
					+ ", OU=FC, O=UL, L=Lisboa, ST=LS, C=PT \" -storepass " + user[2] + " -keypass " + user[2];
/*
keytool -genkey -noprompt \
 -alias alias1 \
 -dname "CN=mqttserver.ibm.com, OU=ID, O=IBM, L=Hursley, S=Hants, C=GB" \
 -keystore keystore \
 -storepass password \
 -keypass password
*/
			String[] cmd = command.split(" ");
			System.out.println("*************************************");
			try {
				Runtime.getRuntime().exec(cmd);
				System.out.println("--------------------------------------");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			System.out.println("thread do server para cada cliente");
		}

		public void run() {

			try {
				// cria as duas strings
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
				InputStream in = null;
	        	OutputStream out = null;
	        	BufferedInputStream bis = null;

				// AQUIIII
				String operation = (String) inStream.readObject();
				if (operation == "-g"){
					
					// send file
					
				} else {
				int filenumber = (int) inStream.readObject();
					
					// RECEIVE FILE
					for (int j = 0; j < filenumber; j++) {
						String filename = (String) inStream.readObject();
						int filesize = (int) inStream.readObject();
						if (operation == "-e"){
							File newDir = new File("ServerFiles/" + "received_" + filename + ".seguro");
							newDir.createNewFile();}
						else {
							File newDir = new File("ServerFiles/" + "received_" + filename);
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

					        System.out.println(filename + " has been saved.");
					}
					}
			} catch (IOException e) {
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			}
		
		}
	}
}
				


