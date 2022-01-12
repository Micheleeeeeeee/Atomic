package Commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PingCommand extends ListenerAdapter {

    @Override
    public void onSlashCommand(SlashCommandEvent e) {
        if (!e.getName().equals("ping")) return;

        long time = System.currentTimeMillis();
        e.reply("Pong!").setEphemeral(true) // reply or acknowledge
                .flatMap(v -> e.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time))
                .queue(); // Queue both reply and edit
    }

}
