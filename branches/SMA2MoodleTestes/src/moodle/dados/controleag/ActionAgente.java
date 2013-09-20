package moodle.dados.controleag;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ag_actions_agentes")
public class ActionAgente {

	@Id
	@GeneratedValue
	private long id;
	
	private int id_agente;
	private int id_action;
	private BigInteger id_curso;
	private BigInteger id_aluno;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getId_agente() {
		return id_agente;
	}
	public void setId_agente(int id_agente) {
		this.id_agente = id_agente;
	}
	public int getId_action() {
		return id_action;
	}
	public void setId_action(int id_action) {
		this.id_action = id_action;
	}
	public BigInteger getId_curso() {
		return id_curso;
	}
	public void setId_curso(BigInteger id_curso) {
		this.id_curso = id_curso;
	}
	public BigInteger getId_aluno() {
		return id_aluno;
	}
	public void setId_aluno(BigInteger id_aluno) {
		this.id_aluno = id_aluno;
	}
	
	
	
}
