/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/

    import java.io.File;  
    import java.io.FileInputStream;  
    import java.io.ObjectInputStream;  
    import java.io.ObjectOutputStream;  
    import java.net.Socket;  
    import java.util.Arrays;  
    import java.net.*;

    public class AnexoCliente extends Thread {

    	private String caminho;
        private String nome;
    	private String ip_servidor;


    	public AnexoCliente(String _caminho, String _nome, String _ip_servidor)
    	{
    		caminho = _caminho;
            nome = _nome;
    		ip_servidor= _ip_servidor;
    		this.start();
    	}

    	public void run()
    	{

    		String nome_ficheiro = null;
    		
            try
    		{
    			nome_ficheiro = caminho;

                //System.out.println("IP do servidor == "+ip_servidor+"\n");

    			File file = new File(nome_ficheiro);
    			Socket socket = new Socket(ip_servidor, 3335);
    			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());  
            	ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());  

            	oos.writeObject(file.getName());

            	FileInputStream fis = new FileInputStream(file);  
	            byte [] buffer = new byte[6000];  
	            Integer bytesRead = 0;

	            while((bytesRead = fis.read(buffer)) > 0)
	            {
	                oos.writeObject(bytesRead);  
	                oos.writeObject(Arrays.copyOf(buffer, buffer.length));  
	            } 

	            oos.close();  
	            ois.close();  
	            socket.close();
	            return;
	            
    		}catch (Exception e) { 

            	//e.printStackTrace();
    			System.out.println("Erro na anexação.");

            }  
    	}


    }