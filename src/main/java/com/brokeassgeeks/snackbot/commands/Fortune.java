package com.brokeassgeeks.snackbot.commands;

import com.brokeassgeeks.snackbot.Command;
import com.brokeassgeeks.snackbot.Utils.Utils;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

import java.io.File;
import java.io.FileNotFoundException;


public class Fortune extends Command {

    public Fortune(GenericMessageEvent event, String[] args) {
        super(event, args);
    }
    public Fortune(MessageReceivedEvent event, String[] args) { super(event, args); }

    @Override
    public void run() {

        try {
            String s = Utils.chooseRandomLine(new File("data/fun/fortunes.txt"));

            super.respond(String.format("<B><b>%s,<N> %s",sender, s));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}