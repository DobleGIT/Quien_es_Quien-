package es.usal.pa;

import weka.classifiers.trees.J48;


//ayuda para entender esta clase
//https://waikato.github.io/weka-wiki/use_weka_in_your_java_code/
public class Pregunta 
{
	protected J48 j48;
	protected String nodoActual="N0";
	
	/**
	 * Guarda cada una de las líneas del árbol que contiene la información de los nodos
	 */
	protected String lineas[];
	
	Pregunta(J48 j48) throws Exception
	{
		this.j48=j48;
		
		String arbol = j48.graph();
		System.out.println(arbol);
		lineas=arbol.split("\n");

	}
	
	/**
	 * devuelve true si es un nodo final, es decir, no hay nodos del estio NX->
	 * @return
	 */
	protected boolean esNodoFinal()
	{

		for(int i=0;i<lineas.length;i++)
			if(lineas[i].startsWith(nodoActual+"->"))
				return false;
		return true;
	}
	

	/**
	 * Devuelve la pregunta del nodo nodoActual en el que se encuentra el usuario
	 * @return
	 */
	public String obtenerPreguntaNodo()
	{
		int lineaPregunta=obtenerLineaNodo();
		String valorLabel=obtenerValorLabel(lineas[lineaPregunta]);
		//System.out.println("Variable "+valorLabel);
		
		return valorLabel;
	}
	
	/**
	 * Devuelve la índide de la línea que contine el nodo nodoActual. Ejemplo N0 [label="mujer" ]
	 * @param lineas
	 * @return
	 */
	protected int obtenerLineaNodo() 
	{
		int linea;
		
		for(int i=0;i<lineas.length;i++)
		{
			if(lineas[i].startsWith(nodoActual+" ["))
				return i;
		}
		
		return -1;
	}
	
	/**
	 * devuelve el contenido almacenado en label en la línea pasada como argumento
	 * @param linea
	 * @return
	 */
	protected String obtenerValorLabel(String linea)
	{
		String variable=null;
		
		int inicio=linea.indexOf("\"");
		int fin=linea.indexOf("\"", inicio+1);
		inicio++;
		
		variable=linea.substring(inicio, fin);
		
		return variable;
	}
	
	
	
	/**
	 * Navega desde nodoActual al nodo correspondiente en función de la respuesta del usuario.
	 * No se hace ningún tipo de valiación aunque sólo se permite contesar s o n por los datos del csv 
	 * pero no se restringe por si se hiciera otro ejemplo.
	 * Ejemplo de nodo navegación N0->N1 [label="= n"]
	 * @param respuesta 
	 */
	public void navegarNodoRespuesta(String respuesta)
	{
		int linea;
		String valorLabel=null;
		String nuevoNodo=null;
		
		for(int i=0;i<lineas.length;i++)
		{
			if(lineas[i].startsWith(nodoActual+"->"))
			{
				valorLabel=obtenerValorLabel(lineas[i]);
				if(valorLabel.equals("= "+respuesta))
				{
					nuevoNodo=navegarNuevoNodo(lineas[i]);
				}
					
			}
		}
		nodoActual=nuevoNodo;
		
	}
	
	protected String navegarNuevoNodo(String linea)
	{
		String nuevoNodo=null;
		
		int inicio=linea.indexOf("->");
		int fin=linea.indexOf(" ", inicio+1);
		inicio++;
		
		nuevoNodo=linea.substring(inicio+1, fin);
		
		return nuevoNodo;
	}
}
