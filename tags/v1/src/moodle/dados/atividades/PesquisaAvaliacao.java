package moodle.dados.atividades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mdl_survey")
@NamedQuery(name="PesquisaAvaliacao.findByCourse", query = "SELECT p FROM PesquisaAvaliacao p WHERE course = ?1")
public class PesquisaAvaliacao extends AtividadeParticipacao {
	
	private Long timecreated;
	private Long timemodified;
	
	public Date getTimecreated(){
		return new Date(timecreated * 1000);
	}
	
	public Date getTimemodified(){
		return new Date(timemodified * 1000);
	}
	
	public Date getDataInicio(){
		return getTimecreated();
	}
	
}
