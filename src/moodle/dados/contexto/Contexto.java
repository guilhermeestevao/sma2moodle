package moodle.dados.contexto;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="mdl_context")
public class Contexto {
	 
//	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Id
	private BigInteger id;
	private BigInteger contextlevel;
	private BigInteger instanceid;
	private String path;
	private short depth = 4;
	
	
	public void setId(BigInteger id) {
		this.id = id;
	}

	public BigInteger getContextlevel() {
		return contextlevel;
	}
	
	public void setContextlevel(BigInteger contextlevel) {
		this.contextlevel = contextlevel;
	}
	
	public BigInteger getInstanceid() {
		return instanceid;
	}
	
	public void setInstanceid(BigInteger instanceid) {
		this.instanceid = instanceid;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String path) {
		this.path = path;
	}
	
	public BigInteger getId() {
		return id;
	}
	
}