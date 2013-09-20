package moodle.Agentes.actions.Companheiro.ativas;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import dao.GerenciaCurso;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.CompanheiroAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;
import jamder.behavioural.Condition;

public class PesquisarData extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 620707710910838950L;
	private boolean done = false;
	private boolean mantemAtivo;
	
	
	
	public PesquisarData(String name) {
		super(name);
		idAction = 15;
		
	}
	
	public PesquisarData(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 15;
	}
	
	public void execute(Environment env, Object[] params){
		
		block(24 * 1000L);
		
		MoodleEnv envir = (MoodleEnv)env;
		
		mantemAtivo = envir.getMantemAgentesAtivos();
		
		if(!mantemAtivo)
			return;
		
		boolean podeEnviar = false;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		
		GerenciaCurso manager = envir.getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			for(Aluno aluno : curso.getAlunos()){
				
				if(verificaControle(curso.getId(), aluno.getId()))
					continue;
				
					
				
				
				
				podeEnviar = false;
				
				BigInteger useridto = aluno.getId();
				
				String smallmessage = "Prezado " + aluno.getCompleteName() + ", \n";
				smallmessage += "Na disciplina " + curso.getFullName() + " h� algumas atividades pendentes, s�o elas: \n\n";
				
				
				for(AtividadeNota at : curso.getAtividadesNota()){
					
					
					if(!verificarData(at.getDataFinal()))
						continue;
					
					if(at.getAlunosComNotas().containsKey(aluno))
						continue;
					
					
					
					podeEnviar = true;
					dateFormat.applyPattern("dd/MM/yyyy");
					smallmessage += at.getName() + " - Finaliza em: "  + dateFormat.format(at.getDataFinal());
					dateFormat.applyPattern("H:mm");
					smallmessage += " �s " + dateFormat.format(at.getDataFinal()) + "\n\n";
				
					if(envir.getAtividadesEncerrando().containsKey(aluno)){
						envir.getAtividadesEncerrando().get(aluno).add(at);
					}else{
						List<Atividade> ats = new ArrayList<Atividade>();
						ats.add(at);
						envir.addAtividadeEncerrando(aluno, ats );
					}
				
				}
				
				for(AtividadeParticipacao at : curso.getAtividadesParticipacao()){
					
					
					if(!verificarData(at.getDataFinal()))
						continue;
					
					if(at.getAlunosParticipantes().contains(aluno) || at.getAlunosComNotas().containsKey(aluno))
						continue;
					
					podeEnviar = true;
					dateFormat.applyPattern("dd/MM/yyyy");
					smallmessage += at.getName() + " - Finaliza em: " + dateFormat.format(at.getDataFinal());
					dateFormat.applyPattern("H:mm");
					smallmessage += " �s " + dateFormat.format(at.getDataFinal()) + "\n\n";
				
					if(envir.getAtividadesEncerrando().containsKey(aluno)){
						envir.getAtividadesEncerrando().get(aluno).add(at);
					}else{
						List<Atividade> ats = new ArrayList<Atividade>();
						ats.add(at);
						envir.addAtividadeEncerrando(aluno, ats );
					}
					
				}
				
				
				
				if(podeEnviar){
				
					CompanheiroAgente comp = (CompanheiroAgente)myAgent;
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), aluno.getId(), curso.getId());
					
					
					String fullmessage = smallmessage;
					
					fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail é a cópia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder. ";
					
					Long time = System.currentTimeMillis();
					
					Mensagem msg = new Mensagem();
					msg.setSubject("Nova mensagem do Administrador");
					msg.setUseridfrom(useridfrom);
					msg.setUseridto(useridto);
					msg.setSmallmessage(smallmessage);
					msg.setFullmessage(fullmessage);
					msg.setTimecreated(time);
					
					envir.addMensagem(msg);
		


					
				}
				
				
			}
		}
		
	
		//done = true;
		
	}
	
	public boolean done() {
		return done;
	}
	
	public boolean verificarData(Date dataAtividade){
		
		 
		Date dataAtual = new Date();
		
		
		if(dataAtividade.after(dataAtual)){
			
			
			Calendar calDataAtual = Calendar.getInstance();
			calDataAtual.setTime(dataAtual);
			
			Calendar calDataAtividade = Calendar.getInstance();
			calDataAtividade.setTime(dataAtividade);
			
			if(calDataAtividade.get(Calendar.MONTH) == calDataAtual.get(Calendar.MONTH)){
				
				
				if(calDataAtividade.get(Calendar.DAY_OF_MONTH) - calDataAtual.get(Calendar.DAY_OF_MONTH) <= 7 )
					return true;
		
			}
			
			
			
		}
		
		return false;
		
		
	}

}
