package moodle.Agentes.actions;

import java.math.BigInteger;
import java.util.List;

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
			
			JPAUtil.beginTransaction(this.getClass());
			
			Query q = 	JPAUtil.getEntityManager().createNativeQuery("SELECT id FROM ag_actions_agentes WHERE id_curso = ? AND id_aluno = ? AND id_action = ?");
			q.setParameter(1, idCurso);
			q.setParameter(2, idAluno);
			q.setParameter(3, idAction);
			
			q.getSingleResult();
			
			return true;
			
		}catch(NoResultException e){
			
			return false;
		}finally{
			JPAUtil.closeEntityManager(this.getClass());
		}
		
		
	}

	protected boolean verificaMens(BigInteger idCurso, BigInteger idAluno, String mens){
		try{
			
			JPAUtil.beginTransaction(this.getClass());
			
			Query q = 	JPAUtil.getEntityManager().createNativeQuery("SELECT mensagem FROM ag_actions_agentes WHERE id_aluno = ? AND id_action = ?");
			q.setParameter(1, idAluno);
			q.setParameter(2, idAction);
			
			List<String> mensagens = q.getResultList();
			
			for(String mensa: mensagens){
				if(mensa.equals(mens)){
					return true;
				}
			}
			
			
		}catch(NoResultException e){
			//retorna false caso não exista mensagem enviada por essa action nesse curso para esse atual aluno
			// retornando falso ele irá enviar a mensagem
			return false;
		}finally{
			JPAUtil.closeEntityManager(this.getClass());
		}
		//retorna false caso exista mensagem enviada por essa action mas não sejá igual a que já foi enviada.
		return false;
	}
	
	
}
