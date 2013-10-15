package moodle.Agentes;

import java.util.List;
import java.util.Map;

import moodle.Agentes.actions.AgenteAuxiliarDeEvasao.ativas.VerificarEvasao;
import moodle.Agentes.actions.Formador.comunicacao.ResponderAgentes;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jamder.Environment;
import jamder.agents.UtilityAgent;
import jamder.behavioural.Action;
import jamder.behavioural.Plan;
import jamder.roles.AgentRole;
import jamder.structural.Belief;
import jamder.structural.Goal;

public class AgenteAuxiliarDeEvasao extends UtilityAgent{

	private int idAgente;
	
	public AgenteAuxiliarDeEvasao(String name, Environment environment, AgentRole agentRole) {
		super(name, environment, agentRole);
		// TODO Auto-generated constructor stub
	
		setIdAgente(9);
	
		Action verificarEvasao = new VerificarEvasao("verificarEvasao ",null,null);
		//addAction("verificarEvasao ", verificarEvasao);
	
	}

	
	protected void setup() {
		super.setup();
		
		Map<String, Action> actions = this.getAllActions();
		
		if(!actions.isEmpty()){
		
			for(Map.Entry<String, Action> results : actions.entrySet()){
				addBehaviour(results.getValue());
			}
		}
		
	}
	
	
	@Override
	protected Integer utilityFunction(Action arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Goal formulateGoalFunction(Belief arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<Action> formulateProblemFunction(Belief arg0, Goal arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Belief nextFunction(Belief arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Plan planning(List<Action> arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	public int getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(int idAgente) {
		this.idAgente = idAgente;
	}
}
