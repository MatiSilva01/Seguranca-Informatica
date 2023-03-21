package myCloud;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.security.Key;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;



public class VerificaAssinatura {
    public static Boolean verificaAssinatura(List<String> filelist) throws Exception {
	    	List<String> files = new ArrayList<>();
			int j = 0;
			Boolean verify = true;
			for (String filename : filelist) {				
				
				// cria um novo nome de arquivo com a extensão ".assinado"
				String newFileName = filename + ".assinado";

				// cria uma nova instância de FileInputStream para o arquivo original
				FileInputStream originalFileInputStream = new FileInputStream(filename);

				// cria uma nova instância de FileOutputStream para o arquivo de cópia com a extensão ".assinado"
				FileOutputStream copyFileOutputStream = new FileOutputStream(newFileName);

				// lê e escreve cada byte do arquivo original no arquivo de cópia
				int bytesRead;
				byte[] buffer2 = new byte[1024];
				while ((bytesRead = originalFileInputStream.read(buffer2)) != -1) {
				    copyFileOutputStream.write(buffer2, 0, bytesRead);
				}

				// fecha as streams de entrada e saída
				originalFileInputStream.close();
				copyFileOutputStream.close();

				// adiciona o novo nome de arquivo à lista "files"
				files.add(newFileName);
				
				
				
			//TODO enviar ficheiro copia - .assinado
	        //como obter a chave publica de quem assina
	          FileInputStream kfile2 = new FileInputStream("keystore.maria");  //keystore
	          KeyStore kstore = KeyStore.getInstance("PKCS12");
	          kstore.load(kfile2, "mariapass".toCharArray());
	          Certificate cert  = kstore.getCertificate("maria");
          
            //fileinputstream p ficheiro a assinar
            FileInputStream file  = new FileInputStream(filename); //porque
            byte[] buffer = new byte[16]; 
            Signature s = Signature.getInstance("SHA256withRSA");
            s.initVerify(cert);
            int n;
            while((n=file.read(buffer))!=-1) {
                s.update(buffer,0,n);
            }
            byte[] assinatura = new byte[256]; //melhorar 
            FileInputStream fileAssignature = new FileInputStream(filename+".assinatura");
            fileAssignature.read(assinatura);
            boolean b = s.verify(assinatura);
            if (b == false){
                verify = false;
            }
            System.out.println(b);
            fileAssignature.close();
            file.close();
			}
			return verify;

    }
}