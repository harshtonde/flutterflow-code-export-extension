package actions;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class FlutterFlowConfigAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        MyDialog dialog = new MyDialog();
        dialog.showAndGet();
    }

    private class MyDialog extends DialogWrapper {

        private JBTextField projectIdField;
        private JBTextField apiTokenField;
        private JBTextField destinationPathField;
        private JCheckBox enterpriseIndiaCheckBox;

        protected MyDialog() {
            super(true);

            FlutterFlowConfig config = ServiceManager.getService(FlutterFlowConfig.class);
            projectIdField = new JBTextField(config.projectId);
            apiTokenField = new JBTextField(config.apiToken);
            destinationPathField = new JBTextField(config.destinationPath);
            enterpriseIndiaCheckBox = new JCheckBox("Check this if you're using Enterprise India", config.isEnterpriseIndia);

            init();
            setTitle("FlutterFlow Configuration");
        }

        @Nullable
        @Override
        protected JComponent createCenterPanel() {
            JPanel dialogPanel = new JPanel(new GridLayout(0, 1));
            dialogPanel.setPreferredSize(new Dimension(500, 200));

            dialogPanel.add(new JBLabel("FlutterFlow project ID"));
            dialogPanel.add(projectIdField);
            dialogPanel.add(new JBLabel("Destination path"));
            dialogPanel.add(destinationPathField);
            dialogPanel.add(new JBLabel("API token"));
            dialogPanel.add(apiTokenField);
            dialogPanel.add(enterpriseIndiaCheckBox);

            return dialogPanel;
        }

        @Override
        protected ValidationInfo doValidate() {
            if (projectIdField.getText().trim().isEmpty()) {
                return new ValidationInfo("Please enter Project ID", projectIdField);
            }
            if (apiTokenField.getText().trim().isEmpty()) {
                return new ValidationInfo("Please enter API Token", apiTokenField);
            }
            if (destinationPathField.getText().trim().isEmpty()) {
                return new ValidationInfo("Please enter Destination Path", destinationPathField);
            }
            return null;
        }

        @Override
        protected void doOKAction() {
            FlutterFlowConfig config = ServiceManager.getService(FlutterFlowConfig.class);
            config.projectId = projectIdField.getText().trim();
            config.apiToken = apiTokenField.getText().trim();
            config.destinationPath = destinationPathField.getText().trim();
            config.isEnterpriseIndia = enterpriseIndiaCheckBox.isSelected();

            super.doOKAction();
        }
    }
}
