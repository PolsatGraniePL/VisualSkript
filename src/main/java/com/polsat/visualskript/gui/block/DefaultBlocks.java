package com.polsat.visualskript.gui.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DefaultBlocks {

    public static List<Block> getDefaultBlocks(){

        Block StructAliases = new Block("Aliases",
                BlockType.STRUCTURE,
                "[Structure] Aliases",
                "aliases",
                "1.0",
                "Used for registering custom aliases for a script.",
                """
                        aliases:
                            blacklisted items = TNT, bedrock, obsidian, mob spawner, lava, lava bucket
                            shiny swords = gold sword, iron sword, diamond sword
                        """
        );
        Block StructCommand = new Block("Command",
                BlockType.STRUCTURE,
                "[Structure] Command",
                "command <.+>",
                "1.0",
                "Used for registering custom commands.",
                """
                        command /broadcast <string>:
                            usage: A command for broadcasting a message to all players.
                            permission: skript.command.broadcast
                            permission message: You don't have permission to broadcast messages
                            aliases: /bc
                            executable by: players and console
                            cooldown: 15 seconds
                            cooldown message: You last broadcast a message %elapsed time% ago. You can broadcast another message in %remaining time%.
                            cooldown bypass: skript.command.broadcast.admin
                            cooldown storage: {cooldown::%player%}
                            trigger:
                                broadcast the argument
                        """
        );
        Block StructFunction = new Block("Function",
                BlockType.STRUCTURE,
                "[Structure] Function",
                "[local] function <.+>",
                "2.2, 2.7 (local functions)",
                "Functions are structures that can be executed with arguments/parameters to run code. They can also return a value to the trigger that is executing the function. Note that local functions come before global functions execution",
                """
                        function sayMessage(message: text):
                            broadcast {_message} # our message argument is available in '{_message}'
                        
                        local function giveApple(amount: number) :: item:
                            return {_amount} of apple
                        
                        function getPoints(p: player) returns number:
                            return {points::%{_p}%}
                        """
        );
        Block StructOptions = new Block("Options",
                BlockType.STRUCTURE,
                "[Structure] Options",
                "options",
                "1.0",
                "Options are used for replacing parts of a script with something else. For example, an option may represent a message that appears in multiple locations. Take a look at the example below that showcases this.",
                """
                        options:
                            no_permission: You're missing the required permission to execute this command!
                        
                        command /ping:
                            permission: command.ping
                            permission message: {@no_permission}
                            trigger:
                                message "Pong!"
                        
                        command /pong:
                            permission: command.pong
                            permission message: {@no_permission}
                            trigger:
                                message "Ping!"
                        """
        );
        Block StructVariables = new Block("Variables",
                BlockType.STRUCTURE,
                "[Structure] Variables",
                "variables",
                "1.0",
                "Used for defining variables present within a script. This section is not required, but it ensures that a variable has a value if it doesn't exist when the script is loaded.",
                """
                        variables:
                            {joins} = 0
                            {balance::%player%} = 0
                        
                        on join:
                            add 1 to {joins}
                            message "Your balance is %{balance::%player%}%"
                        """
        );
        Block SecConditional = new Block("Conditionals",
                BlockType.SECTION,
                "[Section] Conditionals",
                """
                        else
                        else [parse] if <.+>
                        else [parse] if (any|at least one [of])
                        else [parse] if [all]
                        [parse] if (any|at least one [of])
                        [parse] if [all]
                        [parse] if <.+>
                        then [run]
                        implicit:<.+>""",
                "1.0",
                """
                        Conditional sections if: executed when its condition is true else if: executed if all previous chained conditionals weren't executed, and its condition is true else: executed if all previous chained conditionals weren't executed

                        parse if: a special case of 'if' condition that its code will not be parsed if the condition is not true else parse if: another special case of 'else if' condition that its code will not be parsed if all previous chained conditionals weren't executed, and its condition is true""",
                """
                        if player's health is greater than or equal to 4:
                            send "Your health is okay so far but be careful!"
                        
                        else if player's health is greater than 2:
                            send "You need to heal ASAP, your health is very low!"
                        
                        else: # Less than 2 hearts
                            send "You are about to DIE if you don't heal NOW. You have only %player's health% heart(s)!"
                        
                        parse if plugin "SomePluginName" is enabled: # parse if %condition%
                            # This code will only be executed if the condition used is met otherwise Skript will not parse this section therefore will not give any errors/info about this section
                        """
        );
        Block SecLoop = new Block("Loop",
                BlockType.SECTION,
                "[Section] Loop",
                "loop %objects%",
                "1.0",
                """
                        Loop sections repeat their code with multiple values.
                        A loop will loop through all elements of the given expression, e.g. all players, worlds, items, etc. The conditions & effects inside the loop will be executed for every of those elements, which can be accessed with ‘loop-’, e.g. send "hello" to loop-player. When a condition inside a loop is not fulfilled the loop will start over with the next element of the loop. You can however use stop loop to exit the loop completely and resume code execution after the end of the loop.

                        Loopable Values All expressions that represent more than one value, e.g. ‘all players’, ‘worlds’, etc., as well as list variables, can be looped. You can also use a list of expressions, e.g. loop the victim and the attacker, to execute the same code for only a few values.

                        List Variables When looping list variables, you can also use loop-index in addition to loop-value inside the loop. loop-value is the value of the currently looped variable, and loop-index is the last part of the variable's name (the part where the list variable has its asterisk *).""",
                """
                        loop all players:
                            send "Hello %loop-player%!" to loop-player
                        
                        loop items in player's inventory:
                            if loop-item is dirt:
                                set loop-item to air
                        
                        loop 10 times:
                            send title "%11 - loop-value%" and subtitle "seconds left until the game begins" to player for 1 second # 10, 9, 8 etc.
                            wait 1 second
                        
                        loop {Coins::*}:
                            set {Coins::%loop-index%} to loop-value + 5 # Same as "add 5 to {Coins::%loop-index%}" where loop-index is the uuid of the player and loop-value is the actually coins value such as 200
                        """
        );
        Block EffSecSpawn = new Block("Spawn",
                BlockType.SECTION,
                "[Section] Spawn",
                "(spawn|summon) %entity types% [%directions% %locations%]\n" +
                        "(spawn|summon) %number% of %entity types% [%directions% %locations%]",
                "1.0, 2.6.1 (with section)",
                "Spawn a creature. This can be used as an effect and as a section. If it is used as a section, the section is run before the entity is added to the world. You can modify the entity in this section, using for example 'event-entity' or 'cow'. Do note that other event values, such as 'player', won't work in this section.",
                """
                        spawn 3 creepers at the targeted block
                        spawn a ghast 5 meters above the player
                        spawn a zombie at the player:
                            set name of the zombie to ""
                        """
        );
        Block SecWhile = new Block("While Loop",
                BlockType.SECTION,
                "[Section] While Loop",
                "[do] while <.+>",
                "2.0, 2.6 (do while)",
                "While Loop sections are loops that will just keep repeating as long as a condition is met.",
                """
                        while size of all players < 5:
                            send "More players are needed to begin the adventure" to all players
                            wait 5 seconds
                        
                        set {_counter} to 1
                        do while {_counter} > 1: # false but will increase {_counter} by 1 then get out
                            add 1 to {_counter}
                        
                        # Be careful when using while loops with conditions that are almost
                        # always true for a long time without using 'wait %timespan%' inside it,
                        # otherwise it will probably hang and crash your server.
                        while player is online:
                            give player 1 dirt
                            wait 1 second # without using a delay effect the server will crash
                        """
        );
        Block CombineString = new Block("Combine texts",
                BlockType.TYPE_LIST,
                "[Types] Combine Texts",
                "Missing patterns.",
                "Special Visual Script block",
                "A block that allows you to create a list of texts in order to be able to add the use of variables, options, etc.",
                "Missing example."
        );
        Block ObjectsList = new Block("Objects List",
                BlockType.TYPE_LIST,
                "[Types] Objects List",
                "Missing patterns.",
                "Special Visual Script block",
                "A block allowing you to create a list of objects.",
                "Missing example."
        );
        Block Comment = new Block("Comment",
                BlockType.COMMENT,
                "[Comment] Comment",
                "Missing patterns.",
                "Special Visual Script block",
                "Add a comment to your code!",
                "Missing example."
        );

        return new ArrayList<>(Arrays.asList(StructAliases, StructCommand, StructFunction, StructOptions, StructVariables, SecLoop, SecConditional, SecWhile, EffSecSpawn, CombineString, ObjectsList, Comment));
    }

}
