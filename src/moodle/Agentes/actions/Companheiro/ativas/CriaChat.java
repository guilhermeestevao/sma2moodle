package moodle.Agentes.actions.Companheiro.ativas;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import moodle.Agentes.AgenteUtil;
import moodle.Agentes.CompanheiroAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.Companheiro.comunicacao.InitiatorAcompanhanteTutorAgChatCriado;
import moodle.Org.Dao;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;

import moodle.dados.contexto.Contexto;
import moodle.dados.contexto.Topico;
import moodle.dados.Curso;
import moodle.dados.contexto.ModuloCurso;
import moodle.dados.controleag.ActionAgente;
import moodle.dados.atividades.AtividadeNota;

import moodle.dados.atividades.Chat;

import moodle.dados.atividades.Questionario;
import moodle.dados.mensagem.Mensagem;
import dao.GerenciaCurso;
import dao.JPAUtil;
import jade.core.AID;
import jade.domain.FIPANames;
import jade.lang.acl.ACLMessage;
import jamder.Environment;

import jamder.behavioural.Condition;

public class CriaChat extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5905221591128832559L;
	private boolean done = false;
	private boolean mantemAtivo;

	public CriaChat(String name) {
		super(name);

		idAction = 12;
	}

	public CriaChat(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);

		idAction = 12;
		
	}

	public void execute(Environment env, Object[] params) {

		block(20 * 1000L);

		mantemAtivo = ((MoodleEnv) env).getMantemAgentesAtivos();

		if (!mantemAtivo)
			return;

		boolean podeEnviar = false;

		SimpleDateFormat dateFormat = new SimpleDateFormat("dd:MM:yyyy - hh:mm:ss");

		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();
		Map<Curso, Chat> informcoesDoChat = new HashMap<Curso, Chat>();

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso c : cursos) {

			for (AtividadeNota an : c.getAtividadesNota()) {

				if (an instanceof Questionario) {
					
					Questionario questionario = (Questionario) an;
					
					if (difDias(questionario.getDataFinal()) == 3) {
						podeEnviar = true;
						
						JPAUtil.beginTransaction();
						EntityManager entManager = JPAUtil.getEntityManager();
						
						// MAPEA UM CHAT PARA MDL_CHAT
						Chat chat = new Chat();
						chat.setName("Chat para tirar d�vidas");
						chat.setCourse(c.getId());
						chat.setIntro("Chat criado autom�ticamente para que possam ser tidadas d�vidas antes da proxima Avalia��o");
						long criadoEm = getTimeStamp(new Date());
						chat.setTimemodified(criadoEm);
						// Falta criar ainda a data para o chat
						long dataDoChat = criadoEm+86400;
						chat.setChattime(dataDoChat);
						entManager.persist(chat);
						// MAPEA UM MODULO PARA MDL_COURSE_MODULE
						ModuloCurso mc = new ModuloCurso();
						mc.setCourse(c.getId());
						BigInteger moduluChat = new BigInteger("4");
						mc.setModule(moduluChat);
						mc.setInstance(chat.getId());
						BigInteger section = (BigInteger) entManager.createNativeQuery("SELECT section FROM mdl_course_modules WHERE course="+ c.getId()+ " AND instance="+ questionario.getId()+ " AND module = 16").getSingleResult();
						mc.setSection(section);
						mc.setAdded(criadoEm);
						entManager.persist(mc);
						// MAPEA MDL_CONYEXT
						Contexto contexto = new Contexto();
						BigInteger contextLevel = new BigInteger("70");
						contexto.setContextlevel(contextLevel);
						BigInteger idModulo = mc.getId();
						contexto.setInstanceid(idModulo);
						BigInteger instanceContest = (BigInteger) entManager.createNativeQuery("SELECT id FROM mdl_context WHERE contextlevel = 50 AND instanceid = "+ c.getId()).getSingleResult();
						contexto.setPath("/1/3/" + instanceContest + "/");
						entManager.persist(contexto);
						// ATUALIZANDO ALGUNS VALORES NO BANCO
						BigInteger idContexto = contexto.getId();
						String newPath = contexto.getPath() + contexto.getId();
						
						Contexto contextoTemp = entManager.find(Contexto.class, contexto.getId());
				    	contextoTemp.setPath(newPath);
			
						String sequence = (String) entManager.createNativeQuery("SELECT sequence FROM mdl_course_sections WHERE id="+ section).getSingleResult();
						String newSequence = sequence + "," + idModulo;
						
						Topico top = entManager.find(Topico.class, section);
				    	top.setSequence(newSequence);
						
						c.setSectioncache("NULL");
						
						JPAUtil.closeEntityManager();
						
						for(Aluno aluno : c.getAlunos()){
							
							if(podeEnviar){
							
								if(verificaControle(c.getId(), aluno.getId()))
									continue;
								else{
									
									CompanheiroAgente comp = (CompanheiroAgente)myAgent;
									AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), aluno.getId(), c.getId());
									
									
								}
								
								
								BigInteger useridfrom = new BigInteger("2");
								BigInteger useridto = aluno.getId();
								String smallmessage = "Prezado " + aluno.getCompleteName() + ", \n";
								smallmessage+="com a aproxima��o de uma atividade avaliativa foi marcado um chat na deguinte data e hora :"+dateFormat.format(chat.getChattime())+". \n\n";
								smallmessage+="Aproveite para tirar possiveis d�vidas com o tutor e/ou seus colegas";
								
								
								smallmessage += "\n"; 	
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
								
								((MoodleEnv)env).addMensagem(msg);
								
							}
							
						}
						
						informcoesDoChat.put(c, chat);
					}
				}

			}

		}
		
		try {
			ACLMessage aclMsg = new ACLMessage(ACLMessage.REQUEST);
			aclMsg.setProtocol(FIPANames.InteractionProtocol.FIPA_REQUEST);
			aclMsg.addReceiver(new AID("AcompanhanteTutorAg", AID.ISLOCALNAME));
			aclMsg.setConversationId("UmChatFoiCriado");
			aclMsg.setContentObject((Serializable)informcoesDoChat);
			myAgent.addBehaviour(new InitiatorAcompanhanteTutorAgChatCriado(myAgent, aclMsg));
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}

	private static int difDias(Date data) {
		long dataEntrada = data.getTime();
		long dataAtual = new Date().getTime();
		long dif = dataEntrada - dataAtual;
		int dias = (int) (dif / 86400000);
		return dias;
	}

	private static long getTimeStamp(Date data) {

		long milisec = data.getTime();
		long div = milisec / 1000;
		long rest = milisec % 1000;
		long timestamp = div + rest;
		return timestamp;

	}

	private Date calculaDia() {
		long hoje = new Date().getTime();
		long proximo = hoje + 86400000;
		return new Date(proximo);
	}
}
