package com.brokeassgeeks.snackbot.listeners;

import com.brokeassgeeks.snackbot.commands.Command;
import com.brokeassgeeks.snackbot.commands.CommandManager;
import com.brokeassgeeks.snackbot.Configuration.Config;
import com.brokeassgeeks.snackbot.commands.SimpleCommand;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.types.GenericMessageEvent;

import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CommandListener extends ListenerAdapter{
    private final Logger logger = (Logger) LoggerFactory.getLogger(CommandListener.class);

    private ExecutorService threadPool;

    public CommandListener() {
        threadPool = Executors.newSingleThreadExecutor();
    }

    @Override
    public void onGenericMessage(final GenericMessageEvent event) throws Exception {

        String message = event.getMessage();
        String sender ="";

        if (message.startsWith("<") && message.contains(">")) {
            sender = message.substring(message.indexOf("<") + 1, message.indexOf(">"));
            message = message.replaceAll("^<.*?>", "").trim();
        } else { sender=event.getUser().getNick(); }

        if(!message.startsWith(Config.CATCH_CHAR)) {
            return;
        }

        String[] args = message.split(" ");

        for(Class<?> cmd : CommandManager.getInstance().getCommands()) {
            Command command;
            try {
                command = (Command) cmd.getDeclaredConstructor(
                        GenericMessageEvent.class, String[].class).newInstance(event, args);
            } catch (InstantiationException | IllegalAccessException
                    | InvocationTargetException |NoSuchMethodException e) {
                e.printStackTrace();
                continue;
            }
            args[0] = args[0].replaceAll(Config.CATCH_CHAR,"");

            if(command instanceof SimpleCommand) {
                logger.info("Simple Command: " + command.getClass().getCanonicalName());

                if (((SimpleCommand) command).getTriggers().contains(args[0].toLowerCase()))
                    threadPool.submit(command);
            }

            else if (CommandManager.getInstance().getTriggers(command).contains(args[0].toLowerCase())) {
                if(CommandManager.getInstance().isCommandEnabled(command)) {
                    logger.info("Executing Command: " + command.getClass().getCanonicalName());
                    command.setSender(sender);
                    threadPool.submit(command);
                }
                else
                    logger.warn("Command Disabled: " + command.getClass().getCanonicalName());

            }

        }
    }
}
