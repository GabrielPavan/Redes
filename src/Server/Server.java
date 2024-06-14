package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String[] args) {
        ServerSocket servidor = null;
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            servidor = new ServerSocket(50569);
            System.out.println("Servidor iniciado na porta 50569...");

            socket = servidor.accept();
            System.out.println("Cliente conectado: " + socket.getInetAddress());

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String mensagemRecebida;
            while ((mensagemRecebida = in.readLine()) != null) {
                System.out.println("Mensagem recebida: " + mensagemRecebida);
                out.println("Servidor: " + mensagemRecebida);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (socket != null) socket.close();
                if (servidor != null) servidor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}