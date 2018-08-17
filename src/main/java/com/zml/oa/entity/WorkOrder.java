package com.zml.oa.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 工单对象
 * @author admin
 *
 */
@Entity
@Table(name = "T_WORK_ORDER")
// 在Hibernate中可以利用@DynamicInsert和@DynamicUpdate生成动态SQL语句，
//即在插入和修改数据的时候,语句中只包括要插入或者修改的字段。
@DynamicUpdate(true)
@DynamicInsert(true)
public class WorkOrder extends BaseVO implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 686350462242080885L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID", unique = true)
	private Integer id;
	/**
	 * 工单提交时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "APPLY_DATE")
	private Date applyDate;
	/**
	 * 项目id
	 */
	@ManyToOne(targetEntity = Project.class)
	@JoinColumn(name="PROJECT_ID")
	private Project project;
	/**
	 * 工单提交人ID
	 */
	@Column(name = "APPLY_USER_ID")
	private Integer applyUserId;
	/**
	 * 工单提交人部门审核人ID
	 */
	@Column(name = "BUSINESS_AUDIT_USER_ID")
	private Integer businessAuditUserId;	
	/**
	 * 工单提交人部门审核时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "BUSINESS_AUDIT_DATE")
	private Date businessAuditDate;
	/**
	 * 开发人员ID
	 */
	@Column(name = "CODER_ID")
	private Integer coderId;
	
	
	/**
	 * 开发部门审核人ID
	 */
	@Column(name = "CODER_AUDIT_ID")
	private Integer coderAuditId;
	/**
	 * 开发部门审核时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CODER_AUDIT_DATE")
	private Date coderAuditDate;
	/**
	 * 测试人员ID
	 */
	@Column(name = "TESTER_ID")
	private Integer testerId;
	/**
	 * 测试部门审核人ID
	 */
	@Column(name = "TESTER_AUDIT_ID")
	private Integer testerAuditId;
	/**
	 * 测试部门审核时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "TEST_AUDIT_DATE")
	private Date testAuditDate;
	/**
	 * 部署人员ID
	 */
	@Column(name = "WEB_MASTER_ID")
	private Integer webMasterId;
	/**
	 * 部署上线
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "DEPLOY_DATE")
	private Date deployDate;
	/**
	 * 工单确时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "VERIFY_DATE")
	private Date verifyDate;
	/**
	 * 开发提交版本号
	 */
	@Column(name = "CODER_VERSION")
	private String coderVersion;
	/**
	 * 项目域名
	 */
	@ManyToOne(targetEntity = Domain.class)
	@JoinColumn(name="domain_ID")
	private Domain domain;
	/**
	 * 开发提交SVN地址
	 */
	@Column(name = "CODER_SVN")
	private String coderSvn;
	/**
	 * 测试提交版本号
	 */
	@Column(name = "TEST_VERSION")
	private String testVersion;
	/**
	 * 测试提交SVN地址
	 */
	@Column(name = "TEST_SVN")
	private String testSvn;
	/**
	 * 发布说明
	 */
	@Column(name = "DEVELOP_EXPLAIN")
	private String developExplain;
	/**
	 * 备注
	 */
	@Column(name = "MEMO")
	private String memo;
	/**
	 * 流程ID
	 */
	@Column(name = "PROC_INST_ID")
	private String procInstId;
	/**
	 * 当前状态
	 */
	@Column(name = "STATUS")
	private String status;
	/**
	 * 提交人姓名
	 */
	@Column(name = "APPLY_USER")
	private String applyUser;
	/**
	 * 提交部门审核人姓名
	 */
	@Column(name = "BUSINESS_AUDIT_USER")
	private String businessAuditUser;
	/**
	 * 开发人员姓名
	 */
	@Column(name = "CODER")
	private String coder;
	/**
	 * 开发部门审核人姓名
	 */
	@Column(name = "CODER_AUDIT")
	private String coderAudit;
	/**
	 * 测试人员姓名
	 */
	@Column(name = "TESTER")
	private String tester;
	/**
	 * 测试部门审核人姓名
	 */
	@Column(name = "TESTER_AUDIT")
	private String testerAudit;
	/**
	 * 部署人员姓名
	 */
	@Column(name = "WEB_MASTER")
	private String webMaster;
	/**
	 * 开发提交时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "CODER_DATE")
	private Date coderDate;
	/**
	 * 测试提交时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "TESTER_DATE")
	private Date testerDate;
	/**
	 * 运维部门审核人
	 */
	@Column(name = "WEB_MASTER_AUDIT")
	private String webMasterAudit;
	/**
	 * 运维部门审核人ID
	 */
	@Column(name = "WEB_MASTER_AUDIT_ID")
	private Integer webMasterAuditId;
	/**
	 * 运维部门审核时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "WEB_MASTER_AUDIT_DATE")
	private Date webMasterAuditDate;
	/**
	 * 优先级(降序)
	 */
	@Column(name = "PRIORITY")
	private Integer priority;
	/**
	 * 类别
	 */
	@Column(name = "TYPE")
	private String type;
	/**
	 * 入库人员姓名
	 */
	@Column(name = "COMMITER")
	private String commiter;
	/**
	 * 入库人员ID
	 */
	@Column(name = "COMMITER_ID")
	private Integer commiterId;
	/**
	 * 测试描述
	 */
	@Column(name = "TEST_DESC")
	private String testDesc;
	/**
	 * 入库时间
	 */
	@Column(name = "COMMITER_DATE")
	private Date commiterDate;
	/**
	 * 是否回滚（1，是；0，否）
	 */
	@Column(name = "IS_ROLLBACK")
	private String rollbackFlag;
	/**
	 * 回滚版本号
	 */
	@Column(name = "ROLLBACK_VERSION")
	private String rollbackVersion;
	/**
	 * 回滚原因
	 */
	@Column(name = "ROLLBACK_REASON")
	private String rollbackReason;
	/**
	 * 项目版本号
	 */
	@Column(name = "PROJECT_VERSION")
	private String projectVersion;
	
	public WorkOrder(){
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Integer getApplyUserId() {
		return applyUserId;
	}

	public void setApplyUserId(Integer applyUserId) {
		this.applyUserId = applyUserId;
	}

	public Integer getBusinessAuditUserId() {
		return businessAuditUserId;
	}

	public void setBusinessAuditUserId(Integer businessAuditUserId) {
		this.businessAuditUserId = businessAuditUserId;
	}

	public Date getBusinessAuditDate() {
		return businessAuditDate;
	}

	public void setBusinessAuditDate(Date businessAuditDate) {
		this.businessAuditDate = businessAuditDate;
	}

	public Integer getCoderId() {
		return coderId;
	}

	public void setCoderId(Integer coderId) {
		this.coderId = coderId;
	}

	public Integer getCoderAuditId() {
		return coderAuditId;
	}

	public void setCoderAuditId(Integer coderAuditId) {
		this.coderAuditId = coderAuditId;
	}

	public Date getCoderAuditDate() {
		return coderAuditDate;
	}

	public void setCoderAuditDate(Date coderAuditDate) {
		this.coderAuditDate = coderAuditDate;
	}

	public Integer getTesterId() {
		return testerId;
	}

	public void setTesterId(Integer testerId) {
		this.testerId = testerId;
	}

	public Integer getTesterAuditId() {
		return testerAuditId;
	}

	public void setTesterAuditId(Integer testerAuditId) {
		this.testerAuditId = testerAuditId;
	}

	public Date getTestAuditDate() {
		return testAuditDate;
	}

	public void setTestAuditDate(Date testAuditDate) {
		this.testAuditDate = testAuditDate;
	}

	public Integer getWebMasterId() {
		return webMasterId;
	}

	public void setWebMasterId(Integer webMasterId) {
		this.webMasterId = webMasterId;
	}

	public Date getDeployDate() {
		return deployDate;
	}

	public void setDeployDate(Date deployDate) {
		this.deployDate = deployDate;
	}

	public Date getVerifyDate() {
		return verifyDate;
	}

	public void setVerifyDate(Date verifyDate) {
		this.verifyDate = verifyDate;
	}

	public String getCoderVersion() {
		return coderVersion;
	}

	public void setCoderVersion(String coderVersion) {
		this.coderVersion = coderVersion;
	}
	
	public Domain getDomain() {
		return domain;
	}

	public void setDomain(Domain domain) {
		this.domain = domain;
	}

	public String getCoderSvn() {
		return coderSvn;
	}

	public void setCoderSvn(String coderSvn) {
		this.coderSvn = coderSvn;
	}

	public String getTestVersion() {
		return testVersion;
	}

	public void setTestVersion(String testVersion) {
		this.testVersion = testVersion;
	}

	public String getTestSvn() {
		return testSvn;
	}

	public void setTestSvn(String testSvn) {
		this.testSvn = testSvn;
	}

	public String getDevelopExplain() {
		return developExplain;
	}

	public void setDevelopExplain(String developExplain) {
		this.developExplain = developExplain;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApplyUser() {
		return applyUser;
	}

	public void setApplyUser(String applyUser) {
		this.applyUser = applyUser;
	}

	public String getBusinessAuditUser() {
		return businessAuditUser;
	}

	public void setBusinessAuditUser(String businessAuditUser) {
		this.businessAuditUser = businessAuditUser;
	}

	public String getCoder() {
		return coder;
	}

	public void setCoder(String coder) {
		this.coder = coder;
	}

	public String getCoderAudit() {
		return coderAudit;
	}

	public void setCoderAudit(String coderAudit) {
		this.coderAudit = coderAudit;
	}

	public String getTester() {
		return tester;
	}

	public void setTester(String tester) {
		this.tester = tester;
	}

	public String getTesterAudit() {
		return testerAudit;
	}

	public void setTesterAudit(String testerAudit) {
		this.testerAudit = testerAudit;
	}

	public String getWebMaster() {
		return webMaster;
	}

	public void setWebMaster(String webMaster) {
		this.webMaster = webMaster;
	}

	public Date getCoderDate() {
		return coderDate;
	}

	public void setCoderDate(Date coderDate) {
		this.coderDate = coderDate;
	}

	public Date getTesterDate() {
		return testerDate;
	}

	public void setTesterDate(Date testerDate) {
		this.testerDate = testerDate;
	}

	public String getWebMasterAudit() {
		return webMasterAudit;
	}

	public void setWebMasterAudit(String webMasterAudit) {
		this.webMasterAudit = webMasterAudit;
	}

	public Integer getWebMasterAuditId() {
		return webMasterAuditId;
	}

	public void setWebMasterAuditId(Integer webMasterAuditId) {
		this.webMasterAuditId = webMasterAuditId;
	}

	public Date getWebMasterAuditDate() {
		return webMasterAuditDate;
	}

	public void setWebMasterAuditDate(Date webMasterAuditDate) {
		this.webMasterAuditDate = webMasterAuditDate;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getProjectName() {
		if(project!=null&&StringUtils.isNotBlank(project.getName())) {
			return project.getName();
		}
		return "";
	}
	
	public String getDomainDomain() {
		if(domain!=null&&StringUtils.isNotBlank(domain.getDomain())) {
			return domain.getDomain();
		}
		return "";
	}
	
	public String getDomainName() {
		if(domain!=null&&StringUtils.isNotBlank(domain.getName())) {
			return domain.getName();
		}
		return "";
	}
	
	public String getHome() {
		if(domain!=null&&StringUtils.isNotBlank(domain.getSubjection())) {
			return domain.getSubjection();
		}
		return "";
	}
	
	public String getArea() {
		if(domain!=null&&StringUtils.isNotBlank(domain.getArea())) {
			return domain.getArea();
		}
		return "";
	}
	
	public String getPriorityStr() {
		if(priority!=null&&priority==60) {
			return "紧急";
		}else {
			return "正常";
		}
	}

	public String getCommiter() {
		return commiter;
	}

	public void setCommiter(String commiter) {
		this.commiter = commiter;
	}

	public Integer getCommiterId() {
		return commiterId;
	}

	public void setCommiterId(Integer commiterId) {
		this.commiterId = commiterId;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public Date getCommiterDate() {
		return commiterDate;
	}

	public void setCommiterDate(Date commiterDate) {
		this.commiterDate = commiterDate;
	}

	public String getRollbackFlag() {
		return rollbackFlag;
	}

	public void setRollbackFlag(String rollbackFlag) {
		this.rollbackFlag = rollbackFlag;
	}

	public String getRollbackVersion() {
		return rollbackVersion;
	}

	public void setRollbackVersion(String rollbackVersion) {
		this.rollbackVersion = rollbackVersion;
	}

	public String getRollbackReason() {
		return rollbackReason;
	}

	public void setRollbackReason(String rollbackReason) {
		this.rollbackReason = rollbackReason;
	}

	public String getProjectVersion() {
		return projectVersion;
	}

	public void setProjectVersion(String projectVersion) {
		this.projectVersion = projectVersion;
	}
	
	public String getDescribe() {
		if(StringUtils.isBlank(this.developExplain)) {
			return "";
		}
		if(StringUtils.isNotBlank(this.projectVersion)) {
			return this.projectVersion + " " +this.developExplain;
		}
		return this.developExplain;
	}
	
	public String getRollbackDesc() {
		if("1".equals(this.rollbackFlag)) {
			return "是";
		}else {
			return "否";
		}
	}
}
