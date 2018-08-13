package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
* @ClassName: Project  
* @Description: TODO(这里用一句话描述这个类的作用)  
* @author Administrator  
* @date 2018年6月28日  
*
 */
@Entity
@Table(name = "T_PROJECT")
@DynamicUpdate(true)
@DynamicInsert(true)
public class Project implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -622628316981046957L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	/**
	 * 项目名称
	 */
	@Column(name = "NAME")
	private String name;
	/**
	 * 开发语言及版本
	 */
	@Column(name = "LANGUAGE")
	private String language;
	/**
	 * 编译说明
	 */
	@Column(name = "BUILD_INFO")
	private String buildInfo; 
	/**
	 * 是否有测试用例（0 无  ，1 有）
	 */
	@Column(name = "HAVE_TEST")
	private String haveTest;
	/**
	 * 是否有源码（0 无  ，1 有）
	 */
	@Column(name = "HAVE_CODE")
	private String haveCode;
	/**
	 * 是否有发布版本（0 无  ，1 有）
	 */
	@Column(name = "HAVE_RELEASE")
	private String haveRelease;
	/**
	 * 是否有文档
	 */
	@Column(name = "HAVE_DOC")
	private String haveDoc;
	/**
	 * 开发人员
	 */
	@Column(name = "CODER")
	private String coder;
	/**
	 * 应用服务器信息
	 */
	@Column(name = "SERVER")
	private String server;
	/**
	 * 操作系统
	 */
	@Column(name = "OS")
	private String os;
	/**
	 * 数据库
	 */
	@Column(name = "DB")
	private String db;
	/**
	 * 上线时间
	 */
	@Column(name = "RELEASE_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date releaseTime;
	/**
	 * 状态（0 正常，1 下线）
	 */
	@Column(name = "STATUS")
	private String status;
	/**
	 * 下线时间
	 */
	@Column(name = "DISCARD_TIME")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date discardTime;
	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;
	/**
	 * 开发svn地址
	 */
	@Column(name = "CODER_SVN")
	private String coderSvn;
	/**
	 * 测试svn地址
	 */
	@Column(name = "TESTER_SVN")
	private String testerSvn;
	/**
	 * 归属
	 */
	@Column(name = "HOME")
	private String home;
	/**
	 * 应用地区
	 */
	@Column(name = "AREA")
	private String area;
	/**
	 * 项目版本前缀
	 */
	@Column(name = "version_prefix")
	private String versionPrefix;
	/**
	 * 程序类修改版本
	 */
	@Column(name = "version_code")
	private Integer versionCode;
	/**
	 * 非程序类修改版本
	 */
	@Column(name = "version_no_code")
	private Integer versionNoCode;
	
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
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getBuildInfo() {
		return buildInfo;
	}
	public void setBuildInfo(String buildInfo) {
		this.buildInfo = buildInfo;
	}
	public String getHaveTest() {
		return haveTest;
	}
	public void setHaveTest(String haveTest) {
		this.haveTest = haveTest;
	}
	public String getHaveCode() {
		return haveCode;
	}
	public void setHaveCode(String haveCode) {
		this.haveCode = haveCode;
	}
	
	public String getCoder() {
		return coder;
	}
	public void setCoder(String coder) {
		this.coder = coder;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getDiscardTime() {
		return discardTime;
	}
	public void setDiscardTime(Date discardTime) {
		this.discardTime = discardTime;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCoderSvn() {
		return coderSvn;
	}
	public void setCoderSvn(String coderSvn) {
		this.coderSvn = coderSvn;
	}
	public String getTesterSvn() {
		return testerSvn;
	}
	public void setTesterSvn(String testerSvn) {
		this.testerSvn = testerSvn;
	}
	public String getHaveRelease() {
		return haveRelease;
	}
	public void setHaveRelease(String haveRelease) {
		this.haveRelease = haveRelease;
	}
	public String getHaveDoc() {
		return haveDoc;
	}
	public void setHaveDoc(String haveDoc) {
		this.haveDoc = haveDoc;
	}
	public Date getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getVersionPrefix() {
		return versionPrefix;
	}
	public void setVersionPrefix(String versionPrefix) {
		this.versionPrefix = versionPrefix;
	}
	public Integer getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	public Integer getVersionNoCode() {
		return versionNoCode;
	}
	public void setVersionNoCode(Integer versionNoCode) {
		this.versionNoCode = versionNoCode;
	}	
}
