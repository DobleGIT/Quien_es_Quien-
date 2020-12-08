package es.usal.pa;

import java.io.IOException;

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

public class AgentCliente extends Agent {
	
	protected cyclicBehaviourJuego cyclicBehaviourJuego;
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		
		super.setup();
		//Creamos el agente cliente
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setName("cliente");
		sd.setType("cliente");
		sd.addOntologies("ontologia");
		sd.addLanguages(new SLCodec().getName());
		
		dfd.addServices(sd);
		
		try{
			DFService.register(this,dfd);
		} catch(FIPAException e){
			System.err.println("Agente"+getLocalName()+": "+e.getMessage());
		}
		
		//se genera un cyclicBehaviour que sera el que reciba los mensajes del AgentQuienEsQuien
		cyclicBehaviourJuego=new cyclicBehaviourJuego(this);
		this.addBehaviour(cyclicBehaviourJuego);
		

	}
}
