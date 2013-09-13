package moodle.Agentes.actions.Pedagogico.comunicacao;

import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;

public class OrientarAlunoNotaBaixaDisciplina extends Action {

	private boolean done = false;
	private boolean mantemAtivo;
	private Map<Curso, List<Aluno>> alunosNotaBaixa;
	
	
	public OrientarAlunoNotaBaixaDisciplina(String name) {
		super(name);
		
	}
	
	public OrientarAlunoNotaBaixaDisciplina(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);

	}
	
	public OrientarAlunoNotaBaixaDisciplina(String name, Map<Curso, List<Aluno>> als) {
		super(name);
		alunosNotaBaixa = als; 
		
	}
	
	
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		MoodleEnv envir = (MoodleEnv) env;
				
		
		
		
			for(Map.Entry<Curso, List<Aluno>> results : alunosNotaBaixa.entrySet()){
				
				StringBuilder cursos = new StringBuilder();
				boolean isPreRequisito = false;
				
				
				for(Curso c : envir.getGerenciaCurso().getCursos()){
					
					if(c.getCursosPreRequisito().contains(results.getKey())){
						cursos.append(c.getFullName() + "\n");
						isPreRequisito = true;
					}
				}
				
				
				
				if(!isPreRequisito)
					continue;
				
				
				
				
				for(Aluno al : results.getValue()){
					
					StringBuilder smallmessage = new StringBuilder();
					smallmessage.append("Prezado Aluno, \n\n");
					smallmessage.append("Estude mais a disciplina " + results.getKey().getFullName() + ", pois seu rendimento está baixo. É uma disciplina fundamental, sendo pre-requisito das seguintes disciplinas: \n");
					smallmessage.append(cursos.toString());
					
					
					Long time = System.currentTimeMillis();
					
					BigInteger useridfrom = new BigInteger("2");
					BigInteger useridto = new BigInteger(al.getId().toString());
					
					StringBuilder fullmessage = new StringBuilder(smallmessage);
					fullmessage.append("\n--------------------------------------------------------------------- \nEste e-mail é a cópia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder.");
		
					
					Mensagem msg = new Mensagem();
					msg.setSubject("Nova mensagem do Administrador");
					msg.setUseridfrom(useridfrom);
					msg.setUseridto(useridto);
					msg.setSmallmessage(smallmessage.toString());
					msg.setFullmessage(fullmessage.toString());
					msg.setTimecreated(time);
					
					envir.addMensagem(msg);
		
					
					
					
				}
							
				
			}
	
		
		
		
		done = true;
		 
	}
	
	@Override
	public boolean done() {
		return done;
	}

}
