package moodle.dados.grupos;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import moodle.dados.Aluno;

@Entity
@Table(name = "mdl_groups")
@NamedQuery(name="Grupo.findByCourse", query="SELECT grupo FROM Grupo grupo WHERE courseid = ?1")
public class Grupo implements Serializable{
	
	@Id
	@GeneratedValue
	private BigInteger id;
	
	private BigInteger courseid;
	private String name;
	private String description;
	private int descriptionformat;
	private Long timecreated;
	
	@Transient
	private Collection<Aluno> membros;

	public Grupo(){
		membros = new HashSet<Aluno>();
	}
	
	public BigInteger getId() {
		return id;
	}

	public BigInteger getCourseid() {
		return courseid;
	}
	
	public void setCourseid(BigInteger courseid){
		this.courseid = courseid;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	
	public void setDescription(String des){
		this.description = des;
	}

	public void setDescriptionformat(int num){
		descriptionformat = num;
	}
	
	public void setTimecreated(Long t){
		timecreated = t;
	}
	
	public Collection<Aluno> getMembros() {
		return membros;
	}
	
	public void addMembro(Aluno aluno){
		membros.add(aluno);
	}
	
	public void addMembros(Collection<Aluno> alunos){
		membros.addAll(alunos);
	}
	
}
