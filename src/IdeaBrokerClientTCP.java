/*
*Autores:
* Bruno Miguel Oliveira Rolo nº 2010131200
* Joao Artur Ventura Valerio Nobre nº 2010131516
*/

import java.net.*;
import java.util.*;
import java.io.*;



public class IdeaBrokerClientTCP{

	static int estado_servidor = 0;
	static int MAXIMO_BUFFER = 5; 
    static Vector<Pacote> buffer = new Vector<Pacote>();
    static Vector<Integer> comandos_offline = new Vector<Integer>();
    static String ip_actual = null;

	//Portas que tentaremos ligar
  	private static int [] Portas1 = { 1234, 2234 };
    private static int [] Portas2 = { 1233, 2233 };
    private static String [] ServidoresTCP = {"", ""};
    private static int debug = 0;

	public static void main(String args[]) {

		int contador = 0;
		Socket input_output = null; //usado para notificacoes e controlo do fail-over
        String informacao; // stream para troca de mensagens
        Scanner dadosServidor;
        
		if (args.length <= 1) {
		    System.out.println("Modo de iniciação: java -cp . IdeaBrokerClientTCP <IP_servidorTCP_primario> <IP_servidorTCP_secundario>");
		    System.exit(0);
		}

		if(args[0].compareTo(args[1]) != 0) 
        {
            Portas1[1] = Portas1[0];
            Portas2[1] = Portas2[0];
        }

        ServidoresTCP[0] = args[0];
        ServidoresTCP[1] = args[1];
        debug = 0;
        if(debug == 1)
        {
	        System.out.println("Servidor 1 == "+ServidoresTCP[0]+"\n");
	        System.out.println("Servidor 2 == "+ServidoresTCP[1]+"\n");
	    }

	    /**Ligacao com os servidores**/
        while(true)
        {
        	try{

                // System.out.println("Contador  == " + contador);
                // System.out.println("Contador == " + contador);
        		input_output = new Socket(ServidoresTCP[contador],Portas1[contador]);
                dadosServidor = new Scanner(input_output.getInputStream());
               
                estado_servidor = -1;
                MenuPrincipal thread = new MenuPrincipal();
                thread.setNewSocket(new Socket(ServidoresTCP[contador], Portas2[contador]));
                thread.iniciar();
                ip_actual = ServidoresTCP[contador];
                estado_servidor = 1;

                /**Espera por dados do servidor**/
                while(true)
                {
                    try{
                            informacao = dadosServidor.next();
                            //System.out.println("Dentro do while a escuta\n");
                            if(informacao.contentEquals("qwerty"))
                            {
                                // Le o username a que se destina esta notificacao e verifica-se somos nos
                                //System.out.println("Esta a verificar notificacao.....\n");
                                String utilizador_notificacao = dadosServidor.next();
                                //System.out.println("Recebi uma notificacao utilzador == "+utilizador_notificacao+" \n");
                                if(utilizador_notificacao.contentEquals(thread.getUtilizadologado()))
                                {
                                    String tipo = dadosServidor.next();
                                    tipo += dadosServidor.nextLine();
                                    thread.acidionar_notificacao("[NOTIFICAÇÃO] "+tipo+"\n");
                                    
                                }
                            }
            
                        }
                        catch(NoSuchElementException e)
                        {    
                            // System.out.println("Contador 5 == " + contador);
                            // Alterna entre varias portas 
                            ++contador;
                            if (contador > 1) contador = 0;
                            //System.out.println("Catch no such e")
                            estado_servidor = 0;
                            //System.out.println("Contador  == " + contador);
                            try {
                                
                                input_output = new Socket(ServidoresTCP[contador], Portas1[contador]);
                                thread.setNewSocket(new Socket(ServidoresTCP[contador], Portas2[contador]));
                                dadosServidor=new Scanner(input_output.getInputStream());
                                thread.iniciar();
                                ip_actual = ServidoresTCP[contador];
                                System.out.println("O servidor actual == "+ip_actual+"\n");
                                thread.anunciar_utilizador();
                                estado_servidor = 1;
                                
                            } catch(Exception e2) {

                                try { Thread.sleep(1500); } catch(Exception a) { 
                                    a.printStackTrace();
                                }

                            }
                             //System.out.println("Contador 6 == " + contador);
                        }
                }

        	} catch (UnknownHostException e) {

        		contador++;
        		if(contador > 1) contador = 0;
        		System.out.println("Erro:" + e);
        										
        	} catch (EOFException e) {
            
                //System.out.println("Erro:" + e);
            
            } catch (IOException e) {
            
                ++contador;
                if(contador > 1) contador = 0;
                //System.out.println("Erro:" + e);
            
            }

        }

	}

}


