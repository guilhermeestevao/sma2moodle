package moodle.dados.atividades;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="mdl_wiki")
@NamedQuery(name="Wiki.findByCourse", query="SELECT w FROM Wiki w WHERE course = ?1")
public class Wiki extends AtividadeParticipacao {

	private String wikimode;
	private Long timecreated;
	private Long timemodified;
	
	
	public String getWikimode(){
		return wikimode;
	}
	
	public Date getTimecreated(){
		return new Date(timecreated * miliSec);
	}
	
	public Date getTimemodified(){
		return new Date(timemodified * miliSec);
	}
	
	public Date getDataInicio(){
		return getTimecreated();
	}
}
