package moodle.Agentes.actions.Pedagogico.ativas;

import jamder.Environment;
import jamder.behavioural.Condition;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;




import java.util.ResourceBundle.Control;

import moodle.Agentes.AgenteUtil;
import moodle.Agentes.PedagogicoAgente;
import moodle.Agentes.actions.ActionMoodle;
import moodle.Agentes.actions.ControleActions;
import moodle.Org.MoodleEnv;
import moodle.dados.Aluno;
import moodle.dados.Atividade;
import moodle.dados.Curso;
import moodle.dados.mensagem.Mensagem;
import dao.GerenciaCurso;

public class InformarAtividadesDisciplina extends ActionMoodle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1226074260407782797L;
	private boolean done = false;
	private boolean mantemAtivo;
	
	public InformarAtividadesDisciplina(String name){
		super(name);
		idAction = 17;
	}
	
	public InformarAtividadesDisciplina(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		idAction = 17;
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
	
		if(!ControleActions.isInformaAtividadeDisciplina())
			return;
		
		
		System.out.println(myAgent.getLocalName()+ " - "+this.getName());
		
		GerenciaCurso manager = ((MoodleEnv)env).getGerenciaCurso();
		
		BigInteger useridfrom = new BigInteger("2");
		
		List<Curso> cursos = new ArrayList<Curso>(manager.getCursos());
		
		for(Curso curso : cursos){
			
			
			String smallmessage = new String();
			smallmessage+="Prezado(a) Aluno, \n\n";
			smallmessage+="As atividades da disciplina " + curso.getFullName() + " s�o: \n";
			
			for(Atividade at : curso.getAllAtividades()){
				smallmessage+="- " + at.getName() + "\n";
			}
			
			for(Aluno al : curso.getAlunos()){

				
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
		
		ControleActions.setInformaAtividadeDisciplina(false);
	}
	
	@Override
	public boolean done() {
		return done;
	}

}
