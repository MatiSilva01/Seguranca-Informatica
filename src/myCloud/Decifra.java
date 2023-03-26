//Seguranca-Informatica
//Projetos SI - grupo 16
//
//Raphael Marques - 55135
//Ruben Silva - 56911
//Matilde Silva - 56895

package myCloud;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;

public class Decifra {

	public static String decipherFile(String filename, String keyname) throws Exception {
            FileInputStream fkey = new FileInputStream(keyname);
            byte[] chave = new byte[256];
            fkey.read(chave);
            FileInputStream kfile = new FileInputStream("keystore.mariaCloud");  //keystore
            KeyStore kstore = KeyStore.getInstance("PKCS12");
            kstore.load(kfile, "mariapass".toCharArray());
            Key myPrivateKey = kstore.getKey("maria", "mariapass".toCharArray());
            Cipher c2 = Cipher.getInstance("RSA");
            c2.init(Cipher.UNWRAP_MODE, myPrivateKey);
            SecretKey aesKey = (SecretKey) c2.unwrap(chave, "AES", Cipher.SECRET_KEY);

            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, aesKey);    //SecretKeySpec � subclasse de secretKey
    
            FileInputStream fis;
            FileOutputStream fos;
            CipherInputStream cis;
    
            //ler do ficheiro a.txt
            try {
	                fis = new FileInputStream(filename);
	                cis = new CipherInputStream(fis, c);
	                fos = new FileOutputStream("decifrado-"+filename.replace(".cifrado", ""));
	                
	        
	                byte[] b = new byte[16];
	                int i = cis.read(b);
	                while (i != -1) {
	                    fos.write(b, 0, i);
	                    i = cis.read(b);
	                }
	                cis.close();
	                fos.close();
                    
	                
                } catch (FileNotFoundException e) {
        			System.out.println("O ficheiro " + filename + " n�o foi encontrado");
                }
                fkey.close();
                String files = "decifrado-" + filename.replace(".cifrado", "");
                
        return files;
	}
	
}