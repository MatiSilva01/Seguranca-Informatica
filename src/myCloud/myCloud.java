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
import java.util.ArrayList;
import java.util.List;
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
        filelist.add("livro.pdf");
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
        sendFiles(filelist, "127.0.0.1", 23457);
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
    //o c é o cipher, e envia p a string de output
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

 keytool -genkeypair -keysize 2048 -alias maria -keyalg rsa -keystore keystore.maria -storetype PKCS12
//password
 * repetir pass
 * primeiro e ultimo nome
 * organizacao unit
 * organizao
 * cidade
 * estado
 * pt
 * Is CN=matilde silva, OU=fcul, O=fcul, L=lisboa, ST=kaa, C=pt correct?

public static void genKeyStore(String[] user) {
		String command = "keytool -genkeypair -alias " + user[0]
				+ " -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore." + user[0] + " -dname CN=" + user[1]
				+ " -dname OU=FC -dname O=UL -dname L=Lisboa -dname ST=LS -dname C=PT" + " -keypass " + user[2];

		String[] cmd = command.split(" ");
		try {
			Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
 */