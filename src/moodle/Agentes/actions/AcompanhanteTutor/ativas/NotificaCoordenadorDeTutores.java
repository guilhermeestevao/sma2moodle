package moodle.Agentes.actions.AcompanhanteTutor.ativas;

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

import dao.GerenciaCurso;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Coordenador;
import moodle.dados.Curso;
import moodle.dados.Tutor;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class NotificaCoordenadorDeTutores extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9126986834845305676L;
	private	final String username = "gesma2moodle@gmail.com";
	private final String password = "#Gesma2@Moodle4&Sma$";
	private boolean done = false;
	private boolean mantemAtivo;

	public NotificaCoordenadorDeTutores(String name) {
		super(name);
	}

	public NotificaCoordenadorDeTutores(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
	}

	/*
	 * Não precisa de controlador de action
	 * 
	 */
	
	public void execute(Environment env, Object[] params) {
		

		if(!ControleActions.isNotificaCoordenadorDeTutores())
			return;
		
		System.out.println(myAgent.getLocalName()+" -- "+this.getClass());
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

		boolean podeEnviar = false;

		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for (Curso c : cursos) {

			try {

				List<Tutor> tutores = c.getTutores();
				
				for(Tutor t: tutores){
				Coordenador coordenador = t.getCoordenador();
				
				
				if (t.getContAdveretencias() != 0 && t.getContAdveretencias() % 10 == 0)
					podeEnviar = true;
							
				if(podeEnviar){
					
					String assunto = "A definir";
					String mensagem = " Sr(a) Coordenador(a) de tutores \nFoi detectado que o(s) tutore(s) "+t.getCompleteName()+" não estão desempenhando suas atividades a contento em relação a participação nos fóruns. Favor entrar em contato com este(s) para fazer o acompanhamento e incentivá-los a uma melhor participação. ";
					String destinatario = coordenador.getEmail();
				
					Properties props = new Properties();
					props.put("mail.smtp.auth", "true");
					props.put("mail.smtp.starttls.enable", "true");
					props.put("mail.smtp.host", "smtp.gmail.com");
					props.put("mail.smtp.port", "587");
			 
					Session session = Session.getInstance(props,
					  new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					  });
			 
					try {
	
						Message message = new MimeMessage(session);
						message.setFrom(new InternetAddress(username));
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

}
