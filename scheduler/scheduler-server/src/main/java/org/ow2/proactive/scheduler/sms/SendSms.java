package org.ow2.proactive.scheduler.sms;

import org.apache.log4j.Logger;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by leven2 on 16-4-22.
 */
public class SendSms {
    private static final Logger logger = Logger.getLogger(SendSms.class);

    private static final String CHMOD_777 = "chmod 777";

    private String smsPath;

    private String recipient;

    private String msg;

    public SendSms(String smsPath,String recipient, String msg)
    {
        this.smsPath=smsPath;
        this.recipient=recipient;
        this.msg=msg;
    }

    public boolean send(){
        boolean send =false;
        if(chmod777()){
            String cmd[] = new String[3];
            cmd[0] = smsPath;
            cmd[1] = recipient;
            cmd[2] = msg;
            return  ExeShell(cmd);
        }
        return send;
    }

    public boolean chmod777()  {
        try {
            String cmd = CHMOD_777 + smsPath;
            Process process = Runtime.getRuntime().exec(cmd);
            process.waitFor();
        } catch (Exception e) {
            logger.info("chmod777 e:"+e);
        }
        return true;
    }

    public boolean ExeShell(String cmd[]){
            Runtime run = Runtime.getRuntime();
            String result = "";
            BufferedReader br=null;
            BufferedInputStream in=null;
            try {
                Process p = run.exec(cmd);
                if(p.waitFor() != 0){
                    result+="没有进程号";
                    logger.info("ExeShell 没有进程号");
                    return false;
                }
                in = new BufferedInputStream(p.getInputStream());
                br = new BufferedReader(new InputStreamReader(in));
                String lineStr;
                while ((lineStr = br.readLine()) != null) {
                    result += lineStr+"\n";
                }
            } catch (Exception e) {
                logger.info("ExeShell e:"+e);
                return false;
            }finally{
                if(br!=null){
                    try {
                        br.close();
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                logger.info("ShellUtil.ExeShell:"+result);
            }
            return true;
    }

}
