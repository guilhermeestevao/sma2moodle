package moodle.dados.grupos;

import java.math.BigInteger;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "mdl_groups_members")
public class MembrosGrupo {

	@Id
	@GeneratedValue
	private BigInteger id;
	
	private BigInteger groupid;
	private BigInteger userid;
	private Long timeadded;
	
	public BigInteger getGroupid() {
		return groupid;
	}
	
	public void setGroupid(BigInteger groupid) {
		this.groupid = groupid;
	}
	
	public BigInteger getUserid() {
		return userid;
	}
	
	public void setUserid(BigInteger userid) {
		this.userid = userid;
	}
	
	public void setTimeadded(Long t){
		timeadded = t;
	}
	
	public BigInteger getId() {
		return id;
	}
	
}
