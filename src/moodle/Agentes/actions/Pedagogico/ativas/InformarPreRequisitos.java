package moodle.Agentes.actions.Pedagogico.ativas;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


import dao.GerenciaCurso;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.PedagogicoAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;
import jamder.behavioural.Condition;



public class InformarPreRequisitos extends ActionMoodle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2271354684724434396L;
	private boolean done = false;
	private boolean mantemAtivo;
	
	public InformarPreRequisitos(String name){
		super(name);
		idAction = 18;
	}
	
	public InformarPreRequisitos(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 18;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		block(24*1000L);
		
		mantemAtivo = ((MoodleEnv)env).getMantemAgentesAtivos();
		
		if(!mantemAtivo)
			return;
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			if(curso.getCursosPreRequisito().isEmpty())
				continue;
			
			
			
			StringBuilder smallmessage = new StringBuilder();
			smallmessage.append("Prezado Aluno, \n\n");
			smallmessage.append("Revise os principais conceitos da(s) disciplina(s) ");
			
			int cont = 0;
			
			for(Curso preReq : curso.getCursosPreRequisito()){
				if(cont > 0)
					smallmessage.append(", ");
				smallmessage.append(preReq.getFullName());
				cont++;
			}
			
			smallmessage.append(" para que seu aprendizado em " + curso.getFullName() +  " seja maximizado");
			
			
			
			for(Aluno al : curso.getAlunos()){
				
				if(verificaControle(curso.getId(), al.getId()))
					continue;
				else{
					
					PedagogicoAgente comp = (PedagogicoAgente)myAgent;
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), curso.getId());
				}
				
				
				BigInteger useridto = al.getId();
				
				StringBuilder fullmessage = new StringBuilder(smallmessage);
				fullmessage.append("\n--------------------------------------------------------------------- \nEste e-mail é a cópia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder.");
				
				Long time = System.currentTimeMillis();
				
				Mensagem msg = new Mensagem();
				msg.setSubject("Nova mensagem do Administrador");
				msg.setUseridfrom(useridfrom);
				msg.setUseridto(useridto);
				msg.setSmallmessage(smallmessage.toString());
				msg.setFullmessage(fullmessage.toString());
				msg.setTimecreated(time);
				
				((MoodleEnv)env).addMensagem(msg);

				
			}
			
			
		}
		
				
	}
	
	@Override
	public boolean done() {
		return done;
	}


}
