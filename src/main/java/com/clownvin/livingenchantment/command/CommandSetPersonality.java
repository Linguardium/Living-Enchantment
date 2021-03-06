package com.clownvin.livingenchantment.command;

import com.clownvin.livingenchantment.LivingEnchantment;
import com.clownvin.livingenchantment.personality.Personality;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandSetPersonality {

    public static int setPersonality(CommandSource source, String personality) throws CommandException {
        try {
            EntityPlayer player = source.asPlayer();
            ItemStack held = player.getHeldItemMainhand();
            NBTTagCompound tag = LivingEnchantment.getEnchantmentNBTTag(held);
            if (tag == null) {
                source.sendErrorMessage(new TextComponentTranslation("commands.livingenchantment.mainhand_item_not_living"));
                return 2;
            }
            Personality p = Personality.getPersonality(personality);
            if (p == null) {
                source.sendErrorMessage(new TextComponentTranslation("commands.setpersonality.not_in_range", personality));
                return 1;
            }
            tag.setFloat(LivingEnchantment.PERSONALITY, Personality.getValue(p));
            tag.setString(LivingEnchantment.PERSONALITY_NAME, p.name);
            source.sendFeedback(new TextComponentTranslation("commands.setpersonality.success", new Object[] {held.getDisplayName().getFormattedText(), p.name}), true);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void register(CommandDispatcher<CommandSource> commandDispatcher) {
        commandDispatcher.register((LiteralArgumentBuilder)(((LiteralArgumentBuilder) Commands.literal("setpersonality").requires((p_198359_0_) -> p_198359_0_.hasPermissionLevel(2)
        )).then((Commands.argument("personality", StringArgumentType.string()).executes((p_198352_0_) ->
                setPersonality(p_198352_0_.getSource(), StringArgumentType.getString(p_198352_0_, "personality"))
        )))));
    }
}