class MenuPrincipal extends Thread
{

    PrintWriter output;
    Socket ligacao;
    Scanner inputMensagem;
    Scanner inputestado;
    ObjectInputStream objMensagem;
    ObjectOutputStream objMensagemOut;

    static String utilizador_logado = null;

    Vector<String> notificacao = new Vector<String> ();

    public MenuPrincipal() {
        this.start();
    }

    public void iniciar()
    {
        try
        {
            output = new PrintWriter(ligacao.getOutputStream(), true);
            inputMensagem = new Scanner(System.in);
            inputestado = new Scanner(ligacao.getInputStream());
            objMensagem = new ObjectInputStream(ligacao.getInputStream());
            objMensagemOut = new ObjectOutputStream(ligacao.getOutputStream());
            new Buffer(output, inputestado).start();

        } catch(IOException e) {

        }
    }

    public void acidionar_notificacao(String texto)
    {
        notificacao.add(texto);
    }

    public static String getUtilizadologado()
    {
        return utilizador_logado;
    }

    /** Envia utilizador com que estavamos logados antes da ligação cair**/
    /*public void Utilizador_v2()
    {
        if(utilizador_logado != null)
        {
            output.println(101);
            output.println(utilizador_logado); 
        }
        else
            output.println(1); // login a false, precisa fazer login
    }*/

      /**
     * Envia o username com que estavamos logged antes da ligaçao cair
     */
    public void anunciar_utilizador()
    {
       if (utilizador_logado != null)
       {

           output.println(101);
           output.println(utilizador_logado);
           System.out.println("Passa por aqui\n");

       } else { 

           //Anuncia que vamos fazer login/registar
           output.println(1);

       }
    }

    /**
    * Utilizado para criar a existencia de uma thread que trata dos retries quando a ligação 
    * ao serivdor é perdida
    **/
    public void setNewSocket(Socket _ligacao)
    {
        ligacao = _ligacao;
    }

