/*
* Autores: 
* Bruno Miguel Oliveiroa Rolo 2010131200 brolo@student.dei.uc.pt
* Joao Artur Ventura Valerio Nobre 2010131516 janobre@student.dei.uc.pt
*/

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.rmi.server.*;
import java.rmi.registry.Registry;

@SuppressWarnings("unchecked, deprecation")
public class IdeaBrokerServerTCP  extends UnicastRemoteObject implements ClienteRMIInterface
{

	private static Vector<ClienteTCP> listaThreads = new Vector<ClienteTCP>();
/*    public static Vector<String> onlineUsers = new Vector<String>();
    public static Vector<Temporizador> listaTimers = new Vector<Temporizador>(); // Armazena os timers de notificações delayed
    public static Vector<Utilizador> listaUtilizadores = new Vector<Utilizador>();
    */
    static Vector<String> notificacoes=new Vector<String>();
  // IdeiaMethods h;
    static String IP_rmi; 
	public static String type = "S"; // Tipo de servidor: P- primario, S - secundario
	
    public IdeaBrokerServerTCP() throws RemoteException
	{
		super();
	}

    public void addNotification(String note) throws RemoteException
    {
        notificacoes.add(note);
    }
    
  /* public String getUsername() throws RemoteException
    {
        return myUsername;
    }*/


	public static void main(String[] args) throws IOException 
    {

	 	if (args.length <= 1) {

            System.out.println("Uso: java IdeaBrokerServerTCP <servidorTCP_address> <servidorRMI_address>");
            System.exit(0);
       
        }

        IP_rmi = args[1];

        /*System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());*/
        System.out.println("IP RMI == "+IP_rmi+" \n");
        ServerSocket informacao=null;                                     
        ServerSocket estado=null;
        Socket cliente_informacao = null, cliente_estado=null;

    	try {
            
            informacao = new ServerSocket(1234);
            estado = new ServerSocket(1233);

           	ClienteUDP UDPThread = new ClienteUDP(args[0]);
            UDPThread.start();
            
            // Cria um timer que apartir dos 10s e de 5 em 5s, vai escrever para o disco
            //Timer timer = new Timer();
            //timer.schedule(new DiskTimer(), 10000, 5000);
            
        } catch (SecurityException e) {

            System.out.println("Erro a iniciar servidor" + e);

        } catch (BindException e) {
            
            informacao = new ServerSocket(2234);
            estado = new ServerSocket(2233);
            ClienteUDP UDPThread = new ClienteUDP(args[0]);
            UDPThread.start();
            
            //Cria um timer que apartir dos 10s e de 5 em 5s, vai escrever para o disco
          	//Timer timer = new Timer();
            // timer.schedule(new DiskTimer(), 10000, 5000);

        }  

        while (true) {

            if (type.compareTo("P") == 0) {

                try {

                    System.out.println("Entra em acção... . . . \n");

                    cliente_informacao = informacao.accept();
                    cliente_estado  = estado.accept();

                } catch (Exception e) {

                    System.out.println("Erro no accept.. ");

                }

                ClienteTCP novoCliente = new ClienteTCP(cliente_informacao,cliente_estado);
                listaThreads.add(novoCliente);
                //h.adicionarListaThreads(novoCliente);
                novoCliente.start();

            } else {

                try {
      
                    Thread.sleep(1000);

                } catch (Exception e) {

                    System.out.println("Error in sleep");

                }

            }

        }

	 }

}

/* <--------------- FAZER ---------------> */
class ClienteTCP extends Thread 
{

    IdeiaMethods h;
 	String meu_utilizador;
    ObjectOutputStream mensagem_out;
    ObjectInputStream mensagem_in;
    Socket cliente_informacao;                                        
    Socket cliente_estado;
    Scanner inputControlo;                                              // scanner para ler dados do socket do cliente
    PrintWriter outputMensagem,outputControlo;                      // Para escrever dados para o socket
    Utilizador eu;
    ArrayList<Integer> Caminho_percorrido = new ArrayList<Integer>(); 
    //ArrayList<String> Notificacoes = new ArrayList<String>(); 
    String Notificacoes = null;
    String notificacaoTotal = null;
    Integer ultimo_menu = -1;

