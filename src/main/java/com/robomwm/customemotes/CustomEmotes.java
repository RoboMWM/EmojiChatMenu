package com.robomwm.customemotes;

import com.robomwm.usefulutil.UsefulUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created on 3/17/2018.
 *
 * @author RoboMWM
 */
public class CustomEmotes extends JavaPlugin implements CommandExecutor, Listener
{
    private List<EmoteRequest> emoteRequests = new ArrayList<>();
    private Map<Pattern, List<String>> emojiMovie = new HashMap<>();
    private YamlConfiguration emoteYaml;
    public void onEnable()
    {
        saveConfig();
        emoteYaml = UsefulUtil.loadOrCreateYamlFile(this, "emotes.yml", '•');
        for (String emoteCode : emoteYaml.getKeys(false))
            for (String emote : emoteYaml.getStringList(emoteCode))
                put(emoteCode, emote);
        if (getConfig().getBoolean("firstrun", true))
        {
            getConfig().set("firstrun", false);
            put(":shrug:", "\u00AF\\_(\u30C4)_/\u00AF");
            put(":shrug:", " ┐('～`)┌");
            put(":shrug:", " ┐('～`；)┌");
            put(":shrug:", "~\\_(''/)_/~ ");
            put(":flip:", "( ﾉ⊙︵⊙）ﾉ ︵ ┻━┻");
            put(":flip:", "┻━┻ ︵ ¯\\ (ツ)/¯ ︵ ┻━┻");
            put(":flip:", "(ノᚖ⍘ᚖ)ノ彡┻━┻");
            put("unflip", "┬──┬◡ﾉ(° -°ﾉ)");
            put(":lenny:", "( ͡°( ͡° ͜ʖ( ͡° ͜ʖ ͡°)ʖ ͡°) ͡°)");
            put(":lenny:", "( ͜。 ͡ʖ ͜。)");
            put(":lenny:", "乁(´益`)ㄏ");
            put(":lenny:", "| ﾟ! ﾟ|");
            put(":lenny:", "°.ʖ ͡°");
            put(":lenny:", "ಠ_ಠ");
            put(":lenny:", "(╭☞ ͠° ͜ʖ °)╭☞");
            put(":heart:", "♥");
            put(":swag:", "ⓈⓌⒶⒼ");
            put(":derp:", "◴_◶");
            put(":fiteme:", "ლ(ಠ益ಠლ)");
            put(":checkmark:", "✔");
            put(":x:", "✖");
            put(":box:", "☐");
            put(":checkbox:", "☑");
            put(":xbox:", "☒");
            put(":triangle:", "▲");
            put(":square:", "■");
            put(":circle:", "○");
            put(":katakana:", "ツ");
            put(":copy:", "©");
            put(":trademark:", "™");
            put(":1/2:", "½");
            put(":clock:", "⌚");
            put(":registered:", "®");
            put(":registered:", "®");
            put(":hourglass:", "⌛");
            put(":star:", "★");
            put(":star:", "☆");
            put(":armystar:", "✪");
            put(":up:", "↑");
            put(":down:", "↓");
            put(":left:", "←");
            put(":right:", "→");
            put(":sun:", "☼");
            put(":sun:", "☀");
            put(":moon:", "☾");
            put(":moon:", "☽");
            put(":cloud:", "☁");
            put(":umbrella:", "☂");
            put(":snowman:", "☃");
            put(":zap:", "ϟ");
            put(":airplane:", "✈");
            put(":crossbones:", "☠");
            put(":music:", "♪");
            put(":music:", "♫");
            put(":music:", "♪♫");
            put(":anchor:", "⚓");
            put(":warning:", "⚠");
            put(":radioactive:", "☢");
            put(":biohazard:", "☣");
            put(":coffee:", "☕");
            put(":gear:", "⚙");
            put("<3", "♥");
            put(":relaxed:", "☺");
            put(":)", "☻");
            put(">:(", "Ò╭╮Ó");
            put(":(", "☹");
            saveEmotes();
        }
        getConfig().addDefault("promptToAdd", true);
        getConfig().options().copyDefaults(true);
        saveConfig();
        getServer().getPluginManager().registerEvents(this, this);
    }

