package moodle.Agentes.AgentesSimuladores.Actions;

import java.util.List;
import java.util.Random;

import javax.persistence.PersistenceException;

import dao.PostForumDAO;
import dao.PostForumJpaDAO;

import moodle.Agentes.AgentesSimuladores.Aluno.Aluno;
import moodle.Agentes.AgentesSimuladores.Dados.DiscussaoForum;
import moodle.Agentes.AgentesSimuladores.Dados.PostForum;

import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class AddPost extends Action {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5938907338631754977L;
	private boolean done;
	
	public AddPost(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Environment env, Object[] params) {
		super.execute(env, params);
		
		
		Aluno aluno = (Aluno) myAgent;
		try{
			List<DiscussaoForum> discussoes = aluno.recuperaDiscussoesForum();
			
			Random rand = new Random();
			
			int tam = discussoes.size();
			
			DiscussaoForum disRand = discussoes.get(rand.nextInt(tam));
			
			
			PostForum post = new PostForum();
			post.setDiscussion(disRand.getId());
			post.setSubject(new String("Re: " + disRand.getName()));
			post.setMessage("<p>Nova mensagem sendo inserida pelo agente</p>");
			post.setParent(disRand.getFirstPost());
			post.setUserid(aluno.getId());
			
			
			PostForumDAO postDao = new PostForumJpaDAO();
			postDao.beginTransaction();
			postDao.save(post);
			postDao.close();

			
		}catch(PersistenceException e){
			
		}
		
		
		
	}
	
	public boolean done() {
		return done;
	}
	
}
