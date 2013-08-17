package nu.wasis.stunden.plugins.stdout.config;

import java.util.LinkedList;
import java.util.List;

public class StundenSTDOutPluginConfig {

    private List<String> noWork = new LinkedList<>();

    public List<String> getNoWork() {
        return noWork;
    }

    public void setNoWork(final List<String> noWork) {
        this.noWork = noWork;
    }

}
