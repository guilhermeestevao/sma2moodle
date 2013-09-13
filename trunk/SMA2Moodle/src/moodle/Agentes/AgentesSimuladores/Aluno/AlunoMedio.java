package moodle.Agentes.AgentesSimuladores.Aluno;

import moodle.Agentes.AgentesSimuladores.Actions.AlunoMedio.AddNota;
import moodle.Agentes.AgentesSimuladores.Actions.AlunoMedio.AddPost;
import jamder.Environment;
import jamder.roles.AgentRole;

@SuppressWarnings("serial")
public class AlunoMedio extends Aluno {

	public AlunoMedio(String name, Environment environment, AgentRole agentRole) {
		super(name, environment, agentRole);
		
		idAluno = 13;
		
	}
	
	@Override
	public void setup() {
		
		super.setup();
		addBehaviour(new AddPost("addPost", null, null));
		addBehaviour(new AddNota("addNota", null, null));
	}

}