    public void run()
    {
        int escolha_2 = -1;
        /**Espera pela outra thread, que todos os elementos estejam ligados**/
        while(IdeaBrokerClientTCP.estado_servidor == -1)
        {
            //System.out.println("Sleeeep\n");
            try{

                Thread.sleep(200);  // Espera pela outra thread

            }catch(Exception e){
               // a.printStackTrace();
            }

        } // Espera que todos os scanners,etc estejam ligados

        boolean login_status = false;
        System.out.println("\n\n <----------- Bem-vindo ao IdeaBroker -----------> \n\n");
        while(!(login_status=login())) {}

        /**<--- Menu Principal --->**/
        while(true && login_status)
        { 
            System.out.println("\n 1 -> Ver Topicos \n 2 -> Historico \n 3 -> Profile \n 4 -> Sair");

            //System.out.println("Notificacao == "+notificacao.size()+"\n");
            if(notificacao.size() != 0) for (String s: notificacao)
            {
                System.out.println(s);
            } 
            notificacao.clear();
            
            System.out.println("Insira a sua escolha: ");

            int escolha = -1;
            while( (escolha < 1) || (escolha > 4) )
            {
                try
                {
                    escolha = inputMensagem.nextInt();
                }
                catch(Exception e)
                {
                    try
                    {
                        System.out.println("Numero Invalido! \n Insira a sua escolha:");
                        inputMensagem.next();
                    }catch(NoSuchElementException e2){
                        return;
                    }
                }
            }
            
           // System.out.println("A escolha === "+escolha+"\n");

            switch(escolha){
                
                case 1: {

                    if(IdeaBrokerClientTCP.estado_servidor != 0)
                    {
                        //System.out.println("Primeira Escolha == "+escolha+"\n");
                        escolha_2 = -1;

                        output.println(10);
                        listagem_topicos();
                        System.out.println("\n 1 -> Criar Topico\n 2 -> Apagar Topico \n 3 -> Entrar no Topico \n 4 -> Voltar\n");
                        
                        if(notificacao.size() != 0) for (String s: notificacao)
                        {
                            System.out.println(s);
                        } 
                        notificacao.clear();

                        System.out.println("Insira a sua escolha: ");

                        while ( (escolha_2 < 1) || (escolha_2 > 4) )
                        {
                            try{

                                //System.out.println("Insira a sua opção: ");
                                escolha_2 = lerInt();

                            } catch(Exception e){

                                System.out.println("\nNumero Invalido!\n");
                            }
                        }

                       //output.println(escolha_2);
                       //System.out.println("Segunda Escolha == "+escolha_2+"\n");
                           
                        switch (escolha_2){
                            case 1:
                                {
                                    output.println(11);
                                    criartopico();
                                    break;

                                }
                            case 2:

                                if(IdeaBrokerClientTCP.estado_servidor != 0)
                                {
                                    output.println(12);
                                    apagartopico();
                                }
                                else
                                    System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                                
                                break;
                            
                            case 3: {

                                if(IdeaBrokerClientTCP.estado_servidor != 0)
                                {
                                    output.println(13);
                                    //IdeaBrokerClientTCP.comenados_offline(13);
                                    
                                    System.out.println("Introduza o ID do Tópico que pretende entrar: ");
                                    int id_topico = inputMensagem.nextInt();

                                    listagem_ideias(id_topico);

                                    int escolha_3 = -1;
                                    if(IdeaBrokerClientTCP.estado_servidor != 0)
                                    {
                                        System.out.println("\n 1 -> Criar Ideia \n 2 -> Responder a Ideia \n 3 -> Comprar Accoes da Ideia \n 4 -> Download do Ficheiro de Anexo \n 5 -> Apagar Ideia \n 6 -> Voltar \n");
                                        //escolha_3 = -1;
                                        if(notificacao.size() != 0) for (String s: notificacao)
                                        {
                                            System.out.println(s);
                                        } 
                                        notificacao.clear();
                
                                        while ((escolha_3 < 1) || (escolha_3 > 6))
                                        {
                                            try{
                                                System.out.println("Insira a sua opção: ");
                                                escolha_3 = lerInt();
                                            } catch(Exception e){
                                                System.out.println("\nNumero Invalido!\n");
                                            }
                                        }

                                        //System.out.println("Terceira Escolha == "+escolha_3+"\n");   
                                        
                                        //output.println(escolha_3);
                                        switch (escolha_3)
                                        {
                                            case 1: 

                                                output.println(14);
                                                criarideia(id_topico);
                                                break;

                                            case 2: 

                                                output.println(15);
                                                responder_ideia(id_topico);
                                                break;

                                            case 3:

                                                if(IdeaBrokerClientTCP.estado_servidor != 0)
                                                {
                                                    output.println(16);
                                                    bolsa(id_topico);
                                                }
                                                else
                                                    System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                              
                                                break;

                                            case 4:

                                                if(IdeaBrokerClientTCP.estado_servidor != 0)
                                                {
                                                    output.println(17);
                                                    System.out.println("Insira o ID da ideia que pretende fazer download do anexo: ");
                                                    int id_ideia = lerInt();
                                                    download_ficheiro(id_ideia);
                                                }
                                                else
                                                    System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                                                
                                                break;

                                            case 5:

                                            	output.println(19);
                                            	System.out.println("Insira o ID da ideia que pretende apagar");
                                            	int id_ideia=lerInt();
                                            	output.println(id_topico);
                                            	output.println(id_ideia);
                                            	String texto="";
                                            	texto= inputestado.nextLine();
                                            	System.out.println(texto);
                                            	break;

                                            case 6:

                                               // output.println(18);
                                                break;
                                        }
                                        //break; 
                                    }
                                    else
                                    {
                                        System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                                        break;
                                    }
                                }
                                else
                                {
                                    System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                                    break;
                                }
                                
                            }
                            break;
                            
                            case 4:
                                break;
                            default:
                                break;
                        }
                    }
                    else
                        System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                    break;

                    }
                    

                case 2: {

                        if(IdeaBrokerClientTCP.estado_servidor != 0)
                        {
                            output.println(40);
                            historico();
                        }
                        else
                            System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                              
                    }
                    break;

                case 3: {

                        if(IdeaBrokerClientTCP.estado_servidor != 0)
                        {
                            output.println(41);
                            profile();
                        }
                        else
                            System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                                                                     
                    }
                    break;

                case 4: {
                    
                     //output.println(14);
                     try {

                        output.println(163);
                        ligacao.close();
                        output.close();
                        login_status = false;
                        
                        } catch (IOException e){

                            System.out.println("Erro ao fechar o socket\n");

                        }
                        
                        System.out.println("\n Obrigado pela visita! :) \n");
                        System.exit(0);
                    }
                    break;


            }


        }   

    }

