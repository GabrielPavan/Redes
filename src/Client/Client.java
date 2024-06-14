package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) {
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader stdIn = null;

        try {
            socket = new Socket("localhost", 50569);
            System.out.println("Conectado ao servidor na porta 12345...");
            
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            stdIn = new BufferedReader(new InputStreamReader(System.in));

            String mensagemParaEnviar;
            while ((mensagemParaEnviar = stdIn.readLine()) != null) {
                out.println(mensagemParaEnviar);
                System.out.println("Resposta do servidor: " + in.readLine());
            }

        } catch (UnknownHostException e) {
            System.err.println("Não foi possível conectar ao host.");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) in.close();
                if (out != null) out.close();
                if (stdIn != null) stdIn.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}