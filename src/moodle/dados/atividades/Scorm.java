package moodle.dados.atividades;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="mdl_scorm")
@NamedQuery(name="Scorm.findByCourse", query="SELECT s FROM Scorm s WHERE course = ?1")
public class Scorm extends AtividadeParticipacao{

	@Transient
	private boolean avaliativo;
	
	public Scorm(){}
	
	public boolean isAvaliativo() {
		return avaliativo;
	}

	public void setAvaliativo(boolean avaliativo) {
		this.avaliativo = avaliativo;
	}
}
