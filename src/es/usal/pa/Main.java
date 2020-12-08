package es.usal.pa;

import java.util.Scanner;

import jade.lang.acl.ACLMessage;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class Main
{
	public static void main(String args[]) throws Exception
	{
		
		 DataSource source = new DataSource("famosos.csv");
		 Instances dataEntrenamiento = source.getDataSet();
		 
		 
		 //indicar el atributo con la categor�a a clasificar
		 if (dataEntrenamiento.classIndex() == -1)
			 dataEntrenamiento.setClassIndex(0);

		 
		 J48 j48 = new J48();
		 j48.setOptions(new String[] {"-C", "0.25", "-M", "1"});
		 j48.setUnpruned(true);
		 j48.buildClassifier(dataEntrenamiento);
		 
		 //procecidimiento habitual para la clasificaci�n de instancias
		 double clasePredicha=j48.classifyInstance(dataEntrenamiento.lastInstance());
		 System.out.println("Clase "+dataEntrenamiento.classAttribute().value((int)clasePredicha));
		 
		 
		 //para el ejemplo vamos a hacer algo diferente al procedimiento habitual de clasificar
		 Scanner scanner=new Scanner(System.in);
		 Pregunta pregunta=new Pregunta(j48);
		 
		 while(!pregunta.esNodoFinal())
		 { 
			// Pregunta que tiene que hacer el agente quién es quién
			 String temp=pregunta.obtenerPreguntaNodo();
			 // La pregunta la tendrá que enviar al agente cliente
			 System.out.println(temp);
			 
			 // El usuario responde, a través del agente cliente, si la pregunta formulada es verdadera o falsa
			 String respuesta=scanner.nextLine();
			 
			 // La respuesta se tiene que enviar de nuevo al agente quién es quién
			 // El agente quién es quién le pasará la respuesta a "pregunta" (encargada del aprendizaje) 
			 pregunta.navegarNodoRespuesta(respuesta);
		 }
		 
		// La solución se envía al cliente
		 String temp2=pregunta.obtenerPreguntaNodo();
				 System.out.println("Es "+temp2);
				 
				 //ACLMessage.
				 ACLMessage mensaje=null;
				 if(mensaje.getPerformative()==ACLMessage.PROPOSE) {
					 // Me mandan una respuesta/solución	
					 }
	}
	
}
