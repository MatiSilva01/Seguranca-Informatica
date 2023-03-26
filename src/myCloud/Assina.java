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
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.List;


public class Assina {
	public static List<String> assina(List<String> filelist) throws Exception {
	    	List<String> files = new ArrayList<>();
			for (String filename : filelist) {
		        //como obter a chave privada de quem assina 
		          FileInputStream kfile2 = new FileInputStream("keystore.mariaCloud");  //keystore
		          KeyStore kstore = KeyStore.getInstance("PKCS12");
		          kstore.load(kfile2, "mariapass".toCharArray());
		          Key myPrivateKey = kstore.getKey("maria", "mariapass".toCharArray());
		          
		            //fileinputstream p ficheiro a assinar
		          try {
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
			            String signatureFileName = filename + ".assinatura";
			            files.add(signatureFileName);
			            file.close();
				
			} catch (FileNotFoundException e) {
    			System.out.println("Nao foi possivel assinar o ficheiro " + filename + " pois nao foi encontrado");
            }}
			return files;

    }
}