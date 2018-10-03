package com.github.shynixn.astraledit.bukkit.logic.business.command;

import com.github.shynixn.astraledit.api.bukkit.business.command.PlayerCommand;
import com.github.shynixn.astraledit.bukkit.AstralEditPlugin;
import com.github.shynixn.astraledit.bukkit.Permission;
import com.github.shynixn.astraledit.bukkit.logic.business.Operation;
import com.github.shynixn.astraledit.bukkit.logic.business.OperationType;
import com.github.shynixn.astraledit.bukkit.logic.business.SelectionManager;
import com.github.shynixn.astraledit.bukkit.logic.lib.DoubleChecker;
import org.bukkit.entity.Player;

public class MoveCommand implements PlayerCommand {
    private final SelectionManager selectionManager;

    public MoveCommand(SelectionManager selectionManager){
        this.selectionManager = selectionManager;
    }

    @Override
    public boolean onPlayerExecuteCommand(Player player, String[] args) {
        if(isMoveToPlayerCommand(player, args)){
            this.moveRenderToPlayer(player);
        }else if(isMoveToPositionCommand(player, args)){
            this.moveRenderCommand(
                    player,
                    Double.parseDouble(args[1]),
                    Double.parseDouble(args[2]),
                    Double.parseDouble(args[3])
            );
        }else{
            return false;
        }

        return true;
    }

    /**
     * Moves the rendered Object to the given coordinates
     *
     * @param player player
     * @param x      x
     * @param y      y
     * @param z      z
     */
    private void moveRenderCommand(Player player, double x, double y, double z) {
        if (!this.selectionManager.hasSelection(player)) {
            player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
        } else {
            final Operation operation = new Operation(OperationType.MOVE);
            operation.setOperationData(this.selectionManager.getSelection(player).getLocation().clone());
            this.selectionManager.getSelection(player).move(
                    this.selectionManager.getSelection(player).getLocation().add(x, y, z));
            this.selectionManager.addOperation(player, operation);
        }
    }

    /**
     * Moves the rendered Object to the player
     *
     * @param player player
     */
    private void moveRenderToPlayer(Player player) {
//        this.runAsyncTask(() -> {
//            if (!this.manager.hasSelection(player)) {
//                player.sendMessage(AstralEditPlugin.PREFIX_ERROR + "You don't have a valid render.");
//            } else {
//                final Operation operation = new Operation(OperationType.MOVE);
//                operation.setOperationData(this.manager.getSelection(player).getLocation().clone());
//                this.manager.getSelection(player).move(player.getLocation().add(0, -2, 0));
//                this.manager.addOperation(player, operation);
//            }
//        });
        moveRenderCommand(player,
                player.getLocation().getX(),
                player.getLocation().getY() - 2,
                player.getLocation().getZ());
    }

    private boolean isMoveToPositionCommand(Player player, String[] args) {
        return args.length == 4 &&
                args[0].equalsIgnoreCase("move") &&
                new DoubleChecker().areDoubles(
                        args[1],
                        args[2],
                        args[3]
                ) &&
                Permission.MOVE_COORDINATE.hasPermission(player);
    }

    private boolean isMoveToPlayerCommand(Player player, String[] args) {
        return args.length == 1 &&
                args[0].equalsIgnoreCase("move") &&
                Permission.MOVE_PLAYER.hasPermission(player);
    }
}
