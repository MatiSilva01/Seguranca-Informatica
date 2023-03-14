package myCloudServer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class myCloudServer {

	public static void main(String[] args) {
		/**
		 * if (args.length != 1) {
		 * System.err.println("Usage: java myCloudServer port");
		 * System.exit(1);
		 * }
		 */
		System.out.println("servidor: main");
		int portNumber = 23457;
		// Integer.parseInt(args[0]);
		myCloudServer server = new myCloudServer();
		server.startServer(portNumber);

	}

	public void startServer(int port) {

		ServerSocket sSoc = null;

		try {
			sSoc = new ServerSocket(port);
		} catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(-1);
		}

		while (true) {
			try {
				Socket inSoc = sSoc.accept();
				ServerThread newServerThread = new ServerThread(inSoc);
				newServerThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	class ServerThread extends Thread {

		private Socket socket = null;

		ServerThread(Socket inSoc) {
			socket = inSoc;
			System.out.println("thread do server para cada cliente");
		}

		public void run() {

			try {
				// cria as duas strings
				ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
				ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());

				// AQUIIII
				// novo para receber file
				long size = inStream.readLong();
				// le do socket e escreve para ficheiro
				// crio o ficheito
				FileOutputStream novo = new FileOutputStream("cc.pdf", false);
				int n;
				// o nosso buffer so aguenta 1024
				byte buffer[] = new byte[1024];
				// enquanto nao receber tudo
				while (size > 0) {
					// le
					// se size maior q 1024 le so 1024, se nao le tudo e converte de long para int

					n = inStream.read(buffer, 0, (int) (size > 1024 ? 1024 : size));

					// escreve
					if (n > 0) {
					    novo.write(buffer, 0, n);
					}					size -= n;
				}

				outStream.close();
				inStream.close();

				socket.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}