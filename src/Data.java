/*
*Autores:
* Bruno Miguel Oliveira Rolo nº 2010131200
* Joao Artur Ventura Valerio Nobre nº 2010131516
*/

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Data implements Serializable{

	private static final long serialVersionID = 1L;
	public int ano,mes,dia,hora,min;

	Data()
	{
		GregorianCalendar hoje = new GregorianCalendar();
		ano = hoje.get(Calendar.YEAR);
		mes = hoje.get(Calendar.MONTH);
		dia = hoje.get(Calendar.DATE);
		hora = hoje.get(Calendar.HOUR_OF_DAY);
		min = hoje.get(Calendar.MINUTE);

	}

	Data(int ano, int mes, int dia, int hora, int min)
	{
		this.ano=ano;
		this.mes=mes;
		this.dia=dia;
		this.hora=hora;
		this.min=min;
	}

	public java.util.Date converteParaData()
	{
		return new java.util.Date(ano-1900, mes, dia, hora, min);
	}

	int Ontem()
	{
		/** Verifica se e Ontem em relacao a data actual **/
		java.util.Date _hoje = new java.util.Date();
		if(_hoje.compareTo(converteParaData()) >= 0)
			return 0;
		else
			return -1;

	}

	public int getAno() {
		return ano;
	}
	public void setAno(int ano) {
		this.ano = ano;
	}
	public int getMes() {
		return mes;
	}
	public void setMes(int mes) {
		this.mes = mes;
	}
	public int getDia() {
		return dia;
	}
	public void setDia(int dia) {
		this.dia = dia;
	}
	public int getHora() {
		return hora;
	}
	public void setHora(int hora) {
		this.hora = hora;
	}
	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}





}