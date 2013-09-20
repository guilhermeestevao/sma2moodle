package moodle.Agentes.AgentesSimuladores.Aluno;

import moodle.Agentes.AgentesSimuladores.Actions.AlunoBom.AddPost;
import moodle.Agentes.AgentesSimuladores.Actions.AlunoBom.AddNota;
import jamder.Environment;
import jamder.roles.AgentRole;

@SuppressWarnings("serial")
public class AlunoBom extends Aluno {

	public AlunoBom(String name, Environment environment, AgentRole agentRole) {
		super(name, environment, agentRole);
		
		idAluno = 12;
	}

	@Override
	public void setup() {
		// TODO Auto-generated method stub
		super.setup();
		
		addBehaviour(new AddPost("addPost", null, null));
		addBehaviour(new AddNota("addNota", null, null));
	}
	
}
