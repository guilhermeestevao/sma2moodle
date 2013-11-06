package moodle.dados;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mdl_user")
@NamedQuery(name="Professor.findById", query="SELECT professor FROM Professor professor WHERE id = ?1")
public class Professor extends Pessoa{
	
	

}
