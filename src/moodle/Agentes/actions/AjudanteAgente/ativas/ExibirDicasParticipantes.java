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
import moodle.Agentes.actions.ControleEnvio;
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

public class ExibirDicasParticipantes extends ActionMoodle {

	private static final long serialVersionUID = 8530674308196288375L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	public ExibirDicasParticipantes(String name, Condition pre_condition,
			Condition pos_condition, BigInteger idAgente) {
		super(name, pre_condition, pos_condition);
		idAction = 9;
		this.idAgente = idAgente;
	}

	public ExibirDicasParticipantes(String name) {
		super(name);
		idAction = 9;
	}

	private boolean isViewAll(Aluno a) {
		List<Log> logs = a.getLogs();

		String module = "user";
		String action = "view all";

		for (Log log : logs) {
			if (log.getModule().equals(module)
					&& log.getAction().equals(action)) {

				return false;

			}
		}

		return true;
	}

	@Override
	public void execute(Environment env, Object[] params) {
		
		if(!ControleActions.isExibirDicasPArticipantes())
			return;
		
		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");
		
		
		
		//JPAUtil.beginTransaction();
		
		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		

		for (Curso c : cursos) {
			
			if(!c.getAgentesAtivosNoCursos().contains(idAgente))
				continue;

			for (Aluno al : c.getAlunos()) {

				
				podeEnviar = isViewAll(al);
				
				if (podeEnviar) {
				
					EntityManager entManager = JPAUtil.getEntityManager();
					
					Query ss = entManager.createNamedQuery("byMensagemCustomizada");
					BigInteger ac = new BigInteger(""+this.getId_action());
					ss.setParameter(1, this.getIdAgente());
					ss.setParameter(2, ac);
					
					MensagemCustomizada mensC = (MensagemCustomizada) ss.getResultList().get(0);
					String smallmessage = mensC.getMensagem();
					smallmessage = smallmessage.replaceAll("<nome do aluno>", al.getCompleteName());
				
					BigInteger useridto = al.getId();
					
					//String smallmessage = "Olá "+ al.getCompleteName()+ " para conhecer seus colegas e quem é o tutor responsável pelo acompanhamento de sua turma ou o professor da disciplina";
					//smallmessage += ", basta ir no menu de navegação no canto direito da tela do moodle e clicar ";
					//smallmessage += "sobre o nome do curso, depois clique em PARTICIPANTES.";
					//smallmessage += "A partir da visualização de participantes, você poderá entrar em contato com qualquer um deles clicando em ENVIAR MENSAGEM.\n";

					
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
					//((MoodleEnv) env).addMensagem(msg);

					ControleEnvio.enviar(msg, env, idAction);
					
				}
			}
		}
		
		ControleActions.setExibirDicasPArticipantes(false);
	}
	
	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}
	
}
