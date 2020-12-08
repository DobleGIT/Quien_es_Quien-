package es.usal.pa;

import java.io.IOException;
import java.io.Serializable;

import jade.content.lang.sl.SLCodec;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
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

public class OneShotBehaviourPregunta extends OneShotBehaviour  {
	
	private int id;
	private ACLMessage msg;
	
	private static final long serialVersionUID = 1L;
	
	public OneShotBehaviourPregunta(int id, ACLMessage msg) {
		 this.id=id;
		 this.msg=msg;
	}

	@Override
	public void action() {
		DataSource source = null;
		try {
			source = new DataSource("famosos.csv");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		 Instances dataEntrenamiento = null;
		try {
			dataEntrenamiento = source.getDataSet();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		 

		 if (dataEntrenamiento.classIndex() == -1)
			 dataEntrenamiento.setClassIndex(0);

		 
		 J48 j48 = new J48();
		 try {
			j48.setOptions(new String[] {"-C", "0.25", "-M", "1"});
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		 j48.setUnpruned(true);
		 try {
			j48.buildClassifier(dataEntrenamiento);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		 double clasePredicha = 0;
		try {
			clasePredicha = j48.classifyInstance(dataEntrenamiento.lastInstance());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		 
		
		Pregunta pregunta = null;
		try {
			pregunta = new Pregunta(j48);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//Hasta aqui ya se ha hecho todo lo necesario para generar las preguntas
		
		
		while(!pregunta.esNodoFinal()) //mientras la pregunta no sea la pregunta final
		 { 		
		
			String temp=pregunta.obtenerPreguntaNodo();		
			String cadenaAEnviar= temp+"-"+id;
			
			ACLMessage aclMessage =new ACLMessage(ACLMessage.INFORM);
			
			aclMessage.setConversationId(String.valueOf(id));
			aclMessage.addReceiver(msg.getSender());
			aclMessage.setOntology("ontologia");
            //el lenguaje que se define para el servicio
            aclMessage.setLanguage(new SLCodec().getName());
            //el mensaje se transmita en XML
            aclMessage.setEnvelope(new Envelope());
			//cambio la codificacion de la carta
			aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");
            //aclMessage.getEnvelope().setAclRepresentation(FIPANames.ACLCodec.XML); 
    		try {
				aclMessage.setContentObject(cadenaAEnviar);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    									
			this.myAgent.send(aclMessage);
			 			
			ACLMessage msg=this.myAgent.blockingReceive(
					MessageTemplate.and(MessageTemplate.MatchConversationId(String.valueOf(id)),
					MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("ontologia"))));
						
			String temporal = null;
			
			try {
				temporal = (String)msg.getContentObject();
			} catch (UnreadableException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			String[] respuesta= temporal.split("-");
									
			pregunta.navegarNodoRespuesta(respuesta[0]);
		 }//fin del while
		
		String temp=pregunta.obtenerPreguntaNodo();
		String cadenaAEnviar= temp+"-"+id;
			
		ACLMessage aclMessage =new ACLMessage(ACLMessage.PROPOSE);
		
		aclMessage.setConversationId(String.valueOf(id));
		aclMessage.addReceiver(msg.getSender());
		aclMessage.setOntology("ontologia");
		aclMessage.setLanguage(new SLCodec().getName());
		aclMessage.setEnvelope(new Envelope());
		aclMessage.getEnvelope().setPayloadEncoding("ISO8859_1");

		try {
			aclMessage.setContentObject(cadenaAEnviar);
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.myAgent.send(aclMessage);	
	}
}
