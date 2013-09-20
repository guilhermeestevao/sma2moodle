package moodle.Agentes.AgentesSimuladores.Dados;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mdl_grade_grades")
public class Nota {

	@Id 
	@GeneratedValue
	private Long id;
	
	private long itemid;
	private long userid;
	
	
	@Column(nullable=true)
	private Long usermodified;
	
	@Column(nullable=true)
	private Double rawgrade;
	private double finalgrade;
	
	
	public Nota(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getItemid() {
		return itemid;
	}

	public void setItemid(long itemid) {
		this.itemid = itemid;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public Long getUsermodified() {
		return usermodified;
	}

	public void setUsermodified(Long usermodified) {
		this.usermodified = usermodified;
	}

	public Double getRawgrade() {
		return rawgrade;
	}

	public void setRawgrade(Double rawgrade) {
		this.rawgrade = rawgrade;
	}

	public double getFinalgrade() {
		return finalgrade;
	}

	public void setFinalgrade(double finalgrade) {
		this.finalgrade = finalgrade;
	}
	
	
		
	
	
}