    ArrayList<String> notifications = new ArrayList<String> ();

	public ClienteTCP(Socket _cliente_informacao, Socket _cliente_estado)
	{
		this.cliente_informacao = _cliente_informacao;
		this.cliente_estado = _cliente_estado;

		try {

            mensagem_out = new ObjectOutputStream(cliente_estado.getOutputStream());
            mensagem_in = new ObjectInputStream(cliente_estado.getInputStream());
            this.inputControlo = new Scanner(cliente_estado.getInputStream());                     //cria um Scanner para ler dados do Socket
            this.outputMensagem = new PrintWriter(cliente_informacao.getOutputStream(), true);        //cria um PrintWriter para escrever dados para o socket
            this.outputControlo = new PrintWriter(cliente_estado.getOutputStream(), true);     //cria um PrintWriter para escrever dados para o socket
            
        } catch (IOException e) {

            System.out.println("Erro a criar streams de dados ou a activar o keep-alive do socket.");
        
        }

	}

    public void enviar_notificacao(String notas,String utilizador)
    {
       // System.out.println("Estou a enviar uma notificação utilizador == "+utilizador+"\n");
       // System.out.println("E a notificacao == "+notas+"\n");
        /** Envia notificacao para o socket aberto **/
        outputMensagem.println("qwerty");
        outputMensagem.println(utilizador);
        outputMensagem.println(notas);
    }

	public void run()
	{
		int escolha = -1;
		System.out.println(" Servidor RMI == "+IdeaBrokerServerTCP.IP_rmi+"\n");
         
        System.getProperties().put("java.security.policy", "policy.all");
        System.setSecurityManager(new RMISecurityManager());
              
        try {

                //ESTE H tem todos os métodos do servidor
               //h = (IdeiaMethods) Naming.lookup("Ideia");

                try{
                    System.out.println("//"+ IdeaBrokerServerTCP.IP_rmi +":1099/Ideia");
                    h = (IdeiaMethods) Naming.lookup("//"+ IdeaBrokerServerTCP.IP_rmi +":1099/Ideia");
                
                } catch(RemoteException e) {

                     h = (IdeiaMethods) Naming.lookup("//"+ IdeaBrokerServerTCP.IP_rmi +":6000/Ideia");
                
                }
               
        } catch (Exception e) {

                System.out.println("Exception in main: RMI == "+ IdeaBrokerServerTCP.IP_rmi +"\n");
                return;

        }

        try{

                h.sayHello();

        } catch(RemoteException e) {   }



		while(true)
		{
        	try {

                escolha = inputControlo.nextInt();
               
            } catch (Exception e) {
            
                // Utilizador saiu
                // SocialMoreServer.onlineUsers.remove(eu.getUsername());
                /*try
                {
                    //h.removerUsersOnline(eu.getusername());
                 } catch (RemoteException e1){ return; }  */  
                return;
            
            }

            try
            {
                if(eu != null)
                    Notificacoes = h.verficarNotificacao(eu.getusername());

            } 
            catch (RemoteException e)
            {

            }

            if(Notificacoes != null)
            {
                //System.out.println("Vou enviar notificação pelo TCP...do utilizador == "+eu.getusername()+"\n");   
                enviar_notificacao(Notificacoes,eu.getusername());
            }

            System.out.println("Primeira escolha == "+escolha+"\n");

            switch (escolha) {
                
                case 1:{

                    //Caminho_percorrido.add(1);
                    ultimo_menu = 1;
                	login();
                	break;

                }
                case 101:{

                    String usr = inputControlo.next();
                    try{
                        eu = h.procuraUtilizador(usr);
                        //h.adicionarUsersOnline(usr);
                    } catch (Exception e) {
                            System.out.println("Exception in main: " + e);
                            return;
                    }

                    break;

                }
                case 10:{

                    //Caminho_percorrido.add(10);
                    /*String usr = inputControlo.next();
                    try{
                        eu = h.procuraUtilizador(usr);
                        h.adicionarUsersOnline(usr);
                    } catch (Exception e) {
                            System.out.println("Exception in main: " + e);
                            return;
                    }*/

                    ultimo_menu = 10;
                    listartopico();
                    break;

                }
                case 11:{

                    //System.out.println("Entra aqui... escolha_2 == "+escolha_2+"\n");
                    Caminho_percorrido.add(11);
                    ultimo_menu = 11;
                    criartopico();
                    break;

                }
                case 12:{

                    Caminho_percorrido.add(12);
                    ultimo_menu = 12;
                    apagartopico();
                    break;

                }
                case 13:{

                    Caminho_percorrido.add(13);
                    ultimo_menu = 13;
                    entrartopico();
                    break;

                }
                case 14:{

                    Caminho_percorrido.add(14);
                    ultimo_menu = 14;
                    criarideia();
                    break; 

                }
                case 15:{

                    Caminho_percorrido.add(15);
                    ultimo_menu = 15;
                    responder_ideia();
                    break;

                }
                case 16: {
                    Caminho_percorrido.add(16);
                    ultimo_menu = 16;
                    bolsa();
                    break;
                }
                case 17: {
                    Caminho_percorrido.add(17);
                    ultimo_menu = 17;
                    download_ficheiro();
                    break;
                }
                case 19: {
                    Caminho_percorrido.add(17);
                    apagarideia();
                    break;
                }
                case 40:{
                    ultimo_menu = 40;
                    historico();
                    break;
                }
                case 41:{
                    ultimo_menu = 41;
                    profile();
                    break;
                }
                case 163:{

                    // Utilizador saiu
                    try{
                        
                        //h.removerUsersOnline(eu.getusername());
                        System.out.println("Utilizador saiu\n");

                    } catch (Exception e) {

                            System.out.println("Exception in main: " + e);
                            return;

                    }

                   // break;

                }
                case 170:
                {
                    break;
                }   
          		default:                
                	break;

            }
		}
	}



