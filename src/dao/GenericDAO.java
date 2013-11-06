package dao;

import java.util.List;
import java.util.Map;

public interface GenericDAO<T> {
	
	public void save(T entity);
	public T find(Object id);
	public List<T> findAll();
	public void delete(T entity);
	public T findParameterized(String namedQuery, Map<String, Object> parameters);
	public List<T> findAllParameterized(String namedQuery, Map<String, Object> parameters);
	public List<T> findAllParameterized(String namedQuery, Map<String, Object> parameters, int startPosition, int qntPerPag);
	public void deleteById(Object id);
	public T merge(T entity);
	public void beginTransaction();
	public void commit();
	public void rollback();
	public void close();
	
}
