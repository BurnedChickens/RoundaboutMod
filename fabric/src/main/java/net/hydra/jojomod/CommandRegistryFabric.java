package net.hydra.jojomod;

import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;

public class CommandRegistryFabric {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("heal").requires(source
                        -> source.hasPermission(2))).executes(context -> RoundaboutCommands.executeHeal((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> RoundaboutCommands.executeHeal((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drSummon").requires(source
                        -> source.hasPermission(2))).executes(context -> RoundaboutCommands.executeDebugSummon((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> RoundaboutCommands.executeDebugSummon((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drAttack").requires(source
                        -> source.hasPermission(2))).executes(context -> RoundaboutCommands.executeDebugAttack((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> RoundaboutCommands.executeDebugAttack((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drBarrage").requires(source
                        -> source.hasPermission(2))).executes(context -> RoundaboutCommands.executeDebugBarrage((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> RoundaboutCommands.executeDebugBarrage((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drGuard").requires(source
                        -> source.hasPermission(2))).executes(context -> RoundaboutCommands.executeDebugGuard((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> RoundaboutCommands.executeDebugGuard((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) ->
                dispatcher.register(dispatcher.register((((Commands.literal("drCancel").requires(source
                        -> source.hasPermission(2))).executes(context -> RoundaboutCommands.executeDebugCancel((CommandSourceStack)context.getSource(),
                        ImmutableList.of(((CommandSourceStack)context.getSource()).getEntityOrException())))).then(Commands.argument("targets",
                        EntityArgument.entities()).executes(context -> RoundaboutCommands.executeDebugCancel((CommandSourceStack)context.getSource(),
                        EntityArgument.getEntities(context, "targets")))))).createBuilder()));
    }
}