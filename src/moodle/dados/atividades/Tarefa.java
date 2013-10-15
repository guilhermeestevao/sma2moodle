package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.CascadeType;

import java.util.HashSet;
import java.util.Collection;

import moodle.dados.Assunto;
import moodle.dados.Material;

@Entity
@Table(name = "mdl_assign")
@NamedQuery(name="Tarefa.findByCourse", query = "SELECT tar FROM Tarefa tar WHERE course = ?1")
public class Tarefa extends AtividadeNota {
	
	public BigDecimal grade;
	public Long allowsubmissionsfromdate;
	public Long duedate; //Data termino de submissao
	@ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinTable(name="ag_tarefa_material", joinColumns=@JoinColumn(name="id_tarefa"), 
	inverseJoinColumns=@JoinColumn(name="id_material"))
	private Collection<Material> materiais = new HashSet<Material>();
	
	
	
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
	
	public Collection<Material> getMateriais() {
		return materiais;
	}

	public void addMateriais(Collection<Material> materiais) {
		this.materiais.addAll(materiais);
	}
	public void addMateriais(Material material) {
		this.materiais.add(material);
	}
	
}