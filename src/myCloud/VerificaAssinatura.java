//Seguranca-Informatica
//Projetos SI - grupo 16
//
//Raphael Marques - 55135
//Ruben Silva - 56911
//Matilde Silva - 56895

package myCloud;

import java.io.FileInputStream;
import java.security.cert.Certificate;
import java.security.KeyStore;
import java.security.Signature;

public class VerificaAssinatura {
	public static boolean verificaAssinatura(String filename) throws Exception {
	    boolean verify = true;

	    // como obter a chave p�blica de quem assina
	    FileInputStream kfile2 = new FileInputStream("keystore.mariaCloud"); // keystore
	    KeyStore kstore = KeyStore.getInstance("PKCS12");
	    kstore.load(kfile2, "mariapass".toCharArray());
	    Certificate cert = kstore.getCertificate("maria");
	    //tirar a extensao .assinatura para obter o nome do ficheiro original
	    String nomeArquivo = filename.replace(".assinatura", "");
	    FileInputStream file = new FileInputStream(nomeArquivo);
	    byte[] buffer = new byte[16]; 
	    Signature s = Signature.getInstance("SHA256withRSA");
	    s.initVerify(cert);
	    int n;
	    while ((n = file.read(buffer)) != -1) {
	        s.update(buffer, 0, n);
	    }
	    FileInputStream fileAssinatura = new FileInputStream(filename);
	    byte[] assinatura = new byte[256]; //TODO confirmar?
	   
	    if ((fileAssinatura.read(assinatura)) != assinatura.length) {
	        verify = false; 
	    } else {
	    		 verify = s.verify(assinatura);
	    }
	    fileAssinatura.close();
	    file.close();
	    return verify;
	    }
}
