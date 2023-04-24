//Seguranca-Informatica
//Projetos SI - grupo 16
//
//Raphael Marques - 55135
//Ruben Silva - 56911
//Matilde Silva - 56895

package myCloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Cifra {

	public static List<String> cipherFile(List<String> filelist) throws NoSuchAlgorithmException, NoSuchPaddingException {
		//gerar uma chave aleatoria para utilizar com o AES
		KeyGenerator kg = null;
		FileInputStream fis;
		FileOutputStream fos;
		CipherOutputStream cos;
		List<String> files = new ArrayList<>();

		for (String filename : filelist) {
			File file = new File(filename);
			if (file.exists()) {
		try {
			kg = KeyGenerator.getInstance("AES");
			kg.init(128);
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	
		//gera chave
		SecretKey key = kg.generateKey();
		Cipher c = Cipher.getInstance("AES");
		try {
			c.init(Cipher.ENCRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		
		//ler do ficheiro filename
		try {
			fis = new FileInputStream(filename);
			//cifra e mete no a.cif
			fos = new FileOutputStream(filename+".cifrado");
	        files.add(filename + ".cifrado"); 
			cos = new CipherOutputStream(fos, c);
			byte[] b = new byte[16];  
			int i = fis.read(b);
			while (i != -1) {
				cos.write(b, 0, i);

				i = fis.read(b);
			}
			fis.close();
			cos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("O ficheiro " + filename + " n�o foi encontrado");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		FileInputStream kfile;
		try {
			kfile = new FileInputStream("keystore.mariaCloud");
			KeyStore kstore = KeyStore.getInstance("PKCS12");
			kstore.load(kfile, "mariapass".toCharArray());           //password
			Certificate cert = kstore.getCertificate("maria");  //alias do utilizador

			//cipher com o RSA
			Cipher c2 = Cipher.getInstance("RSA");
			c2.init(Cipher.WRAP_MODE, cert);

			byte[] keyEncoded = c2.wrap(key);
			FileOutputStream kos = new FileOutputStream(filename+".chave_secreta");
	        files.add(filename + ".chave_secreta"); 
			kos.write(keyEncoded);
			kos.close();
			kfile.close();
		} catch (FileNotFoundException e) {
			System.out.println("O ficheiro " + filename + " nao foi encontrado");
		}  catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		}
		}	else {
			System.out.println("O ficheiro " + filename + " nao foi encontrado.");
		}
		}
		return files;

	}

	

}
