package org.ow2.proactive.scheduler.email;

/**
 * Created by leven2 on 16-4-12.
 */
public class ProactiveInfo {

    private String status;

    private String logUrl;

    private String note;

    private String Version;

    private String Hostname;

    private int Issues;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogUrl() {
        return logUrl;
    }

    public void setLogUrl(String logUrl) {
        this.logUrl = logUrl;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getHostname() {
        return Hostname;
    }

    public void setHostname(String hostname) {
        Hostname = hostname;
    }

    public int getIssues() {
        return Issues;
    }

    public void setIssues(int issues) {
        Issues = issues;
    }

    @Override
    public String toString() {
        return "ProactiveInfo{" +
                "status='" + status + '\'' +
                ", logUrl='" + logUrl + '\'' +
                ", note='" + note + '\'' +
                ", Version='" + Version + '\'' +
                ", Hostname='" + Hostname + '\'' +
                ", Issues=" + Issues +
                '}';
    }

    public String toHtmlString() {
        StringBuffer str = new StringBuffer();
        str.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">")
                .append("<html>")
                .append("<head>")
                .append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">")
                .append("<title>proactive send email</title>")
                .append("<style type=\"text/css\">")
                .append(".class_001{font-family:\"Microsoft Yahei\";font-size: 18px;color: blue;}")
                .append(".class_002{font-family:\"Microsoft Yahei\";font-size: 18px;color: red;}")
                .append("</style>")
                .append("</head>")
                .append("<body>")
                .append("<span class=\"class_001\"> Status: </span><span>"+getStatus()+"</span>")
                .append("<br/>")
                .append("<span class=\"class_001\"> Logger: </span><a href=\""+getLogUrl()+"\">"+getLogUrl()+"</a>")
                .append("<br/>")
                .append("<span class=\"class_001\"> Issues: </span><span>"+getIssues()+"</span>")
                .append("<br/>")
                .append("<span class=\"class_001\"> Version: </span><span>"+getVersion()+"</span>")
                .append("<br/>")
                .append("<span class=\"class_001\"> Hostname: </span><span>"+getHostname()+"</span>")
                .append("<br/>")
                .append("<br/>")
                .append("<br/>")
                .append("<br/>")
                .append("<br/>")
                .append("<span class=\"class_002\"> "+getNote()+"</span>")
                .append("</body>")
                .append("</html>");
        return str.toString();
    }
}
