package me.luci.plugin;

import com.google.common.base.Charsets;
import com.google.common.io.ByteStreams;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginAwareness;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.logging.Level;

public class Config {
    private File f;

    private FileConfiguration cfg;

    private final String path_;

    private final String fileName_;

    private final Plugin main;

    //Register the main config
    public Config(String path, String fileName, Plugin javaPluginExtender) {
        this.main = javaPluginExtender;
        this.path_ = path;
        this.fileName_ = fileName;
    }

    //Register the reloading of the config
    public void reloadConfig() {
        this.cfg = YamlConfiguration.loadConfiguration(this.f);
        //Loads the file into the input stream
        InputStream defConfigStream = getResource(this.fileName_);
        //If the filestream isn't empty, reload it
        if (defConfigStream != null)
            this.cfg.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }

    //Creates a new config
    public Config create() {
        this.f = new File(this.path_, this.fileName_);
        this.cfg = YamlConfiguration.loadConfiguration(this.f);
        return this;
    }

    //Sets the default config to a custom file
    public void setDefault(String filename) {
        YamlConfiguration defConfig;
        InputStream defConfigStream = this.main.getResource(filename);
        if (defConfigStream == null)
            return;
        if (isStrictlyUTF8()) {
            defConfig =
                    YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream,
                            Charsets.UTF_8));
        } else {
            byte[] contents;
            defConfig = new YamlConfiguration();
            try {
                contents = ByteStreams.toByteArray(defConfigStream);
            } catch (IOException e) {
                this.main.getLogger().log(Level.SEVERE,
                        "Unexpected failure reading " + filename, e);
                return;
            }
            String text = new String(contents, Charset.defaultCharset());
            if (!text.equals(new String(contents, Charsets.UTF_8)))
                this.main.getLogger()
                        .warning(
                                "Default system encoding may have misread " + filename + " from plugin jar");
            try {
                defConfig.loadFromString(text);
            } catch (InvalidConfigurationException e) {
                this.main.getLogger().log(Level.SEVERE,
                        "Cannot load configuration from jar", (Throwable) e);
            }
        }
        this.cfg.setDefaults((Configuration) defConfig);
    }

    //Makes sure the file is UTF8 to stop errors
    @SuppressWarnings("deprecation")
    private boolean isStrictlyUTF8() {
        return this.main.getDescription().getAwareness().contains(
                PluginAwareness.Flags.UTF8);
    }

    //Saves the custom config
    public void saveConfig() {
        try {
            this.cfg.save(this.f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return this.cfg;
    }

    public File toFile() {
        return this.f;
    }

    //Saves the set default config, or the default generated
    public void saveDefaultConfig() {
        if (!exists())
            saveResource(this.fileName_, false);
    }

    //Saves the path resource in a file
    public void saveResource(String resourcePath, boolean replace) {
        if (resourcePath != null && !resourcePath.equals("")) {
            resourcePath = resourcePath.replace('\\', '/');
            InputStream in = getResource(resourcePath);
            if (in == null)
                throw new IllegalArgumentException("The embedded resource '" + resourcePath + "' cannot be found in " + toFile());
            File outFile = new File(this.path_, resourcePath);
            int lastIndex = resourcePath.lastIndexOf('/');
            File outDir = new File(this.path_, resourcePath.substring(0, Math.max(lastIndex, 0)));
            if (!outDir.exists())
                outDir.mkdirs();
            try {
                if (outFile.exists() && !replace) {
                    this.main.getLogger().log(Level.WARNING, "Could not save " + outFile.getName() + " to " + outFile + " because " + outFile.getName() + " already exists.");
                } else {
                    OutputStream out = new FileOutputStream(outFile);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0)
                        out.write(buf, 0, len);
                    out.close();
                    in.close();
                }
            } catch (IOException var10) {
                this.main.getLogger().log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile, var10);
            }
        } else {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }
    }

    //Gets the resources of a config
    public InputStream getResource(String filename) {
        if (filename == null)
            throw new IllegalArgumentException("Filename cannot be null");
        try {
            URL url = getClass().getClassLoader().getResource(filename);
            if (url == null)
                return null;
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException var4) {
            return null;
        }
    }

    public boolean exists() {
        return this.f.exists();
    }
}
