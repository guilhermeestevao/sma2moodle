package moodle.Agentes;

import java.util.Map;

import moodle.Agentes.actions.AcompanhanteDeProfessores.InformarNotasAtrasadas;
import jamder.Environment;
import jamder.agents.ModelAgent;
import jamder.behavioural.Action;
import jamder.roles.AgentRole;
import jamder.structural.Belief;

public class AcompanhanteDeProfessores extends ModelAgent{

	private int idAgente;
	
	public AcompanhanteDeProfessores(String name, Environment environment, AgentRole agentRole) {
		super(name, environment, agentRole);
		
		Action informarNotasAtrasadas = new InformarNotasAtrasadas("informarNotasAtrasadas", null, null);
		addAction("informarNotasAtrasadas", informarNotasAtrasadas);
		
		setIdAgente(1);
		
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

	@Override
	protected Belief nextFunction(Belief arg0, String arg1) {
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
