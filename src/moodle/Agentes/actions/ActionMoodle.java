package moodle.Agentes.actions;

import java.math.BigInteger;

import javax.persistence.NoResultException;
import javax.persistence.Query;

import dao.JPAUtil;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class ActionMoodle extends Action {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7851712932131646172L;
	protected int idAction;
	
	public ActionMoodle(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public ActionMoodle(String name, Condition pre_condition,
			Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		// TODO Auto-generated constructor stub
	}

	public int getId_action() {
		return idAction;
	}

	public void setId_action(int idAction) {
		this.idAction = idAction;
	}

	
	protected boolean verificaControle(BigInteger idCurso, BigInteger idAluno){
		
		try{
			
			JPAUtil.beginTransaction();
			
			Query q = 	JPAUtil.getEntityManager().createNativeQuery("SELECT id FROM ag_actions_agentes WHERE id_curso = ? AND id_aluno = ? AND id_action = ?");
			q.setParameter(1, idCurso);
			q.setParameter(2, idAluno);
			q.setParameter(3, idAction);
			
			q.getSingleResult();
			
			return true;
			
		}catch(NoResultException e){
			
			return false;
		}finally{
			JPAUtil.closeEntityManager();
		}
		
		
	}

}
