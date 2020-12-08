package es.usal.pa;


import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AgentQuienEsQuien extends Agent {
	
	protected ParallelBehaviour parallelBehaviour; 
	private static final long serialVersionUID = 1L;

	protected void setup() {
		
		super.setup();
		//Creamos el agente quien es quien especificando sus caracteristicas
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		
		ServiceDescription sd = new ServiceDescription();
		sd.setName("QuienEsQuien");
		sd.setType("QuienEsQuien");
		sd.addOntologies("ontologia");
		sd.addLanguages(new SLCodec().getName());
		
		dfd.addServices(sd);
		
		try{
			DFService.register(this,dfd);
		} catch(FIPAException e){
			System.err.println("Agente"+getLocalName()+": "+e.getMessage());
		}

		//UTILIZAMOS UN PARALLEL PARA REALIZAR 5 PETICIONES A LA VEZ
	    parallelBehaviour=new ParallelBehaviour();
	    ThreadedBehaviourFactory tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new ParallelBehaviourTratamiento(this)));
	    
	    /*tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new ParallelBehaviourTratamiento(this)));
	    
	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new ParallelBehaviourTratamiento(this)));

	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new ParallelBehaviourTratamiento(this)));

	    tbf=new ThreadedBehaviourFactory();
	    parallelBehaviour.addSubBehaviour(tbf.wrap(new ParallelBehaviourTratamiento(this)));*/
	    
	    addBehaviour(parallelBehaviour);
	
}}