    public synchronized void bolsa()
    {

        int id_ideia=inputControlo.nextInt();
        System.out.println("Recebi idideia=" + id_ideia);
        int id_topico=inputControlo.nextInt();
        System.out.println("Recebi idtopico=" + id_topico);
        ArrayList<Topico> Topicos = new ArrayList<Topico>();
        //Enviar ao Cliente que acções pode comprar
        try {
            Topicos= h.gettopicos();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            System.out.println("Erro a fazer retrieve dos topicos ao servidor RMI");
        }
            //Encontrar topico
        int idarraytopico=-1;
        for(int i=0;i<Topicos.size();i++)
        {
            if(Topicos.get(i).getidtopico()==id_topico)
            {
                idarraytopico=i;
                System.out.println("Topico encontrado");
                break;
            }
        }
        //Encontrar ideia
        int idarrayideia=-1;
        for(int i=0;i<Topicos.get(idarraytopico).getIdeia().size();i++)
        {
            if(Topicos.get(idarraytopico).getIdeia().get(i).getidideia()==id_ideia)
            {
                //Ideia encontrada
                System.out.println("Ideia Encontrada\n");
                idarrayideia=i;
            }
        }
        //Retornar String com todos os Utilizadores que detem alguma share na ideia selecionada para o utilizador comprar
        String texto="";
        if(!Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().isEmpty())
        {
            texto="Estão á venda as seguintes Acções:\n";
            for(int i=0;i<Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().size();i++)
            {
                texto+="\t Utilizador [" + Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().get(i).getutilizador() + "] detem " + Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().get(i).getpercentagem() + " da ideia ao preço de: " + Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().get(i).getpreco() + " unidades.\n";
            }
        }
        System.out.println("Enviando....");
        System.out.println(texto);
        outputControlo.println(1);
        outputControlo.println(texto);
        outputControlo.println("-9999");
        
        String utilizador=inputControlo.next();
        utilizador+=inputControlo.nextLine();
        
        if(utilizador.compareTo("-1")==0)
        {
            System.out.println("Cancelar compra");
            return;
        }
        else
        {
            int idaccao=-1;
            System.out.println("Recebi o Utilizador [" + utilizador + "].");
            for(int i=0;i<Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().size();i++)
            {
                System.out.println("Tamanho Ideias = " + Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().size());
                //Encontrar accao pretendida pelo utilizador
                if(Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().get(i).getutilizador().compareTo(utilizador)==0)
                {
                    System.out.println("Utilizador encontrado");
                    idaccao=i;
                    break;
                }
            }
            System.out.println("Envia:" + Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().get(idaccao).getpercentagem());
            outputControlo.println(Topicos.get(idarraytopico).getIdeia().get(idarrayideia).getAccoes().get(idaccao).getpercentagem());
            int percentagem=inputControlo.nextInt();
            System.out.println("Recebi percentagem : " + percentagem);
            int preconovasaccoes=inputControlo.nextInt();
            try {
                int controlo=h.efectuarcompra(eu.getusername(), utilizador, idarraytopico, idarrayideia, idaccao, percentagem, preconovasaccoes);
                if(controlo == 1)
                {
                    /** Comprada com sucesso **/
                    try {
                       // System.out.println("Vou enviar uma notificacao ao RMI...\n");
                        h.adicionarNotasAgendadas("Compraram as tuas acções",eu.getusername());
                    } catch (RemoteException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //Efetuar compra
            //Lock
            
            //Unlock
            
        }

        /** Comprada com sucesso **/
        try {
           // System.out.println("Vou enviar uma notificacao ao RMI...\n");
            h.adicionarNotasAgendadas("Compraram as tuas acções",eu.getusername());
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void download_ficheiro()
    {

    }
    
    public synchronized void apagarideia()
	{
        /** Apagar Ideia**/
        int id_topico=inputControlo.nextInt();
        int id_ideia=inputControlo.nextInt();
        String controlo="";
        System.out.println("Vou apagar do topico " + id_topico + " o id ideia nº " + id_ideia);
        try {
            controlo=h.apagarideia(id_topico,id_ideia,eu.getusername());
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        outputControlo.println(controlo);
	}
    
    public void apagartopico() 
    {
        /** Apagar topico **/
        //int id_topico = inputControlo.nextInt();

       // System.out.println("Vou apagar o topico com id == "+id_topico+"\n");
        /*if(true)
            outputControlo.println(true);
        else
            outputControlo.println(false);*/
    }

    
    public synchronized void criarideia()
    {
    	Anexo _anexo = null;
        String texto = null;
        String nome_f = null;
        String caminho = null;
        String nome_anexo = null;
        Data data_envio = null;

        int escolha = inputControlo.nextInt();
        int id_topico = inputControlo.nextInt();
        int controlo=0;
      //  System.out.println("Recebi IDTOPICO ==" + id_topico); 
       // System.out.println("Recebi Escolha ==" + escolha); 
        
        switch (escolha) {

            case 1:

                texto = inputControlo.next();
                texto += inputControlo.nextLine();
                //Criar a ideia
                // System.out.println("Recebi o texto ==" + texto); 
                
                try {

    				controlo=h.criarideia(id_topico, texto, eu.getusername(),null);
    			
                } catch (RemoteException e) {

    				// TODO Auto-generated catch block
    				System.out.println("Erro a criar ideia");
    				outputControlo.println(false);
    			
                }

                if(controlo==1)
                {
                	outputControlo.println(true);
                }
                else
                {
                	outputControlo.println(false);
                }
                break;
            
            case 2:

            	caminho = inputControlo.next();
                caminho += inputControlo.nextLine();
                System.out.println("Caminho == "+caminho+"\n");
                nome_anexo = inputControlo.next();
                nome_anexo += inputControlo.nextLine();
                System.out.println("Nome ficheiro == "+nome_anexo+"\n");
                _anexo = new Anexo(nome_anexo);
                new AnexoServidor(nome_anexo);

    			try {

    				controlo=h.criarideia(id_topico,"",eu.getusername(),_anexo);

    			} catch (RemoteException e) {

    				// TODO Auto-generated catch block
    				System.out.println("Erro a criar ideia");

    			}

    			if(controlo==1)
                {
                	outputControlo.println(true);
                }
                else
                {
                	outputControlo.println(false);
                }               
                break;


            case 3:

                texto = inputControlo.next();
                texto += inputControlo.nextLine();
                caminho = inputControlo.next();
                caminho += inputControlo.nextLine();
                
                nome_anexo = inputControlo.next();
                nome_anexo += inputControlo.nextLine();
                
                _anexo = new Anexo(nome_anexo);
                new AnexoServidor(nome_anexo);
                
                try {
    			
                controlo=h.criarideia(id_topico,texto,eu.getusername(),_anexo);
    			
                } catch (RemoteException e) {
    				
                    // TODO Auto-generated catch block
    				System.out.println("Erro a criar ideia");

    			}

                if(controlo==1)
                {
                	outputControlo.println(true);
                }
                else
                {
                	outputControlo.println(false);
                }

            break;
            
            default:
                break;
            
        }

        //eu.addIdeia(new Ideia(texto, anexo));
        // Envia  notificacao para todos os utilizadores, atraves de um timer
        //Timer t = new Timer();
        //DelayedNoti d = new DelayedNoti(getAmigos(eu.getUsername()), "Novo Post de " + eu.getUsername(), agend.converteParaData() ); 
        //t.schedule(d , agend.converteParaData());
        // IdeaBrokerServerTCP.listaTimers.add(d);

     }

    public String listartopico() 
    {
        /** Construir a String com os topicos **/
        ArrayList<Topico> Topicos=null;
        String topico_text = "";

        try {

            Topicos = h.gettopicos();
        
        } catch (RemoteException e) {
        
            // TODO Auto-generated catch block
            System.out.println("Erro a listar topicos RMI");
            e.printStackTrace();
        
        }
        
        
        topico_text += "---------------- Tópicos Disponíveis ---------------\n";
        for(int i=0;i<Topicos.size();i++)
        {
            topico_text+="" + Topicos.get(i).getidtopico() + " -> " + Topicos.get(i).gettema() + "\n";
        }
        
        System.out.println("Chega aqui....\n");
        outputControlo.println(2);
        System.out.println("Chega aqui....2\n");
        outputControlo.println(topico_text);
        outputControlo.println("-9999");
        return topico_text;

    }

    public int geraridtopico(ArrayList<Topico> Topicos)
    {
        int max=-1;
        for(int i=0;i<Topicos.size();i++)
        {
            if(Topicos.get(i).getidtopico()>max)
            {
                max=Topicos.get(i).getidtopico();
            }
        }
        return max;
    }

    public synchronized void criartopico()
    {
         /** Cria Topico **/
        System.out.println("Estou a espera d tema... \n");

        String tema = inputControlo.next();
        tema += inputControlo.nextLine();
        
        ArrayList<Topico> Topicos;
        System.out.println("Vou criar o topico com o tema == "+tema);

        //Lock
        try
        {

            Topicos = h.gettopicos();  

        } catch (Exception e) {

            System.out.println("Problema no gettopicos ao RMI");
            outputControlo.println(false);
            return;
        
        }
        
        for(int i=0;i<Topicos.size();i++)
        {
            if(Topicos.get(i).gettema().compareTo(tema)==0)
            {
                System.out.println("Tópico já existente.");
                outputControlo.println(false);
                return;
            }
        }

        try {
            System.out.println("EU sou do == "+eu.getusername()+"\n");
            h.adicionartopico(new Topico(geraridtopico(Topicos)+1,tema,eu.getusername(),null));
          //  h.adicionarNotasAgendadas("Criado Novo Topico ["+tema+"]\n",eu.getusername());

        } catch (RemoteException e) {
       
            System.out.println("Erro a criar topico devido a ligação RMI");
            outputControlo.println(false);
            return;
       
        }

        
        outputControlo.println(true);
       // enviar_notificacao(eu.getusername(),"Foi criado um novo topico pelo utilizador ["+eu.getusername()+"]");
       
        return;
        //Unlock
    
    }

  

    public void entrartopico() 
    {
        int id_topico = inputControlo.nextInt();
        ArrayList <Topico> Topicos=null;
        String texto="";
        
        try {
        Topicos=h.gettopicos();
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            System.out.println("Problema a fazer fetch dos topicos ao RMI.");
            //Retornar erro;
            return;
        }
        System.out.println("Vou entrar o topico com id == "+id_topico+"\n");
        int indice=-1;
        for(int i=0;i<Topicos.size();i++)
        {
            if(Topicos.get(i).getidtopico()==id_topico)
            {
                indice=i;
                break;
            }
        }
        if(indice==-1)
        {
            System.out.println("Tópico dado pelo Cliente não encontrado");
            outputControlo.println(1);
            outputControlo.println("Tópico não existe...");
            outputControlo.println("-9999");
        }
        else
        {
            texto+="-------------Listagem do Tópico:" + Topicos.get(indice).gettema() + "-------------\n\n";
            if(!Topicos.get(indice).getIdeia().isEmpty())
            {
                System.out.println("N IDEIAS:" + Topicos.get(indice).getIdeia().size());
                for(int i=Topicos.get(indice).getIdeia().size()-1;i>=0;i--)
                {
                    System.out.println("\n-------------------------------------------------------------\n");
                        //List ideia
                        texto+="Ideia nº" + Topicos.get(indice).getIdeia().get(i).getidideia() + "    Por:" + Topicos.get(indice).getIdeia().get(i).getautor() +"\n";
                        if(Topicos.get(indice).getIdeia().get(i).getdescricao().compareTo("")!=0)
                        {
                            texto+="Descrição da Ideia:" + Topicos.get(indice).getIdeia().get(i).getdescricao() + "\n";
                            //Ver quem detem os direitos
                            if(!Topicos.get(indice).getIdeia().get(i).getAccoes().isEmpty())
                            {
                            	texto+="Acções da Ideia:\n";
                            	for(int k=0;k<Topicos.get(indice).getIdeia().get(i).getAccoes().size();k++)
                            	{
                            		texto+="Utilizador [" + Topicos.get(indice).getIdeia().get(i).getAccoes().get(k).getutilizador() + "] detêm "+ Topicos.get(indice).getIdeia().get(i).getAccoes().get(k).getpercentagem() + "% da ideia com um valor de COMPRA TOTAL de:" + Topicos.get(indice).getIdeia().get(i).getAccoes().get(k).getpreco() + " unidades\n";
                            	}
                            }
                        }
                        if(Topicos.get(indice).getIdeia().get(i).getficheiro()!=null)
                        {
                            texto+="Anexado: " + Topicos.get(indice).getIdeia().get(i).getficheiro().getAnexo() + "\n\n";
                        }
                        //  Ver respostas da ideia
                        if(!Topicos.get(indice).getIdeia().get(i).getResposta().isEmpty())
                        {
                            for(int j=0;j<Topicos.get(indice).getIdeia().get(i).getResposta().size();j++)
                            {
             
                                texto+="\n\tResposta nº" + j + ",    Natureza da Ligação:" + Topicos.get(indice).getIdeia().get(i).getResposta().get(j).gettipo() + "   Por:" + Topicos.get(indice).getIdeia().get(i).getResposta().get(j).getideia().getautor() + "\n";
                                texto+="\t" + Topicos.get(indice).getIdeia().get(i).getResposta().get(j).getideia().getdescricao();
                                texto+="\n\t-----------------------------------------------------------------\n";
                            }
                        }
                        texto+="\n--------------------------------------------------------\n";
                 
                }
            
            }
            System.out.println("OUTPUT");
            outputControlo.println(1);
            outputControlo.println(texto);
            outputControlo.println("-9999");
        }

    }
      
    public void historico()
    {
        System.out.println("ENTRA Hist.");
        String texto="";
        try {
            texto=h.historicoutilizador(eu.getusername());
        } catch (RemoteException e) {
        
            texto="Erro Interno :(";
//          e.printStackTrace();
        }
        outputControlo.println(texto);
        outputControlo.println("-9999");
    }  

    public void profile()
    {
        String texto="";
        try {
            texto=h.dadosutilizador(eu.getusername());
        } catch (RemoteException e) {
        
            texto="Erro Interno :(";
//          e.printStackTrace();
        }
        outputControlo.println(texto);
        outputControlo.println("-9999");
    }

    public void login()
	{
		
        /** executa login no tcp, true se logado correctamente, false caso contrario **/
        int escolha=-1, escolha2;

        try{

            escolha = inputControlo.nextInt();
            System.out.println("Sou o servidor e recebo a escolha == "+escolha+"\n");
            
            switch(escolha)
            {
                case 1: {
                    Caminho_percorrido.add(1);
                    /** <------ Login --------> **/
                    String utilizador, password;
                    Utilizador dados_utilizador;
                    utilizador = inputControlo.next();
                    password = inputControlo.next();
                    password += inputControlo.nextLine();
                    
                    System.out.println("Utilizador == "+utilizador+"\n");
                    System.out.println("Password == "+password+"\n");
                    try {
                        dados_utilizador=h.login(utilizador,password);
                    } catch (Exception e) {
                        System.out.println("Erro no Login");
                        outputControlo.println(false);
                        return;
                    }
                    
                    if(dados_utilizador==null)
                    {
                        outputControlo.println(false);
                    }
                    else
                    {
                        eu = dados_utilizador;
                        outputControlo.println(true);
                        //meu_utilizador = utilizador;
                        try {
                            System.out.println("Envia par o RMI \n");
                           // h.adicionarUsersOnline(utilizador);
                         } catch (Exception e) {
                            System.out.println("Erro no Login");
                            outputControlo.println(false);
                            return;
                        }

                    }

                    break;

                }
                case 2:{

                    Caminho_percorrido.add(2);
                    /** <-------- Registo -------> **/
                    String  novoutilizador, novapassword, novapassword2;
                    boolean validanome = false, validaPassword = false;
                   
                    // nome = inputControlo.next();
                    // nome += inputControlo.nextLine();

                    novoutilizador = inputControlo.nextLine();
                    novoutilizador += inputControlo.nextLine();    
                    novapassword = inputControlo.next();
                    novapassword += inputControlo.nextLine();
                     
                    Utilizador controlo=h.registar(novoutilizador, novapassword);
                     
                     
                    if(controlo != null)
                    {
                        outputControlo.println(true);
                    }
                    else
                    {
                        outputControlo.println(false);
                    }
                     
                     //eu = new Utilizador(0,novoutilizador, (new Integer(novapassword.hashCode()).toString()));
                     // IdeaBrokerServerTCP.onlineUsers.add(novapassword);
                     
                     meu_utilizador = novoutilizador;
                     eu = controlo;
                     //IdeaBrokerServerTCP.onlineUsers.add(novoutilizador);
                    
                     //h.adicionarUsersOnline(novoutilizador);
                     //IdeaBrokerServerTCP.listaUtilizadores.add(controlo);

                    break;
                }
                case 3:{

                    //fechar
                    inputControlo.close();
                    outputControlo.close();
                    outputControlo.close();
                    //System.out.println("Estou de saida");
                    break;

                }
                default:
                    break;

            }

        } catch(Exception e) {
            //fechar
            inputControlo.close();
            outputControlo.close();
            outputControlo.close();
            
        }

	}



    /**
    * Retorna uma palavra aleatoria de 5 chars
    */
    public String createRandomName()
    {
         String lexico = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";

         Random rand = new Random();

         Set<String> identifiers = new HashSet<String>();
    
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5)+5;
            for(int i = 0; i < length; i++)
                builder.append(lexico.charAt(rand.nextInt(lexico.length())));
            if(identifiers.contains(builder.toString())) 
                builder = new StringBuilder();
        }
            return builder.toString();
    }
    
    
    public synchronized void responder_ideia()	
	{
        System.out.println("ENTRA");
		String natureza,texto = null;
		natureza = inputControlo.next();
		natureza += inputControlo.nextLine();
		
		texto = inputControlo.next();
		texto += inputControlo.nextLine();
		
		int id_ideia = inputControlo.nextInt();
		int id_topico= inputControlo.nextInt();
		System.out.println("opcao == "+natureza+"\n");

		System.out.println("texto == "+texto+"\n");

		System.out.println("id_ideia == "+id_ideia+"\n");
		System.out.println("id_topico == "+id_topico+"\n");
		
		try {

			if(h.responderideia(natureza, id_topico, id_ideia, texto, eu.getusername(), null) != 0)
            {
                outputControlo.println(true);
            }
            else
            {
                outputControlo.println(false);
            }
		  
        } catch (RemoteException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		
	}


}