package moodle.Agentes.actions.Pedagogico.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Util.SalvarLog;
import dao.GerenciaCurso;
import dao.JPAUtil;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.PedagogicoAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import jamder.Environment;
import jamder.behavioural.Condition;



public class InformarPreRequisitos extends ActionMoodle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2271354684724434396L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	public InformarPreRequisitos(String name, BigInteger id){
		super(name);
		idAction = 18;
		idAgente = id;
	}
	
	public InformarPreRequisitos(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 18;
		idAgente = id;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
				
		if(!ControleActions.isInformaPreRequisito())
			return;
		
		System.out.println(myAgent.getLocalName()+ " - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+ " - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		JPAUtil.beginTransaction();
		
		BigInteger useridfrom = new BigInteger("2");
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			if(curso.getCursosPreRequisito().isEmpty())
				continue;
			
			
		
			
			for(Aluno al : curso.getAlunos()){

				
				EntityManager entManager = JPAUtil.getEntityManager(); 
				
				Query ss = entManager.createNamedQuery("byMensagemCustomizada");
				BigInteger ac = new BigInteger(""+this.getId_action());
				ss.setParameter(1, this.getIdAgente());
				ss.setParameter(2, ac);
				
				String smallmessage = retornaMensagem(ss.getResultList(), "introducao");
				smallmessage = smallmessage.replaceAll("<nome do aluno>", al.getCompleteName());
				
				//String smallmessage = new String();
				//smallmessage+="Prezado(a) Aluno(a), \n\n";
				//smallmessage+="Revise os principais conceitos da(s) disciplina(s)";
				
				
				int cont = 0;
				
				for(Curso preReq : curso.getCursosPreRequisito()){
					if(cont > 0)
					smallmessage += retornaMensagem(ss.getResultList(), "requisito");
					smallmessage = smallmessage.replaceAll("<pre-requisito>", preReq.getFullName());
					//smallmessage+=preReq.getFullName();
					cont++;
				}
				
				smallmessage += retornaMensagem(ss.getResultList(), "fim");
				smallmessage = smallmessage.replaceAll("<nome do curso>", curso.getFullName());
				//smallmessage+=" para que seu aprendizado em " + curso.getFullName() +  " seja maximizado";
				
				
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
				
				((MoodleEnv)env).addMensagem(msg);

				
			}
			
			
		}
		
			ControleActions.setInformaPreRequisito(false);
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
