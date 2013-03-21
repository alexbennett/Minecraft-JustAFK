/*
 * Copyright (c) 2013 Alex Bennett
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package net.alexben.JustAFK;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class JCommands implements CommandExecutor
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        Player player = (Player) sender;

        if(JUtility.hasPermission(player, "justafk.basic"))
        {
            if(command.getName().equalsIgnoreCase("afk"))
            {
                if(JUtility.isAway(player))
                {
                    JUtility.setAway(player, false);
                    JUtility.sendMessage(player, ChatColor.AQUA + "You are no longer away.");

                    return true;
                }

                // If they included an away message then set it BEFORE setting away.
                if(args.length > 0)
                {
                    String msg = StringUtils.join(args, " ");

                    JUtility.setAwayMessage(player, msg);
                }

                // Now set away status
                JUtility.setAway(player, true);

                // Send the messages.
                JUtility.sendMessage(player, ChatColor.AQUA + "You are now set to away.");

                return true;
            }
            else if(command.getName().equalsIgnoreCase("whosafk"))
            {
                if(JUtility.getAwayPlayers().isEmpty())
                {
                    JUtility.sendMessage(player, "There is nobody currently set to away.");
                    return true;
                }

                ArrayList<String> playerNames = new ArrayList<String>();

                for(Player item : JUtility.getAwayPlayers())
                {
                    playerNames.add(item.getName());
                }

                JUtility.sendMessage(player, "These players are currently away: " + StringUtils.join(playerNames, ", "));

                return true;
            }
            else if(command.getName().equalsIgnoreCase("setafk") && JUtility.hasPermissionOrOP(player, "justafk.admin"))
            {
                Player editing = Bukkit.getPlayer(args[0]);

                if(editing != null)
                {
                    if(!JUtility.isAway(editing))
                    {
                        JUtility.setAway(editing, true);
                        JUtility.sendMessage(editing, ChatColor.GRAY + "" + ChatColor.ITALIC + "You have been set to away by " + player.getDisplayName() + ".");
                    }
                    else
                    {
                        JUtility.setAway(editing, false);
                        JUtility.sendMessage(editing, ChatColor.GRAY + "" + ChatColor.ITALIC + "You have been set to available by " + player.getDisplayName() + ".");
                    }

                }

                return true;
            }
            else if(command.getName().equalsIgnoreCase("justafk"))
            {
                player.sendMessage(ChatColor.AQUA + "JustAFK" + ChatColor.GRAY + " is a plugin created for Bukkit intended for the use");
                player.sendMessage(ChatColor.GRAY + "of simple - yet powerful - away messages and other features");
                player.sendMessage(ChatColor.GRAY + "within Minecraft survival multiplayer.");
                player.sendMessage("");
                player.sendMessage(ChatColor.GRAY + "Author: " + ChatColor.AQUA + "_Alex");
                player.sendMessage(ChatColor.GRAY + "Source: " + ChatColor.AQUA + "http://github.com/alexbennett/Minecraft-JustAFK");

                return true;
            }
        }

        return false;
    }
}