    public void criarideia(int id_topico)
    {

    	/** Criar ideia no topico x **/
        String opcao = null;
        String anexo = null;
        String nome_anexo = null;
        String texto = null;
        Data _data = null;
        ArrayList<Integer> Comandos = new ArrayList<Integer>();
        
        boolean buffer = false;
        System.out.println("Vou criar uma ideia no idtopico=" + id_topico);
        System.out.println("\n 1- Ideia com texto\n 2- Ideia com anexo\n3- Ideia com texto e anexo");
        int escolha = -1;

        while ((escolha < 1) || (escolha > 3)) {
            System.out.println("Por favor escolha uma opcao: ");
            escolha = lerInt();
        }

        switch (escolha) {

            case 1: {            		
            	
                    System.out.println("\nIntroduza o texto pretendido: ");
                    texto = inputMensagem.next();
                    texto += inputMensagem.nextLine();
                
            	}
                break;

            case 2: {

                    System.out.println("\nIntroduza o caminho para o anexo pretendida: ");
                    anexo = inputMensagem.next();
                    anexo += inputMensagem.nextLine();
                    System.out.println("\nIntroduza o nome do anexo: ");
                    nome_anexo = inputMensagem.next();
                    nome_anexo += inputMensagem.nextLine();
                
                }
                break;
                
            case 3: {

                    System.out.println("\nIntroduza o texto pretendido: ");
                    texto = inputMensagem.next();
                    texto += inputMensagem.nextLine();

                    System.out.println("\nIntroduza o caminho para o anexo pretendida: ");
                    anexo = inputMensagem.next();
                    anexo += inputMensagem.nextLine();
                
                    System.out.println("\nIntroduza o nome do anexo: ");
                    nome_anexo = inputMensagem.next();
                    nome_anexo += inputMensagem.nextLine();
                
            	}
                break;
        }

        try
        {

            if(IdeaBrokerClientTCP.estado_servidor != 0)
            {   
                output.println(escolha);
                output.println(id_topico);
            }
            else
            {
                 Comandos.add(14);
                 Comandos.add(escolha);
                 Comandos.add(id_topico);
                 buffer = true;
            }
               
            
            //System.out.println("escolha ==  "+ escolha+"\n");
            
            if(escolha != 2)
                if(IdeaBrokerClientTCP.estado_servidor != 0 && buffer == false)
                {
                	output.println(texto);
                }
                else
                {
                    //Comandos.add(texto);
                    buffer = true;
                }
                    

            if(escolha != 1)
                if(IdeaBrokerClientTCP.estado_servidor != 0 && buffer == false)
                {
                    System.out.println("O IP do servidor == "+IdeaBrokerClientTCP.ip_actual+"\n");
                    try
                    {
                    	new AnexoCliente(anexo, nome_anexo, IdeaBrokerClientTCP.ip_actual);
                    	output.println(anexo);
                    	output.println(nome_anexo);
                    }
                    catch(Exception ex)
                    {
                    	System.out.println("Nao foi possível efetuar anexar a informação");
                    	
                    	//ex.printStackTrace();
                    }
                }
                else
                {
                    /*Comandos.add(anexo);
                    Comandos.add(nome_anexo);*/
                    buffer = true;
                }
                    

            /*String data_envio = _data.getAno() + " " + _data.getMes() + " " + _data.getDia() + " " + _data.getHora() + " " + _data.getMin(); 
            if(IdeaBrokerClientTCP.estado_servidor != 0 && buffer == false)
                output.println(data_envio);
            else
                buffer = true; */
            
            if(IdeaBrokerClientTCP.estado_servidor != 0 && buffer == false)
                inputestado.nextBoolean();
            else
                buffer = true;

            if(buffer == true)
            {
                if(IdeaBrokerClientTCP.buffer.size() < IdeaBrokerClientTCP.MAXIMO_BUFFER)
                    IdeaBrokerClientTCP.buffer.add(new Pacote(Comandos, null, texto, nome_anexo, anexo));
                else
                    System.out.println(" Nao e possivel aceitar mais pedidos. Pedimos desculpa pelo incomodo. \n");
            }


        }
        catch( Exception e )
        {
            if(IdeaBrokerClientTCP.buffer.size() < IdeaBrokerClientTCP.MAXIMO_BUFFER)
                 IdeaBrokerClientTCP.buffer.add(new Pacote(Comandos, null, texto, nome_anexo, anexo));
            else
                System.out.println(" Nao e possivel aceitar mais pedidos. Pedimos desculpa pelo incomodo. \n");
        }

    }

