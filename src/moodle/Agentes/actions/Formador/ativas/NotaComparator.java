package moodle.Agentes.actions.Formador.ativas;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;

import moodle.dados.Aluno;

public class NotaComparator implements Comparator<Aluno> {

	private Map<Aluno, BigDecimal> map;
	
	public NotaComparator(Map<Aluno, BigDecimal> map){
		this.map = map;
	}
	
	@Override
	public int compare(Aluno o1, Aluno o2) {
		
		BigDecimal nota1 = map.get(o1);
		BigDecimal nota2 = map.get(o2);
		
		return nota1.compareTo(nota2);
	}

	
}
