package moodle.dados.atividades;
import java.math.BigDecimal;
import java.util.Date;


import javax.persistence.*;

@Entity
@Table(name = "mdl_data")
@NamedQuery(name="DataBase.findByCourse", query="SELECT d FROM DataBase d WHERE course = ?1")

public class DataBase extends AtividadeParticipacao{
	
	
	private BigDecimal scale;
	private int assessed;
	
	
	private Long timeavailablefrom; //Disponivel a partir de
	private Long timeavailableto; //Diposnivel até
	private Long timeviewfrom; //Visivel a partir de
	private Long timeviewto; //Visiavel até
	private Long assesstimestart; //Data inicial de avaliação 
	private Long assesstimefinish; // Data final de avaliação

	public DataBase(){}

	public boolean isAvaliativo() {
		return assessed == 0 ? false : true;	
	}

	public Date getTimeavailablefrom() {
		return new Date(timeavailablefrom*miliSec);
	}

	public Date getTimeavailableto() {
		return new Date(timeavailableto*miliSec);
	}

	public Date getTimeviewfrom() {
		return new Date(timeviewfrom*miliSec);
	}

	public Date getTimeviewto() {
		return new Date(timeviewto*miliSec);
	}

	public Date getAssesstimestart() {
		return new Date(assesstimestart*miliSec);
	}

	public Date getAssesstimefinish() {
		return new Date(assesstimefinish*miliSec);
	}

	public BigDecimal getScale() {
		return scale;
	}

	@Override
	public Date getDataInicio() {
		return getTimeavailablefrom();
	}

	@Override
	public Date getDataFinal() {
		return getAssesstimefinish();
	}

	public BigDecimal getNotaMaxima(){
		return scale;
	}


}
