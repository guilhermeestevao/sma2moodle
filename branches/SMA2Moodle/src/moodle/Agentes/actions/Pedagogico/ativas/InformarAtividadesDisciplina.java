package moodle.Agentes.actions.Pedagogico.ativas;

import jamder.Environment;
import jamder.behavioural.Condition;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;




import java.util.ResourceBundle.Control;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Util.SalvarLog;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.PedagogicoAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Agentes.actions.ControleEnvio;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import dao.GerenciaCurso;
import dao.JPAUtil;

public class InformarAtividadesDisciplina extends ActionMoodle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1226074260407782797L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	public InformarAtividadesDisciplina(String name, BigInteger id){
		super(name);
		idAction = 17;
		idAgente = id;
	}
	
	public InformarAtividadesDisciplina(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 17;
		idAgente = id;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
	
		if(!ControleActions.isInformaAtividadeDisciplina())
			return;
		
		
		System.out.println(myAgent.getLocalName()+ " - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+ " - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		/*
		JPAUtil.beginTransaction();
		
		BigInteger useridfrom = new BigInteger("2");
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				continue;
			
			EntityManager entManager = JPAUtil.getEntityManager(); 
			Query ss = entManager.createNamedQuery("byMensagemCustomizada");
			BigInteger ac = new BigInteger(""+this.getId_action());
			ss.setParameter(1, this.getIdAgente());
			ss.setParameter(2, ac);
			
			String smallmessage = retornaMensagem(ss.getResultList(), "introducao");
			smallmessage = smallmessage.replaceAll("<nome do curso>", curso.getFullName());
			
			//String smallmessage = new String();
			//smallmessage+="Prezado(a) Aluno, \n\n";
			//smallmessage+="As atividades da disciplina " + curso.getFullName() + " s�o: \n";
			
			for(Atividade at : curso.getAllAtividades()){
				smallmessage +=retornaMensagem(ss.getResultList(), "atividades");
				smallmessage = smallmessage.replaceAll("<nome da atividade>", at.getName());
			}
			
			for(Aluno al : curso.getAlunos()){

				
				PedagogicoAgente comp = (PedagogicoAgente)myAgent;
				if(verificaMens(curso.getId(), al.getId(), smallmessage))
					continue;
				else{
					Timestamp atual = new Timestamp(System.currentTimeMillis());
					AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), curso.getId(),atual,smallmessage);
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
				
				

				ControleEnvio.enviar(msg, env, idAction);
				
				
				
			}
			
			
		}
		
		ControleActions.setInformaAtividadeDisciplina(false);
		*/
	}
	
	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}
	
	public String retornaMensagem(List<MensagemCustomizada> mensagens, String tipo){
		String ativ="";
		
		for(int i=0;i<mensagens.size();i++){	
			if(mensagens.get(i).getTipo().equals(tipo)){	
				ativ = mensagens.get(i).getMensagem();
			}
		}
		return ativ;
	}
	
	
	@Override
	public boolean done() {
		return done;
	}

}
