package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;



@Entity
@Table(name = "mdl_lti")
@NamedQuery(name="FerramentaExterna.findByCourse", query = "SELECT fe FROM FerramentaExterna fe WHERE course = ?1")
public class FerramentaExterna extends AtividadeNota {
	
	public BigDecimal grade;
	public Long timecreated;
	public Long timemodified;
	
	public FerramentaExterna(){}	
	
	public Date getTimecreated(){
		return new Date(timecreated * miliSec);
	}
	
	public Date getTimemodified(){
		return new Date(timemodified * miliSec);
	}
	
	public BigDecimal getGrade(){
		return grade;
	}

	@Override
	public Date getDataInicio() {
		return getTimecreated();
	}

	public BigDecimal getNotaMaxima(){
		return grade;
	}

	
	
	

}