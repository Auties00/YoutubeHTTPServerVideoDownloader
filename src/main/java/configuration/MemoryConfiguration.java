package configuration;

import java.util.Map;

public class MemoryConfiguration extends MemorySection implements Configuration {
    private Configuration defaults;
    public MemoryConfigurationOptions options;

    public MemoryConfiguration() {

    }

    public MemoryConfiguration(Configuration defaults) {
        this.defaults = defaults;
    }

    public void addDefault(String path, Object value) {
        if (this.defaults == null) {
            this.defaults = new MemoryConfiguration();
        }
        this.defaults.set(path, value);
    }

    public void addDefaults(Map<String, Object> defaults) {
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            addDefault(entry.getKey(), entry.getValue());
        }
    }

    public void addDefaults(Configuration defaults) {
        addDefaults(defaults.getValues(true));
    }

    public void setDefaults(Configuration defaults) {
        this.defaults = defaults;
    }

    public Configuration getDefaults() {
        return this.defaults;
    }

    public ConfigurationSection getParent() {
        return null;
    }

    public MemoryConfigurationOptions options() {
        if (this.options == null) {
            this.options = new MemoryConfigurationOptions(this);
        }
        return this.options;
    }
}
