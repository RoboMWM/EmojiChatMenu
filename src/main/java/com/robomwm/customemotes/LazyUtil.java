package com.robomwm.customemotes;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 3/10/2018.
 *
 * @author RoboMWM
 */
public class LazyUtil
{
    public static BookMeta getBookMeta()
    {
        return (BookMeta)(new ItemStack(Material.WRITTEN_BOOK).getItemMeta());
    }

    public static ItemStack getBook(BookMeta bookMeta)
    {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setItemMeta(bookMeta);
        return book;
    }

    public static BaseComponent[] buildPage(Object... strings)
    {
        List<BaseComponent> baseComponents = new ArrayList<>(strings.length);
        for (Object object : strings)
        {
            if (object instanceof BaseComponent)
                baseComponents.add((BaseComponent)object);
            else if (object instanceof String)
                baseComponents.addAll(Arrays.asList(TextComponent.fromLegacyText((String)object)));
        }
        return baseComponents.toArray(new BaseComponent[baseComponents.size()]);
    }

    public static List<BaseComponent> addLegacyText(String string, List<BaseComponent> baseComponents)
    {
        for (BaseComponent baseComponent : TextComponent.fromLegacyText(string))
            baseComponents.add(baseComponent);
        return baseComponents;
    }

    public static TextComponent getClickableCommand(String message, String command)
    {
        return getClickableCommand(message, command, command);
    }

    public static TextComponent getClickableCommand(String message, String command, String hover)
    {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setColor(ChatColor.DARK_AQUA);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
        if (hover != null)
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)));
        return textComponent;
    }

    public static TextComponent getClickableURL(String message, String URL, String hover)
    {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setColor(ChatColor.DARK_AQUA);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, URL));
        if (hover != null)
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)));
        return textComponent;
    }

    public static TextComponent getClickableSuggestion(String message, String suggestion)
    {
        return getClickableSuggestion(message, suggestion, suggestion);
    }

    public static TextComponent getClickableSuggestion(String message, String suggestion, String hover)
    {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setColor(ChatColor.DARK_AQUA);
        textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, suggestion));
        if (hover != null)
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)));
        return textComponent;
    }

    public static TextComponent getHoverable(String message, String hover)
    {
        TextComponent textComponent = new TextComponent(message);
        textComponent.setColor(ChatColor.AQUA);
        textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(hover)));
        return textComponent;
    }

    /**
     * Loads a yaml file with the specified pathSeparator
     * @param plugin
     * @param fileName
     * @param pathSeparator
     * @return
     */
    public static YamlConfiguration loadOrCreateYamlFile(Plugin plugin, String fileName, char pathSeparator)
    {
        YamlConfiguration yamlConfiguration = new YamlConfiguration();
        yamlConfiguration.options().pathSeparator(pathSeparator);
        File storageFile = new File(plugin.getDataFolder(), fileName);
        try
        {
            yamlConfiguration.load(storageFile);
        }
        catch (InvalidConfigurationException | IOException ignored) {}
        return yamlConfiguration;
    }
}
