package moodle.Agentes;
import jade.core.AID;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jamder.behavioural.*;
import jamder.Environment;
import jamder.roles.AgentRole;
import jamder.structural.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import moodle.Agentes.actions.Companheiro.ativas.CriaChat;
import moodle.Agentes.actions.Companheiro.ativas.InformarAndamento;
import moodle.Agentes.actions.Companheiro.ativas.MostraNovaDisciplina;
import moodle.Agentes.actions.Companheiro.ativas.PesquisarData;
import moodle.Agentes.actions.Companheiro.comunicacao.ComunicarAcompanhanteTutorAgAtividadesEncerrando;
import moodle.Agentes.actions.Companheiro.comunicacao.ComunicarAcompanhanteTutorAgNotasBaixas;
import moodle.Agentes.actions.Companheiro.comunicacao.ComunicarFormadorAgNotasBaixas;
import moodle.Agentes.actions.Companheiro.comunicacao.ComunicarPedagogicoAgNotasBaixas;
import moodle.dados.Aluno;
import jamder.agents.*;

public class CompanheiroAgente extends ModelAgent {
	
	private int idAgente;
	
   //Constructor 
   public CompanheiroAgente (String name, Environment env, AgentRole agRole) {
     super(name, env, agRole);
     addBelief("crencasAprendizagem.pl", new Belief("crencasAprendizagem.pl", "String", ""));
   
     idAgente = 4;
     
     /*
      * Actions acionadadas pelo ControladorActions
      */
      
     Action comunicarFormadorAgNotasBaixas = new ComunicarFormadorAgNotasBaixas("comunicarFormadorAgNotasBaixas", null, null);
     addAction("comunicarFormadorAgNotasBaixas", comunicarFormadorAgNotasBaixas);
     
     
     Action comunicarAcompanhanteTutorAgNotasBaixas = new ComunicarAcompanhanteTutorAgNotasBaixas("comunicarAcompanhanteTutorAgNotasBaixas", null, null);
     addAction("comunicarAcompanhanteTutorAgNotasBaixas",comunicarAcompanhanteTutorAgNotasBaixas);
     
     
     Action comunicarPedagogicoAgNotasBaixas = new ComunicarPedagogicoAgNotasBaixas("comunicarPedagogicoAgNotasBaixas", null, null);
     addAction("comunicarPedagogicoAgNotasBaixas",comunicarPedagogicoAgNotasBaixas);
     
     
     
     Action comunicarAcompanhanteTutorAgAtividadesEncerrando = new ComunicarAcompanhanteTutorAgAtividadesEncerrando("ComunicarAcompanhanteTutorAgAtividadesEncerrando", null, null);
     addAction("ComunicarAcompanhanteTutorAgAtividadesEncerrando", comunicarAcompanhanteTutorAgAtividadesEncerrando);
     
     
     /*
      * *********************************
      */
     
     
     Action informarAndamento = new InformarAndamento("informarAndamento", null, null);
     addAction("informarAndamento", informarAndamento);
     
     Action pesquisarDatas = new PesquisarData("pesquisarDatas", null, null);
     addAction("pesquisarDatas", pesquisarDatas);
     
     Action criaChat = new CriaChat("criaChat", null, null);
     addAction("criaChat", criaChat);
     
     Action mostraNovaDisciplina = new MostraNovaDisciplina("mostraNovaDisciplina", null, null);
     addAction("mostraNovaDisciplina", mostraNovaDisciplina);
     
     
     
     
     
     
   }
   
   
   @Override
	protected void setup() {
		super.setup();
		
		  Map<String, Action> actions = this.getAllActions();
			 
			
			if(!actions.isEmpty()){
			
				for(Map.Entry<String, Action> results : actions.entrySet()){
					addBehaviour(results.getValue());
				}
			}
		
	}
   
   
   protected Belief nextFunction(Belief belief, String perception) {
	return funcaoProximoAprendizagem(belief, perception);
   }
   private Belief funcaoProximoAprendizagem(Belief belief, String perception) {
	return null;
   }
   
   public void addBehaviour(String nomeAction){
	   Action action = getAction(nomeAction);
	   try{
		   addBehaviour(action);
	   }catch(NullPointerException e){
		   System.out.println("ACTION É NULL - COMPANHEIROAGENTE");
	   }
	   
   }


	public int getIdAgente() {
		return idAgente;
	}
	
	
	public void setIdAgente(int idAgente) {
		this.idAgente = idAgente;
	}
  
}
