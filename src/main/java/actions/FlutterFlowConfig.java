package actions;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.Nullable;

@State(
        name = "FlutterFlowConfig",
        storages = @Storage("FlutterFlowConfig.xml")
)
public class FlutterFlowConfig implements PersistentStateComponent<FlutterFlowConfig> {
    public String projectId = "";
    public String apiToken = "";
    public String destinationPath = "";
    public boolean isEnterpriseIndia = false;

    @Nullable
    @Override
    public FlutterFlowConfig getState() {
        return this;
    }

    @Override
    public void loadState(FlutterFlowConfig state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
