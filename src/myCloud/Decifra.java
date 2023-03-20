package myCloud;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Decifra {

	public static List<String> decipherFile(List<String> filelist) throws Exception {
		
        int j = 0;
        List<String> files = new ArrayList<>();
        
        for(String filename : filelist){

            //files.add(filename + ".decifrado");

            FileInputStream fkey = new FileInputStream(filelist.get(j)+".chave_secreta");
            byte[] chave = new byte[256];
            fkey.read(chave);

        
            //decifrar chave
            //1 obter chave privada
                FileInputStream kfile = new FileInputStream("keystore.maria");  //keystore
                KeyStore kstore = KeyStore.getInstance("PKCS12");
                kstore.load(kfile, "mariapass".toCharArray());
                Key myPrivateKey = kstore.getKey("maria", "mariapass".toCharArray());
            //2 criar o cipher para decifrar a chave
            Cipher c2 = Cipher.getInstance("RSA");
                c2.init(Cipher.UNWRAP_MODE, myPrivateKey);
            //3 decifrar
                SecretKey aesKey = (SecretKey) c2.unwrap(chave, "AES", Cipher.SECRET_KEY);
        
            //transformar o bytearray numa key
            // SecretKeySpec keySpec2 = new SecretKeySpec(chave, "AES");
                Cipher c = Cipher.getInstance("AES");
                c.init(Cipher.DECRYPT_MODE, aesKey);    //SecretKeySpec é subclasse de secretKey
        
                FileInputStream fis;
                FileOutputStream fos;
                CipherInputStream cis;
        
                //ler do ficheiro a.txt
                fis = new FileInputStream(filelist.get(j)+".cifrado");
                cis = new CipherInputStream(fis, c);
                fos = new FileOutputStream("decifrado"+filelist.get(j));
                
        
                byte[] b = new byte[16];
                int i = cis.read(b);
                while (i != -1) {
                    fos.write(b, 0, i);
                    i = cis.read(b);
                }
                cis.close();
                fos.close();
                j++;
        }
        return files;
	}
}