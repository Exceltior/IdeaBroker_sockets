import java.io.Serializable; 
import java.util.ArrayList;

public class Ideia implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int idideia; //Id unico que identifica a ideia
	public String descricao;//Descrição da ideia em si
	public Anexo ficheiro;//????? N sei o tipo que vamos dar
	public String autor;
	public ArrayList<Resposta> resposta = new ArrayList<Resposta>();//Array do tipo resposta que contem todas as respostas dadas a esta ideia por outros utilizadores
	public ArrayList<Accao> accoes = new ArrayList<Accao>();
	//Construtor 
	public Ideia(int ididea, String descricao,Anexo ficheiro,String autor,ArrayList<Resposta> resposta,Accao accoes) {
		this.idideia=ididea;
		this.descricao=descricao;
		this.ficheiro=ficheiro;
		this.autor=autor;
		if(accoes!=null)
		{
			this.accoes.add(accoes);
		}
			
		
		
		
	}
	
	//Getters
	//Retorna id da ideia
	
	public ArrayList<Accao> getAccoes()
	{
		return accoes;
	}
	public int getidideia()
	{
		return this.idideia;
	}
	//Retorna a descricao da ideia 
	public String getdescricao()
	{
		return this.descricao;
	}
	//Retorna ficheiro anexado se tal existir
	public Anexo getficheiro()
	{
		return this.ficheiro;
	}
	//Retorna Array de Respostas
	public ArrayList<Resposta> getResposta()
	{
		return this.resposta;

	}
	public String getautor()
	{
		return this.autor;
	}
	//Setters
	public void setidideia(int idideia)
	{
		this.idideia=idideia;

	}
	public void setautor(String autor)
	{
		this.autor=autor;
	}
	//Set descrição da ideia
	public void setdescricao(String  descricao)
	{
		this.descricao=descricao;

	}

	public void setfile(Anexo ficheiro)
	{
		this.ficheiro=ficheiro;

	}
	

	public void setResposta(ArrayList<Resposta> resposta)
	{
		this.resposta=resposta;

	}

	/*public String getAscii()
    {
    	ASCIIConverter conv = new ASCIIConverter();
        String anexo = "";
        try {
        	if ((ficheiro != null) && (!ficheiro.getAnexo().contentEquals(""))) {
        		anexo = conv.convertAndResize(ficheiro.getPicture());
        	}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return anexo;

    }*/
    

}