    private void saveEmotes()
    {
        for (Pattern pattern : emojiMovie.keySet())
        {
            String code = pattern.pattern().substring(9, pattern.pattern().length() - 8);
            emoteYaml.set(code, emojiMovie.get(pattern));
        }
        UsefulUtil.saveYamlFileDelayed(this, "emotes.yml", emoteYaml);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onEmoteModJoin(PlayerJoinEvent event)
    {
        notifyNewRequests(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void chatter(AsyncPlayerChatEvent event)
    {
        event.setMessage(playEmojiMovie(event.getMessage()));
    }
    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    private void signer(SignChangeEvent event)
    {
        String[] lines = event.getLines();
        int i = 0;
        for (String line : lines)
            event.setLine(i++, playEmojiMovie(line));
    }

    private String playEmojiMovie(String message)
    {
        for (Pattern pattern : emojiMovie.keySet())
        {
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) //Big performance difference - don't create unnecessary string objects when there's nothing to change!
                message = matcher.replaceAll(Matcher.quoteReplacement(emojiMovie.get(pattern).get(ThreadLocalRandom.current().nextInt(emojiMovie.get(pattern).size()))));
        }
        return message;
    }

    private void put(String patternString, String emote)
    {
        //Match the patternString exactly, and only if nothing except whitespace is pre/appended
        patternString = "(?<!\\S)\\Q" + patternString + "\\E(?!\\S)";
        List<String> thing = null;
        for (Pattern pattern : emojiMovie.keySet())
        {
            if (pattern.pattern().equalsIgnoreCase(patternString))
            {
                thing = emojiMovie.get(pattern);
                break;
            }
        }
        if (thing == null)
        {
            thing = new ArrayList<>();
            emojiMovie.put(Pattern.compile(patternString), thing);
        }
        thing.add(emote);
    }

    private boolean notifyNewRequests(CommandSender sender)
    {
        if (!sender.hasPermission("emote.moderator") || emoteRequests.isEmpty())
            return false;
        sender.sendMessage("New emote requests! Click to add:");
        for (EmoteRequest emoteRequest : emoteRequests)
        {
            sender.sendMessage(LazyUtil.getClickableSuggestion(emoteRequest.getCode() + " " + emoteRequest.getEmote(),"/addemote " + emoteRequest.getCode() + " " + emoteRequest.getEmote()));
        }
        sender.sendMessage(LazyUtil.getClickableCommand("[Clear all requests]", "/clearemoterequests"));
        return true;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("addemote") && args.length > 1)
        {
            String code = args[0];
            args[0] = null;
            String emote = StringUtils.join(args, ' ').substring(1);

            if (!sender.hasPermission("emote.moderator"))
            {
                emoteRequests.add(new EmoteRequest(code, emote));
                sender.sendMessage("Your new emote is pending approval. " + code + " " + emote);
            }
            else
            {
                Iterator<EmoteRequest> iterator = emoteRequests.iterator();
                while (iterator.hasNext())
                {
                    EmoteRequest emoteRequest = iterator.next();
                    if (emoteRequest.getCode().equals(code) && emoteRequest.getEmote().equals(emote))
                        iterator.remove();

                }
                put(code, emote);
                saveEmotes();
                sender.sendMessage("Added " + emote + " with code " + code);
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("clearemoterequests"))
        {
            emoteRequests.clear();
            sender.sendMessage("Cleared all emote requests");
            return true;
        }

        if (getConfig().getBoolean("promptToAdd"))
            sender.sendMessage(LazyUtil.getClickableSuggestion(" + Click to add your own emote!",
                    "/addemote :happy: •ᴗ•", "/addemote <code> <emote...>"));

        List<BaseComponent> baseComponents = new ArrayList<>(emojiMovie.keySet().size());
        int i = 0;
        for (Pattern pattern : emojiMovie.keySet())
        {
            String code = pattern.pattern().substring(9, pattern.pattern().length() - 8);
            String example = emojiMovie.get(pattern).get(ThreadLocalRandom.current().nextInt(emojiMovie.get(pattern).size()));
            baseComponents.add(LazyUtil.getClickableSuggestion(" " + code + " ", code, example));
            i += code.length() + 2;
            if (i >= 60)
            {
                sender.sendMessage(baseComponents.toArray(new BaseComponent[baseComponents.size()]));
                baseComponents.clear();
                i = 0;
            }
        }
        sender.sendMessage(baseComponents.toArray(new BaseComponent[baseComponents.size()]));

        notifyNewRequests(sender);

        return true;
    }
}

class EmoteRequest
{
    private String code;
    private String emote;

    EmoteRequest(String code, String emote)
    {
        this.code = code;
        this.emote = emote;
    }

    public String getCode()
    {
        return code;
    }

    public String getEmote()
    {
        return emote;
    }
}
