package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="mdl_quiz")
@NamedQuery(name="Questionario.findByCourse", query="SELECT q FROM Questionario q WHERE course = ?1")
public class Questionario extends AtividadeNota {
	
	
	public BigDecimal grade;
	public Long timeopen;
	public Long timeclose;
	
	public Questionario(){}	
	
	public Date getTimeopen(){
		return new Date(timeopen * miliSec);
	}
	
	public Date getTimeclose(){
		return new Date(timeclose * miliSec);
	}
	
	public BigDecimal getGrade(){
		return grade;
	}
	
	public Date getDataInicio(){
		return getTimeopen();
	}
	
	public Date getDataFinal(){
		return getTimeclose();
	}
	
	public BigDecimal getNotaMaxima(){
		return grade;
	}
	
}
