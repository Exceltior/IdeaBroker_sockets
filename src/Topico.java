import java.io.Serializable; 
import java.util.ArrayList;

public class Topico implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int idtopico;
	public String tema;
	public String autor;
	public ArrayList<Ideia> ideia = new ArrayList<Ideia>();
	
	//Construtor
	public Topico(int idtopico, String tema,String autor,ArrayList<Ideia> ideia) {

		this.idtopico=idtopico;
		this.tema=tema;
		this.autor=autor;

	}

	
	
	//Getters
	//Retorna id do topico
	public int getidtopico()
	{
		return this.idtopico;
	}

	//Retorna o tema do tópico
	public String gettema()
	{
		return this.tema;
	}

	//Retorna autor
	public String getautor()
	{
		return this.autor;
	}

	//Retorna o array de ideias, contido em cada topico
	public ArrayList<Ideia> getIdeia()
	{
		return this.ideia;

	}

	//Setters
	public void setidtopico(int idtopico)
	{
		this.idtopico=idtopico;

	}
	public void settema(String tema)
	{
		this.tema=tema;

	}

	public void setautor(String autor)
	{
		this.autor=autor;

	}
	//Retorna o array de ideias, contido em cada topico
	public void setIdeia(ArrayList<Ideia> ideia)
	{
		this.ideia=ideia;

	}

}