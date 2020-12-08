package es.usal.pa;

import java.io.IOException;
import java.io.Serializable;
import java.util.Scanner;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
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
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class CyclicBehaviourPregunta extends CyclicBehaviour {

	private int identificador= 1;
	
	private static final long serialVersionUID = 1L;
	protected OneShotBehaviourPregunta oneShotBehaviourPregunta;
	protected ParallelBehaviourTratamiento paralelo;

	public CyclicBehaviourPregunta(ParallelBehaviourTratamiento paralelo) {
		
		this.paralelo=paralelo;
	}

	@Override
	public void action() {
					 
		ACLMessage msg=this.myAgent.blockingReceive(MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.REQUEST), MessageTemplate.MatchOntology("ontologia")));
		
		int siguienteID = identificador;
		identificador++;
			
		paralelo.generadorPreguntasOneShot(siguienteID,msg);
	}

}
