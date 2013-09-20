package moodle.Agentes.actions.AcompanhanteTutor.comunicacao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class InformarAtividadesEncerrando extends Action {

	private boolean done = false;
	private Map<Aluno,List<Atividade>> atividadesEncerrando;
	
	public InformarAtividadesEncerrando(String name) {
		super(name);
		
	}
	
	public InformarAtividadesEncerrando(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		
	}
	
	public InformarAtividadesEncerrando(String name, Map<Aluno, List<Atividade>> als) {
		super(name);
		atividadesEncerrando = als; 
	}
	
	@Override
	public void execute(Environment env, Object[] params) {

		MoodleEnv envir = (MoodleEnv) env;
		
		List<Curso> cursos = envir.getGerenciaCurso().getCursos();
		Map<Aluno, List<Atividade>> atividades = atividadesEncerrando;
		
		for(Curso curso : cursos){
		
			if(curso.getTutor() == null)
				continue;
			
			
			// Lembrar de add o nome do curso
			//Lembrar de avisar ao guilherme para consertar problema de
			//enviar aluno que nao tem atividades encerrando
			
			StringBuilder smallmessage = new StringBuilder();
			smallmessage.append("Tutor, \n\n");
			smallmessage.append("Abaixo os alunos que ainda não participaram das atividades que estão encerrando na disciplina " + curso.getFullName() +": \n\n");
			
			
			for(Map.Entry<Aluno, List<Atividade>> results : atividades.entrySet()){
				
				if(!curso.getAlunos().contains(results.getKey()))
					continue;
				
				smallmessage.append("\n\nAluno: " + results.getKey().getCompleteName() + "\n");
				smallmessage.append("Atividades:\n\n");
				
				for(Atividade at : results.getValue()){
					
					if((!curso.getAtividadesNota().contains(at)) && (!curso.getAtividadesParticipacao().contains(at)))
						continue;
				
					smallmessage.append(at.getName() + "\n");
					
				}
			
			}
				
				Long time = System.currentTimeMillis();
				
				BigInteger useridfrom = new BigInteger("2");
				BigInteger useridto = new BigInteger(curso.getTutor().getId().toString());
				
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
		
		
		
		
		
		done = true;
	}
	
	@Override
	public boolean done() {
		return done;
	}

	
}
