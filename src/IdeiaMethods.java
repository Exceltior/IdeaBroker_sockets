import java.rmi.*;
import java.util.ArrayList;

public interface IdeiaMethods extends Remote {
	
	public void loaddados() throws java.rmi.RemoteException;

	public String sayHello() throws java.rmi.RemoteException;

	public void remote_print(String s) throws java.rmi.RemoteException;

	public Utilizador login(String user,String password) throws java.rmi.RemoteException;

	public Utilizador registar(String username,String password) throws java.rmi.RemoteException;
	
	public ArrayList<Topico> gettopicos() throws java.rmi.RemoteException;
	
	public int adicionartopico(Topico topico) throws java.rmi.RemoteException;
		
	public Utilizador procuraUtilizador(String util) throws java.rmi.RemoteException;

	public void Notificacao_vista(int indice)  throws java.rmi.RemoteException;

	public void adicionarNotasAgendadas(String notificacao, String user) throws java.rmi.RemoteException;

	public void removerNotasAgendadas(String notificacao, String user) throws java.rmi.RemoteException;
	
	public String verficarNotificacao(String _user) throws java.rmi.RemoteException;

	public int criarideia(int idtopico,String texto,String autor,Anexo anexo) throws java.rmi.RemoteException;
	
	public int responderideia(String natureza,int idtopico,int idideia,String texto,String autor,Anexo anexo) throws java.rmi.RemoteException;

	public String apagarideia(int id_topico,int id_ideia,String username) throws java.rmi.RemoteException;
	
	public int efectuarcompra(String comprador,String vendedor,int idtopico,int idideia,int idaccao,int percentagem,int precorevenda) throws java.rmi.RemoteException;
	
	public String dadosutilizador(String username) throws java.rmi.RemoteException;
	
	public String historicoutilizador(String username) throws java.rmi.RemoteException;
	
	
}