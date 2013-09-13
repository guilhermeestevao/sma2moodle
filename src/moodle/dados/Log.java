package moodle.dados;

import java.io.Serializable;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="mdl_log")
@NamedQueries({
@NamedQuery(name="Log.findByCourse", query="SELECT log FROM Log log WHERE course=?1 AND userid=?2"),
@NamedQuery(name="Log.findByUm",  query="SELECT log FROM Log log WHERE course = 1 AND userid=?1")
})
public class Log implements Serializable{
	
	@Id
	@GeneratedValue
	private BigInteger id;
	private Long time;
	private String module;
	private String action;
	private BigInteger course;
	
	public BigInteger getId() {
		return id;
	}
	
	public String getModule() {
		return module;
	}

	public Date getTime() {
		long milisec = 1000;
		return new Date(time*milisec);
	}

	public String getAction() {
		return action;
	}
	
	public BigInteger getCourseId() {
		return course;
	}
	
	@Override
	public String toString() {
		
		SimpleDateFormat formato = new SimpleDateFormat("dd:MM:yyyy / hh:mm:ss");
		
		String log="Log: "+this.getId()+" - "+
			 "Data/Hora: "+formato.format(getTime())+" - "+
			 "Modulo: "+this.getModule()+" - "+
			 "A��o: "+this.getAction()+" - "+
			 "ID do Curso: "+this.getCourseId();
		
		return log;
	}
}
