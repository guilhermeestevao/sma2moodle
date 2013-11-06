package moodle.dados.atividades;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "mdl_forum")
@NamedQuery(name="Forum.findByCourse", query="SELECT f FROM Forum f WHERE course = ?1")
public class Forum extends AtividadeParticipacao {
	
	private BigDecimal scale;
	private int assessed;
	private Long assesstimestart;
	private Long assesstimefinish;
	private Long timemodified;
	@Transient
	private boolean avaliativo;
	@Transient
	private boolean tutorParticipa;
	@Transient
	private Long ultimaPartipacao;
	@Transient
	private Long ultimoPost;
	
	public Forum(){}
	
	public boolean isTutorParticipa(){
		return tutorParticipa;
	}
	

	
	public void setTutorParticipa(boolean participacao){
		this.tutorParticipa = participacao;
	}
	
	public Date getUltimaPartipacao() {
		return new Date(ultimaPartipacao*miliSec);
	}

	public void setUltimaPartipacao(Long ultimaPartipacao) {
		this.ultimaPartipacao = ultimaPartipacao;
	}
	
	public Date getUltimoPost() {
		return new Date(ultimoPost*miliSec);
	}

	public void setUltimoPost(Long ultimoPost) {
		this.ultimoPost = ultimoPost;
	}

	public Date getTimemodified() {
		return new Date(timemodified*miliSec);
	}

	public BigDecimal getScale() {
		return scale;
	}
	
	public boolean isAvaliativo() {
		return assessed == 0 ? false : true;
	}
	
	public Date getAssesstimestart() {
		if(assesstimestart!=0)
			return new Date(assesstimestart*miliSec);
		else
			return new Date();
	}

	public Date getAssesstimefinish() {
		if(assesstimefinish!=0)
			return new Date(assesstimefinish*miliSec);
		else
			return new Date();
	}
	
	public Date getDataInicio(){
		return getAssesstimestart();
	}
	
	public Date getDataFinal(){
		return getAssesstimefinish();
	}
	
	public BigDecimal getNotaMaxima(){
		return scale;
	}
	
}