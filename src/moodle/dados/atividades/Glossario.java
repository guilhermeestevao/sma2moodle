package moodle.dados.atividades;

import java.util.Date;
import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "mdl_glossary")
@NamedQuery(name="Glossario.findByCourse", query="SELECT g FROM Glossario g WHERE course = ?1")
public class Glossario extends AtividadeParticipacao {
	
	@Transient
	private boolean avaliativo;
	
	private BigDecimal scale;
	private int assessed;
	private Long timecreated;
	private Long assesstimestart;
	private Long assesstimefinish;
	
	
	public Glossario(){}
	
	public boolean isAvaliativo() {
		return  assessed == 0 ? false : true;
	}
	
	public Date getTimecreated(){
		return new Date(timecreated * miliSec);
	}
	
	public Date getAssesstimestart(){
		return new Date(assesstimestart * 1000);
	}
	
	public Date getAssesstimefinish(){
		return new Date(assesstimefinish * 1000);
	}
	
	public BigDecimal getScale(){
		return scale;
	}

	public Date getDataInicio(){
		return getTimecreated();
	}
	
	public Date getDataFinal(){
		return getAssesstimefinish();
	}
	
	public BigDecimal getNotaMaxima(){
		return scale;
	}
	
}