    public void download_ficheiro(int id_ideia)
    {
        System.out.println("Funcionalidade não Disponivel.\n");
    }

    public void responder_ideia(int id_topico)
    {
    	int id_ideia = -1;
        String opcao,texto = null;
        boolean buffer = false;
        ArrayList<Integer> Comandos = new ArrayList<Integer>();

        System.out.println("Qual é o tipo desta resposta?\n\t1-Concordo\n\t2-Neutro\n\t3-Discordo");
        opcao = inputMensagem.next();
        
        if(opcao.compareTo("1") == 0)
        {
        	opcao = "Concordo";
        }
        else if(opcao.compareTo("2") == 0)
        {
        	opcao = "Neutro";
        }
        else if(opcao.compareTo("3") == 0)
        {
        	opcao = "Discordo";
        }
        else
        {
        	opcao = "N/R";
        }

        System.out.println(" Introduza o ID da Ideia que quer responder: ");
        //output.println(opcao);
        id_ideia =  lerInt();
        System.out.println(" Introduza a sua resposta:");
        texto = inputMensagem.next();
        texto += inputMensagem.nextLine();
        //estado_servidor
        //output.println(texto);
        //output.println(id_ideia);
        //output.println(id_topico);

        try
        {
            if(IdeaBrokerClientTCP.estado_servidor != 0)
            {   
                output.println(opcao);
                output.println(texto);
                output.println(id_ideia);
                output.println(id_topico);
            }
            else
            {
                //Comandos.add(opcao);
                //Comandos.add(texto);
                Comandos.add(15);
                Comandos.add(id_ideia);
                Comandos.add(id_topico);
                buffer = true;
            }
                
            
            if(IdeaBrokerClientTCP.estado_servidor != 0 && buffer == false)
            {
                if(inputestado.nextBoolean())
                    System.out.println(" Resposta Inserida com Sucesso. \n");
                else
                    System.out.println(" Insucesso a criar a Resposta. \n");
            }
            else
            {
                buffer = true;
            }
      
            if(buffer == true)
            {
                if(IdeaBrokerClientTCP.buffer.size() < IdeaBrokerClientTCP.MAXIMO_BUFFER)
                    IdeaBrokerClientTCP.buffer.add(new Pacote(Comandos, null, texto, opcao, null));
                else
                    System.out.println(" Nao e possivel aceitar mais pedidos. Pedimos desculpa pelo incomodo. \n");
            }

        }
        catch( Exception e )
        {
            if(IdeaBrokerClientTCP.buffer.size() < IdeaBrokerClientTCP.MAXIMO_BUFFER)
               IdeaBrokerClientTCP.buffer.add(new Pacote(Comandos, null, texto, opcao, null));
            else
                System.out.println(" Nao e possivel aceitar mais pedidos. Pedimos desculpa pelo incomodo. \n");
        }


    }

