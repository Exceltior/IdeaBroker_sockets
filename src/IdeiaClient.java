import java.rmi.*;
import java.util.ArrayList;
import java.util.Scanner;


public class IdeiaClient {
	//Servidor TCP
	public ArrayList<Topico> Topicos = new ArrayList<Topico>();
	
	
	public int geraridtopico()
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
	
	
	//Funções
	public int criartopico(String tema)
	{
		//Get de todos os topicos existentes no RMI
		
		String autor="eu";
		//Real String autor=Utilizadores.getusername();
		//lock
		for(int i=0;i<Topicos.size();i++)
		{
			if(Topicos.get(i).gettema().compareTo(tema)==0)
			{
				//Unlock
				return 0;
			}
		}
		int id=geraridtopico();
		//Criar topico
		Topicos.add(new Topico(id,tema,autor,null));
		//Unlock
		return 1;
	}

	public static void main(String args[]) {
		
		//Teste registo 
		int a=0;
		Utilizador user;
		String utilizador;
		String password;
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());
		
		/*try {
			
			//ESTE H tem todos os métodos do servidor
			IdeiaMethods h = (IdeiaMethods) Naming.lookup("Ideia");
			h.sayHello();

			Scanner sc = new Scanner(System.in);
			while(a==0)
			{
				int teste;
				System.out.println("Introduza um Utilizador:");
				utilizador=sc.nextLine();
				System.out.println("Introduza uma Password:");
				password=sc.nextLine();
				//MeuObjecto qlqcoisa = new MeuObjecto(a,b,c);
				//	h.publish(qlqcoisa);
				teste=h.registar(utilizador,password);
				if(teste==1)
				{
					System.out.println("Utilizador foi registado com sucesso");
				}
				else
				{
					System.out.println("Erro no registo");
				}
				
			}
		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
		}*/
		
		//Topicos
		try {
			//ESTE H tem todos os métodos do servidor
			IdeiaMethods h = (IdeiaMethods) Naming.lookup("Ideia");
			h.sayHello();
			Scanner sc = new Scanner(System.in);
			while(a==0)
			{
				Utilizador teste;
				System.out.println("Introduza um Utilizador:");
				utilizador=sc.nextLine();
				System.out.println("Introduza uma Password:");
				password=sc.nextLine();
				//MeuObjecto qlqcoisa = new MeuObjecto(a,b,c);
				//	h.publish(qlqcoisa);
				teste=h.registar(utilizador,password);
				if(teste!=null)
				{
					System.out.println("Utilizador foi registado com sucesso");
				}
				else
				{
					System.out.println("Erro no registo");
				}
				
				
			}
		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
		}
		

	}

}