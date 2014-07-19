package moodle.Agentes.actions.AgenteAuxiliarDeEvasao.ativas;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.ParseException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import dao.GerenciaCurso;
import dao.JPAUtil;
import jamder.Environment;
import jamder.behavioural.Condition;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Org.MoodleEnv;
import moodle.dados.Curso;
import moodle.dados.Aluno;
import moodle.dados.Log;

public class VerificarEvasao extends ActionMoodle{
	
	//private static final long serialVersionUID = -2896538484818688710L; serialVersionUID de acordo com o que esta no banco
	private boolean done = false;
	private boolean mantemAtivo;
	private String login = "";
	private String senha = "";
	public VerificarEvasao(String name) {
		super(name);
		idAction = 21;// atribuir ao idAction o id da action no banco
	}

	public VerificarEvasao(String name, Condition pre_condition,
		Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 21;// atribuir ao idAction o id da action no banco
	}
	
	@Override
	public void execute(Environment env, Object[] params) {	
		
		MoodleEnv envir = (MoodleEnv) env;
		mantemAtivo = envir.getMantemAgentesAtivos();
		block(10 * 1000L);
		if(!mantemAtivo)
			return;
		JPAUtil.beginTransaction(this.getClass());
		EntityManager entManager = JPAUtil.getEntityManager();		
		String sql = "Select login from ag_login";
		Query q = entManager.createNativeQuery(sql);
		login = (String)q.getSingleResult();	
		senha = "#Gesma2@Moodle4&Sma$";
		
		GerenciaCurso manager = ((MoodleEnv) env).getGerenciaCurso();

        for (Curso c : manager.getCursos()) {
        		for(Aluno a : c.getAlunos()){
        			
        			Query query = entManager.createQuery("Select log from Log log where userid=?1 AND module=?2 AND action=?3");
        			query.setParameter(1, a.getId());
        			query.setParameter(2, "user");
        			query.setParameter(3, "login");
        			List<Log> logs = query.getResultList();
        			
        			Log ultimo = logs.get(logs.size()-1);

        			SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        				
        			Date data = new Date();
					try {
						data = sd.parse(sd.format(ultimo.getTime()));
					} catch (java.text.ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Date hoje = new Date();
						
					if(hoje.getMonth() != data.getMonth() && (hoje.getDate() -data.getDate() <= 0) ){
	
								//envia email
								String assunto = "A definir";
                                String mensagem = "Olá aluno "+a.getCompleteName()+", seu ultimo acesso foi em"+sd.format(data) +"....";
                                String destinatario = "douglasjva@hotmail.com";
                                
                                
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
                                	throw new RuntimeException(e);
                                }        

							}
                       //break;
        		}
        		//break;
        }
       	JPAUtil.closeEntityManager(this.getClass());	
	}

	
}