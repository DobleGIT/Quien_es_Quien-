package es.usal.pa;

import java.io.IOException;
import java.util.Scanner;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;

import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.Envelope;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class cyclicBehaviourJuego extends CyclicBehaviour  {

	

	private static final long serialVersionUID = 1L;
	
	public cyclicBehaviourJuego(Agent agent)
	{
		super(agent);
	}

	@Override
	public void action() {
		
		//Especificamos las caracteristicas del agente a buscar
		DFAgentDescription template=new DFAgentDescription(); 
		ServiceDescription templateSd=new ServiceDescription(); 
		templateSd.setType("QuienEsQuien"); 
		template.addServices(templateSd); 

		SearchConstraints sc = new SearchConstraints();
		sc.setMaxResults(Long.MAX_VALUE);
				
		DFAgentDescription [] results=null;
		try { 

			results = DFService.search(myAgent, template, sc); 

		} catch(FIPAException e){
			e.printStackTrace();
		}
			
		ACLMessage aclMessage =new ACLMessage(ACLMessage.REQUEST);

		aclMessage.addReceiver(results[0].getName());
		aclMessage.setOntology("ontologia");
		aclMessage.setLanguage(new SLCodec().getName());
		aclMessage.setEnvelope(new Envelope());
		aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");

		try {
			
			aclMessage.setContentObject("saludo");
			
		} catch (IOException e) {

			e.printStackTrace();
		}
		//Enviamos un mensaje de tipo request al agente quien es quien para establecer la conexion		
		this.myAgent.send(aclMessage);
				
		System.out.println("\n*************** Quién Es Quién De JF ***************");
				
		Scanner scanner=new Scanner(System.in);
		
		ACLMessage msg;
		String identificador = null;
		boolean primeraEjec= true;  //Este flag se utiliza para comprobar si es la primera pregunta o no
		boolean flag=false;
		while(flag==false) { //Este while se ejecuta hasta que llegue un mensaje de tipo PROPOSE
			
			if(primeraEjec==true) {
				msg=this.myAgent.blockingReceive(
						MessageTemplate.and(
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM), 
								MessageTemplate.MatchPerformative(ACLMessage.PROPOSE)),
								MessageTemplate.MatchOntology("ontologia")));
				
				identificador = msg.getConversationId();
				primeraEjec= false;
			}
			else {
				msg=this.myAgent.blockingReceive(
						MessageTemplate.and(MessageTemplate.MatchConversationId(identificador),
						MessageTemplate.and(
						MessageTemplate.or(MessageTemplate.MatchPerformative(ACLMessage.INFORM), 
								MessageTemplate.MatchPerformative(ACLMessage.PROPOSE)),
								MessageTemplate.MatchOntology("ontologia"))));
			}
			
			
			if(msg.getPerformative()==ACLMessage.PROPOSE) 
			{
						
				flag= true;						
				String temporal = null;
				
				try {
					
					temporal = (String)msg.getContentObject();
					
				} catch (UnreadableException e1) {
							
					e1.printStackTrace();
				}
				
				String[] cadena= temporal.split("-");							
				System.out.println("La solucion es: " +cadena[0]); //Imprimimos las solucion	
						
			}else { //si el mensaje no es PROPOSE es de tipo INFORM por lo que imprimimos el mensaje que nos llega
						
				String mensaje;			
				String temporal = null;
				
				try {
					
					temporal = (String)msg.getContentObject();
					
				} catch (UnreadableException e1) {
						
					e1.printStackTrace();
				}
					
				String[] cadena= temporal.split("-");		//Guardamos en la cadena la pregunta y el identificador					
				System.out.println("Pregunta: "+ cadena[0]); //imprimimos las pregunta
													
				mensaje=scanner.nextLine();
				String mensajeAEnviar= mensaje+"-"+cadena[1];
				
				
				ACLMessage aclMessage1 =new ACLMessage(ACLMessage.INFORM);
				
				aclMessage1.setConversationId(cadena[1]);
				aclMessage1.addReceiver(msg.getSender());
				aclMessage1.setOntology("ontologia");
				aclMessage1.setLanguage(new SLCodec().getName());
				aclMessage1.setEnvelope(new Envelope());
				aclMessage1.getEnvelope().setPayloadEncoding("ISO8859_1");

				try {
					aclMessage1.setContentObject(mensajeAEnviar);
				} catch (IOException e) {

					e.printStackTrace();
				}
						
				this.myAgent.send(aclMessage1);
						
			}//fin del else
		
					
		}//fin del while
		
	}//fin del action
		
}//fin de la clase
