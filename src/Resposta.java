import java.io.Serializable; 
public class Resposta implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String tipo;
	public Ideia Ideia;
	
	//Construtor 
	public Resposta(String tipo, Ideia Ideia) {
		this.tipo=tipo;
		this.Ideia=Ideia;
	}
	//Getters
	//Retorna tipo de resposta neutra boa , ma..
	public String gettipo()
	{
		return this.tipo;
	}
	//Retorna o texto da resposta
	public Ideia getideia()
	{
		return this.Ideia;
	}
	
	//Setters
	//Muda o tipo de resposta
	public void settipo(String tipo)
	{
		this.tipo=tipo;

	}
	//Set texto da resposta
	public void setIdeia(Ideia ideia)
	{
		this.Ideia=ideia;

	}

}