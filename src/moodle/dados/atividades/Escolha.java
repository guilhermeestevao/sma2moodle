package moodle.dados.atividades;

import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "mdl_choice")
@NamedQuery(name="Escolha.findByCourse", query="SELECT e FROM Escolha e WHERE course = ?1")
public class Escolha extends AtividadeParticipacao{

	private Long timeopen;
	private Long timeclose;
	
	public Escolha(){}
	
	public Date getTimeopen(){
		return new Date(timeopen * miliSec);
	}
	
	public Date getTimeclose(){
		return new Date(timeclose * miliSec);
	}

	@Override
	public Date getDataInicio() {
		return getTimeopen();
	}

	@Override
	public Date getDataFinal() {
		return getTimeclose();
	}
	
}
	
	
	

