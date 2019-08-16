package alvinJNI.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;

/**
 * 统一管理REG命令执行
 *
 * @author zsl
 */
public class LocalCommandExecutor {

    public String executeCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String result = "";
            if (bufferedReader.readLine() != null) {
                String line = null;
                StringBuilder buf = new StringBuilder();
                while (StringUtils.isNotBlank((line = bufferedReader.readLine()))) {
                    buf.append(line + "\n");
                }
                result = buf.toString();
//	            if(StringUtils.isNotBlank(result)&&command.indexOf("QUERY")==-1){
//	            	System.out.println(result);
//	            }
            }
            process.waitFor();
            process.exitValue();
            return result;
        } catch (IOException ex) {
            System.out.println("The command [" + command + "] execute failed.");
            ex.printStackTrace();
            return null;
        } catch (InterruptedException ex) {
            System.out.println("The command [" + command + "] did not complete due to an interrupted error.");
            ex.printStackTrace();
            return null;
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

}