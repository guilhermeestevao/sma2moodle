package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mdl_lesson")
@NamedQuery(name="Licao.findByCourse", query = "SELECT lic FROM Licao lic WHERE course = ?1")
public class Licao extends AtividadeNota {
	
	public Long available; //Data disponivel
	public Long deadline; //Data de termino
	public BigDecimal grade;
	
	public Licao(){}

	public Date getAvailable(){
		return new Date(available * miliSec);
	}
	
	public Date getDeadline(){
		return new Date(deadline * miliSec);
	}
	
	public BigDecimal getGrade(){
		return grade;
	}
	
	public Date getDataInicio(){
		return getAvailable();
	}
	
	public Date getDataFinal(){
		return getDeadline();
	}
	
	public BigDecimal getNotaMaxima(){
		return grade;
	}
}