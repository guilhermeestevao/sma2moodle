package moodle.Agentes;

import java.math.BigInteger;
import java.sql.Timestamp;

import moodle.dados.controleag.ActionAgente;
import dao.ActionAgenteDAO;
import dao.ActionAgenteJpaDAO;
import jade.core.Agent;

public final class AgenteUtil {

public static void addActionAgente(int idAction, int idAgente, BigInteger idAluno, BigInteger idCurso, Timestamp s, String mens){
		
		ActionAgenteDAO actAg = new ActionAgenteJpaDAO();
		
		try{
		
			actAg.beginTransaction();
			
			ActionAgente ag = new ActionAgente();
			ag.setId_action(idAction);
			ag.setId_agente(idAgente);
			ag.setId_aluno(idAluno);
			ag.setId_curso(idCurso);
			ag.setData_envio(s);
			ag.setMensagem(mens);
			
			actAg.save(ag);
			actAg.commit();
			actAg.close();
		
		}catch(Exception e){
			
			actAg.rollback();
			e.printStackTrace();
			System.out.println("Mensagem: " + e.getMessage());
			System.out.println("Causa: " + e.getCause());
		}
	}
}
