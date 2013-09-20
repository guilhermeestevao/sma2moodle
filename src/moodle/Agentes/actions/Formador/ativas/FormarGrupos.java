package moodle.Agentes.actions.Formador.ativas;

import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import moodle.Agentes.FormadorAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.grupos.Grupo;
import dao.GerenciaCurso;

public class FormarGrupos extends ActionMoodle {
	
	private boolean done = false;
	private boolean mantemAtivo;
	
	
	public FormarGrupos(String name){
		super(name);
		idAction = 16;
	}
	
	public FormarGrupos(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 16;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		block(50 * 1000L);
		
		MoodleEnv environment = (MoodleEnv)env;
		
		mantemAtivo = environment.getMantemAgentesAtivos();
		
		if(!mantemAtivo)
			return;
		
		
		
		GerenciaCurso manager = environment.getGerenciaCurso();
		
		FormadorAgente agent = (FormadorAgente)myAgent;
		
		
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
				
			Map<Aluno, BigDecimal> alunosMap = new TreeMap<Aluno, BigDecimal>(new NotaComparator(curso.getNotaGeralAlunos()));
			System.out.println("Curso");
			Set<Grupo> grupos = (Set<Grupo>) curso.getGrupos();
			
			for(Aluno al : curso.getNotaGeralAlunos().keySet()){
				System.out.println(al.getCompleteName());
				alunosMap.put(al, curso.getNotaGeralAlunos().get(al));
			}
			
			for(Grupo g : grupos){
				
				for(Aluno a : g.getMembros()){
					
					try{
						alunosMap.remove(a);
					}catch(NullPointerException e){
						continue;
					}
					
					
				}
			}
					
			
			
			if(alunosMap.keySet().isEmpty())
				continue;
			
			
			
			
			ArrayList<Aluno> alunos = new ArrayList<Aluno>(alunosMap.keySet());
		
			
			
			while(!alunos.isEmpty()){
			
				Grupo grupo = new Grupo();
				
				if(alunos.size() < 4 ){
					
					for(Aluno al : alunos){
						grupo.addMembro(al);
					}	
					
					alunos.clear();
					
					
				}else{

					Aluno al = alunos.remove(0);
					String cidade = al.getCity().toLowerCase();
					
					grupo.addMembro(al);
					
					int cont = 0;
					
					int pos[] = new int[alunos.size()];
					
					for(Aluno a : alunos){
						
						if(a.getCity().toLowerCase().equals(cidade)){
							pos[cont] = alunos.indexOf(a);
							cont++;
							
						}
					}
					
					int total = 0;
					int alunoFinal = 0;
					int alunoMedio = 0;
					
					if(cont > 2){
						
						total = cont;
						alunoFinal = pos[total-1];
						alunoMedio = pos[total/2 - 1];
					
						
					}else{
					
						total = alunos.size();
						alunoFinal = total-1;
						alunoMedio = total/2 - 1; //se par ou impar, fará a diferença, mas continuara pegando a ideia da nota no meio
						
						
						
					}
					
					
					grupo.addMembro(alunos.remove(alunoFinal));
					grupo.addMembro(alunos.remove(alunoFinal-1));
					grupo.addMembro(alunos.remove(alunoMedio));

					
				}
				
				grupo.setCourseid(curso.getId());
				grupo.setName("Grupo Automatico");
				grupo.setDescription("Grupo formado automaticamente");
				grupo.setDescriptionformat(1);
				grupo.setTimecreated(System.currentTimeMillis());
				
				environment.getGrupos().add(grupo);
				
				System.out.println("GRUPO ADICIONADO");
				System.out.println("********************");
			
			}
			
			
			
			
			
			
		}
		
		
		
	}
	
	@Override
	public boolean done() {
		return done;
	}

}
