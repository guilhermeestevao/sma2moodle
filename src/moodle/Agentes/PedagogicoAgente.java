package moodle.Agentes;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jamder.behavioural.*;
import jamder.Environment;
import jamder.roles.AgentRole;
import jamder.structural.*;
import java.util.List;
import java.util.Map;

import moodle.Agentes.actions.Pedagogico.ativas.InformarAtividadesDisciplina;
import moodle.Agentes.actions.Pedagogico.ativas.InformarPreRequisitos;
import moodle.Agentes.actions.Pedagogico.comunicacao.OrientarAlunoNotaBaixaDisciplina;
import moodle.Agentes.actions.Pedagogico.comunicacao.ResponderAgentes;
import jamder.agents.*;

public class PedagogicoAgente extends GoalAgent {	

	private int idAgente;
	
   //Constructor 
   public PedagogicoAgente (String name, Environment env, AgentRole agRole) {
   super(name, env, agRole);
   
   	 idAgente = 7;
   
     addBelief("crencasPedagogico.pl", new Belief("crencasPedagogico.pl", "String", ""));
  
     
     
       
     Action informarPreRequisito = new InformarPreRequisitos("informarPreRequisito", null, null);
     addAction("informarPreRequisito", informarPreRequisito);
     
     Action informarAtividadesDisciplina = new InformarAtividadesDisciplina("informarAtividadesDisciplina", null, null);
     addAction("informarAtividadesDisciplina", informarAtividadesDisciplina);
     
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
		
		
		
		MessageTemplate protocolo = MessageTemplate.MatchProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
		MessageTemplate perfomative = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		
		MessageTemplate filtro = MessageTemplate.and(protocolo, perfomative);
		
		addBehaviour(new ResponderAgentes(this, filtro));
	}
   
   protected Plan planning(List<Action> actions){
      return null;
   }
      
   protected Goal formulateGoalFunction(Belief belief) {
      return goalFuncPedagogico(belief);
   }
   private Goal goalFuncPedagogico(Belief belief) {
      return null;
   }
   protected List<Action> formulateProblemFunction(Belief belief, Goal goal) {
      return probFuncPedagogico(belief, goal);
   }
   private List<Action> probFuncPedagogico(Belief belief, Goal goal) {
      return null;
   }
   protected Belief nextFunction(Belief belief, String perception) {
      return proximoPedagogico(belief, perception);
   }
   private Belief proximoPedagogico(Belief belief, String perception) {
      return null;
   }

   public void percept(String perception) { }

public int getIdAgente() {
	return idAgente;
}

public void setIdAgente(int idAgente) {
	this.idAgente = idAgente;
}
}

