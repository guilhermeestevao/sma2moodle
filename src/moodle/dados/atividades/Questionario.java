package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumns;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.ManyToMany;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;

import java.util.HashSet;
import java.util.Collection;

import moodle.dados.Assunto;
import moodle.dados.Material;

@Entity
@Table(name="mdl_quiz")
@NamedQuery(name="Questionario.findByCourse", query="SELECT q FROM Questionario q WHERE course = ?1")
public class Questionario extends AtividadeNota {
	
	
	public BigDecimal grade;
	public Long timeopen;
	public Long timeclose;
	@ManyToMany(fetch = FetchType.EAGER, cascade={CascadeType.PERSIST})
	@JoinTable(name="ag_questionario_material", joinColumns=@JoinColumn(name="id_questionario"), 
	inverseJoinColumns=@JoinColumn(name="id_material"))
	private Collection<Material> materiais = new HashSet<Material>();
	
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
