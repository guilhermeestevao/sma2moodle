package moodle.dados;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name= "mdl_coordenador")
@NamedQuery(name="Coordenador.findByCourse", query="SELECT coordenador From Coordenador coordenador WHERE curso = ?1 ")
public class Coordenador implements Serializable {
	
	@Id @GeneratedValue
	private BigInteger id;
	private BigInteger curso;
	private String email;
	
	public BigInteger getCurso() {
		return curso;
	}
	
	public void setCurso(BigInteger curso) {
		this.curso = curso;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public BigInteger getId() {
		return id;
	}
}
