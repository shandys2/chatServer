import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {

    public static List<HiloCliente> clienteList;
    public static List<String> usuariosList;
    public static void main(String[] args) {

        ServerSocket serverSocket=null;

        clienteList= new ArrayList<>();
        usuariosList= new ArrayList<>();
        System.out.println("conectado");
        try {
             serverSocket= new ServerSocket(5555);
             while (true){
                 Socket socket=serverSocket.accept();
                 HiloCliente cliente= new HiloCliente(socket);
                 cliente.start();
                 clienteList.add(cliente);
             }
        }catch (Exception e){
           System.out.println(e.getMessage());
        }
    }

    static OutputStream outputStream;
    static BufferedWriter bufferedWriter;

    public static void broadcast(String msg) throws IOException {

        for (HiloCliente cliente:clienteList) {
            outputStream=cliente.socket.getOutputStream();
            bufferedWriter= new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write(msg);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }

    public static void imprimirLista(String msg){
        System.out.println("IMPRIMIMENTO LISTACLIENTES" + msg);
        for (HiloCliente cliente:clienteList) {
            System.out.println(cliente.user);
        }
    }

}
