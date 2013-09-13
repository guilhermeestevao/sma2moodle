package moodle.Agentes.AgentesSimuladores.Tutor;

import moodle.Agentes.AgentesSimuladores.Actions.TutorBom.AddPost;
import moodle.Agentes.AgentesSimuladores.Aluno.Aluno;
import jamder.Environment;
import jamder.roles.AgentRole;

@SuppressWarnings("serial")
public class TutorBom extends Aluno {

	public TutorBom(String name, Environment environment, AgentRole agentRole) {
		super(name, environment, agentRole);
		
		idAluno = 15;
			
	}
	
	@Override
	public void setup() {
		
		super.setup();
		addBehaviour(new AddPost("addPost", null, null));
		
	}
	
}