    public void bolsa(int id_topico)
    {

        System.out.println("Introduza o ID da Ideia que deseja comprar");
        int id_ideia=inputMensagem.nextInt();
        output.println(id_ideia);
        output.println(id_topico);
        System.out.println("Esperando texto....\n");
        String texto="";
        String aux;
          while(true)
          {
              aux = inputestado.nextLine();
              if(aux.contentEquals("-9999")) break;
              texto += "\n" + aux;
              
          }
        System.out.println(texto);
        System.out.println("\n Introduza o nome do utilizador a que deseja comprar as acções ou escreva -1 para cancelar compra:");
        String nome=inputMensagem.next();
        nome += inputMensagem.nextLine();
        
        output.println(nome);
        if(nome.compareTo("-1")==0)
        {
        	System.out.println("Compra Cancelada.\n");
        	return;
        }
        int max=inputestado.nextInt();
        int percentagemuser=0;
        while(percentagemuser<1 || percentagemuser>max)
        {
            System.out.println("Introduza a percentagem de accções que deseja comprar:\n(Entre 1% e " + max + "%)");
            percentagemuser=inputMensagem.nextInt();
        }
        output.println(percentagemuser);
        System.out.println("Introduza a que preço deseja vender as acções:");
        int preco=inputMensagem.nextInt();
        output.println(preco);

    }

    public void listagem_topicos()
    {
        /**Listagem dos topicos do sistema**/
        int tamanho;
        if(IdeaBrokerClientTCP.estado_servidor != 0)
        {
            tamanho = inputestado.nextInt();
        }
        else
        {
            System.out.println("Sem ligação ao Servidor\n");
            return;
        }

        if(tamanho != 0)
        {
            String aux = "", aux_2 = "";
            System.out.println("<------ Tópicos ------>");

            while(true)
            {
                if(IdeaBrokerClientTCP.estado_servidor != 0)
                    aux_2 = inputestado.nextLine();
                else
                    break;
                if(aux_2.contentEquals("-9999")) break;
                aux += "\n" + aux_2;
            }
            
            System.out.println(aux);
            System.out.println("<---------------->\n");
        }
        else
            System.out.println("\n Não existem Tópicos....\n");
    }
    
    public void listagem_ideias(int topico)
    {
        /** Listagem da ideia**/
        int escolha, tamanho;

        output.println(topico);    

        tamanho = inputestado.nextInt();
        if(tamanho != 0)
        {
            String aux = "", aux_2 = "";
            while(true)
            {
                aux_2 = inputestado.nextLine();
                if(aux_2.contentEquals("-9999")) break;
                aux += "\n" + aux_2;
            }
            System.out.println(aux);

        }
        else
            System.out.println("\n Não existem ideias....\n");

    }

    public void criartopico()
    {
        String tema = null;
        boolean buffer = false;
        ArrayList<Integer> Comandos = new ArrayList<Integer>();

        System.out.println("\n <---- Criação e Tópico ----> \n");
        System.out.println("\n Introduza o Tema do tópico: \n");
        //inputMensagem.nextLine();
        tema = inputMensagem.next();
        tema += inputMensagem.nextLine();

       //output.println(tema);
        
          try {
    
            if (IdeaBrokerClientTCP.estado_servidor != 0) {
                output.println(tema);
            } else {
                Comandos.add(11);
                buffer = true;
            }
            
            if (buffer == true) {

                if (IdeaBrokerClientTCP.buffer.size() < IdeaBrokerClientTCP.MAXIMO_BUFFER) {

                    IdeaBrokerClientTCP.buffer.add(new Pacote(Comandos, null, tema, null, null));
               
                } else {

                    System.out.println("Nao e' possivel aceitar mais pedidos.\n");

                }

            }

        } catch (Exception e) {

            if (IdeaBrokerClientTCP.buffer.size() < IdeaBrokerClientTCP.MAXIMO_BUFFER) {
               IdeaBrokerClientTCP.buffer.add(new Pacote(Comandos, null, tema, null, null));
            } else {
                System.out.println("Nao e' possivel aceitar mais pedidos.\n");
            }
        }

        if((IdeaBrokerClientTCP.estado_servidor != 0) && (buffer == false))
            if(inputestado.nextBoolean())
                System.out.println(" Topico criado com sucesso! \n");
            else
                System.out.println(" Erro ao criar o Topico! \n");
    }


