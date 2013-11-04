package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.HashMap;
import moodle.dados.Aluno;
import moodle.dados.Atividade;

public abstract class AtividadeNota extends Atividade {
	

	public AtividadeNota(){
		notas = new HashMap<Aluno, BigDecimal>();
	}
	
	

}
