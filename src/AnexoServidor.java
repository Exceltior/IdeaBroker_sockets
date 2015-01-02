/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/


import java.io.FileOutputStream;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.net.ServerSocket;  
import java.net.Socket;  
  
 public class AnexoServidor extends Thread
 {
 	private String nome;

 	public AnexoServidor(String _nome)
 	{
 		nome = _nome;
 		this.start();
 	}

 	public void run()
 	{
 		try
 		{
            System.out.println("Vou ligar o socket do Anexo...\n");

 			ServerSocket servidor_socket = new ServerSocket(3335);

 			Socket canal = servidor_socket.accept();
 			salvar_ficheiro(canal);

 			servidor_socket.close();
 			return;
 		} catch (Exception e){
 			e.printStackTrace();

 		}
 	}

 	private void salvar_ficheiro(Socket socket)
 	{
 		try {

    	ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  
        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  
        FileOutputStream fos = null;  
        byte [] buffer = new byte[6000];  
  
        // 1. Le o nome ficheiro 
        Object o = ois.readObject();  
  
            fos = new FileOutputStream(nome);  
   
  
        // 2. le o ficheiro até ao fim
        Integer bytesRead = 0;  
  
        do {  
            o = ois.readObject();  

            bytesRead = (Integer)o;  
  
            o = ois.readObject();  

            buffer = (byte[])o;  
  
            // 3. escreve output no ficheiro  
            fos.write(buffer, 0, bytesRead);  
        } while (bytesRead == 6000);  
  
        fos.close();  
        ois.close();  
        oos.close();  
        } catch (Exception e) { e.printStackTrace(); }

 	} 




 }