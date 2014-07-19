package moodle.Agentes.actions;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Properties;

import javassist.expr.NewArray;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.swing.JOptionPane;

import dao.JPAUtil;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.mensagem.Mensagem;
import jamder.Environment;

public class ControleEnvio {

	private static String login;
	private static String senha;
	private static Map<Integer, String> formasDeEnvio;

	static{
		formasDeEnvio = new HashMap<Integer, String>();
		formasDeEnvio.put(new Integer(1), "SMS");
		formasDeEnvio.put(new Integer(2), "E-Mail");
		formasDeEnvio.put(new Integer(3), "Moodle");	
	}

	public static List<Integer> verificaFormasEnvio(int action){
		try{
			Query query = JPAUtil.getEntityManager().createNativeQuery("SELECT forma FROM ag_forma_envio_action WHERE action = ?");
			query.setParameter(1, action);
			List<Integer> formas = query.getResultList();
			
			return formas;
		}catch(NoResultException e){
			System.err.println(e.getMessage());
			return null;
		}finally{
			
		}
	}

	public static void enviar(Mensagem mensagem, Environment env, int action){
		List<Integer> formasCadastradas = verificaFormasEnvio(action);
		if(!formasCadastradas.isEmpty()){
			Set<Integer> idsFormas = formasDeEnvio.keySet();
			for(int forma: idsFormas){
				if(formasCadastradas.contains(forma)){

					if(formasDeEnvio.get(forma).equals("SMS")){
						enviarViaSMS(mensagem);
					}

					if(formasDeEnvio.get(forma).equals("E-Mail")){
						enviarViaEmail(mensagem);
					}

					if(formasDeEnvio.get(forma).equals("Moodle")){
						enviarViaMoodle(mensagem, env);
					}
				}
			}
		}else{
			System.out.println("Não há formas de envio cadastradas para essa action");
		}
	}

	private static void enviarViaMoodle(Mensagem mensagem, Environment env){	
		System.out.println("Enviando via Moodle");
		((MoodleEnv)env).addMensagem(mensagem);
	}

	private static void enviarViaEmail(Mensagem mensagem){
		System.out.println("Enviando via E - mail");

		try{
			String conteudo = mensagem.getSmallmessage();
			String assunto = mensagem.getSubject();

			EntityManager manager = JPAUtil.getEntityManager();
			Query queryAluno = manager.createNamedQuery("Aluno.findById");

			queryAluno.setParameter(1, mensagem.getUseridto());
			Aluno a = (Aluno)queryAluno.getSingleResult();

			String destinatario = a.getEmail();

			destinatario = "guilhermeestevo@gmail.com";
			
			EntityManager entManager = JPAUtil.getEntityManager();		
			String sqlLogin = "Select login from ag_login";
			Query queryLogin = entManager.createNativeQuery(sqlLogin);
			login = (String)queryLogin.getSingleResult();	


			String sqlSenha = "Select senha from ag_login";
			Query querySenha = entManager.createNativeQuery(sqlSenha);
			senha = (String)querySenha.getSingleResult();	

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

		}catch(Exception e){

		}
	}

	private static void enviarViaSMS(Mensagem mensagem){
		System.out.println("Enviando via SMS");
		try{
			// monta estrutura de parametros a serem eviados
			String data = URLEncoder.encode( "nome", "UTF-8" ) + "=" + URLEncoder.encode( "nome", "UTF-8" );
			
			EntityManager manager = JPAUtil.getEntityManager();
			Query queryAluno = manager.createNamedQuery("Aluno.findById");

			queryAluno.setParameter(1, mensagem.getUseridto());
			Aluno a = (Aluno)queryAluno.getSingleResult();

			String destinatario = a.getTelefone();
			
			//teste para quando usuario nao tiver telefone cadastrado
			
			String telefonedeorigem="558899272294";
			String telefonededestino=destinatario ;
			telefonededestino = "558899272294";
			String username="guilhermeestevo"; 
			String password="guilhermeestevo";  
			String mensagemaenviar= mensagem.getSmallmessage();
			
			mensagemaenviar.replaceAll(" ", "+");
			String mensagemlonga = "0";
			String tipo = "normal"; 
		         URL url = new URL( "http://www.lusosms.com/enviar_sms_get.php?username="+username+"&password="+password+"&origem="+telefonedeorigem+"&destino="+telefonededestino+"&mensagem="+mensagemaenviar+"&mensagemlonga="+mensagemlonga+"&tipo="+tipo );
	                 URLConnection urlConnection = url.openConnection();
 
	                 // Envia os dados
	                 urlConnection.setDoOutput(true); 
	                 OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());  //Trata os dados recebidos
	                 osw.write(data);
	                 osw.flush();
 
	                 // Obtem as respostas
	                 InputStreamReader inputReader = new InputStreamReader(urlConnection.getInputStream());
	                 BufferedReader bufferedReader = new BufferedReader( inputReader );
 
			System.out.println( "\n** retorno da página web **" );
			String linha = "";
			while ( (linha = bufferedReader.readLine()) != null){
				System.out.println(linha);
			}
 
		} catch (Exception e) {
			
			System.out.println("Exception: "+ e.getMessage() );
		}
	}

}
