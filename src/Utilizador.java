
import java.io.Serializable; 
import java.util.ArrayList;
public class Utilizador implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int iduser;
	private String username;
	private String password;
	private double dinheiro=1000;
	private ArrayList<NotificacaoRMI> historico = new ArrayList<NotificacaoRMI>();

	//Construtor
	public Utilizador()
	{
		
	}
	
	public Utilizador(int iduser,String username, String password) {

		this.iduser=iduser;
		this.username=username;
		this.password=password;
	}


	
	//Getters
	//Retorna id do utilizador
	public ArrayList<NotificacaoRMI> getNotificacao()
	{
		return this.historico;
	}
	public int getiduser()
	{
		return this.iduser;

	}
	//Retorna o username
	public String getusername()
	{
		return this.username;

	}
	//Retorna a password
	public String getpassword()
	{
		return this.password;

	}
	public double getsaldo()
	{
		return this.dinheiro;
	}
	//SETTERS
	//Actualiza o iduser
	
	public void setsaldo(double saldo)
	{
		this.dinheiro=saldo;
	}
	public void retirarsaldo(double saldo)
	{
		this.dinheiro=this.dinheiro-saldo;
	
	}
	
	public void adicionarsaldo(double saldo)
	{
		this.dinheiro+=saldo;
	}
	public void setiduser(int iduser)
	{
		this.iduser=iduser;

	}
	//Actualiza o Username
	public void setusername(String username)
	{
		this.username=username;

	}
	//Actualiza a password
	public void setpassword(String password)
	{
		this.password=password;

	}



	//Funções


	//Retorna tudo separado por uma virgula
	public String getAll()
	{
		return this.iduser+"," + this.username+ "," +this.password;
	}

}