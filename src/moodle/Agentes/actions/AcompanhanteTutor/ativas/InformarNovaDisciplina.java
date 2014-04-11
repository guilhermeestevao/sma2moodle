package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import Util.SalvarLog;
import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Agentes.actions.ControleEnvio;
import moodle.Org.MoodleEnv;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import dao.GerenciaCurso;
import dao.JPAUtil;
import jamder.Environment;
import jamder.behavioural.Condition;

public class InformarNovaDisciplina extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7818579743279287872L;
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;
	
	public InformarNovaDisciplina(String name, Condition pre_condition,
			Condition pos_condition,BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction = 2;
		idAgente = id;
	}

	public InformarNovaDisciplina(String name, BigInteger id) {
		super(name);
		idAction = 2;
		idAgente = id;
	}

	@Override
	public void execute(Environment env, Object[] params) {

		if (!ControleActions.isInformaAtividadeDisciplina())
			return;

		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		BigInteger useridfrom = new BigInteger("2");

		boolean podeEnviar = false;

		int dias;
		List<Curso> novosCursos = new ArrayList<Curso>();
		List<Tutor> tutores = new ArrayList<Tutor>();

		//JPAUtil.beginTransaction();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());

		for (Curso curso : cursos) {

			//if(!curso.getAgentesAtivosNoCursos().contains(idAgente))
				//continue;
			
			dias = difDias(curso.getDataCriacao());

			if (dias == 0)
				novosCursos.add(curso);
		}

		if (!novosCursos.isEmpty()) {
			podeEnviar = true;

			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");

			for (Curso c : novosCursos) {

				for (Curso cn : novosCursos) {

					if (c.getCategory() == cn.getCategory()) {

						for (Tutor t : c.getTutores()) {

							try {
								BigInteger useridto = t.getId();

								EntityManager entManager = JPAUtil.getEntityManager();
								
								Query ss = entManager.createNamedQuery("byMensagemCustomizada");
								BigInteger ac = new BigInteger(""+this.getId_action());
								ss.setParameter(1, this.getIdAgente());
								ss.setParameter(2, ac);
								
								MensagemCustomizada mensC = (MensagemCustomizada)ss.getResultList().get(0);
								String smallmessage = mensC.getMensagem();
								smallmessage = smallmessage.replaceAll("<nome tutor>", t.getCompleteName());
								smallmessage = smallmessage.replaceAll("<data criação>", formato.format(c.getDataCriacao()));
								smallmessage = smallmessage.replaceAll("<nome disciplina>", c.getFullName());
									
								
								
							//	String smallmessage = "Prezado(a) "+ t.getLastName() + ". \n";

								//smallmessage += "Em "+ formato.format(c.getDataCriacao())+ " foi criado uma nova disciplina";

							//	smallmessage += " chamada " + c.getFullName()+ ".";

							//	smallmessage += " Você deve ler o conteúdo  que está disponível na página inicial da disciplina do moodle. \n";

								if (podeEnviar) {
									// Timestamp atual = new
									// Timestamp(System.currentTimeMillis());
									// AcompanhanteTutorAgente comp =
									// (AcompanhanteTutorAgente)myAgent;

									// AgenteUtil.addActionAgente(getId_action(),
									// comp.getIdAgente(), t.getId(),
									// c.getId(),atual);

									smallmessage += "\n";

									AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente) myAgent;
									if (verificaMens(c.getId(), t.getId(),smallmessage))
										continue;
									else {
										Timestamp atual = new Timestamp(System.currentTimeMillis());
										AgenteUtil.addActionAgente(getId_action(),comp.getIdAgente(), t.getId(),c.getId(), atual, smallmessage);
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
								JPAUtil.closeEntityManager();
							} catch (NullPointerException e) {
								JPAUtil.closeEntityManager();
								ControleActions
										.setInformaAtividadeDisciplina(false);
							}

						}
					}
				}
			}
		}
		//JPAUtil.closeEntityManager();
		ControleActions.setInformaAtividadeDisciplina(false);
	}

	private int difDias(Date data) {
		long dataEntrada = data.getTime();
		long dataAtual = new Date().getTime();
		long dif = dataEntrada - dataAtual;
		int dias = (int) (dif / 86400000);
		return dias;
	}

	public BigInteger getIdAgente() {
		return idAgente;
	}

	public void setIdAgente(BigInteger idAgente) {
		this.idAgente = idAgente;
	}

	
}
