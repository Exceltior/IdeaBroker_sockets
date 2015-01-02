/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/

/**
 * Classe que representa e armazena do anexo
 */
import java.io.*;
public class Anexo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String anexo;

    public Anexo(String _anexo) {
        this.anexo = _anexo;
    }

    public String getAnexo() {
        return anexo;
    }

    public void setAnexo(String _anexo) {
        this.anexo = _anexo;
    }


}
