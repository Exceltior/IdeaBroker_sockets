/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/


import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class ClienteUDP extends Thread{

    DatagramSocket aSocket = null;
    DatagramPacket recebe, envia;
    String texto = "ping", adress;
    byte[] msgIn = new byte[10];
    byte[] msgOut;
    int serverPort = 1235;
	IdeiaMethods h;

    public ClienteUDP(String adress) {
        this.adress = adress;
         
    }


    public void run() {

       /* try {
		        //ESTE H tem todos os métodos do servidor
    		System.setProperty("java.rmi.server.hostname", "10.42.0.1");

            h = (IdeiaMethods) Naming.lookup("//10.42.0.1:1099/Ideia");

		        h.sayHello();
		       } catch (Exception e5) {
		         	System.out.println("Exceptione in main: " + e5);
		       }*/

        try {
                    aSocket = new DatagramSocket(); 
                    System.out.println("Servidor Secundário Pronto!"); 
                    aSocket.setSoTimeout(3000); 
                    InetAddress aHost = InetAddress.getByName(adress); 
                    msgOut = texto.getBytes(); 
            	while (true) {  //  enquanto Backup
                // Envia
                try {
                    
                    envia = new DatagramPacket(msgOut, msgOut.length, aHost, serverPort);
                    aSocket.send(envia);

                    recebe = new DatagramPacket(msgIn, msgIn.length);           // cria um datagram packet
                    aSocket.receive(recebe);                                // fica a aguardar dados
                    String conteudo = new String(recebe.getData(), 0, recebe.getLength());

                } catch (SocketException e) {
                    System.out.println("Socket Exception");
                } catch (java.net.UnknownHostException e) {
                    System.out.println("Unknown Host Exception");
                } catch (IOException e) {

                    System.out.println("Mudar para servidor primario...");
                    System.out.println("Servidor Primario Pronto!");
                    IdeaBrokerServerTCP.type = "P";
                    try {
                        //h.loaddados();
                    } catch (Exception e6) {
                        System.out.println("Exception in main: " + e6);
                    }

                    try {
                        aSocket.setSoTimeout(0);
                    } catch (SocketException ex) {
                        System.out.println("Socket Exception");
                    }
                    break;
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } //while

            aSocket = new DatagramSocket(serverPort);
        } catch (SocketException ex) {
            System.out.println("Socket Exception");
        } catch (java.net.UnknownHostException e) {
            System.out.println("Unknown Host Exception");
        }

        while (true) {  // While enquanto Master
            
            // RECEBE
            try {
                recebe = new DatagramPacket(msgIn, msgIn.length);           // cria um datagram packet
                aSocket.receive(recebe);                                // fica a aguardar dados
                String text = new String(recebe.getData(), 0, recebe.getLength());

                msgOut = IdeaBrokerServerTCP.type.getBytes();
                envia = new DatagramPacket(msgOut, msgOut.length, recebe.getAddress(), recebe.getPort());
                aSocket.send(envia);
            } catch (Exception e) {
                e.printStackTrace();


            }


        } //while
    }


}