package es.usal.pa;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;
import jade.lang.acl.ACLMessage;


public class ParallelBehaviourTratamiento extends ParallelBehaviour {
	
	private static final long serialVersionUID = 1L;
	protected Behaviour cyclicBehaviourPregunta;
	protected Behaviour oneShotBehaviourPregunta;

	public ParallelBehaviourTratamiento(Agent agent)
	{	
		super();
	
		ThreadedBehaviourFactory threadedBehaviourFactory = new ThreadedBehaviourFactory();
		cyclicBehaviourPregunta = new CyclicBehaviourPregunta(this);
		addSubBehaviour(threadedBehaviourFactory.wrap(cyclicBehaviourPregunta));
	}
	
	
	public void generadorPreguntasOneShot(int id, ACLMessage msg)
	{
		ThreadedBehaviourFactory threadedBehaviourFactoryOneShot = new ThreadedBehaviourFactory();
		oneShotBehaviourPregunta = new OneShotBehaviourPregunta(id,msg); 
		addSubBehaviour(threadedBehaviourFactoryOneShot.wrap(oneShotBehaviourPregunta));	
	}
	
	
	
	
	
	
	

	

	

}
