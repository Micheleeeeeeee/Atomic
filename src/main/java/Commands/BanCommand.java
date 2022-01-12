package Commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.requests.ErrorResponse;

public class BanCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent e) {
        if (!e.getName().equals("aban")) return;
        e.deferReply(true).queue(); // We have received the command!
        InteractionHook hook = e.getHook();
        hook.setEphemeral(true);

        if (!e.getMember().hasPermission(Permission.BAN_MEMBERS)) { // If command executor cannot ban members
            hook.sendMessage("**ERROR:** You do not have permissions to execute the selected command.").queue();
            return;
        }

        User user = e.getOption("user").getAsUser(); // User from args
        String reason = e.getOption("reason").getAsString(); // Reason from args

        try {
            e.getGuild().ban(user, 7, reason)
                    .flatMap(v -> hook.sendMessage("Banned user " + user.getName()))
                    .queue();
        } catch (HierarchyException ex) {
            hook.sendMessage("You cannot ban a user whom has a higher or equal highest role to you!").queue();
        }
    }
}
