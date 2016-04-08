package org.ow2.proactive.scheduler.core.db;


import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "JOB_EVENT_DATA")
public class JobEventData {

    private Long id;

    private Long jobId;

    private String jobName;

    private String batch;

    private Date createTime;

    static JobEventData createJobData(Long jobId,String jobName,String batch,Date createTime) {

        JobEventData JobEventData = new JobEventData();
        JobEventData.setJobId(jobId);
        JobEventData.setJobName(jobName);
        JobEventData.setBatch(batch);
        JobEventData.setCreateTime(createTime);
        return JobEventData;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "JOBEVENTID_SEQUENCE")
    @SequenceGenerator(name = "JOBEVENTID_SEQUENCE", sequenceName = "JOBEVENTID_SEQUENCE")
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

    @Column(name = "BATCH", updatable = false)
    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    @Column(name = "CREATE_TIME", updatable = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
