package myCloud;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.crypto.CipherInputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Cifra {

	public static List<String> cipherFile(List<String> filelist) throws NoSuchAlgorithmException, NoSuchPaddingException {
		
		//gerar uma chave aleatoria para utilizar com o AES
		KeyGenerator kg = null;
		FileInputStream fis;
		FileOutputStream fos;
		CipherOutputStream cos;
		List<String> files = new ArrayList<>();
		for (String filename: filelist) {
			files.add(filename+ ".cifrado");
			files.add(filename+ ".chave_secreta");
		
		
		try {
			kg = KeyGenerator.getInstance("AES");
			//numero de bits da chave
			kg.init(128);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//gera chave
		SecretKey key = kg.generateKey();
		Cipher c = Cipher.getInstance("AES");
		try {
			//cipher para cifrar com a chave de cima
			c.init(Cipher.ENCRYPT_MODE, key);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//ler do ficheiro filename
		try {
			fis = new FileInputStream(filename);
			//cifra e mete no a.cif
			fos = new FileOutputStream(files.get(0));
			//o c Ã© o cipher, e envia p a string de output
			cos = new CipherOutputStream(fos, c);
			byte[] b = new byte[16];  
			int i = fis.read(b);
			while (i != -1) {
				cos.write(b, 0, i);

				i = fis.read(b);
			}
			cos.close();
			fos.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//parte da TP3
		//Como obter um certificado da keystore ?
		FileInputStream kfile;
		try {
			kfile = new FileInputStream("keystore.maria");
			KeyStore kstore = KeyStore.getInstance("PKCS12");
			kstore.load(kfile, "mariapass".toCharArray());           //password
			Certificate cert = kstore.getCertificate("maria2");  //alias do utilizador

			//cipher com o RSA
			Cipher c2 = Cipher.getInstance("RSA");
			c2.init(Cipher.WRAP_MODE, cert);

			//cifrar com a chave AES a chave publica do certificado
			byte[] keyEncoded = c2.wrap(key);
			
			FileOutputStream kos = new FileOutputStream(files.get(1));
			kos.write(keyEncoded);
			kos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
		return files;

	}

	

}
