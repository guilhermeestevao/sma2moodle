package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import Util.ClassificaFuzzy;
import dao.GerenciaCurso;
import jamder.Environment;
import jamder.behavioural.Condition;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.atividades.AtividadeNota;

public class ClassificaAluno extends ActionMoodle{
	

	private BigInteger idAgente;
	
	public ClassificaAluno(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		
		idAgente = id;
	}

	@Override
	public void execute(Environment env, Object[] params) {
		
		if(!ControleActions.isClassificaAluno())
			return;

		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso curso : cursos) {
			
			Set<Atividade> atividades = curso.getAllAtividades();
			
			if(!atividades.isEmpty()){
				
				
				Set<Aluno> alunos = curso.getAlunos();
				
				if(!alunos.isEmpty()){
					
					
					for (Aluno aluno : alunos) {
						double nota1 =0;
						double nota2 =0;
						
						for(Atividade atv : atividades){
							
							Map<Aluno, BigDecimal> notas = atv.getAlunosComNotas();
							
							if(!notas.isEmpty()){
								
								if(notas.containsKey(aluno)){
									/* ALuno com nota na atividade */
									
									BigDecimal nota = atv.getNotaAluno(aluno);
									
									System.out.println(atv.getName());
									System.out.println(aluno.getCompleteName());
									System.out.println(nota.doubleValue());
									System.out.println("======================");
									
									if(nota1==0){
										nota1 = nota.doubleValue();
									}else{
										nota2 = nota.doubleValue();
									}
									/******** Dá continuidade aqui ******/
									
								}else{
									/* ALuno sem nota na atividade */
									
									System.out.println(atv.getName());
									System.out.println(aluno.getCompleteName());
									System.out.println(0);
									System.out.println("======================");
									
									
								}
								
							}
							
						}
						
						ClassificaFuzzy.classificar(nota1, nota2);
						
					}
					
					
				}
				
				
			}
			
			
		}
		
		ControleActions.setClassificaAluno(false);;
		
	}
	
	
	
	
	
}
