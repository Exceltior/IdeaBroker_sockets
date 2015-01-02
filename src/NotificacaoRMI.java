/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/

import java.io.Serializable;


public class NotificacaoRMI implements Serializable{

	String notificao,user;
	Integer executado = 0;

	public NotificacaoRMI(String notificacao,String user, Integer _executado) {

		this.notificao = notificacao;
		this.user = user;
		this.executado = _executado;

	}

	public NotificacaoRMI(String notificacao)
	{
		this.notificao=notificacao;
	}

	
	public String getNotificao() {
		return notificao;
	}

	public void setNotificao(String notificao) {
		this.notificao = notificao;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Integer getExecutado() {
		return executado;
	}

	public void setExecutado(Integer _executado) {
		this.executado = _executado;
	}

	

}
