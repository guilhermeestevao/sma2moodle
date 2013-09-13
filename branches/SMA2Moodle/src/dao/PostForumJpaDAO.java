package dao;

import moodle.Agentes.AgentesSimuladores.Dados.PostForum;

public class PostForumJpaDAO extends GenericJpaDAO<PostForum> implements PostForumDAO {

	public PostForumJpaDAO() {
		this.persistentClass = PostForum.class;
	}
}
