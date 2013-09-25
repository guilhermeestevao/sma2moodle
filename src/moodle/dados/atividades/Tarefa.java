package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;
import java.util.HashSet;
import java.util.Collection;
import moodle.dados.Assunto;

@Entity
@Table(name = "mdl_assign")
@NamedQuery(name="Tarefa.findByCourse", query = "SELECT tar FROM Tarefa tar WHERE course = ?1")
public class Tarefa extends AtividadeNota {
	
	public BigDecimal grade;
	public Long allowsubmissionsfromdate;
	public Long duedate; //Data termino de submissao
	@ManyToMany(cascade={CascadeType.PERSIST})
	@JoinTable(name="ag_tarefa_assunto", joinColumns=@JoinColumn(name="id_tarefa"), 
	inverseJoinColumns=@JoinColumn(name="id_assunto"))
	private Collection<Assunto> assuntos = new HashSet<Assunto>();
	
	public Tarefa(){}	
	
	public Date getAllowsubmissionsfromdate(){
		return new Date(allowsubmissionsfromdate * miliSec);
	}
	
	public Date getDuedate(){
		return new Date(duedate * miliSec);
	}
	
	public BigDecimal getGrade(){
		return grade;
	}
	
	public Date getDataInicio(){
		return getAllowsubmissionsfromdate();
	}
	
	public Date getDataFinal(){
		return getDuedate();
	}
	
	public BigDecimal getNotaMaxima(){
		return grade;
	}
	
	public Collection<Assunto> getAssuntos(){
		return assuntos;
	}
	
	public void addAssunto(Collection<Assunto> assunto) {
		assuntos.addAll(assunto);
	}
	public void addAssunto(Assunto assunto){
		assuntos.add(assunto);
	}
	
}