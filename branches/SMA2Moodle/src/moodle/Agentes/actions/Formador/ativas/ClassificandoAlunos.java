package moodle.Agentes.actions.Formador.ativas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.GerenciaCurso;
import jamder.Environment;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.Log;
import moodle.dados.atividades.Forum;

public class ClassificandoAlunos  extends ActionMoodle{

	public ClassificandoAlunos(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Environment env, Object[] params) {
		if(!ControleActions.isClassificandoAlunos())
			return;
		System.out.println(myAgent.getLocalName()+" - "+this.getName());

		
		ControleActions.setClassificandoAlunos(false);
	}
	
}
