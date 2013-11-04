package moodle.Agentes.AgentesSimuladores.Actions.AlunoRuim;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import moodle.Agentes.AgentesSimuladores.Dao;
import moodle.Agentes.AgentesSimuladores.Aluno.Aluno;
import moodle.Agentes.AgentesSimuladores.Dados.Nota;
import jamder.Environment;
import jamder.behavioural.Action;
import jamder.behavioural.Condition;

public class AddNota extends Action {
	private boolean done;
	
	public AddNota(String name, Condition pre_condition, Condition pos_condition) {
		super(name, pre_condition, pos_condition);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void execute(Environment env, Object[] params) {
		
		block(35 * 1000L);
        
        EntityManager manager = Dao.getEntityManager();
        double media = 0;
        BigInteger userId  = null;
        long itemIdCursoSelecionado = 0;
        
        try{
                
                
                manager.getTransaction().begin();
                        
                Aluno ag = (Aluno) myAgent;
                
                Random rand = new Random();
                
                Map<BigInteger, BigInteger> ns = ag.getItemIdCurso(); 
                
                List<BigInteger> idsCurso = new ArrayList<BigInteger>(ns.keySet());
                
                int qntIds = idsCurso.size();
                
                BigInteger idCursoSelecionado = idsCurso.get(rand.nextInt(qntIds));
                
                List<BigInteger> idsAtividadesCurso = ag.recuperaItemIdsAtividades(idCursoSelecionado);
                
                
                double notaAluno = 0 + rand.nextInt(51);
                
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
                
                manager.getTransaction().commit();
                
                
                //Se o commit acima lan�ar rollback, entao nao foi adicionado nova nota, assim nao preciso
                // recalcular a media
                
                manager.getTransaction().begin();
                
                userId = new BigInteger(new Long(ag.getId()).toString());
                
                media = ag.calculaMedia(idCursoSelecionado, userId);
                
                System.out.println("**** MEDIA: " + media);
        
                itemIdCursoSelecionado = ns.get(idCursoSelecionado).longValue();
                
                System.out.println("entrando aqui " + ns.get(idCursoSelecionado));
                Query q = manager.createQuery("SELECT n FROM Nota n WHERE itemid = ?1");
                q.setParameter(1, itemIdCursoSelecionado);
                          
                Nota mediaRec = (Nota) q.getSingleResult();
                
                System.out.println("*** JA TEM MEDIA ****");
                System.out.println("** VALOR: " + mediaRec.getFinalgrade());
                        
                mediaRec.setFinalgrade(media);
                
                manager.getTransaction().commit();
              
        }catch(NoResultException e){
                        
                System.out.println("*** NAO TEM MEDIA ****");
                
                Nota mediaAtualizada = new Nota();
                mediaAtualizada.setFinalgrade(media);
                mediaAtualizada.setUserid(userId.longValue());
                mediaAtualizada.setItemid(itemIdCursoSelecionado);
                System.out.println("*** VAI PERSISTIR A NOTA****");
                manager.persist(mediaAtualizada);
             
                manager.getTransaction().commit();
              
            
         }catch(RollbackException e){
                        
                System.out.println("**** ROLLBACK *****");
                manager.getTransaction().rollback();
        
        }catch(PersistenceException e){
            System.out.println("**** ROLLBACK *****");
        
        }finally{
                manager.close();
                
        }
		
		
	}
	
	@Override
	public boolean done() {
	
		return done;
	}

}
