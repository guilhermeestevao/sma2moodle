package moodle.dados.atividades;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import moodle.dados.Aluno;

@Entity
@Table(name = "mdl_workshop")
@NamedQuery(name="LaboratorioDeAvaliacao.findByCourse", query = "SELECT la FROM LaboratorioDeAvaliacao la WHERE course = ?1")
public class LaboratorioDeAvaliacao extends AtividadeNota {
	
	public Long submissionstart;
	public Long submissionend;
	public Long assessmentstart;
	public Long assessmentend;
	public BigDecimal grade;
	public BigDecimal gradinggrade;
	
	@Transient
	private Map<Aluno, BigDecimal> nota2; //Map para guardar a segunda nota do aluno (Parte de avaliaçao)
	
	public LaboratorioDeAvaliacao(){
		
		nota2 = new HashMap<Aluno, BigDecimal>();
	}

	public Date getSubmissionstart(){
		return new Date(submissionstart * miliSec);
	}
	
	public Date getSubmissionend(){
		return new Date(submissionend * miliSec);
	}
	
	public Date getAssessmentstart(){
		return new Date(assessmentstart * miliSec);
	}
	
	public Date getAssessmentend(){
		return new Date(assessmentend * miliSec);
	}
	
	public BigDecimal getGrade(){
		return grade;
	}
	
	public BigDecimal getGradinggrade(){
		return gradinggrade;
	}
	
	public Map<Aluno, BigDecimal> getNota2(){
		return nota2;
	}
	
	public void addNota2(Aluno aluno, BigDecimal nota){
		nota2.put(aluno, nota);
	}
	
	public Date getDataInicio(){
		return getSubmissionstart();
	}
	
	public Date getDataFinal(){
		return getSubmissionend();
	}
	
	public BigDecimal getNotaMaxima(){
		return grade;
	}
	

}