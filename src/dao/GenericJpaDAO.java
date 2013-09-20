package dao;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
 
public abstract class GenericJpaDAO<T> implements GenericDAO<T> {
 
  protected Class<T> persistentClass;
 	
	/*
	 * Persiste o objeto no banco de dados
	 */
	@Override
	public void save(T entity) {		
		getEm().persist(entity);
	}

	
	/*
	 * Busca um objeto no banco de dados
	 */
	@Override
	public T find(Object id) {
		return getEm().find(persistentClass, id);
		
	}

	/*
	 * Busca um objeto no banco de dados passando uma Named Query e seus Parametros
	 */
	@SuppressWarnings("unchecked")
	public T findParameterized(String namedQuery, Map<String, Object> parameters){
		
		try {
			
			Query q = setFindParameterized(namedQuery, parameters);
			return (T) q.getSingleResult();
		
		} catch (Exception e) {
		
			System.out.println("Erro: " + e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}
	
	/*
	 * Busca todos os objetos da entidade mapeada 
	 */
	@Override
	public List<T> findAll() {
		CriteriaQuery<T> critQuery = getEm().getCriteriaBuilder().createQuery(persistentClass);
		critQuery.from(persistentClass);
		return getEm().createQuery(critQuery).getResultList();
	}
	
	
	/*
	 * Busca objetos da entidade mapeada que correspondem a Named Query com seus parametros definidos
	 */
	@SuppressWarnings("unchecked")
	public List<T> findAllParameterized(String namedQuery, Map<String, Object> parameters){
		 
		
		try {
			
			Query q = setFindParameterized(namedQuery, parameters);
			return q.getResultList();
		
		} catch (Exception e) {
		
			System.out.println("Erro: " + e.getMessage());
			e.printStackTrace();
		}
		
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> findAllParameterized(String namedQuery, Map<String, Object> parameters, int startPosition, int qntPerPag){
		 
		
		try {
			
			Query q = setFindParameterized(namedQuery, parameters);
			q.setFirstResult(startPosition);
			q.setMaxResults(qntPerPag);
			return q.getResultList();
		
		} catch (Exception e) {
		
			System.out.println("Erro: " + e.getMessage());
			e.printStackTrace();
		}
		
		
		return null;
	}
	

	/*
	 * Delete um objeto no banco de dados
	 */
	@Override
	public void delete(T entity) {
		getEm().remove(getEm().merge(entity));
	}

	/*
	 * Deleta um objeto no banco de dados a partir do ID passado
	 */
	public void deleteById(Object id) {
		getEm().remove(getEm().getReference(persistentClass, id));
	}
	
	/*
	 * Seta todos os parametros na Named Query passada
	 */
	private void populateQueryParameters(Query query, Map<String, Object> parameters) {
        for (Entry<String, Object> entry : parameters.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }
    }
	
	/*
	 * Constroi a Query parametrizada
	 */
	private Query setFindParameterized(String namedQuery, Map<String, Object> parameters) throws Exception{
		
		if(parameters == null || parameters.isEmpty())
			throw new Exception("Query sem parametros definidos.");
		
			
			
		Query q = getEm().createNamedQuery(namedQuery);
		populateQueryParameters(q, parameters);
		
		return q;
		
	}
	
	public T merge(T entity){
		return getEm().merge(entity);
	}

 
	public EntityManager getEm() {
		return JPAUtil.getEntityManager();
	}
 
	public void beginTransaction() {
		JPAUtil.beginTransaction();
	}
 
	public void commit() {
		JPAUtil.commit();
	}
 
	public void rollback() {
		JPAUtil.rollback();
	}
 
	public void close() {
		JPAUtil.closeEntityManager();
	}
 
}
