package dao;

import moodle.dados.Curso;

public class CursoJpaDAO extends GenericJpaDAO<Curso> implements CursoDAO {

	public CursoJpaDAO() {
		this.persistentClass = Curso.class;
	}
}
