package moodle.Agentes.actions.AcompanhanteTutor.comunicacao;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class OrientarAlunoNotaBaixa extends Action {
	
	private boolean done = false;
	private boolean mantemAtivo;
	private Map<Curso, List<Aluno>> alunosNotaBaixa;
	
	public OrientarAlunoNotaBaixa(String name) {
		super(name);
		
	}
	
	public OrientarAlunoNotaBaixa(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);

	}
	
	public OrientarAlunoNotaBaixa(String name, Map<Curso, List<Aluno>> aluno){
		super(name);
		alunosNotaBaixa = aluno;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		MoodleEnv envir = (MoodleEnv) env;

		
		/*
		 * Guilherme, está mandando mensagem para o tutor mesmo nao havendo aluno para informar.
		 * 
		 */
		
		Long time = System.currentTimeMillis();
		
		BigInteger useridfrom = new BigInteger("2");
		
		
			for(Map.Entry<Curso, List<Aluno>> results : alunosNotaBaixa.entrySet()){
				
				if(results.getKey().getTutor() == null)
					continue;
	
				
				
				StringBuilder smallmessage = new StringBuilder();
				smallmessage.append("Tutor, os seguintes alunos, em seu respectivo curso, estão com notas baixas: \n\n");
				
				smallmessage.append(results.getKey().getFullName() + ":\n\n");
				
				BigInteger useridto = new BigInteger(results.getKey().getTutor().getId().toString());			
				
				for(Aluno al : results.getValue()){
					smallmessage.append(al.getCompleteName() + "\n");
				}
				smallmessage.append("Fa�a um acompanhamento mais pr�ximo sugerindo leituras e cobrando mais esfor�o.");
				
				
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