    public void apagartopico()
    {
        int id_topico = -1;

        System.out.println("Funcionalidade não implementada.\n ");
       

    }


    public void profile()
    {
        String aux,texto="";
        while(true)
        {
          aux = inputestado.nextLine();
          if(aux.contentEquals("-9999")) break;
          texto += "\n" + aux;
          
        }
        System.out.println(texto);
    }

    public void historico()
    {
        String aux,texto="";
        while(true)
        {
          aux = inputestado.nextLine();
          if(aux.contentEquals("-9999")) break;
          texto += "\n" + aux;
          
        }
        System.out.println(texto);
    }

    public boolean login()
    {
        boolean login_status = false;
        int escolha = -1;
        boolean buffer = false;

        if(IdeaBrokerClientTCP.estado_servidor != 0)
            output.println(1);
        else
        {
            System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
            return false;                                
        }

        //System.out.println("Vai entrar aqui...");
        
        System.out.println("\n 1 -> Login \n\n 2 -> Registo \n\n 3 -> Sair");

        while((escolha < 1) || (escolha > 3))
        {
            try
            {
                System.out.println(" Escolha opcao pretendida:");
                escolha = inputMensagem.nextInt();
            
            }catch (Exception e){
            
                try
                {
                    System.out.println(" Inseriu numero invalido! \n");
                    inputMensagem.next();
                }catch(NoSuchElementException e2)
                {
                    return false;
                }
            
            }
        }
        
        
        output.println(escolha);
        //System.out.println("A escuta recebe a escolha == "+escolha+"\n");

        switch(escolha)
        {
            case 1: {

                String utilizador, password;
                System.out.println("\n <------- Login ------->\n");
                System.out.println("Utilizador: ");
                utilizador = inputMensagem.next();
                
                if(IdeaBrokerClientTCP.estado_servidor != 0)
                    output.println(utilizador);
                else
                {
                    return false;                                
                }


                System.out.println("Pass: ");
                password = inputMensagem.next();
                password += inputMensagem.nextLine();
                if(IdeaBrokerClientTCP.estado_servidor != 0)
                    output.println(password);
                else
                {
                    System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                    return false;                                
                }

                if(!inputestado.nextBoolean())
                    System.out.println("\n Erro no login, dados incorrectos!\n");
                else
                {
                    System.out.println("\n Login Efectuado com Sucesso \n");
                    utilizador_logado = utilizador;
                    login_status = true;
                }
                break;
            }
            case 2: {

                String nome, novoUtilizador, nova_password, nova_password2;
                boolean validaUtilizador = false, validarPassword = false;

                System.out.println("\n <------- Criar Novo Utilizador ------->\n");
                //System.out.println(" Utilizador: ");
                //nome = inputMensagem.next();
                //nome += inputMensagem.nextLine();
                //output.println(nome);

                System.out.println(" Introduza o nome:");
                novoUtilizador = inputMensagem.next();
                if(IdeaBrokerClientTCP.estado_servidor != 0)
                    output.println(novoUtilizador);
                else
                {
                    System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                    return false; 
                }

                System.out.println("Introduza a password:");
                nova_password = inputMensagem.next();
                nova_password += inputMensagem.nextLine();
                if(IdeaBrokerClientTCP.estado_servidor != 0)
                    output.println(nova_password);
                else
                {
                    System.out.println("ATENÇÃO! Pedimos desculpa mas a ligação ao servidor falhou. Pelo que não pode efectuar esta operação.\n");
                    return false; 
                }

                if(inputestado.nextBoolean())
                {
                    System.out.println("\n<------ Resgisto feito com sucesso! ------>\n ");
                    login_status = true;
                    utilizador_logado = novoUtilizador;
                }
                else
                    System.out.println("\n<------ Erro no Registo ------>\n ");
                break;
                
            }
            case 3:
                //output.println(163);
                System.out.println("\nEstou a sair...\n ");
                login_status = false;
                System.exit(0);
                break;
            default:
                break;

        }

        return login_status;

    }

