package configuration.file;

public class YamlConfigurationOptions extends FileConfigurationOptions {
    private int indent = 2;

    protected YamlConfigurationOptions(YamlConfiguration configuration) {
        super(configuration);
    }

    public YamlConfiguration configuration()
    {
        return (YamlConfiguration)super.configuration();
    }

    public YamlConfigurationOptions copyDefaults(boolean value)
    {
        super.copyDefaults(value);
        return this;
    }

    public YamlConfigurationOptions pathSeparator(char value)
    {
        super.pathSeparator(value);
        return this;
    }

    public YamlConfigurationOptions header(String value)
    {
        super.header(value);
        return this;
    }

    public YamlConfigurationOptions copyHeader(boolean value)
    {
        super.copyHeader(value);
        return this;
    }

    public int indent()
    {
        return this.indent;
    }

    public YamlConfigurationOptions indent(int value) {
        this.indent = value;
        return this;
    }
}
