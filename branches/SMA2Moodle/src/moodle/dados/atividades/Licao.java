package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;

import java.util.HashSet;

import moodle.dados.Assunto;


@Entity
@Table(name = "mdl_lesson")
@NamedQuery(name="Licao.findByCourse", query = "SELECT lic FROM Licao lic WHERE course = ?1")
public class Licao extends AtividadeNota {
	
	public Long available; //Data disponivel
	public Long deadline; //Data de termino
	public BigDecimal grade;
	@ManyToMany(cascade={CascadeType.PERSIST})
	@JoinTable(name="licao_assunto", joinColumns=@JoinColumn(name="id_licao"), 
	inverseJoinColumns=@JoinColumn(name="id_assunto"))
	private Collection<Assunto> assuntos = new HashSet<Assunto>();
	
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