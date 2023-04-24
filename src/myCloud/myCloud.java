//Seguranca-Informatica
//Projetos SI - grupo 16
//
//Raphael Marques - 55135
//Ruben Silva - 56911
//Matilde Silva - 56895

package myCloud;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.NoSuchPaddingException;
import java.io.InputStream;

public class myCloud {

    public static void main(String[] args)  {
        String[] host = null;
        String operation;
        List<String> filelist;
        filelist = new ArrayList<>();
        String[] user = new String[] {"maria", "maria2", "mariapass"};
        
        
       
        genKeyStore(user);
        
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
                    try { 
						sendFiles(Cifra.cipherFile(filelist),host[0], host[1], operation);
					} catch (NoSuchAlgorithmException e) { 
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
                  
            } else if (args[i].equals("-s")) {
                operation = args[i];
                filelist = new ArrayList<>();
                for (int j = 1; i + j < args.length; j++) {
                    String f = args[i + j];
                    filelist.add(f);
                    }
                    try {
						sendFiles(Assina.assina(filelist), host[0], host[1], operation);
						sendFiles(filelist , host[0], host[1], operation);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
                
                
            } else if (args[i].equals("-e")) {
                operation = args[i];
                filelist = new ArrayList<>();
                for (int j = 1; i + j < args.length; j++) {
                    String f = args[i + j];
                    filelist.add(f);
                    }
                    try { 
						sendFiles(Cifra.cipherFile(filelist),host[0], host[1], operation);
						sendFiles(Assina.assina(filelist), host[0], host[1], operation);
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					} catch (NoSuchPaddingException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
            } else if (args[i].equals("-g")) {
                operation = args[i];
                filelist = new ArrayList<>();
                for (int j = 1; i + j < args.length; j++) {
                    String f = args[i + j];
                    filelist.add(f);
                }
                Socket echoSocket;
                
				try {
					echoSocket = new Socket(host[0], Integer.parseInt(host[1]));
			 		ObjectOutputStream outStream = new ObjectOutputStream(echoSocket.getOutputStream());
					ObjectInputStream inStream = new ObjectInputStream(echoSocket.getInputStream());
					InputStream in = null;
					OutputStream out = null;
					List<String> cifradoList;
			    	List<String> keyList;
			    	cifradoList = new ArrayList<>();
					keyList = new ArrayList<>();
					outStream.writeObject((String) operation);
			        outStream.writeObject(filelist);
			        int filenumber = (int) inStream.readObject();
					if(filenumber == 0){
                        System.out.println("Nenhum ficheiro foi encontrado no servidor.");
                    }
					// RECEIVE FILE
					for (int j = 0; j < filenumber; j++) {
						String filename = (String) inStream.readObject();
						int filesize = (int) inStream.readObject();
						File newDir = new File(filename);
						newDir.createNewFile();

						 try {
					            in = echoSocket.getInputStream();
					        } catch (IOException ex) {
					            System.out.println("Can't get socket input stream. ");
					        }
	
					        try {
					            out = new FileOutputStream(filename);
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
					        
					        if (filename.endsWith(".cifrado")){
                                cifradoList.add(filename);
                            } else if (filename.endsWith(".chave_secreta")){
                                keyList.add(filename);
                            }
                            else if (filename.endsWith(".assinatura")){
                                if(VerificaAssinatura.verificaAssinatura(filename)){
                                    System.out.println("Assinatura valida para o ficheiro: " + filename);
                                } else{
                                    System.out.println("Assinatura nao valida para o ficheiro: " + filename);
                                }
                            }
  					}
					for (int h = 0; h < cifradoList.size(); h++) {
                                Decifra.decipherFile(cifradoList.get(h), keyList.get(h));
                    }
					
				} catch (NumberFormatException | IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
		       
                
               
                
            }
            i++;
        }
      
		}
    	
	    public static void genKeyStore(String[] user) {
			String command = "keytool -genkeypair -noprompt -alias " + user[0]
					+ " -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore keystore." + user[0] + "Cloud -dname \" CN=" + user[1]
					+ ", OU=FC, O=UL, L=Lisboa, ST=LS, C=PT \" -storepass " + user[2] + " -keypass " + user[2];
		String[] cmd = command.split(" ");
			System.out.println("*************************************");
			try {
				Runtime.getRuntime().exec(cmd);
				System.out.println("-------------------------------------");
	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
        public static void sendFiles(List<String> filelist, String address, String socket, String operation) throws IOException{
            Socket echoSocket = new Socket(address, Integer.parseInt(socket));
	        ObjectOutputStream outStream = new ObjectOutputStream(echoSocket.getOutputStream());
			ObjectInputStream inStream = new ObjectInputStream(echoSocket.getInputStream());
			BufferedInputStream bis = null;
			InputStream in = null;
			OutputStream out = null;
			List<String> filesfoundtosend = new ArrayList<>();
			for (String fname: filelist) {
				File filetemp = new File(fname);
				if (filetemp.exists()) {
					filesfoundtosend.add(fname);
				}	else {
					System.out.println("O ficheiro " + fname + " nao foi encontrado.");
				}
			}
			outStream.writeObject((String) operation);
	        outStream.writeObject((int) filesfoundtosend.size());
			for (String fname: filesfoundtosend) {
                try {
                    
                	File file = new File(fname);
                    in = new FileInputStream(file);
			        
			        bis = new BufferedInputStream(in);
					outStream.writeObject(fname);
			        // Get the size of the file
			        long length = file.length();
			        outStream.writeObject((int) length);
			        byte[] bytes = new byte[(int) length];
			       
			        
			        
			        bis.read(bytes, 0, bytes.length);
			        
			        
			        out = echoSocket.getOutputStream();
			        	        	     
			        out.write(bytes, 0, bytes.length);
			        out.flush();
			        Boolean check = (Boolean) inStream.readObject();
			        if (check){
                        System.out.println(fname + " sent!");
                    } else {
                        System.out.println(fname + " already exists on the server");
                    }
			        
			     } catch (FileNotFoundException e) {
			    	 System.out.println("O ficheiro " + fname + " nao foi encontrado");
			     } catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
			out.close();
	        in.close();
	        echoSocket.close();

        }

        
}
