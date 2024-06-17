package Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private static Map<String, Integer> storeItems = new HashMap<>();

    static {
        storeItems.put("Chocolate", 10);
        storeItems.put("Agua", 5);
        storeItems.put("Pipoca", 2);
    }

    public static void main(String[] args) {
        ServerSocket servidor = null;
        Socket socket = null;
        PrintWriter out = null;
        BufferedReader in = null;

        try {
            servidor = new ServerSocket(50569);
            System.out.println("Servidor iniciado na porta 50569");
            socket = servidor.accept();
            System.out.println("Cliente conectado: " + socket.getInetAddress());

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println(" --- Loja no console --- \n");
            out.println(" Comandos disponíveis:");
            out.println(" LIST: Para listar os itens disponíveis");
            out.println(" BUY <item>: Para comprar um item");
            out.println(" Digite um comando: ");

            String mensagemCliente;
            while ((mensagemCliente = in.readLine()) != null) {
                System.out.println("Cliente: " + mensagemCliente);
                handleClientMessage(mensagemCliente, out);
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

    private static void handleClientMessage(String mensagemCliente, PrintWriter out) {
        String[] tokens = mensagemCliente.split(" ");
        String command = tokens[0];

        switch (command.toUpperCase()) {
            case "LIST":
                listItems(out);
                break;
            case "BUY":
                if (tokens.length < 2) {
                    out.println("Comando inválido. Use: BUY <item>");
                } else {
                    String item = tokens[1];
                    buyItem(item, out);
                }
                break;
            default:
                out.println("Comando desconhecido. Comandos disponíveis: LIST, BUY <item>");
        }
    }

    private static void listItems(PrintWriter out) {
        out.println("Itens disponíveis na loja:");
        for (Map.Entry<String, Integer> entry : storeItems.entrySet()) {
            out.printf("%s: %d unidades%n", entry.getKey(), entry.getValue());
        }
    }
    private static void buyItem(String item, PrintWriter out) {
        if (storeItems.containsKey(item)) {
            int stock = storeItems.get(item);
            if (stock > 0) {
                storeItems.put(item, stock - 1);
                out.println("Você comprou um " + item);
            } else {
                out.println("Desculpe, " + item + " está fora de estoque.");
            }
        } else {
            out.println("Desculpe, " + item + " não está disponível na loja.");
        }
    }
}