package moodle.Agentes.AgentesSimuladores.Actions.AlunoMedio;

import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

@SuppressWarnings("serial")
public class AddPost extends moodle.Agentes.AgentesSimuladores.Actions.AddPost {
	
	private boolean done;
	
	public AddPost(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Environment env, Object[] params) {
		super.execute(env, params);
		
		block(45 * 1000L);
		
	}
	
	public boolean done() {
		return done;
	}
	
}
