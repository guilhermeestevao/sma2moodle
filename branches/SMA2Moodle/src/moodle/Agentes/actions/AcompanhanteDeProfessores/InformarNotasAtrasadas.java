package moodle.Agentes.actions.AcompanhanteDeProfessores;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import org.joda.time.DateTime;
import org.joda.time.Days;

import Util.SalvarLog;
import moodle.Agentes.AcompanhanteDeProfessores;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Agentes.actions.ControleEnvio;
import moodle.Org.MoodleEnv;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.Professor;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import dao.GerenciaCurso;
import dao.JPAUtil;
import jamder.Environment;
import jamder.behavioural.Condition;

public class InformarNotasAtrasadas extends ActionMoodle{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8599080239312931795L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	public InformarNotasAtrasadas(String name, BigInteger id) {
		super(name);
		idAction = 19;
		idAgente = id;
	}

	public InformarNotasAtrasadas(String name, Condition pre_condition, Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 19;
		idAgente = id;
	}

	@Override
	public void execute(Environment env, Object[] params) {

		
		if(!ControleActions.isInformarNotasAtrasadas())
			return;
		
		System.out.println("Action chamada");
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		
		
		//JPAUtil.beginTransaction();
		
		boolean podeEnviar = false;

		
		List<Curso> cursos =  manager.getCursos();
		
		for (Curso curso : cursos) {
			
			if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				continue;
				
			System.out.println(curso.getFullName());
			
			Professor professor = curso.getProfessor();
			System.out.println(professor.getCompleteName());
			
			for(Atividade atividade : curso.getAtividadesNota()){
				
				Date data = new Date();
				DateTime hoje = new DateTime(data);
				DateTime fimDaAvaliacao = new DateTime(atividade.getDataFinal());
				
				//Quantidades de dias que se passaram desde o fim da avalia��o
				int diferenca =  Days.daysBetween(fimDaAvaliacao, hoje).getDays();
				
				if(atividade.getAlunosComNotas().isEmpty() && diferenca >= 14)
					podeEnviar = true;
				
				if(podeEnviar){
					
					try{
						
						BigInteger useridto = professor.getId();
						BigInteger useridfrom = new BigInteger("2");
						
						EntityManager entManager = JPAUtil.getEntityManager(); 
						
						Query ss = entManager.createNamedQuery("byMensagemCustomizada");
						BigInteger ac = new BigInteger(""+this.getId_action());
						ss.setParameter(1, this.getIdAgente());
						ss.setParameter(2, ac);
						
						MensagemCustomizada mensC = (MensagemCustomizada) ss.getResultList().get(0);
						String smallmessage = mensC.getMensagem();
						smallmessage = smallmessage.replaceAll("<nome do professor>", professor.getLastName());
						smallmessage = smallmessage.replaceAll("<nome da atividade>", atividade.getName());
						smallmessage = smallmessage.replaceAll("<nome do curso>", curso.getFullName());
						
						
						
						//String smallmessage = "Prezado(a) "+professor.getLastName() +", \n";
						//smallmessage+="Já fazem duas semanas que a atividade "+atividade.getName()+" da disciplina "+curso.getFullName()+" teve seu período de avaliação encerrado, porém as notas dos alunos não foram postadas.\n";
						//smallmessage+="Favor postar as notas dos alunos o quanto antes. \n";
						//smallmessage +="\n";
						
						AcompanhanteDeProfessores comp = (AcompanhanteDeProfessores)myAgent;
						if(verificaMens(curso.getId(), useridto, smallmessage))
							continue;
						else{
							Timestamp atual = new Timestamp(System.currentTimeMillis());
						//	AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), useridto, curso.getId(),atual,smallmessage);
						}
						String fullmessage = smallmessage;
						fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail � uma copia de uma mensagem que foi enviada para você em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user=" + useridto + "&id= " + useridfrom +" para responder. ";
						
						Long time = System.currentTimeMillis();						
						
						Mensagem msg = new Mensagem();
						msg.setSubject("Nova mensagem do Administrador");
						msg.setUseridfrom(useridfrom);
						msg.setUseridto(useridto);
						msg.setSmallmessage(smallmessage);
						msg.setFullmessage(fullmessage);
						msg.setTimecreated(time);
						
						//((MoodleEnv)env).addMensagem(msg);
						
						ControleEnvio.enviar(msg, env, idAction);
						
						
					}catch(NullPointerException e){
						ControleActions.setInformarNotasAtrasadas(false);
					}
					
				}
			}
			
			
		}
		
		ControleActions.setInformarNotasAtrasadas(false);
	}
	
	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}
	
	
}
