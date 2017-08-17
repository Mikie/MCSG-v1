package me.smiileyface.mcsg.command.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.smiileyface.mcsg.Core;
import me.smiileyface.mcsg.game.VoteMap;
import me.smiileyface.mcsg.game.phase.LobbyPhase;
import me.smiileyface.mcsg.player.GamePlayer;
import me.smiileyface.utils.ChatUtils;

public class Vote implements CommandExecutor {

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if((cmd.getName().equalsIgnoreCase("vote")) && (sender instanceof Player)) {
			Player p = (Player)sender;
			if(!Core.get().getGame().getPlayers().contains(GamePlayer.getPlayer(p.getDisplayName()))) {
				p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cYou must be playing to use this command."));
				return true;
			}
			
			if(args.length == 0) {
				p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cYou must specify a Map ID!"));
				return true;
			}
			
			GamePlayer player = GamePlayer.getPlayer(p.getDisplayName());
			
			if(!Core.get().getGame().isVoting()) {
				p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cIt is not time to vote."));
				return true;
			}
			
			LobbyPhase lp = Core.get().getGame().getLobbyPhase();
			
			if(!lp.canVote(player)) {
				p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cYou have already used your vote."));
				return true;
			}
			
			int mapid = 0;
			
			try {
				mapid = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThat is not a valid number."));
				return true;
			} catch (ArrayIndexOutOfBoundsException e) {
				p.sendMessage(ChatUtils.modulate("SurvivalGames", "&cThat number is either too big or small."));
				return true;
			}
			
			VoteMap map = lp.vote(player, mapid);
			
			if(map == null) {
				p.sendMessage(ChatUtils.modulate("SurvivalGames", "That is not a valid option."));
				return true;
			}
		}
		
		return false;
	}
}
