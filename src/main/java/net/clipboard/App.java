package net.clipboard;

import net.clipboard.controller.Transfer;
import net.clipboard.view.SwingView;

import java.awt.datatransfer.Transferable;
import java.util.HashMap;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {
        Transfer transfer = new Transfer(new HashMap<Integer, Transferable>());
        SwingView view = new SwingView(transfer);
        view.create();
    }
}
