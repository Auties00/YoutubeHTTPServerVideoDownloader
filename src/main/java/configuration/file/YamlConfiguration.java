package configuration.file;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.security.spec.ECField;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;

import configuration.Configuration;
import configuration.ConfigurationSection;
import configuration.InvalidConfigurationException;

public class YamlConfiguration extends FileConfiguration {
    protected static final String COMMENT_PREFIX = "# ";
    protected static final String BLANK_CONFIG = "{}\n";
    private final DumperOptions yamlOptions = new DumperOptions();
    private final Representer yamlRepresenter = new YamlRepresenter();
    private final Yaml yaml = new Yaml(new YamlConstructor(), this.yamlRepresenter, this.yamlOptions);

    public YamlConfiguration() {

    }

    public YamlConfiguration(Configuration configuration){
        super(configuration);
    }

    public String saveToString()
    {
        this.yamlOptions.setIndent(options().indent());
        this.yamlOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        this.yamlRepresenter.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);

        String header = buildHeader();
        String dump = this.yaml.dump(getValues(false));
        if (dump.equals("{}\n")) {
            dump = "";
        }
        return header + dump;
    }

    public void loadFromString(String contents) throws InvalidConfigurationException {
        Map <?, ?> input = null;
        try {
           input = this.yaml.load(contents);
        } catch (YAMLException e) {
            throw new InvalidConfigurationException(e);
        } catch (ClassCastException localClassCastException) {
            throw new InvalidConfigurationException("Top level is not a Map.");
        }

        String header = parseHeader(contents);
        if (header.length() > 0) {
            options().header(header);
        }
        if (input != null) {
            convertMapsToSections(input, this);
        }
    }

    protected void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
        for (Map.Entry<?, ?> entry : input.entrySet())
        {
            String key = entry.getKey().toString();
            Object value = entry.getValue();
            if ((value instanceof Map)) {
                convertMapsToSections((Map)value, section.createSection(key));
            } else {
                section.set(key, value);
            }
        }
    }

    protected String parseHeader(String input)
    {
        String[] lines = input.split("\r?\n", -1);
        StringBuilder result = new StringBuilder();
        boolean readingHeader = true;
        boolean foundHeader = false;
        for (int i = 0; (i < lines.length) && (readingHeader); i++)
        {
            String line = lines[i];
            if (line.startsWith("# "))
            {
                if (i > 0) {
                    result.append("\n");
                }
                if (line.length() > "# ".length()) {
                    result.append(line.substring("# ".length()));
                }
                foundHeader = true;
            }
            else if ((foundHeader) && (line.length() == 0))
            {
                result.append("\n");
            }
            else if (foundHeader)
            {
                readingHeader = false;
            }
        }
        return result.toString();
    }

    protected String buildHeader()
    {
        String header = options().header();
        if (options().copyHeader())
        {
            Configuration def = getDefaults();
            if (((def instanceof FileConfiguration)))
            {
                FileConfiguration filedefaults = (FileConfiguration)def;
                String defaultsHeader = filedefaults.buildHeader();
                if ((defaultsHeader != null) && (defaultsHeader.length() > 0)) {
                    return defaultsHeader;
                }
            }
        }
        if (header == null) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        String[] lines = header.split("\r?\n", -1);
        boolean startedHeader = false;
        for (int i = lines.length - 1; i >= 0; i--)
        {
            builder.insert(0, "\n");
            if ((startedHeader) || (lines[i].length() != 0))
            {
                builder.insert(0, lines[i]);
                builder.insert(0, "# ");
                startedHeader = true;
            }
        }
        return builder.toString();
    }

    public YamlConfigurationOptions options()
    {
        if (this.options == null) {
            this.options = new YamlConfigurationOptions(this);
        }
        return (YamlConfigurationOptions)this.options;
    }

    public YamlConfiguration loadConfiguration(File file) {
        YamlConfiguration config = new YamlConfiguration(getDefaults());
        try {
            config.load(file);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return config;
    }

    public YamlConfiguration loadConfiguration(Reader reader) {
        YamlConfiguration config = new YamlConfiguration(getDefaults());
        try {
            config.load(reader);
        } catch (Exception e){
            e.printStackTrace();
        }
        return config;
    }
}
