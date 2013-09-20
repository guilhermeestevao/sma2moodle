package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mdl_assign")
@NamedQuery(name="Tarefa.findByCourse", query = "SELECT tar FROM Tarefa tar WHERE course = ?1")
public class Tarefa extends AtividadeNota {
	
	public BigDecimal grade;
	public Long allowsubmissionsfromdate;
	public Long duedate; //Data termino de submissao
	
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
	
}