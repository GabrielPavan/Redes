package Client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    static Socket socket = null;
    static PrintWriter out = null;
    static BufferedReader in = null;
    static BufferedReader stdIn = null;

    private static void setConnection(String ip, int port) throws Exception {
        socket = new Socket(ip, port);
        System.out.println("Conectado ao servidor " + ip + ":" + port + "\n");

        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        stdIn = new BufferedReader(new InputStreamReader(System.in));
    }

    public static void main(String[] args) {
        int menu = 0;
        String ip = "";
        int port = -1;

        Scanner scanner = new Scanner(System.in);
        System.out.println("---- Codigo Client ----\n");
        System.out.println("Defina o host e a porta que deseja conectar:");
        
        while (menu != -1) {
            System.out.print("IP: ");
            ip = scanner.nextLine();
            System.out.print("Porta: ");
            port = scanner.nextInt();
            scanner.nextLine();

            try {
                setConnection(ip, port);
                menu = -1;
            } catch (Exception e) {
                System.out.println("Erro ao conectar! Tente novamente.");
            } 
        }

        try {
            Thread readThread = new Thread(() -> {
                try {
                    String mensagemServer;
                    while ((mensagemServer = in.readLine()) != null) {
                        System.out.println(mensagemServer);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao ler do servidor: " + e.getMessage());
                }
            });
            readThread.start();

            String userInput;
            while ((userInput = stdIn.readLine()) != null) {
                out.println(userInput);
            }
        } catch (Exception e) {
            System.out.println("Erro na comunicação com o servidor: " + e.getMessage());
        } finally {
            try {
                if (socket != null) socket.close();
                if (in != null) in.close();
                if (out != null) out.close();
                if (stdIn != null) stdIn.close();
                scanner.close();
            } catch (Exception e) {
                System.out.println("Erro ao fechar recursos: " + e.getMessage());
            }
        }
    }
}