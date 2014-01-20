package moodle.Agentes.actions.AcompanhanteTutor.ativas;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import Util.SalvarLog;
import dao.GerenciaCurso;
import dao.JPAUtil;
import moodle.Agentes.AcompanhanteTutorAgente;
import moodle.Agentes.AgenteUtil;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Coordenador;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import moodle.dados.mensagem.Mensagem;
import moodle.dados.mensagem.MensagemCustomizada;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class NotificaCoordenadorDeTutores extends ActionMoodle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9126986834845305676L;
	private String login = "";
	private String senha = "";
	private boolean done = false;
	private boolean mantemAtivo;
	private BigInteger idAgente;

	public NotificaCoordenadorDeTutores(String name, BigInteger id) {
		super(name);
		idAction =22;
		idAgente = id;
	}

	public NotificaCoordenadorDeTutores(String name, Condition pre_condition, Condition pos_condition, BigInteger id) {
		super(name, pre_condition, pos_condition);
		idAction =22;
		idAgente = id;
	}

	/*
	 * Não precisa de controlador de action
	 * 
	 */
	
	public void execute(Environment env, Object[] params) {
		

		if(!ControleActions.isNotificaCoordenadorDeTutores())
			return;
		

		System.out.println(myAgent.getLocalName()+" - "+this.getName());
		SalvarLog.salvarArquivo(myAgent.getLocalName()+" - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		boolean podeEnviar = false;

		JPAUtil.beginTransaction();
		EntityManager entManager = JPAUtil.getEntityManager();		
		String sql = "Select login from ag_login";
		Query q = entManager.createNativeQuery(sql);
		login = (String)q.getSingleResult();	
		senha = "#Gesma2@Moodle4&Sma$";
		JPAUtil.closeEntityManager();
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso c : cursos) {

			try {

				List<Tutor> tutores = c.getTutores();
				
				for(Tutor t: tutores){
				Coordenador coordenador = t.getCoordenador();
				
				
				if (t.getContAdveretencias() != 0 && t.getContAdveretencias() % 10 == 0)
					podeEnviar = true;
				
							
				if(podeEnviar){
					
					Query ss = entManager.createNamedQuery("byMensagemCustomizada");
					BigInteger ac = new BigInteger(""+this.getId_action());
					ss.setParameter(1, this.getIdAgente());
					ss.setParameter(2, ac);
					
					String assunto = retornaMensagem(ss.getResultList(), "assunto");
					assunto = assunto.replaceAll("<assunto>", "A definir");
					String mensagem = retornaMensagem(ss.getResultList(), "mensagem inteira");
					mensagem = mensagem.replaceAll(" <lista de tutores>", t.getCompleteName());
					
					
					//String assunto = "A definir";
					//String mensagem = " Sr(a) Coordenador(a) de tutores \nFoi detectado que o(s) tutore(s) "+t.getCompleteName()+" não estão desempenhando suas atividades a contento em relação a participação nos fóruns. Favor entrar em contato com este(s) para fazer o acompanhamento e incentivá-los a uma melhor participação. ";
					String destinatario = coordenador.getEmail();
					
					AcompanhanteTutorAgente comp = (AcompanhanteTutorAgente)myAgent;
					if(verificaMens(c.getId(), t.getId(), mensagem))
						continue;
					else{
						Timestamp atual = new Timestamp(System.currentTimeMillis());
						AgenteUtil.addActionAgente(getId_action(), comp.getIdAgente(), t.getId(), c.getId(),atual,mensagem);
					}
					
					
					Properties props = new Properties();
					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.smtp.host", "smtp.gmail.com");
					props.put("mail.smtp.port", "587");
			 
					Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(login, senha);
						}
					  });
			 
					try {
	
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(login));
						message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
						message.setSubject(assunto);
						message.setText(mensagem);
						Transport.send(message);
						
						
					} catch (MessagingException e) {
						ControleActions.setNotificaCoordenadorDeTutores(false);
						throw new RuntimeException(e);
					}
				}
				
			}
			} catch (NullPointerException e) {
				ControleActions.setNotificaCoordenadorDeTutores(false);
			}
			
			
			ControleActions.setNotificaCoordenadorDeTutores(false);

		}

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
}
