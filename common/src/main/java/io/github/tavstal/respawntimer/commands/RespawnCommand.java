package io.github.tavstal.respawntimer.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import io.github.tavstal.respawntimer.CommonClass;
import io.github.tavstal.respawntimer.utils.PlayerUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class RespawnCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher){
        dispatcher.register(Commands.literal("respawn").executes((command) -> {
            return execute(command);
        }));
    }
    private static int execute(CommandContext<CommandSourceStack> command){
        try {
            if (command.getSource().getEntity() instanceof ServerPlayer player) {
                if (player.isSpectator() && !CommonClass.IsPlayerDead(player.getStringUUID())) {
                    CommonClass.RespawnPlayer(player, false);
                    PlayerUtils.SendTitleMessage(player, "", "", 5);
                    return Command.SINGLE_SUCCESS;
                }

                if (player.hasPermissions(2) && CommonClass.IsPlayerDead(player.getStringUUID())) {
                    CommonClass.RespawnPlayer(player, false);
                    PlayerUtils.SendTitleMessage(player, "", "", 5);
                    return Command.SINGLE_SUCCESS;
                }
            }
            return Command.SINGLE_SUCCESS;
        }
        catch (Exception ex)
        {
            CommonClass.LOG.error("Error during executing command 'respawn':");
            CommonClass.LOG.error(ex.getLocalizedMessage());
            return 0;
        }
    }
}
