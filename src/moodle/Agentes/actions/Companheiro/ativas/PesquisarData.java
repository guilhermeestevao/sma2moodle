package moodle.Agentes.actions.Companheiro.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle.Control;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import Util.SalvarLog;
import dao.GerenciaCurso;
import dao.JPAUtil;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.CompanheiroAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Agentes.actions.ControleEnvio;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.atividades.AtividadeNota;
import moodle.dados.atividades.AtividadeParticipacao;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import jamder.Environment;
import jamder.behavioural.Condition;

public class PesquisarData extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 620707710910838950L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	
	
	public PesquisarData(String name, BigInteger id) {
		super(name);
		idAction = 15;
		idAgente = id;
	}
	
	public PesquisarData(String name, Condition pre_condition, Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 15;
		idAgente = id;
	}
	
	public void execute(Environment env, Object[] params){
		

		
		MoodleEnv envir = (MoodleEnv)env;
		
		if(!ControleActions.isPesquisaData())
			return;
	
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		boolean podeEnviar = false;
		
		SimpleDateFormat dateFormat = new SimpleDateFormat();
		
		GerenciaCurso manager = envir.getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		//JPAUtil.beginTransaction();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				continue;
	
			System.out.println(">"+curso.getFullName());
			
			for(Aluno aluno : curso.getAlunos()){
				System.out.println("Aluno ->"+aluno.getCompleteName());
				
				podeEnviar = false;
				
				BigInteger useridto = aluno.getId();
				
				EntityManager entManager = JPAUtil.getEntityManager();
				
				Query ss = entManager.createNamedQuery("byMensagemCustomizada");
				BigInteger ac = new BigInteger(""+this.getId_action());
				ss.setParameter(1, this.getIdAgente());
				ss.setParameter(2, ac);
				MensagemCustomizada mensC = (MensagemCustomizada) ss.getResultList().get(0);
				String smallmessage = mensC.getMensagem();
				
				smallmessage = smallmessage.replaceAll("<nome do aluno>", aluno.getCompleteName());
				smallmessage = smallmessage.replaceAll("<nome da disciplina>", curso.getFullName());
				
				String atividades_data="";
				for(AtividadeNota at : curso.getAtividadesNota()){
					
					//se falta 3 dias ou menos enviamos a mensagem
					if(!verificarData(at.getDataFinal()))
						continue;
					//se não foi atribuida nota para o aluno nessa atividade então enviamos a mensagem
					if(at.getAlunosComNotas().containsKey(aluno))
						continue;
					
					System.out.println("Atividade ->"+at.getName());
					podeEnviar = true;
					dateFormat.applyPattern("dd/MM/yyyy");
					
					atividades_data+=at.getName()+" - "+dateFormat.format(at.getDataFinal())+" ";
					
					dateFormat.applyPattern("H:mm");
					
					String horas_falta = "";
					if(calculaDias(at.getDataFinal())==0){
						horas_falta = dateFormat.format(at.getDataFinal())+".";
					}
					
					atividades_data+=horas_falta+"\n";
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

					System.out.println("Atividade ->"+at.getName());
					podeEnviar = true;
					dateFormat.applyPattern("dd/MM/yyyy");
					
					atividades_data+=at.getName()+" - "+dateFormat.format(at.getDataFinal())+" ";
					
					dateFormat.applyPattern("H:mm");
					
					String horas_falta = "";
					if(calculaDias(at.getDataFinal())==0){
						horas_falta = dateFormat.format(at.getDataFinal())+".";
					}
					
					atividades_data+=horas_falta+"\n";
					if(envir.getAtividadesEncerrando().containsKey(aluno)){
						envir.getAtividadesEncerrando().get(aluno).add(at);
					}else{
						List<Atividade> ats = new ArrayList<Atividade>();
						ats.add(at);
						envir.addAtividadeEncerrando(aluno, ats );
					}
					
				}
				
				smallmessage = smallmessage.replaceAll("<nome da atividade - data encerramento>", atividades_data);
				
				
				if(podeEnviar){
					//Timestamp atual = new Timestamp(System.currentTimeMillis());
					//CompanheiroAgente comp = (CompanheiroAgente)myAgent;
					//AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), aluno.getId(), curso.getId(),atual);
					
					CompanheiroAgente comp = (CompanheiroAgente)myAgent;
					if(verificaMens(curso.getId(), aluno.getId(), smallmessage))
						continue;
					else{
						Timestamp atual = new Timestamp(System.currentTimeMillis());
						AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), aluno.getId(), curso.getId(),atual,smallmessage);
					}
					
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
					
					//envir.addMensagem(msg);

					ControleEnvio.enviar(msg, env, idAction);
					
				}
				
			}
		}
		
		JPAUtil.closeEntityManager();
		
		ControleActions.setPesquisaData(false);
		
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
				
				
				if(calDataAtividade.get(Calendar.DAY_OF_MONTH) - calDataAtual.get(Calendar.DAY_OF_MONTH) <= 3 && calDataAtividade.get(Calendar.DAY_OF_MONTH) - calDataAtual.get(Calendar.DAY_OF_MONTH)>=0  )
					return true;
		
			}
			
			
		}
		
		return false;	
	}

	public int calculaDias(Date dataAtividade){
		
		Calendar calDataAt = Calendar.getInstance();
		calDataAt.setTime(dataAtividade);
		
		Date dataAtual = new Date();
		Calendar calDataAtual = Calendar.getInstance();
		calDataAtual.setTime(dataAtual);
		return calDataAt.get(Calendar.DAY_OF_MONTH) - calDataAtual.get(Calendar.DAY_OF_MONTH);
		
	}

	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}

	//public String retornaMensagem(List<MensagemCustomizada> mensagens, String tipo){
		//String ativ="";
		
	//	for(int i=0;i<mensagens.size();i++){	
	//		if(mensagens.get(i).getTipo().equals(tipo)){	
				//ativ = mensagens.get(i).getMensagem();
		//	}
	//	}
	//	return ativ;
	//}
}
