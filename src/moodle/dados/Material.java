package moodle.dados;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name="Material.findByCouse", query="SELECT m FROM Material m WHERE curso = ?1")
@Table(name="material")
public class Material {
	
	@Id
	@GeneratedValue
	private BigInteger id;
	private BigInteger curso;
	private String material;
	private String link;
	
	public BigInteger getCurso() {
		return curso;
	}
	
	public void setCurso(BigInteger curso) {
		this.curso = curso;
	}
	
	public String getMaterial() {
		return material;
	}
	
	public void setMaterial(String material) {
		this.material = material;
	}
	
	public String getLink() {
		return link;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public BigInteger getId() {
		return id;
	}

}
