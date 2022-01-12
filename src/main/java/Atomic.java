import Commands.BanCommand;
import Commands.SayCommand;
import Commands.PingCommand;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class Atomic {
    private static String token;
    private static Dotenv dotenv;
    private static JDA jda;

    public static void main(String[] args) throws LoginException {
        dotenv = Dotenv.configure().load();
        token = getToken();
        if (token.equals("Token Not Found")) {
            System.out.printf("Please create a .env file in the root folder of this project, and add a 'TOKEN' variable to it!\n");
            System.exit(-1);
        }

        JDA jda = JDABuilder.createDefault(token)
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .addEventListeners(new PingCommand())
                .addEventListeners(new SayCommand())
                .addEventListeners(new BanCommand())
                .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                new CommandData("ping", "Find the ping for the bot!")
        );

        commands.addCommands(
                new CommandData("say", "Make the bot say whatever you want!")
                        .addOptions(new OptionData(STRING, "message", "What you want the bot to send.")
                                .setRequired(true))
        );

        commands.addCommands(
                new CommandData("aban", "Ban a user.")
                        .addOptions(new OptionData(USER, "user", "User who will be banned.")
                                .setRequired(true))
                        .addOptions(new OptionData(STRING, "reason", "Reason for punishment")
                                .setRequired(true))
        );

        commands.queue();
    }

    static String getToken() {
        String token = dotenv.get("TOKEN");
        return token == null ? "Token Not Found" : token;
    }

    public static JDA getJda() {
        return jda;
    }
}
