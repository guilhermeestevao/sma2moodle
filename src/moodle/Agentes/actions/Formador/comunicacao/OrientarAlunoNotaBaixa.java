package moodle.Agentes.actions.Formador.comunicacao;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jamder.Environment;
import jamder.agents.GenericAgent;
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
	
	public OrientarAlunoNotaBaixa(String name, Map<Curso, List<Aluno>> als) {
		super(name);
		alunosNotaBaixa = als; 
		
	}
	
	
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		MoodleEnv envir = (MoodleEnv) env;
		
		
			
		
		
			for(Map.Entry<Curso, List<Aluno>> results : alunosNotaBaixa.entrySet()){
				
				for(Aluno al : results.getValue()){
					
					StringBuilder smallmessage = new StringBuilder();
					smallmessage.append("Prezado Aluno, \n\n");
					smallmessage.append("No curso " + results.getKey().getFullName() + ", o seu rendimento está baixo. Procure o seu grupo para apoio ou," +
							"caso ainda não tenha um, busque apoio com seus colegas de curso");
				
					
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
