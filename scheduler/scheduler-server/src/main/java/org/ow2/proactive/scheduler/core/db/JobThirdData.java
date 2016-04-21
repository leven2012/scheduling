package org.ow2.proactive.scheduler.core.db;


import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "JOB_THIRD_DATA")
public class JobThirdData {

    private Long id;

    private Long jobId;

    private String jobName;

    private String thirdIp;

    private String thirdName;

    private String thirdPath;

    private String fileName;

    private Date createTime;

    static JobThirdData createJobThirdData(Long jobId, String jobName,String thirdIp, String thirdName,
                                      String thirdPath,String fileName,Date createTime) {

        JobThirdData jobThirdData = new JobThirdData();
        jobThirdData.setJobId(jobId);
        jobThirdData.setJobName(jobName);
        jobThirdData.setThirdIp(thirdIp);
        jobThirdData.setThirdName(thirdName);
        jobThirdData.setThirdPath(thirdPath);
        jobThirdData.setFileName(fileName);
        jobThirdData.setCreateTime(createTime);
        return jobThirdData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOBTHIRDID_SEQUENCE")
    @SequenceGenerator(name = "JOBTHIRDID_SEQUENCE", sequenceName = "JOBTHIRDID_SEQUENCE")
    @Column(name = "ID")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "JOB_ID", nullable = false, updatable = false)
    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    @Column(name = "JOB_NAME", updatable = false)
    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    @Column(name = "THIRD_IP", updatable = false)
    public String getThirdIp() {
        return thirdIp;
    }

    public void setThirdIp(String thirdIp) {
        this.thirdIp = thirdIp;
    }

    @Column(name = "THIRD_NAME", updatable = false)
    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    @Column(name = "THIRD_PATH", updatable = false)
    public String getThirdPath() {
        return thirdPath;
    }

    public void setThirdPath(String thirdPath) {
        this.thirdPath = thirdPath;
    }

    @Column(name = "FILE_NAME", updatable = false)
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "CREATE_TIME", updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
