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
				System.out.println("-------------------------");

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
				int filenumber = (int) inStream.readObject();
					
					// RECEIVE FILE
					for (int j = 0; j < filenumber; j++) {
						String filename = (String) inStream.readObject();
						int filesize = (int) inStream.readObject();
						File newDir = new File("ServerFiles/" + "received_" + filename);
						newDir.createNewFile();
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
			} catch (IOException e) {
				System.out.println(e);
			} catch (ClassNotFoundException e) {
				System.out.println(e);
			}
		}
		
	}
}
				

/*
 * TP3 cifra
import java.io.FileInputStream;  
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.cert.Certificate;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Cifra {

    public static void main(String[] args) throws Exception {	
    //gerar uma chave aleatoria para utilizar com o AES
    KeyGenerator kg = KeyGenerator.getInstance("AES");
    //numero de bits da chave
    kg.init(128);
    //gera chave
    SecretKey key = kg.generateKey();
    //cipher para cifrar com a chave de cima
    Cipher c = Cipher.getInstance("AES");
    c.init(Cipher.ENCRYPT_MODE, key);

    FileInputStream fis;
    FileOutputStream fos;
    CipherOutputStream cos;
    //ler do ficheiro a.txt
    fis = new FileInputStream("a.txt");
    //cifra e mete no a.cif
    fos = new FileOutputStream("a.cif");
    //o c � o cipher, e envia p a string de output
    cos = new CipherOutputStream(fos, c);
    byte[] b = new byte[16];  
    int i = fis.read(b);
    while (i != -1) {
        cos.write(b, 0, i);
        i = fis.read(b);
    }
    cos.close();
    fos.close();
    
    
    //parte da TP3
     *keytool.exe -genkeypair -keysize 2048 -alias maria -keyalg rsa -keystore keystore.maria -storetype PKCS12
    
    //Como obter um certificado da keystore ?
    FileInputStream kfile = new FileInputStream("keystore.maria");  //keystore
    KeyStore kstore = KeyStore.getInstance("PKCS12");
    kstore.load(kfile, "matildesilva".toCharArray());           //password
    Certificate cert = kstore.getCertificate("maria");  //alias do utilizador

    //cipher com o RSA
    Cipher c2 = Cipher.getInstance("RSA");
    c2.init(Cipher.WRAP_MODE, cert);

    //cifrar com a chave AES a chave publica do certificado
    byte[] keyEncoded = c2.wrap(key);
    
    FileOutputStream kos = new FileOutputStream("a.key");
    kos.write(keyEncoded);
    kos.close();
    

    

    }
}

 */