    /** Validar input -> numero inteiro **/
    public int lerInt() {

        int valor = 0;
        boolean valida = false;

        do {
            try {
                valida = inputMensagem.hasNextInt();
                valor = inputMensagem.nextInt();

                if (valor < 0) {
                    System.out.println("O valor tem de ser um inteiro maior ou igual a 0!\n: ");
                }

            } catch (InputMismatchException e) {
                System.out.println("O valor tem de ser um inteiro positivo!\n ");
                inputMensagem.next();
            }
            
        } while (!valida || valor < 0);

        return valor;
    }

} // Fecha Class MenuPrincipal




/** <------------ VOLTAR AQUI ---------------> **/
class Buffer extends Thread {
    
    PrintWriter output;
    Scanner inputestado;
    
    public Buffer(PrintWriter output, Scanner inputestado) {
        this.output = output;
        this.inputestado = inputestado;
    }
    
    public void run() {
        
        int aux, delay = 5000;
        Data tempo;

        while (true) {
           
            if (!IdeaBrokerClientTCP.buffer.isEmpty()) {
                switch (IdeaBrokerClientTCP.buffer.get(0).getComandos().get(0)) {
                    
                    case (11):   // criar topico
                        
                        try {
                            
                            //output.println(11);
                            output.println(IdeaBrokerClientTCP.buffer.get(0).getComandos().get(0));
                            //output.println(aux = IdeaBrokerClientTCP.buffer.get(0).getComandos().get(1));
                            output.println(IdeaBrokerClientTCP.buffer.get(0).getMensagemTexto());
                            //tempo = IdeaBrokerClientTCP.buffer.get(0).getDataPublicacao();
                            
                            if(inputestado.nextBoolean())
                                System.out.println("Topico Cria com sucesso!\n");
                            else
                                System.out.println("Erro a Criar o Topico!\n");
                            IdeaBrokerClientTCP.buffer.remove(0);

                        } catch (Exception e) {

                        }
                        break;

                    case (14): // criar ideia
                       
                        try {

                            output.println(14);
                            output.println(aux = IdeaBrokerClientTCP.buffer.get(0).getComandos().get(1));
                            output.println(IdeaBrokerClientTCP.buffer.get(0).getComandos().get(2));
                           
                            if(aux != 2){

                                output.println(IdeaBrokerClientTCP.buffer.get(0).getMensagemTexto());
                            
                            }

                            if(aux != 1){

                                output.println(IdeaBrokerClientTCP.buffer.get(0).getAnexo()); 
                                output.println(IdeaBrokerClientTCP.buffer.get(0).getData());  
                                output.println("3335");
                            }

                            //output.println(IdeaBrokerClientTCP.buffer.get(0).getUtilizador());
                            if (!inputestado.nextBoolean()) {
                                System.out.println("\nNão foi possivel enviar a mensagem porque o utilizador "
                                        + IdeaBrokerClientTCP.buffer.get(0).getUtilizador() + " nao existe!\n");
                            } else {
                                //output.println(IdeaBrokerClientTCP.buffer.get(0).getMensagemTexto());
                                System.out.println("Ideia Criada com sucesso\n");
                            }
                            

                            IdeaBrokerClientTCP.buffer.remove(0);

                        } catch (Exception e) {
                                
                        }
                        break;
                    case (15): //responder a ideia

                        try
                        {
                            output.println(IdeaBrokerClientTCP.buffer.get(0).getComandos().get(0));
                            output.println(IdeaBrokerClientTCP.buffer.get(0).getData());
                            output.println(IdeaBrokerClientTCP.buffer.get(0).getMensagemTexto());
                            output.println(IdeaBrokerClientTCP.buffer.get(0).getComandos().get(1));
                            output.println(IdeaBrokerClientTCP.buffer.get(0).getComandos().get(2));
                                                      
                             
                            if (!inputestado.nextBoolean()) {
                                System.out.println("\nNão foi possivel criar a Resposta a Ideia.\n");
                            } else {
                                //output.println(IdeaBrokerClientTCP.buffer.get(0).getMensagemTexto());
                                System.out.println("\nResposta a Ideia Criada com sucesso.\n");
                            }
                            IdeaBrokerClientTCP.buffer.remove(0);

                    
                        } catch (Exception e) {
                                
                        }
                    break;

                }//switch
            }//if
            else {
                return;
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException ex) {
                //ex.printStackTrace();
            }
        }//while
        
    }
}
