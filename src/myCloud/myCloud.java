package myCloud;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class myCloud {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException {
      //  if (args.length < 3 || args[0] != "-a") { // ficheiros?
       //     System.err.println("Usage: java myCloud -a <server address> -c|-s|-e|-g <filename(s)>");
       //     System.exit(1);
      //  }
        String[] host = null;
        String operation;
        List<String> filelist;
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

        // primeira coisa criar o socket
        String serverAdress = "127.0.0.1";
        int socket = 23457;
        Socket echoSocket = new Socket(serverAdress, socket);

        // criar strings object
        ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
        ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());

        // e um pdf
        File myF = new File("pdf.pdf");
        // tamanho do ficheiro
        long size = myF.length();
        out.write((int) size);

        byte buffer[] = new byte[1024];
        int n;
        // ler do ficheiro / abrir
        FileInputStream fStream = new FileInputStream(myF);

        // enquanto ler do ficheiro e enviar p o socket
        while ((n = fStream.read(buffer, 0, 1024)) > 0) {
            // dizer q quero escrever p o socket
            out.write(buffer, 0, n);
        }
        fStream.close();

        out.close();
        in.close();
        echoSocket.close();
    }

}