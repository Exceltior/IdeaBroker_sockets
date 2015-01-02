/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/

import java.util.ArrayList;
import java.net.*;

public class Pacote{

	private int comando1, comando2;
	private ArrayList<Integer> Comandos = new ArrayList<Integer>();
	private String texto, utilizador, anexo;
	private String data;

	Pacote(ArrayList<Integer> _Comandos,String _utilizador,String _texto,String _data,String _anexo)
	{
		/*this.comando1 = _comando1;
		this.comando2 = _comando2;*/
		this.utilizador = _utilizador;
		this.texto = _texto;
		this.data = _data;
		this.anexo = _anexo;
		this.Comandos = _Comandos;

	}

	public ArrayList<Integer> getComandos() {
		return Comandos;
	}

	public void setComandos(ArrayList<Integer>  _Comandos) {
		this.Comandos = _Comandos;
	}

	/*public int getComando1() {
		return comando1;
	}

	public void setComando1(int comando1) {
		this.comando1 = comando1;
	}

	public int getComando2() {
		return comando2;
	}

	public void setComando2(int comando2) {
		this.comando2 = comando2;
	}*/

	public String getMensagemTexto() {
		return texto;
	}

	public void setMensagemTexto(String _texto) {
		this.texto = _texto;
	}

	public String getUtilizador() {
		return utilizador;
	}

	public void setUtilizador(String _utilizador) {
		this.utilizador = _utilizador;
	}

	
	public String getAnexo() {
		return anexo;
	}
	
	public void setAnexo(String _anexo) {
		this.anexo = _anexo;
	}

	public String getData() {
		return data;
	}
	
	public void setData(String _data) {
		this.data = _data;
	}













}