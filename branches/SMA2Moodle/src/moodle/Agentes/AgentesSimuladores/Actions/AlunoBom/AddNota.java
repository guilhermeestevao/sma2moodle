package moodle.Agentes.AgentesSimuladores.Actions.AlunoBom;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


import javax.persistence.PersistenceException;
import javax.persistence.RollbackException;

import javax.persistence.EntityManager;

import javax.persistence.NoResultException;

import javax.persistence.Query;



import dao.JPAUtil;

import moodle.Agentes.AgentesSimuladores.Aluno.Aluno;
import moodle.Agentes.AgentesSimuladores.Dados.Nota;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

@SuppressWarnings("serial")
public class AddNota extends Action {
	private boolean done;

	public AddNota(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
	}
	
	

	@Override
	public void execute(Environment env, Object[] params) {
	
        block(10 * 1000L);
        
        JPAUtil.beginTransaction();
        EntityManager manager = JPAUtil.getEntityManager();
        double media = 0;
        BigInteger userId  = null;
        long itemIdCursoSelecionado = 0;
        
        try{
                
                
                
                        
                Aluno ag = (Aluno) myAgent;
                
                Random rand = new Random();
                
                Map<BigInteger, BigInteger> ns = ag.getItemIdCurso(); 
                
                List<BigInteger> idsCurso = new ArrayList<BigInteger>(ns.keySet());
                
                int qntIds = idsCurso.size();
                
                BigInteger idCursoSelecionado = idsCurso.get(rand.nextInt(qntIds));
                
                List<BigInteger> idsAtividadesCurso = ag.recuperaItemIdsAtividades(idCursoSelecionado);
                
                
                double notaAluno = 70 + rand.nextInt(31);
                
                System.out.println("Id curso: " + idCursoSelecionado);
                for(BigInteger id : idsAtividadesCurso){
                        System.out.println(id.toString() + " ");
                }
                
                System.out.println("Tamanho: " + idsAtividadesCurso.size());
                
                int tam = idsAtividadesCurso.size();
                        
        
                Nota nota = new Nota();
                long idAtividade = idsAtividadesCurso.get(rand.nextInt(tam)).longValue();
                nota.setItemid(idAtividade);
                nota.setRawgrade(notaAluno);
                nota.setUserid(ag.getId());
                nota.setUsermodified(ag.getId());
                nota.setFinalgrade(notaAluno);
        
                manager.persist(nota);
                
                JPAUtil.commit();
                
                
                //Se o commit acima lan�ar rollback, entao nao foi adicionado nova nota, assim nao preciso
                // recalcular a media
                
                JPAUtil.beginTransaction();
                
                userId = new BigInteger(new Long(ag.getId()).toString());
                
                media = ag.calculaMedia(idCursoSelecionado, userId);
                
                System.out.println("**** MEDIA: " + media);
        
                itemIdCursoSelecionado = ns.get(idCursoSelecionado).longValue();
                
                System.out.println("entrando aqui " + ns.get(idCursoSelecionado));
                Query q = manager.createNativeQuery("SELECT n FROM Nota n WHERE itemid = ?1");
                q.setParameter(1, itemIdCursoSelecionado);
                          
                Nota mediaRec = (Nota) q.getSingleResult();
                
                System.out.println("*** JA TEM MEDIA ****");
                System.out.println("** VALOR: " + mediaRec.getFinalgrade());
                        
                mediaRec.setFinalgrade(media);
                
                JPAUtil.commit();
              
        }catch(NoResultException e){
                        
                System.out.println("*** NAO TEM MEDIA ****");
                
                Nota mediaAtualizada = new Nota();
                mediaAtualizada.setFinalgrade(media);
                mediaAtualizada.setUserid(userId.longValue());
                mediaAtualizada.setItemid(itemIdCursoSelecionado);
                System.out.println("*** VAI PERSISTIR A NOTA****");
                
                manager.persist(mediaAtualizada);
             
                JPAUtil.commit();
              
            
         }catch(Exception e){
                        
                System.out.println("**** ROLLBACK *****");
                e.printStackTrace();
                System.out.println("Mensagem: " + e.getMessage());
                System.out.println("Mensagem: " + e.getCause());
                
                JPAUtil.rollback();
        
        }finally{
            JPAUtil.closeEntityManager();
                
        }
	  
	}

	@Override
	public boolean done() {

		return done;
	}

}
