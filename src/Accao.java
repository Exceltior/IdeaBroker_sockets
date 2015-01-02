import java.io.*;

public class Accao implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public double precoaccao;
	public int percentagem;
	public String utilizador;
	//Construtor 
	public Accao(double precoaccao,int percentagem,String utilizador) {
		this.precoaccao=precoaccao;
		this.percentagem=percentagem;
		this.utilizador=utilizador;
	}

	//Getters
	
	public String getutilizador()
	{
		return this.utilizador;
	}
	public double getpreco()
	{
		return this.precoaccao;
	}
	//Retorna o texto da resposta
	public int getpercentagem()
	{
		return this.percentagem;
	}
	
	//Setters
	
	public void adicionaraccao(int percentagem)
	{
		this.percentagem=this.percentagem+percentagem;
		
	}
	public void retiraraccao(int percentagem)
	{
		this.percentagem=this.percentagem-percentagem;
		
	}
	public void setprecoaccao(double preco)
	{
		this.precoaccao=preco;

	}
	
	public void setutilizador(String idutilizador)
	{
		this.utilizador=utilizador;
	}

}