package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import java.util.HashSet;
import java.util.Collection;
import moodle.dados.Assunto;

@Entity
@Table(name="mdl_quiz")
@NamedQuery(name="Questionario.findByCourse", query="SELECT q FROM Questionario q WHERE course = ?1")
public class Questionario extends AtividadeNota {
	
	
	public BigDecimal grade;
	public Long timeopen;
	public Long timeclose;
	@ManyToMany(cascade={CascadeType.PERSIST})
	@JoinTable(name="ag_questionario_assunto", joinColumns=@JoinColumn(name="id_questionario"), 
	inverseJoinColumns=@JoinColumn(name="id_assunto"))
	private Collection<Assunto> assuntos = new HashSet<Assunto>();
	
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

	
	public Collection<Assunto> getAssuntos() {
		return assuntos;
	}

	public void addAssunto(Collection<Assunto> assunto) {
		assuntos.addAll(assunto);
	}
	public void addAssunto(Assunto assunto){
		assuntos.add(assunto);
	}
	
}
