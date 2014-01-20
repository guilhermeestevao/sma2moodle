package moodle.Agentes.actions.AjudanteAgente.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Util.SalvarLog;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.AjudanteAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Curso;
import moodle.dados.Log;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import dao.GerenciaCurso;
import dao.JPAUtil;
import jamder.Environment;
import jamder.behavioural.Condition;

//CODIFICA��O

public class ExibirDicasForuns extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 162089551253081886L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;

	public ExibirDicasForuns(String name, Condition pre_condition,
			Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 8;
		idAgente = id;
	}

	public ExibirDicasForuns(String name, BigInteger id) {
		super(name);
		idAction = 8;
		idAgente = id;
	}

	private boolean isViewForum(Aluno a) {

		// Todos os logs do aluno
		List<Log> logsDoCurso = a.getLogs();

		String modulo = "forum";

		for (Log log : logsDoCurso) {
			if (log.getModule().equals(modulo)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void execute(Environment env, Object[] params) {
		
		
		if(!ControleActions.isExibirDicasForum())
			return;

		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");
		
		JPAUtil.beginTransaction();

		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso c : cursos) {

			for (Aluno al : c.getAlunos()) {

				
				podeEnviar = isViewForum(al);
				

				if (podeEnviar) {
					
					EntityManager entManager = JPAUtil.getEntityManager();
					
					Query ss = entManager.createNamedQuery("byMensagemCustomizada");
					BigInteger ac = new BigInteger(""+this.getId_action());
					ss.setParameter(1, this.getIdAgente());
					ss.setParameter(2, ac);
					
					String smallmessage = retornaMensagem(ss.getResultList(),"mensagem inteira");
					smallmessage = smallmessage.replaceAll("<nome do aluno>", al.getCompleteName());
					
										
					BigInteger useridto = al.getId();
					//String smallmessage = "Olá "+ al.getCompleteName()+ ", caso esteja com alguma dúvida em algum dos conteudo, ";
					//smallmessage += "bastar clicar sobre o nome do curso que está matriculado e procurar algum fórum, ";
					//smallmessage += "todos os cursos por padrão possuem uma atividade chamada Fórum de noticias. ";
					//smallmessage += "Você tanto pode criar um tópico ou participar de um já existente. ";
					//smallmessage += "Em um fórum os alunos podem interagir com tutores e professores com o objetivo de compatilhar conhecimentos ";
					//smallmessage += "tirando dúvidas dos conteudos ou postando noticias ligadas ao assunto em discussão.";
					//smallmessage += "Procure participar dos fóruns dos cursos em que está matrculado, pois alguns são avaliativos. \n ";
					
					smallmessage += "\n";
					
					AjudanteAgente comp = (AjudanteAgente)myAgent;
					if(verificaMens(c.getId(), al.getId(), smallmessage))
						continue;
					else{
						Timestamp atual = new Timestamp(System.currentTimeMillis());
						AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), al.getId(), c.getId(),atual,smallmessage);
					}
					String fullmessage = smallmessage;
					fullmessage += "\n--------------------------------------------------------------------- \nEste e-mail � uma copia de uma mensagem que foi enviada para voc� em \"GESMA\". Clique http://127.0.1.1/moodle/message/index.php?user="
							+ useridto
							+ "&id= "
							+ useridfrom
							+ " para responder. ";
					Long time = System.currentTimeMillis();
					Mensagem msg = new Mensagem();
					msg.setSubject("Nova mensagem do Administrador");
					msg.setUseridfrom(useridfrom);
					msg.setUseridto(useridto);
					msg.setSmallmessage(smallmessage);
					msg.setFullmessage(fullmessage);
					msg.setTimecreated(time);

					((MoodleEnv) env).addMensagem(msg);
				}
			}

		}
		ControleActions.setExibirDicasForum(false);

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
	
	public boolean done() {
		return done;
	}

}
