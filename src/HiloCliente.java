import java.io.*;
import java.net.Socket;
import java.util.Date;

public class HiloCliente extends Thread {
    final String ANSI_CYAN = "\u001B[36m";

    InputStream inputStream;
    BufferedReader bufferedReader;
    OutputStream outputStream;
    BufferedWriter bufferedWriter;
    Socket socket;
    String user;

    public HiloCliente(Socket soc){this.socket=soc;}

    @Override
    public void run(){

        try {
            inputStream = socket.getInputStream();
            bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
            outputStream=socket.getOutputStream();
            bufferedWriter= new BufferedWriter(new OutputStreamWriter(outputStream));

            String line;
            user=bufferedReader.readLine();
            System.out.println("USER :"+user);
            Servidor.usuariosList.add(user); //añadimos el usuario a la lista
            //a continuacion lo que introduzca en el stream seran mensajes
            mandarListadoConectados();

            Servidor.broadcast("LOGIN-"+ user);
            //mientras tengas la ventana
            while ((line=bufferedReader.readLine())!=null){
                System.out.println( line);
                Servidor.broadcast("MENSAJE-"+user+"-"+line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            //Se ejecuta al finalizar el hilo
            try {
                System.out.println("SALIENDO DEL HILO");
                Servidor.broadcast("LOGOFF-" + user);
                imprimirUsuarios("ANTES");
                Servidor.usuariosList.remove(user);
                imprimirUsuarios("DESPUES");
                Servidor.imprimirLista(" finally 1");
                Servidor.clienteList.remove(this);
                mandarListadoConectados();
                Servidor.imprimirLista(" finally 2");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void mandarListadoConectados() throws IOException {
        StringBuilder conectados= new StringBuilder();
        //construimos un string con todos los usernames
        String delimitador="-";

        conectados.append("LISTADO");
        conectados.append(delimitador);

        for (String usuario:Servidor.usuariosList ) {
            conectados.append(usuario);
            conectados.append(delimitador);
        }
        String resultado=conectados.toString();
        //y se lo pasamos a todos los clientes
        System.out.println(resultado);
        imprimirUsuarios("ANTES DEL BROADCAST");
        Servidor.imprimirLista(" mandarListadoConectados()");
        Servidor.broadcast(resultado);
    }
    public void imprimirUsuarios(String msg){
        System.out.println(msg);
        StringBuilder conectados= new StringBuilder();
        //construimos un string con todos los usernames
        String delimitador="-";

        conectados.append("LISTADO");
        conectados.append(delimitador);

        for (String usuario:Servidor.usuariosList ) {
            conectados.append(usuario);
            conectados.append(delimitador);
        }
        String resultado=conectados.toString();
        System.out.println(resultado);

    }


}
