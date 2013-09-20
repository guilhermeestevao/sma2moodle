package dao;

import moodle.dados.controleag.ActionAgente;

public class ActionAgenteJpaDAO extends GenericJpaDAO<ActionAgente> implements ActionAgenteDAO{
	
	public ActionAgenteJpaDAO() {
		this.persistentClass = ActionAgente.class;
	}
}
