package moodle.Agentes.actions;

import java.math.BigInteger;
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
import dao.JPAUtil;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;

public class ControleEnvio {
	
	private static String login;
	private static String senha;
	
	public static void enviarViaMoodle(Mensagem mensagem, Environment env){	
		((MoodleEnv)env).addMensagem(mensagem);
	}
	
	public static void enviarViaEmail(Mensagem mensagem){
		String conteudo = mensagem.getSmallmessage();
		String assunto = mensagem.getSubject();
		
		EntityManager manager = JPAUtil.getEntityManager();
		Query queryAluno = manager.createNamedQuery("Aluno.findById");
		
		queryAluno.setParameter(1, mensagem.getUseridto());
		Aluno a = (Aluno)queryAluno.getSingleResult();
		
		String destinatario = a.getEmail();
		
		JPAUtil.beginTransaction();
		EntityManager entManager = JPAUtil.getEntityManager();		
		String sqlLogin = "Select login from ag_login";
		Query queryLogin = entManager.createNativeQuery(sqlLogin);
		login = (String)queryLogin.getSingleResult();	
		
		
		String sqlSenha = "Select senha from ag_login";
		Query querySenha = entManager.createNativeQuery(sqlSenha);
		senha = (String)querySenha.getSingleResult();	
		
		JPAUtil.closeEntityManager();
		
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
			message.setText(conteudo);
			Transport.send(message);
			
			
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void enviarViaSMS(Mensagem mensagem){
		
	}

}
