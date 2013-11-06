package moodle.Agentes.AgentesSimuladores.Aluno;

import moodle.Agentes.AgentesSimuladores.Actions.AlunoRuim.AddNota;
import moodle.Agentes.AgentesSimuladores.Actions.AlunoRuim.AddPost;
import jamder.Environment;
import jamder.roles.AgentRole;

@SuppressWarnings("serial")
public class AlunoRuim extends Aluno {

	public AlunoRuim(String name, Environment environment, AgentRole agentRole) {
		super(name, environment, agentRole);
		
		idAluno = 14;
	}
	
	@Override
	public void setup() {
		super.setup();
		
		addBehaviour(new AddPost("addPost", null, null));
		addBehaviour(new AddNota("addNota", null, null));
	}

}
