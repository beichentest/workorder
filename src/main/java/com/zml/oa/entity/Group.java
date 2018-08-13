package com.zml.oa.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "T_GROUP")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Group implements Serializable{

	private static final long serialVersionUID = -4469236450461851881L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "GROUP_ID")
	private Integer id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "TYPE")
	private String type;

	//bi-directional many-to-many association to User
//    @ManyToMany(mappedBy = "group")
//    private List<User> user;
	@OneToMany(targetEntity=User.class,cascade=CascadeType.REFRESH)
	@Fetch(FetchMode.JOIN)
	//updatable=false很关键，如果没有它，在级联删除的时候就会报错(反转的问题)
	@JoinColumn(name="GROUP_ID",updatable=false)
	@JsonIgnore
    private Set<User> user = new HashSet<User>();
	
	@OneToOne(targetEntity=User.class,cascade=CascadeType.REFRESH)
	@JoinColumn(name="LEADER",updatable=true)
	private User leader;
	
	@Column(name = "PARENT_ID")
	private Integer parentId;
	
	@Column(name = "SEQ")
	private Double seq;
    
	public Group(){
		
	}
	
	public Group(Integer id){
		this.id = id;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<User> getUser() {
		return user;
	}

	public void setUser(Set<User> user) {
		this.user = user;
	}

	public User getLeader() {
		return leader;
	}

	public void setLeader(User leader) {
		this.leader = leader;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public Double getSeq() {
		return seq;
	}

	public void setSeq(Double seq) {
		this.seq = seq;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
    
	
}
