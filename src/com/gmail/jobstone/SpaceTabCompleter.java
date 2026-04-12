package com.gmail.jobstone;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gmail.jobstone.space.NormalSpace;
import com.gmail.jobstone.space.SpaceGroup;
import com.gmail.jobstone.space.SpacePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class SpaceTabCompleter implements TabCompleter {

    private static final List<String> first;
    private static final List<String> world;

    static {
        first = Arrays.asList("permission", "pmgroup", "on", "off", "selector", "space", "group", "copy");
        world = Arrays.asList("world", "world_nether", "world_the_end", "creative");
    }

    public SpaceTabCompleter(){}

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (cmd.getName().equals("poorspace")) {

                if (args.length == 1) {
                    startCheck(first, args[0], list);
                }
                else if (args.length == 2) {
                    switch (args[0]) {
                        case "permission":
                            list.add("set");
                            break;
                        case "pmgroup":
                            if ("set".startsWith(args[1].toLowerCase())) {
                                list.add("set");
                            }
                            if ("add".startsWith(args[1].toLowerCase())) {
                                list.add("add");
                            }
                            if ("remove".startsWith(args[1].toLowerCase())) {
                                list.add("remove");
                            }
                            break;
                        case "selector":
                            if ("set".startsWith(args[1].toLowerCase())) {
                                list.add("set");
                            }
                            if ("remove".startsWith(args[1].toLowerCase())) {
                                list.add("remove");
                            }
                            if ("list".startsWith(args[1].toLowerCase())) {
                                list.add("list");
                            }
                            break;
                        case "space":
                        case "copy":
                            startCheck(world, args[1], list);
                            break;
                        case "group":
                            if ("search".startsWith(args[1].toLowerCase())) {
                                list.add("search");
                            }
                            if ("add".startsWith(args[1].toLowerCase())) {
                                list.add("add");
                            }
                            if ("remove".startsWith(args[1].toLowerCase())) {
                                list.add("remove");
                            }
                            if ("create".startsWith(args[1].toLowerCase())) {
                                list.add("create");
                            }
                            break;
                    }
                }
                else if (args.length == 3) {
                    if ((args[0].equals("permission") && args[1].equals("set")) || (args[0].equals("pmgroup") && (args[1].equals("set") || args[1].equals("add") || args[1].equals("remove")))) {
                        startCheck(world, args[2], list);
                    }

                    else if (args[0].equals("selector") && args[1].equals("remove")) {

                        SpacePlayer spaceplayer = new SpacePlayer(player.getName());
                        for (String selector : spaceplayer.getSelectorsSet()) {
                            if (selector.startsWith(args[2].toLowerCase())) {
                                list.add(selector);
                            }
                        }

                    }

                    else if (args[0].equals("group")) {

                        switch (args[1]) {
                            case "add":
                            case "remove":
                                String name = player.getName();
                                List<String> groups = (new SpacePlayer(name)).getGroups();
                                for (String group : groups) {
                                    switch ((new SpaceGroup(group)).getRole(name)) {
                                        case OP:
                                        case OWNER:
                                            list.add(group);
                                    }
                                }
                        }

                    }
                }
                else if (args.length == 4) {
                    if ((args[0].equals("permission") && args[1].equals("set")) || (args[0].equals("pmgroup") && (args[1].equals("set") || args[1].equals("add") || args[1].equals("remove"))) && NormalSpace.getWorldId(args[2]) != -1) {
                        if ("now".startsWith(args[3].toLowerCase())) {
                            list.add("now");
                        }
                        if ("all".startsWith(args[3].toLowerCase())) {
                            list.add("all");
                        }
                        if ("new".startsWith(args[3].toLowerCase())) {
                            list.add("new");
                        }

                        SpacePlayer spaceplayer = new SpacePlayer(player.getName());
                        for (String selector : spaceplayer.getSelectorsSet()) {
                            if (selector.startsWith(args[3].toLowerCase())) {
                                list.add(selector);
                            }
                        }
                    }
                }
                else if (args.length == 5) {
                    if ((args[0].equals("permission") && args[1].equals("set")) || (args[0].equals("pmgroup") && (args[1].equals("set") || args[1].equals("add") || args[1].equals("remove"))) && NormalSpace.getWorldId(args[2]) != -1) {
                        if ("1".startsWith(args[4])) {
                            list.add("1");
                        }
                        if ("2".startsWith(args[4])) {
                            list.add("2");
                        }
                        if ("3".startsWith(args[4])) {
                            list.add("3");
                        }
                        if (args[0].equals("permission") && "4".startsWith(args[4].toLowerCase())) {
                            list.add("4");
                        }
                    }
                }
                else if (args.length >= 6) {
                    if (args[0].equals("pmgroup") && (args[1].equals("set") || args[1].equals("add") || args[1].equals("remove")) && NormalSpace.getWorldId(args[2]) != -1) {
                        switch(args[4]) {
                            case "1":
                            case "2":
                            case "3":
                                return null;
                        }
                    }
                }

            }
        }
        return list;
    }

    private static void startCheck(List<String> list, String arg, List<String> addto) {
        for (String string : list) {
            if (string.startsWith(arg.toLowerCase())) {
                addto.add(string);
            }
        }
    }

}
