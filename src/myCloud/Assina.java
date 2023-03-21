package myCloud;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;

public class Assina {
	public static List<String> assina(List<String> filelist) throws Exception {
	    	List<String> files = new ArrayList<>();
			for (String filename : filelist) {
		        //como obter a chave privada de quem assina
		          FileInputStream kfile2 = new FileInputStream("keystore.maria");  //keystore
		          KeyStore kstore = KeyStore.getInstance("PKCS12");
		          kstore.load(kfile2, "mariapass".toCharArray());
		          Key myPrivateKey = kstore.getKey("maria", "mariapass".toCharArray());
		          
		            //fileinputstream p ficheiro a assinar
		          FileInputStream file  = new FileInputStream(filename);
		            byte[] buffer = new byte[16]; 
		            Signature s = Signature.getInstance("SHA256withRSA");
		            s.initSign((PrivateKey) myPrivateKey);
		            int n;
		            while((n=file.read(buffer))!=-1) {
		                if (n > 0) {
		                    s.update(buffer, 0, n);
		                }
		
		            }
		            FileOutputStream fileAssignature = new FileOutputStream(filename+".assinatura");
		            fileAssignature.write(s.sign());
		            fileAssignature.close();
		            file.close();
			}
			return files;

    }
}