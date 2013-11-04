package moodle.Agentes.AgentesSimuladores;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Dao {
	
	private static EntityManagerFactory entityFactory = Persistence.createEntityManagerFactory("moodlePU");;
	
	public static EntityManager getEntityManager(){
		return entityFactory.createEntityManager();
	}
	
}
