/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/

import java.rmi.*;


public interface ClienteRMIInterface extends Remote {
	
	public void addNotification(String note) throws RemoteException;
	//public String getUsername() throws RemoteException;
       
}
