package dao;

import moodle.Agentes.AgentesSimuladores.Dados.DiscussaoForum;

public class DiscussaoForumJpaDAO extends GenericJpaDAO<DiscussaoForum> implements DiscussaoForumDAO{
	
	public DiscussaoForumJpaDAO() {
		this.persistentClass = DiscussaoForum.class;
	}

}
