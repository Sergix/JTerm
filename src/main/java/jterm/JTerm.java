/*
* JTerm - a cross-platform terminal
* Copyright (code) 2017 Sergix, NCSGeek
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

// package = folder :P
package jterm;

import java.lang.reflect.InvocationTargetException;
import jterm.command.Command;
import jterm.command.CommandException;
import jterm.command.CommandExecutor;
import jterm.gui.Terminal;
import jterm.io.InputHandler;
import jterm.io.Keys;
import jterm.io.RawConsoleInput;
import jterm.util.Util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.net.URL;
import java.io.IOException;
import java.security.CodeSource;

public class JTerm {
    private static final Map<String, CommandExecutor> COMMANDS = new HashMap<>();

    public static InputHandler inputHandler;

    public static final String VERSION = "0.6.1";
    public static final String PROMPT = "   \b\b\b>> ";
    public static String LICENSE = "JTerm Copyright (C) 2017 Sergix, NCSGeek, chromechris\n"
            + "This program comes with ABSOLUTELY NO WARRANTY.\n"
            + "This is free software, and you are welcome to redistribute it\n"
            + "under certain conditions.\n";

    // Default value of getProperty("user.dir") is equal to the default directory set when the program starts
    // Global directory variable (use "cd" command to change)
    public static String currentDirectory = System.getProperty("user.dir");
    public static final String USER_HOME_DIR = System.getProperty("user.home");

    public static boolean IS_WIN;
    public static boolean IS_UNIX;

    static {
        setOS();
        initCommands();
    }

    public static BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
    private static Terminal terminal;
    private static boolean headless = false;

    public static void main(String[] args) {
        inputHandler = new InputHandler(new RawConsoleInput());
        if (args.length > 0 && args[0].equals("headless")) {
            headless = true;
            System.out.println(LICENSE);
            System.out.print(PROMPT);
            try {
                while (true) {
                    inputHandler.read();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        } else {
            terminal = new Terminal();
            terminal.setTitle("JTerm");
            terminal.setSize(720, 480);
            terminal.setVisible(true);

        }
    }

    public static void executeCommand(String options) {
        List<String> optionsArray = Util.getAsArray(options);

        if (optionsArray.size() == 0) {
            return;
        }

        String command = optionsArray.remove(0);
        if (!COMMANDS.containsKey(command)) {
            System.out.println("Command \"" + command + "\" is not available");
            return;
        }

        try {
            if(JTerm.isHeadless()) System.out.println();
            COMMANDS.get(command).execute(optionsArray);
        } catch (CommandException e) {
            System.err.println(e.getMessage());
        }
    }

    private static void initCommands() {
        // Reflections reflections = new Reflections("jterm.command", new MethodAnnotationsScanner());
        // Set<Method> methods = reflections.getMethodsAnnotatedWith(Command.class);
        ArrayList<Method> methods = new ArrayList<>();
        Method[] unsortedMethods;
        ArrayList<String> classes = new ArrayList<>();

        try {
            CodeSource src = JTerm.class.getProtectionDomain().getCodeSource();
            if (src != null) {
                URL jar = src.getLocation();
                ZipInputStream zip = new ZipInputStream(jar.openStream());
                while (true) {
                    ZipEntry e = zip.getNextEntry();
                    if (e == null)
                        break;
                    String name = e.getName();
                    if (name.startsWith("jterm/command")) {
                        classes.add(
                                name.replace('/', '.')
                                .substring(0, name.length() - 6)
                        );
                    }
                }
            }
        } catch (IOException ioe) {
            System.out.println(ioe);
        }

        classes.remove(0);

        for (String aClass : classes) {
            try {
                Class<?> clazz = Class.forName(aClass);
                Constructor<?> constructor = clazz.getConstructor();
                Object obj = constructor.newInstance();

                unsortedMethods = obj.getClass().getDeclaredMethods();
                for (Method method : unsortedMethods) {
                    Annotation[] annotations = method.getDeclaredAnnotations();
                    if (method.isAnnotationPresent(Command.class)) {
                        methods.add(method);
                    }
                }
            } catch (ClassNotFoundException cnfe) {
                System.out.println(cnfe);
            } catch (NoSuchMethodException nsme) {
                // System.out.println(nsme);
            } catch (InstantiationException ie) {
                // System.out.println(ie);
            } catch (IllegalAccessException iae) {
                System.out.println(iae);
            } catch (InvocationTargetException iae) {
                // System.out.println(iae);
            }
        }

        for (Method method : methods) {
            method.setAccessible(true);
            Command command = method.getDeclaredAnnotation(Command.class);
            for (String commandName : command.name()) {
                CommandExecutor executor = new CommandExecutor()
                        .setCommandName(commandName)
                        .setSyntax(command.syntax())
                        .setMinOptions(command.minOptions())
                        .setCommand((List<String> options) -> {
                            try {
                                method.invoke(null, options);
                            } catch (Exception e) {
                                System.err.println("Weird stuff...");
                            }
                        });
                COMMANDS.put(commandName, executor);
            }
        }
    }
    private static void setOS() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("windows")) {
            JTerm.IS_WIN = true;
            Keys.initWindows();
        } else if ("linux".equals(os) || os.contains("mac") || "sunos".equals(os) || "freebsd".equals(os)) {
            JTerm.IS_UNIX = true;
            Keys.initUnix();
        }
    }

    public static boolean isHeadless() {
        return headless;
    }

    public static Terminal getTerminal() {
        return terminal;
    }
}
