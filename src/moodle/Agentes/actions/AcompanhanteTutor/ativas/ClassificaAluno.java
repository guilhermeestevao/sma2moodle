package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.atividades.AtividadeNota;

public class ClassificaAluno extends ActionMoodle{
	
	

	public ClassificaAluno(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Environment env, Object[] params) {
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso curso : cursos) {
			
			Set<Atividade> atividades = curso.getAllAtividades();
			
			if(!atividades.isEmpty()){
				
				
				Set<Aluno> alunos = curso.getAlunos();
				
				if(!alunos.isEmpty()){
					
					
					for (Aluno aluno : alunos) {
						
						for(Atividade atv : atividades){
							
							Map<Aluno, BigDecimal> notas = atv.getAlunosComNotas();
							
							if(!notas.isEmpty()){
								
								if(notas.containsKey(aluno)){
									/* ALuno com nota na atividade */
									
									BigDecimal nota = atv.getNotaAluno(aluno);
									
									/******** Dá continuidade aqui ******/
									
								}else{
									/* ALuno sem nota na atividade */
									
									
								}
								
							}
							
						}
						
					}
					
					
				}
				
				
			}
			
		}
		
	}
	
	
	
	
	
}
