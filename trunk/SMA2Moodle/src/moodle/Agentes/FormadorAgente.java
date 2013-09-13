package moodle.Agentes;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jamder.behavioural.*;
import jamder.Environment;
import jamder.roles.AgentRole;
import jamder.structural.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ietf.jgss.MessageProp;

import moodle.Agentes.actions.Formador.ativas.FormarGrupos;
import moodle.Agentes.actions.Formador.comunicacao.OrientarAlunoNotaBaixa;
import moodle.Agentes.actions.Formador.comunicacao.ResponderAgentes;
import moodle.dados.Aluno;

import jamder.agents.*;

public class FormadorAgente extends UtilityAgent {
	
	private int idAgente;
	
	//Constructor 
  public FormadorAgente (String name, Environment env, AgentRole agRole) {
    super(name, env, agRole);
    
    idAgente = 6;
    
    addBelief("crencasFormaGrupos.pl", new Belief("crencasFormaGrupos.pl", "String", ""));
    
    
    Action formarGrupos = new FormarGrupos("formarGrupos");
    addAction("formarGrupos", formarGrupos);
  
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
   
  protected Belief nextFunction(Belief belief, String perception) {
	return funcaoProximoGrupos(belief, perception);
  }
  private Belief funcaoProximoGrupos(Belief belief, String perception) {
	return null;
  }
  protected List<Action> formulateProblemFunction(Belief belief, Goal goal) {
	return probFuncPerceberGrupos(belief, goal);
  }
  private List<Action> probFuncPerceberGrupos(Belief belief, Goal goal) {
	return null;
  }
  protected Goal formulateGoalFunction(Belief belief) {
	return objCriarIntegrarGrupos(belief);
  }
  private Goal objCriarIntegrarGrupos(Belief belief) {
	return null;
  }
  protected Integer utilityFunction(Action action) {
	return utilCriarIntegrarGrupos(action);
  }
  private Integer utilCriarIntegrarGrupos(Action action) {
	return 0;
  }
  public void percept(String perception) { 
	  
	  Action action = getPerceive(perception);
	  
	  if(action != null){
		  addBehaviour(action);
	  }
  }
}
