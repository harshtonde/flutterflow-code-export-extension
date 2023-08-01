package actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.notification.NotificationGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CodeDownloadWithoutAssets extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        // Create a notification
        NotificationGroup flutterFlowGroup = NotificationGroup.findRegisteredGroup("FlutterFlow");
        Notification startingNotification = flutterFlowGroup.createNotification("Starting FlutterFlow code export without assets", NotificationType.INFORMATION);

        // Notify
        Notifications.Bus.notify(startingNotification);

        // Run the CLI command in a new thread
        new Thread(() -> {
            try {
                FlutterFlowConfig config = ServiceManager.getService(FlutterFlowConfig.class);

                List<String> command = new ArrayList<>();
                command.add("dart");
                command.add("pub");
                command.add("global");
                command.add("run");
                command.add("flutterflow_cli");
                command.add("export-code");
                command.add("--project");
                command.add(config.projectId);
                command.add("--dest");
                command.add(config.destinationPath);
                command.add("--no-include-assets");
                command.add("--token");
                command.add(config.apiToken);

                if (config.isEnterpriseIndia) {
                    command.add("--endpoint");
                    command.add("https://api-enterprise-india.flutterflow.io/v1");
                }

                ProcessBuilder builder = new ProcessBuilder(command);
                builder.redirectErrorStream(true);
                Process process = builder.start();

                StringBuilder output = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                    System.out.println(line);
                }

                int exitCode = process.waitFor();
                if (exitCode != 0) {
                    throw new RuntimeException("Command execution failed with exit code " + exitCode + ". \n" + output.toString());
                }

                // Success notification
                Notification successNotification = flutterFlowGroup.createNotification("FlutterFlow code export without assets completed successfully.", NotificationType.INFORMATION);
                Notifications.Bus.notify(successNotification);
            } catch (Exception ex) {
                // Failure notification
                Notification failureNotification = flutterFlowGroup.createNotification("Failed to execute FlutterFlow code export without assets.\n" + ex.getMessage(), NotificationType.ERROR);
                Notifications.Bus.notify(failureNotification);
            }
        }).start();
    }
}
