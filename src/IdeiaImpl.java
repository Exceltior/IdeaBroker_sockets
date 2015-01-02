import java.rmi.*;
import java.rmi.server.*;
import java.util.ArrayList;
import java.net.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class IdeiaImpl extends UnicastRemoteObject implements IdeiaMethods {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Utilizador> Utilizadores= new ArrayList<Utilizador>();
	private ArrayList<Topico> Topicos = new ArrayList<Topico>();
	
	/**<-------------->**/

	//private  ArrayList<ClienteTCP> = new ArrayList<ClienteTCP>();
    private  ArrayList<String> OnlineUsers = new ArrayList<String>();
   
    private  ArrayList<Utilizador> ListaUtilizadores = new ArrayList<Utilizador>();
	private  ArrayList<NotificacaoRMI> NotasAgendadas= new ArrayList<NotificacaoRMI>();
   
    /**<----------------->**/
	
	public IdeiaImpl() throws RemoteException {
		super();
		loaddados();
	}

	/** <--------------------> **/


	public void adicionarUsersOnline(String _onlineuser) throws java.rmi.RemoteException
	{
		//System.out.println("Vou adicionar um novo utilziador == "+_onlineuser+"\n");
		OnlineUsers.add(_onlineuser);
		savedados();
	}


	public ArrayList<String> getOnlineUsers() throws RemoteException {

		return OnlineUsers;

	}

	public String verficarNotificacao(String _user) throws java.rmi.RemoteException
	{
		String NotificacaoLocal = null;

		/** Construi o String de notificacoes do utilizador especifico **/
		if(!NotasAgendadas.isEmpty())
	        for (NotificacaoRMI notas : NotasAgendadas) {
	            if (notas.getUser().compareTo(_user) == 0) {
	            	if(notas.getExecutado() == 0)
	                	NotificacaoLocal = notas.getNotificao() + "\n";
	            }
	        }

	    //System.out.println("Vou enviar a notificacao == "+NotificacaoLocal+"\n");        
        return NotificacaoLocal;
        
	}

	public void adicionarNotasAgendadas(String notificacao, String user) throws java.rmi.RemoteException
	{
		NotasAgendadas.add(new NotificacaoRMI(notificacao,user,0));
		savedados();
	}

	public void removerNotasAgendadas(String notificacao, String user) throws java.rmi.RemoteException
	{
		/** Tem que procurar NOtificacao **/
		NotasAgendadas.remove(new NotificacaoRMI(notificacao,user,0));
		savedados();
	}

	public void Notificacao_vista(int indice) throws java.rmi.RemoteException
	{
		NotasAgendadas.get(indice).setExecutado(1);
		savedados();
	}

	/*public void enviaNotificacao(String notificacao ) throws RemoteException
    {
   		for(ClientInterface userOnline:utilizadoresOnline)
    	{
    		userOnline.addNotification(notificacao);
    	}
    	
    }*/

	/**<------------------>**/
	

	public void say(String saywhat)
	{
		System.out.println("[Server]:" + saywhat);
	}
	
	
	public int adicionartopico(Topico topico) throws java.rmi.RemoteException
	{
		System.out.println("Vou criar o Topico == "+topico.getidtopico()+" \n");
		//Adiciona o topico dado ao arraylist topico
		Topicos.add(topico);
		savedados();
		return 1;
	}

	
	public void savedados()
	{
		//Gravar dados para ficheiro
		say("Gravando Utilizadores.");
		try {

	         // Criar Ficheiro de Stream
	         FileOutputStream out = new FileOutputStream("utilizadores.ser");
	         ObjectOutputStream oout = new ObjectOutputStream(out);
	         // write something in the file
	         for(int i=0;i<Utilizadores.size();i++)
	         {
	        	 oout.writeObject(Utilizadores.get(i));
	         }
	         
	         // close the stream
	         oout.close();
	      } catch (Exception ex) {

	         ex.printStackTrace();
	      
	      }

		//Gravar dados para ficheiro
		say("Gravando Tópicos.");
		try {

	         // Criar Ficheiro de Stream
	         FileOutputStream out = new FileOutputStream("topicos.ser");
	         ObjectOutputStream oout = new ObjectOutputStream(out);
	         // write something in the file
	         for(int i=0;i<Topicos.size();i++)
	         {
	        	 oout.writeObject(Topicos.get(i));
	         }
	         
	         // close the stream
	         oout.close();

	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }

	    //Gravar dados para ficheiro
		say("Gravando Notificação.");
		try {

	         // Criar Ficheiro de Stream
	         FileOutputStream out = new FileOutputStream("notificacao.ser");
	         ObjectOutputStream oout = new ObjectOutputStream(out);
	         // write something in the file
	         for(int i=0;i<NotasAgendadas.size();i++)
	         {
	        	 oout.writeObject(NotasAgendadas.get(i));
	         }
	         
	         // close the stream
	         oout.close();

	      } catch (Exception ex) {
	         ex.printStackTrace();
	      }

	   }
	
	public String historicoutilizador(String username) throws java.rmi.RemoteException
	{
		String texto="\nErro Interno :(";
		for(int i=0;i<Utilizadores.size();i++)
		{
			if(Utilizadores.get(i).getusername().compareTo(username)==0)
			{
				if(!Utilizadores.get(i).getNotificacao().isEmpty())
				{
					texto="O seu historico de acções:\n";
					for(int j=Utilizadores.get(i).getNotificacao().size()-1;j>=0;j--)
					{	
						texto+="\n\t";
						texto+=Utilizadores.get(i).getNotificacao().get(j).getNotificao();
							
					}
				}
				else
				{
					texto="Não tem histórico de acções :( , Faça alguma coisa ! :)";
				}

				return texto;
			}
		}
		return texto;
	}

	public String dadosutilizador(String username) throws java.rmi.RemoteException
	{
		String texto="\nErro Interno :(";
		for(int i=0;i<Utilizadores.size();i++)
		{
			if(Utilizadores.get(i).getusername().compareTo(username)==0)
			{
				//Get data
				texto="O seu perfil:\n";
				texto+="\tID:" + Utilizadores.get(i).getiduser();
				texto+="\n\tUtilizador:" + Utilizadores.get(i).getusername();
				texto+="\n\tSaldo:" + Utilizadores.get(i).getsaldo();
				texto+="\n--------------------------------------";
				return texto;
			}
		}
		return texto;
	}

	public int efectuarcompra(String comprador,String vendedor,int idtopico,int idideia,int idaccao,int percentagem,int precorevenda) throws java.rmi.RemoteException
	{
		int idvendedor=-1,idcomprador=-1;
		//Passo 1 Trabalhar sobre variavel auxiliar para simular uma transação A a ponto B apenas 
		//Passo 2 - Encontrar no ArrayList o Utilizador  vendedor e o Utilizador Comprador
		for(int i=0;i<Utilizadores.size();i++)
		{
			if((Utilizadores.get(i).getusername().compareTo(comprador)==0) || (Utilizadores.get(i).getusername().compareTo(vendedor)==0))
			{
				//Ver qual e
				if(Utilizadores.get(i).getusername().compareTo(comprador)==0)
				{
					idcomprador=i;
				}
				else if(Utilizadores.get(i).getusername().compareTo(vendedor)==0)
				{
					idvendedor=i;
				}
				else
				{
					//Erro interno terminar
					return 0;
				}
			}
		}
		//Ver se foi encontrado os dois utilizadores
		
		if((idcomprador!=-1) && (idvendedor!=-1))
		{
			//Passo 3- Reverificar se acção é valida de ser comprada
			if((Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getutilizador().compareTo(vendedor)==0) && (Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem()>=percentagem))
			{
				//Passo 4 : Ver se Comprador tem saldo suficiente.
				double a_pagar=(percentagem*Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpreco())/Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem();
				if(Utilizadores.get(idcomprador).getsaldo()>a_pagar)
				{ 
					//Tem saldo suficiente logo partir par ao passo 5
					//Passo 5 - Retirar saldo do comprador para o vendedor
					System.out.println("Retirando Saldo ao utilizador:" + Utilizadores.get(idcomprador).getusername());
					System.out.println("Adicionando Saldo ao utilizador:" + Utilizadores.get(idvendedor).getusername());
					Utilizadores.get(idcomprador).retirarsaldo(a_pagar);
					Utilizadores.get(idvendedor).adicionarsaldo(a_pagar);
					//Passo 6- Retirar percentagem ao Vendedor e adicionala ao comprador
						//Passo 6.1 Ver se este utilizador ja tem algumas acções 
						for(int j=0;j<Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().size();j++)
						{
							if(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(j).getutilizador().compareTo(comprador)==0)
							{
								//Adicionar percentagem
								Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(j).adicionaraccao(percentagem);
								//Retirar percentagem
								Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).retiraraccao(percentagem);
								if(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem()==0)
								{
									Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().remove(idaccao);
									
								}
								Utilizadores.get(idcomprador).getNotificacao().add(new NotificacaoRMI("Voce comprou "+ percentagem + "% das acções da ideia " + idideia + " do Topico " + idtopico + "."));
								savedados();
								return 1;
							}
								
						}
						
						
						//6.2 Se utilizador nao tiver acções Criar novo Indice
						//Retirar percentagem
						Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).retiraraccao(percentagem);
						if(Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().get(idaccao).getpercentagem()==0)
						{
							Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().remove(idaccao);
							
						}
						//Adicionar nova accção e atribui percentagem;
						Topicos.get(idtopico).getIdeia().get(idideia).getAccoes().add(new Accao(precorevenda,percentagem,comprador));
						Utilizadores.get(idcomprador).getNotificacao().add(new NotificacaoRMI("Voce comprou "+ percentagem + "% das acções da ideia " + idideia + " do Topico " + idtopico + "."));
						savedados();
						return 1;
				}
				else
				{
					//Erro 
					System.out.println("Erro , Comprador não tem saldo suficiente");
					return 0;
				}
			}
			else
			{
				System.out.println("Erro Interno: Ver se accção e valida , terminar");
				return 0;
				
			}

		}
		else
		{
			System.out.println("Um dos utilizadores nao foram encontrados.");
			return 0;
		}
		//Passo 3 Ver saldo do comprador.

	}


	public void loaddados() throws RemoteException{
		
		//Carregar dados para ficheiro
		say("Carregando Utilizadores");
		 try{
		   FileInputStream fin = new FileInputStream("utilizadores.ser");
		   ObjectInputStream ois = new ObjectInputStream(fin);
		   Object object;
		   while(fin.available()>0)
		   {
			   //System.out.println("ENTRA");
			   object = ois.readObject();
			   Utilizadores.add((Utilizador) object);
			  
		   }
		   ois.close();
	   }catch(Exception ex){
		   
		   say("Erro a ler ficheiro relativo aos Utilizadores.");
		   
	   } 
		 
		 say("Carregando ficheiro topicos.ser ");
		 try{
			   FileInputStream fin = new FileInputStream("topicos.ser");
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   Object object;
			   while(fin.available()>0)
			   {
		
				   object = ois.readObject();
				   Topicos.add((Topico) object);
				  
			   }
			   ois.close();
		   }catch(Exception ex){
			   
			   say("Erro a ler ficheiro relativo aos topicos.");
			   
		   } 
		 say("Carregando ficheiro notificacoes.ser ");
		 try{
			   FileInputStream fin = new FileInputStream("notificacao.ser");
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   Object object;
			   while(fin.available()>0)
			   {
		
				   object = ois.readObject();
				   NotasAgendadas.add((NotificacaoRMI) object);
				  
			   }
			   ois.close();
		   }catch(Exception ex){
			   
			   say("Erro a ler ficheiro relativo ás Notificações.");
			   
		   } 
	}

	/**
	* Le dados guardados em ficheiros, 
	**/
	//@SuppressWarnings("unchecked")
	/*public static void lerdados_tcp()
	{
		 try {

            FileInputStream fin = new FileInputStream("data");
            ObjectInputStream ois = new ObjectInputStream(fin);
            
            listaUtilizadores = (ArrayList<Utilizador>) ois.readObject();
            ois.close();
            
            FileInputStream finNotes = new FileInputStream("notas");
            ObjectInputStream oisNotes = new ObjectInputStream(finNotes);
            
            notasAgendadas = (ArrayList<NotificacaoRMI>) oisNotes.readObject();
            oisNotes.close();
            
            fin = new FileInputStream("Timersfile");
            ois = new ObjectInputStream(fin);
            listaTimers = (ArrayList<DelayedNoti>) ois.readObject();
            // Vai pelo vector e verifica que timers deve recuperar e quais deve eliminar
            for(int i = 0; i < listaTimers.size() ; i++){
            	if (listaTimers.get(i).ocorri == 0){
            		// Re-schedule esta notificacao
        		   	Timer t = new Timer();
               		t.schedule(listaTimers.get(i), listaTimers.get(i).pub);
            	      
        	    } else {
        	    	listaTimers.remove(i);
        	    	i--;
        	    }

        	}
            

            ois.close();

            
        } catch (Exception e) {
            //System.out.println("Erro na leitura do disco");
        }
        
	}*/
	
	/**
	* Escreve dados guardados em ficheiros, 
	**/
	//@SuppressWarnings("unchecked")
	/*public static void escreverdados_tcp()
	{
		 try {

            FileOutputStream fout = new FileOutputStream("data");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
           	oos.writeObject(listaUtilizadores);
            oos.close();
            
            FileOutputStream foutNote = new FileOutputStream("notas");
            ObjectOutputStream oosNote = new ObjectOutputStream(foutNote);
            oosNote.writeObject(notasAgendadas);
            oosNote.close();
            
            fout = new FileOutputStream("Timersfile");
            oos = new ObjectOutputStream(fout);
            oos.writeObject(listaTimers);
            oos.close();
           
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Erro na escrita para o disco");
        }
	}
*/

	public String sayHello() throws RemoteException {
		System.out.println("Printing on server...");
		return "ACK";
	}

	public void remote_print(String s) throws RemoteException {
		System.out.println("Server:" + s);
	}
	
	/*public Message ping_pong(Message m) throws RemoteException {
		Message m1 = new Message("");
		m1.text = m.text + "....";
		return m1;
	}*/

	 /**
     * Procura e devolve um utilizador dado apenas o seu username
     * @param util username
     * @return null caso nao exista nenhum user com estes dados, ou o utilizador encontrado
     */
    public Utilizador procuraUtilizador(String util) throws RemoteException{

        for (Utilizador user : Utilizadores) {
            if (user.getusername().compareTo(util) == 0) {
                return user;
            }
        }

        return null;
    }
	
	public int gerariduser()
	{
		int max=-1;
		for(int i=0;i<Utilizadores.size();i++)
		{
			if(Utilizadores.get(i).getiduser()>max)
			{
				max=Utilizadores.get(i).getiduser();
			}
		}
		return max;
	}
	//Validação de dados de login
	public Utilizador login(String username,String password) throws RemoteException
	{
		System.out.println("Chegou Username:" + username);
		System.out.println("Chegou Pass:" + password);
		System.out.println("Utilizadores.size()="+ Utilizadores.size());

		if(Utilizadores.isEmpty())
		{
			return null;
		}
		
		for(int i=0;i<Utilizadores.size();i++)
		{
			if(Utilizadores.get(i).getusername().compareTo(username)==0)
			{
				System.out.println("Encontra username");
				if(password.compareTo(Utilizadores.get(i).getpassword())==0)
				{
					//Retorna Cliente
					say("Utilizador:["+username+"] logado no sistema.");
					return Utilizadores.get(i);
				}
				else
				{
					return null;
				}
			}
		}
		return null;

	}

	//Registar um utilizador no sistema
	public Utilizador registar(String username,String password) throws RemoteException
	{
		Utilizador _user = null;
		System.out.println("Chegou Username:" + username);
		System.out.println("Chegou Pass:" + password);

		if(!Utilizadores.isEmpty())
		{
			//	Verifica se o utilizador já existe
			for(int i=0;i<Utilizadores.size();i++)
			{
				if((Utilizadores.get(i).getusername().compareTo(username))==0)
				{
					say("Utilizador[" + username + "] já existe no sistema.");
					return null;
				}
				
			}
		}

		System.out.println("Chegou aqui");
		//	Se chegou aqui é porque não existe este utilizador
		//Adicionar utilizador ao sistema
		_user = new Utilizador(gerariduser()+1,username,password);
		Utilizadores.add(_user);
		say(" Utilizador[" + username + "] foi criado no sistema.");
		savedados();
		return _user;

	}
	


	// =======================================================

	public static void main(String args[]) {
		;
		if (args.length!=1) {

            System.out.println("Uso: java IdeiaImpl <this.ip_networkadress>");
            System.exit(0);
       
        }
		String ip=args[0];
		System.out.println("IP=" + ip);
		//Carregar dados das classes
		/*try {
			IdeiaImpl h = new IdeiaImpl();

			Naming.rebind("Ideia", h);
			System.out.println("Ideia Server ready.");
		} catch (RemoteException re) {
			System.out.println("Exception in IdeiaImpl.main: " + re);
		} catch (MalformedURLException e) {
			System.out.println("MalformedURLException in IdeiaImpl.main: " + e);
		}*/
        System.getProperties().put("java.security.policy", "policy.all");
        System.setProperty("java.rmi.server.hostname", ip);
		System.setSecurityManager(new RMISecurityManager());

		 try {

		 	IdeiaImpl h = new IdeiaImpl();
			Naming.rebind("//" + ip + ":1099/Ideia", h);
			
		} catch (RemoteException e) {

			try{
				IdeiaImpl h = new IdeiaImpl();
				Naming.rebind("//localhost:6000/Ideia", h);
			}
			catch(RemoteException e2){
				System.out.println("Erro a iniciar servidor: " + e2);
				return;
			} catch (MalformedURLException e1) {
				System.out.println("MalformedURLException in IdeiaImpl.main: " + e1);
			}

		} catch (MalformedURLException e2) {
			System.out.println("MalformedURLException in IdeiaImpl.main: " + e2);
		}

	}


	public ArrayList<Topico> gettopicos() throws RemoteException {
		return Topicos;
		
	}
	
	public int geraridideia(int i)
	{
		int max=-1;
		if(Topicos.get(i).getIdeia()!=null)
		{
			for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
			{
				if(Topicos.get(i).getIdeia().get(j).getidideia()>max)
				{
				max=Topicos.get(i).getIdeia().get(j).getidideia();
				
				}
			}
		}
		return max;
		
	}
	
	public int geraridresposta(int idtopico,int idideia)
	{
		int max=-1;
		if(!Topicos.get(idtopico).getIdeia().get(idideia).getResposta().isEmpty())
		{
			for(int i=0;i<Topicos.get(idtopico).getIdeia().get(idideia).getResposta().size();i++)
			{
				if(Topicos.get(idtopico).getIdeia().get(idideia).getResposta().get(i).getideia().getidideia()>max)
				{
					max=Topicos.get(idtopico).getIdeia().get(idideia).getResposta().get(i).getideia().getidideia();
				}
			}
	
		}
		return max;
	}
	
	public String apagarideia(int idtopico,int idideia,String username)
	{
		for(int i=0;i<Topicos.size();i++)
		{
			System.out.println("TOPICOS.SIZE()=="+Topicos.size());
			if(Topicos.get(i).getidtopico()==idtopico)
			{
				System.out.println("TOPICO ENCONTRADO");
				//Encontrar a ideia;
				for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
				{
					if(Topicos.get(i).getIdeia().get(j).getidideia()==idideia)
					{
						//Ideia Encontrada , apagala se as acções > 50%
						//Encontrar accções da ideia e verificar se existe alguma com username dado maior que 50%
						for(int k=0;k<Topicos.get(i).getIdeia().get(j).getAccoes().size();k++ )
						{
							if(Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getutilizador().compareTo(username)==0)
							{
								//Verificar se acçoes maior 50%
								if(Topicos.get(i).getIdeia().get(j).getAccoes().get(k).getpercentagem()>50)
								{
									Topicos.get(i).getIdeia().remove(j);
									//Adicionar nas notificações
									for(int l=0;l<Utilizadores.size();l++)
									{
										if(Utilizadores.get(l).getusername().compareTo(username)==0)
										{
											Utilizadores.get(l).getNotificacao().add(new NotificacaoRMI("Você apagou a ideia nº" + idideia + " do topico " + idtopico +"."));
										}
									}
									return "Ideia apagada";
								}
								else
								{
									return "Não tem acções suficientes para apagar esta ideia";
								}
							}
						}
						
					}
				}
				
			}
		}
		return "Erro Interno";
		
	}

	public int responderideia(String natureza,int idtopico,int idideia,String texto,String autor,Anexo anexo) throws java.rmi.RemoteException
	{
		
		for(int i=0;i<Topicos.size();i++)
		{
			System.out.println("TOPICOS.SIZE()=="+Topicos.size());
			if(Topicos.get(i).getidtopico()==idtopico)
			{
				System.out.println("TOPICO ENCONTRADO");
				//Encontrar a ideia;
				for(int j=0;j<Topicos.get(i).getIdeia().size();j++)
				{
					if(Topicos.get(i).getIdeia().get(j).getidideia()==idideia)
					{
						//Ideia Encontrada , adicionar resposta;
						Topicos.get(i).getIdeia().get(j).getResposta().add(new Resposta(natureza,new Ideia(geraridresposta(idtopico,idideia)+1,texto,null,autor,null,null)));
					}
				}
				return 1;
			}
		}
		return 0;
	}
	
	
	public int criarideia(int idtopico,String texto,String autor,Anexo anexo) throws java.rmi.RemoteException
	{
		
		for(int i=0;i<Topicos.size();i++)
		{
			System.out.println("TOPICOS.SIZE()=="+Topicos.size());
			if(Topicos.get(i).getidtopico()==idtopico)
			{
				System.out.println("TOPICO ENCONTRADO");
				//Cria a ideia;
				Accao Accao = new Accao(100,100,autor);
				Ideia Ideia=new Ideia(geraridideia(i)+1,texto,anexo,autor,null,Accao);
				System.out.println("I == "+i+" ideia == "+Ideia.getidideia()+"\n");
				
				Topicos.get(i).getIdeia().add(Ideia);
				for(int l=0;l<Utilizadores.size();l++)
				{
					if(Utilizadores.get(l).getusername().compareTo(autor)==0)
					{
						Utilizadores.get(l).getNotificacao().add(new NotificacaoRMI("Você criou uma ideia no topico " + idtopico +"."));
					}
				}
				return 1;
			}
		}
		return 0;
	}
	

	

}